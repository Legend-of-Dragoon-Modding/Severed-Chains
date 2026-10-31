"""Build uninstalled artwork previews; this does not satisfy runtime packaging gates.

Use the bundled Python with Pillow. Existing runtime packagers remain authoritative.
"""
import argparse
import csv
import json
import shutil
from pathlib import Path

from PIL import Image, ImageChops


def preview(cut, original=None, source=None, clear_transparent=True, plate_directory=None):
    original = Path(original or f'build/disc1/original/cut-{cut}')
    source = Path(source or f'gfx/backgrounds/disc1/cut-{cut}/source')
    manifest = json.loads((original / 'manifest.json').read_text())
    placement = json.loads((source / 'placement.json').read_text())
    assert manifest['cut'] == cut
    assert placement['mapping'] == 'full-canvas'
    size = tuple(manifest['bounds'][2:])
    target = tuple(value * 4 for value in size)
    source_image = placement.get('source_image', 'base.png')
    assert Path(source_image).name == source_image and source_image.endswith('.png')
    with Image.open(source / source_image) as image:
        assert image.size == (placement['width'], placement['height'])
        master = image.convert('RGBA').resize(target, Image.Resampling.LANCZOS)
    names = ['background'] + [f'foreground-{i}' for i in range(manifest['foreground_layers'])]
    output = source / 'asset-preview'
    output.mkdir(exist_ok=True)
    original_composite = Image.new('RGBA', size)
    modern_composite = Image.new('RGBA', target)
    report = []
    for name in names:
        borrowed_bytes = None
        with Image.open(original / f'{name}.png') as image:
            assert image.size == size, (name, image.size, size)
            retail = image.convert('RGBA')
        original_composite.alpha_composite(retail)
        alpha = retail.getchannel('A').resize(target, Image.Resampling.NEAREST)
        if plate_directory is None:
            layer = master.copy()
            layer.putalpha(alpha)
        else:
            with Image.open(Path(plate_directory) / f'{name}.png') as image:
                layer = image.convert('RGBA')
                if image.mode == 'RGBA':
                    borrowed_bytes = layer.tobytes()
            assert layer.size == target
            assert ImageChops.difference(layer.getchannel('A'), alpha).getbbox() is None
        if clear_transparent:
            layer.paste((0, 0, 0, 0), mask=alpha.point(lambda value: 255 if value == 0 else 0))
        if borrowed_bytes is not None and borrowed_bytes == layer.tobytes():
            shutil.copy2(Path(plate_directory) / f'{name}.png', output / f'{name}.png')
        else:
            layer.save(output / f'{name}.png', optimize=True, compress_level=9)
        with Image.open(output / f'{name}.png') as decoded:
            assert decoded.size == target and decoded.mode == 'RGBA'
            assert ImageChops.difference(decoded.getchannel('A'), alpha).getbbox() is None
            modern_composite.alpha_composite(decoded)
        report.append({'name': name, 'width': target[0], 'height': target[1], 'exactOriginalMask': True})
    if plate_directory is not None:
        trace = Path(f'build/disc1/captures/cut-{cut}/original/alignment.csv')
        with trace.open(newline='') as stream:
            rows = list(csv.DictReader(stream))
        final_frame = max(int(row['frame']) for row in rows)
        frame = {int(row['layer']): row for row in rows if int(row['frame']) == final_frame}
        def depth(index):
            flags = int(frame[index]['zFlags'])
            return flags & 0x3fff if flags >> 14 == 2 else int(frame[index]['z'])
        order = ['background'] + [f'foreground-{index}' for index in sorted(frame, key=lambda index: (-depth(index), index))]
        original_composite = Image.new('RGBA', size)
        modern_composite = Image.new('RGBA', target)
        for name in order:
            with Image.open(original / f'{name}.png') as image:
                original_composite.alpha_composite(image.convert('RGBA'))
            with Image.open(output / f'{name}.png') as image:
                modern_composite.alpha_composite(image.convert('RGBA'))
    modern_composite.save(output / 'composite.png')
    original_composite = original_composite.resize(target, Image.Resampling.NEAREST)
    comparison = Image.new('RGB', (target[0] * 2, target[1]), '#202020')
    comparison.paste(original_composite, (0, 0), original_composite)
    comparison.paste(modern_composite, (target[0], 0), modern_composite)
    comparison.thumbnail((2400, 1600))
    comparison.save(output / 'comparison.png')
    validation = {
        'cut': cut, 'status': 'asset-preview-only', 'scale': 4,
        'runtimeInstalled': False, 'gameplayVerified': False,
        'stationaryLayersEstablished': False,
        'transparentRgbCleared': clear_transparent,
        'reusedLayerPlatesFrom': str(plate_directory) if plate_directory is not None else None,
        'composition': 'All-visible asset composite using final recorded metric depth order; character-dependent depth and alternate/hidden states require separate review.' if plate_directory is not None else 'All original layers in index order; alternate/hidden layers require separate review.',
        'layers': report,
    }
    (output / 'validation.json').write_text(json.dumps(validation, indent=2) + '\n')
    return {'cut': cut, 'layers': len(report), 'size': target, 'output': str(output)}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('cut', type=int)
    args = parser.parse_args()
    print(json.dumps(preview(args.cut)))
