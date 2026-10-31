"""Stage reviewed independent plates; preserve retail alpha, slots and motion.

This is separate from the fixed-visible packager. It never installs artwork or
changes recorded traces. Run with the bundled Python/Pillow runtime.
"""
import argparse
import csv
import hashlib
import json
from pathlib import Path

from PIL import Image, ImageChops, ImageDraw


def digest(path):
    with path.open('rb') as stream:
        return hashlib.file_digest(stream, 'sha256').hexdigest()


def layer_depth(row):
    flags = int(row.get('zFlags', 0))
    return flags & 0x3fff if flags >> 14 == 2 else int(row.get('z', 0))


def layer_order(frame):
    return sorted(frame, key=lambda index: (-layer_depth(frame[index]), index))


def exterior_coverage(image, colors):
    assert 1 <= len(colors) <= 16 and all(len(color) == 3 and all(type(value) is int and 0 <= value <= 255 for value in color) for color in colors)
    coverage = Image.new('L', image.size, 255)
    for color in colors:
        match = Image.new('L', image.size, 255)
        for channel, value in zip(image.convert('RGB').split(), color):
            match = ImageChops.multiply(match, channel.point(lambda pixel, wanted=value: 255 if pixel == wanted else 0))
        coverage = ImageChops.subtract(coverage, match)
    return coverage


def bleed_transparent_rgb(image, alpha_threshold=1):
    """Extend reliable sprite RGB into matte texels before retail-mask clipping."""
    import numpy as np
    assert type(alpha_threshold) is int and 1 <= alpha_threshold <= 255
    pixels = np.array(image.convert('RGBA'))
    known = pixels[:, :, 3] >= alpha_threshold
    assert known.any(), 'Cannot extend colors from an entirely transparent sprite or one below the alpha threshold'
    rgb = pixels[:, :, :3]
    neighbors = ((slice(1, None), slice(None), slice(None, -1), slice(None)),
                 (slice(None, -1), slice(None), slice(1, None), slice(None)),
                 (slice(None), slice(1, None), slice(None), slice(None, -1)),
                 (slice(None), slice(None, -1), slice(None), slice(1, None)))
    while not known.all():
        sums = np.zeros(rgb.shape, dtype=np.uint16)
        counts = np.zeros(known.shape, dtype=np.uint8)
        for sy, sx, dy, dx in neighbors:
            sums[dy, dx] += rgb[sy, sx] * known[sy, sx, None]
            counts[dy, dx] += known[sy, sx]
        filled = ~known & (counts != 0)
        assert filled.any()
        weights = counts[filled].astype(np.uint16)[:, None]
        rgb[filled] = (sums[filled] + weights // 2) // weights
        known[filled] = True
    return Image.fromarray(pixels)


def package(cut):
    staged = Path(f'build/disc1/staged/cut-{cut}')
    staged.mkdir(parents=True, exist_ok=True)
    (staged / 'layered-validation.json').unlink(missing_ok=True)
    source = Path(f'gfx/backgrounds/disc1/cut-{cut}/source')
    config_path = source / 'layer-resolution/plates.json'
    config = json.loads(config_path.read_text())
    original = Path(f'build/disc1/original/cut-{cut}')
    manifest = json.loads((original / 'manifest.json').read_text())
    assert manifest['cut'] == cut
    count = manifest['foreground_layers']
    assert set(config['layers']) == {str(i) for i in range(count)}, 'Every retail slot needs a source'
    dynamic = set(config['dynamicLayers'])
    assert dynamic and all(isinstance(i, int) and 0 <= i < count for i in dynamic)
    assert config['visualReview'] is True and config['evidence'], 'Independent plates need explicit review evidence'
    trace = Path(f'build/disc1/captures/cut-{cut}/original/alignment.csv')
    frames = {}
    with trace.open(newline='') as stream:
        for row in csv.DictReader(stream):
            assert int(row['cut']) == cut
            index = int(row['layer'])
            assert 0 <= index < count and row['hidden'] in ('true', 'false')
            assert index not in frames.setdefault(int(row['frame']), {})
            frames[int(row['frame'])][index] = row
            if index not in dynamic:
                assert row['hidden'] == 'false' and all(int(row[k]) == 0 for k in ('startX', 'startY', 'destX', 'destY')), 'Unexpected dynamic layer needs independent artwork'
    assert frames and all(len(layers) == count for layers in frames.values())
    gpu_evidence = trace.parent / 'original-gpu-export.txt'
    assert gpu_evidence.read_text() == f'Original GPU textures exactly match exported PNGs for cut {cut}'
    shared = set(config.get('sharedSceneryLayers', []))
    shared_review = None
    if shared:
        assert shared <= dynamic, 'Shared scenery review must name dynamic retail slots'
        review_path = (config_path.parent / config['sharedSceneryReview']).resolve()
        assert review_path.is_relative_to(source.resolve())
        review = json.loads(review_path.read_text())
        assert review['cut'] == cut and set(review['layers']) == shared
        assert review['geometryUnchanged'] is True and len(review['reviewers']) >= 2
        assert review['traceSha256'] == digest(trace)
        expected_names = {'background.png'} | {f'foreground-{i}.png' for i in range(count)}
        assert set(review['nativeHashes']) == expected_names
        assert all(digest(original / name) == value for name, value in review['nativeHashes'].items()), 'Shared scenery native evidence changed'
        analysis_path = (config_path.parent / review['nativeStateAnalysis']).resolve()
        assert analysis_path.is_relative_to(source.resolve()) and digest(analysis_path) == review['nativeStateAnalysisSha256']
        analysis = json.loads(analysis_path.read_text())
        assert analysis['cut'] == cut and len(analysis['states']) == 2 ** len(shared)
        for frame in frames.values():
            for index in shared:
                row = frame[index]
                assert row['hidden'] == 'true' or all(int(row[k]) == 0 for k in ('startX', 'startY', 'destX', 'destY')), 'Shared scenery cannot move visibly'
        shared_review = {'source': review_path.as_posix(), 'sha256': digest(review_path)}
    size = tuple(value * 4 for value in manifest['bounds'][2:])
    layers = []
    report = []
    source_paths = {'background': config['background']}
    source_paths.update({f'foreground-{i}': config['layers'][str(i)] for i in range(count)})
    for name, relative in source_paths.items():
        definition = relative if isinstance(relative, dict) else {'source': relative}
        sprite = definition if 'sourceBounds' in definition else None
        relative = definition['source']
        path = (config_path.parent / relative).resolve()
        assert path.is_relative_to(source.resolve()), 'Plate sources must remain inside this cut source directory'
        with Image.open(original / f'{name}.png') as image:
            assert image.size == tuple(manifest['bounds'][2:])
            alpha = image.getchannel('A').resize(size, Image.Resampling.NEAREST)
        with Image.open(path) as image:
            if sprite is None:
                rgb = image.convert('RGBA').resize(size, Image.Resampling.LANCZOS)
            else:
                # Atlas placement maps a generated isolated sprite onto the retail slot.
                box = tuple(sprite['sourceBounds'])
                assert len(box) == 4 and 0 <= box[0] < box[2] <= image.width and 0 <= box[1] < box[3] <= image.height
                target = alpha.getbbox()
                assert target, 'Cannot pack a sprite into an empty retail slot'
                fitted = image.convert('RGBA').crop(box).resize((target[2] - target[0], target[3] - target[1]), Image.Resampling.LANCZOS)
                if sprite.get('bleedTransparentRgb', False):
                    fitted = bleed_transparent_rgb(fitted, sprite.get('bleedAlphaThreshold', 1))
                rgb = Image.new('RGBA', size)
                rgb.paste(fitted, target[:2])
        patch_sources = []
        for patch in definition.get('patches', []):
            patch_path = (config_path.parent / patch['source']).resolve()
            assert patch_path.is_relative_to(source.resolve()), 'Patch source must remain inside this cut source directory'
            target = tuple(patch['targetBounds'])
            assert len(target) == 4 and all(type(value) is int for value in target)
            assert 0 <= target[0] < target[2] <= size[0] // 4 and 0 <= target[1] < target[3] <= size[1] // 4
            with Image.open(patch_path) as image:
                box = tuple(patch['sourceBounds'])
                assert len(box) == 4 and 0 <= box[0] < box[2] <= image.width and 0 <= box[1] < box[3] <= image.height
                fitted = image.convert('RGBA').crop(box).resize(((target[2] - target[0]) * 4, (target[3] - target[1]) * 4), Image.Resampling.LANCZOS)
            mask_record = {}
            if patch.get('maskBackgroundRgb', False):
                background_path = original / 'background.png'
                with Image.open(background_path) as image:
                    # Some retail variants tint the opaque exterior checker red.
                    exterior = patch.get('exteriorRgb', [[0, 0, 0], [8, 8, 8], [16, 16, 16]])
                    coverage = ImageChops.multiply(exterior_coverage(image, exterior), image.getchannel('A'))
                coverage = coverage.crop(target).resize(fitted.size, Image.Resampling.NEAREST)
                fitted.putalpha(ImageChops.multiply(fitted.getchannel('A'), coverage))
                mask_record = {'backgroundMaskSource': background_path.as_posix(), 'backgroundMaskSha256': digest(background_path)}
            if 'maskLayer' in patch:
                index = patch['maskLayer']
                assert type(index) is int and 0 <= index < count, 'Patch mask must name a retail foreground slot'
                mask_path = original / f'foreground-{index}.png'
                with Image.open(mask_path) as image:
                    mask = image.getchannel('A')
                assert mask.getbbox() == target, 'Masked patch must fit the exact retail alpha bounds'
                mask = mask.crop(target).resize(fitted.size, Image.Resampling.NEAREST)
                fitted.putalpha(ImageChops.multiply(fitted.getchannel('A'), mask))
                mask_record.update(maskLayer=index, maskSource=mask_path.as_posix(), maskSha256=digest(mask_path))
            rgb.alpha_composite(fitted, (target[0] * 4, target[1] * 4))
            patch_sources.append({'source': patch_path.as_posix(), 'sourceSha256': digest(patch_path),
                                  'sourceBounds': list(box), 'targetBounds': list(target), **mask_record})
        exterior_record = {}
        if 'clearRetailExteriorRgb' in definition:
            clearing = definition['clearRetailExteriorRgb']
            bounds = tuple(clearing['bounds'])
            assert len(bounds) == 4 and all(type(value) is int for value in bounds)
            assert 0 <= bounds[0] < bounds[2] <= size[0] // 4 and 0 <= bounds[1] < bounds[3] <= size[1] // 4
            native_path = original / f'{name}.png'
            with Image.open(native_path) as image:
                exterior = ImageChops.invert(exterior_coverage(image, clearing['colors']))
            mask = Image.new('L', exterior.size)
            mask.paste(exterior.crop(bounds), bounds[:2])
            if 'polygon' in clearing:
                polygon = clearing['polygon']
                assert 3 <= len(polygon) <= 16 and all(len(point) == 2 and all(type(value) is int for value in point) and bounds[0] <= point[0] <= bounds[2] and bounds[1] <= point[1] <= bounds[3] for point in polygon)
                region = Image.new('L', exterior.size)
                ImageDraw.Draw(region).polygon([tuple(point) for point in polygon], fill=255)
                mask = ImageChops.multiply(mask, region)
            rgb.paste((0, 0, 0, 0), mask=mask.resize(size, Image.Resampling.NEAREST))
            exterior_record = {'retailExteriorMaskSource': native_path.as_posix(), 'retailExteriorMaskSha256': digest(native_path)}
        rgb.putalpha(alpha)
        rgb.paste((0, 0, 0, 0), mask=alpha.point(lambda value: 255 if value == 0 else 0))
        output = staged / f'{name}.png'
        rgb.save(output, optimize=True, compress_level=9)
        with Image.open(output) as decoded:
            assert decoded.size == size and decoded.mode == 'RGBA'
            assert ImageChops.difference(decoded.getchannel('A'), alpha).getbbox() is None
        layers.append(rgb)
        report.append({'name': name, 'width': size[0], 'height': size[1], 'exactOriginalMask': True,
                       'source': path.as_posix(), 'sourceSha256': digest(path), 'patchSources': patch_sources, 'sha256': digest(output), **exterior_record})
    with Image.open(original / 'background.png') as image:
        retail_background = image.convert('RGBA')
    for index in dynamic:
        with Image.open(original / f'foreground-{index}.png') as image:
            retail_layer = image.convert('RGBA')
        overlap = ImageChops.multiply(retail_background.getchannel('A'), retail_layer.getchannel('A'))
        original_difference = ImageChops.difference(retail_background.convert('RGB'), retail_layer.convert('RGB'))
        if index not in shared and ImageChops.multiply(original_difference.convert('L'), overlap).getbbox():
            modern_overlap = overlap.resize(size, Image.Resampling.NEAREST)
            modern_difference = ImageChops.difference(layers[0].convert('RGB'), layers[index + 1].convert('RGB'))
            assert ImageChops.multiply(modern_difference.convert('L'), modern_overlap).getbbox(), 'Changing object is still baked identically into its exposed background'
    # Endpoint composites reveal baked duplicates; they are asset previews, not GPU/gameplay captures.
    states = [('all-visible', None), ('dynamic-hidden', set(dynamic))]
    final_frame = frames[max(frames)]
    for label, hidden in states:
        composite = layers[0].copy()
        for index in layer_order(final_frame):
            layer = layers[index + 1]
            if hidden is None or index not in hidden:
                composite.alpha_composite(layer)
        composite.save(config_path.parent / f'{label}.png', optimize=True)
    for endpoint in ('start', 'dest'):
        composite = layers[0].copy()
        frame = final_frame
        for index in layer_order(frame):
            layer = layers[index + 1]
            row = frame[index]
            if row['hidden'] == 'true' or layer_depth(row) < 0:
                continue
            x, y = int(row[endpoint + 'X']) * 4, int(row[endpoint + 'Y']) * 4
            composite.alpha_composite(layer, (x, y))
        composite.save(config_path.parent / f'recorded-{endpoint}.png', optimize=True)
    result = {'cut': cut, 'scale': 4, 'independentLayerPlates': True, 'dynamicLayers': sorted(dynamic),
              'sharedSceneryLayers': sorted(shared), 'sharedSceneryReview': shared_review,
              'configSha256': digest(config_path), 'traceSha256': digest(trace), 'evidence': config['evidence'],
              'layers': report, 'runtimeInstalled': False, 'gameplayVerified': False,
              'previewDepthOrder': layer_order(final_frame),
              'characterDependentDepthApproximation': any(int(row.get('zFlags', 0)) >> 14 == 1 for row in final_frame.values()),
              'previewScope': 'All-visible, dynamic-hidden and recorded final endpoints; not full traversal or intermediate-motion verification'}
    (staged / 'layered-validation.json').write_text(json.dumps(result, indent=2) + '\n')
    print(f'Cut {cut}: {len(layers)} exact-alpha independent plates staged; no runtime install')
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('cut', type=int)
    package(parser.parse_args().cut)
