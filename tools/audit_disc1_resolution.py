"""Audit prepared artwork and any separately recorded runtime installation."""
import hashlib
import json
from pathlib import Path

from PIL import Image, ImageChops


def digest(path):
    with path.open('rb') as stream:
        return hashlib.file_digest(stream, 'sha256').hexdigest()


root = Path('docs/disc1-backgrounds/resolution')
plan = json.loads((root / 'plan.json').read_text())
states = {row['cut']: row for row in json.loads((root / 'original-states.json').read_text())['screens']}
runtime_path = root / 'runtime-integration.json'
runtime = json.loads(runtime_path.read_text()) if runtime_path.exists() else {}
installed = {row['cut']: row for row in runtime.get('packs', []) if row['copied']} if runtime.get('registered') and runtime.get('complete') else {}
ready = {}
for worker in ('early', 'middle', 'final'):
    path = root / f'{worker}.json'
    if path.exists():
        for row in json.loads(path.read_text())['screens']:
            if row.get('ready', row.get('resolved', False)):
                assert row['cut'] not in ready
                ready[row['cut']] = (row, path)
for path in root.glob('cut-*.json'):
    row = json.loads(path.read_text())
    if row.get('packagingPrepared'):
        assert row['cut'] not in ready
        ready[row['cut']] = (row, path)
assert set(ready) <= {job['cut'] for job in plan['screens']}
reports = []
for job in plan['screens']:
    cut = job['cut']
    if cut not in ready:
        reports.append({'cut': cut, 'packagingPrepared': False, 'assetChecksPassed': False,
                        'runtimeInstalled': False, 'gameplayVerified': False})
        continue
    row, report_path = ready[cut]
    assert not row.get('gameplayVerified')
    evidence = states[cut]
    assert evidence['inspectionPassed'] and evidence['gpuExportMatches']
    original = Path(f'build/disc1/original/cut-{cut}')
    source = Path(f'gfx/backgrounds/disc1/cut-{cut}/source')
    staged = Path(f'build/disc1/staged/cut-{cut}')
    manifest = json.loads((original / 'manifest.json').read_text())
    size = tuple(value * 4 for value in manifest['bounds'][2:])
    config_path = source / 'layer-resolution/plates.json'
    if config_path.exists() and json.loads(config_path.read_text())['dynamicLayers']:
        config = json.loads(config_path.read_text())
        assert config['visualReview'] is True
        validation = staged / 'layered-validation.json'
        record = json.loads(validation.read_text())
        assert record['configSha256'] == digest(config_path) and record['traceSha256'] == digest(Path(evidence['trace'])), f'Cut {cut}: plate config changed after staging; rerun the layered packager'
        assert record['independentLayerPlates'] and record['gameplayVerified'] is False
        if record.get('sharedSceneryLayers'):
            review = record['sharedSceneryReview']
            assert digest(Path(review['source'])) == review['sha256']
        for layer in record['layers']:
            assert digest(Path(layer['source'])) == layer['sourceSha256']
            if 'retailExteriorMaskSource' in layer:
                assert digest(Path(layer['retailExteriorMaskSource'])) == layer['retailExteriorMaskSha256']
            for patch in layer.get('patchSources', []):
                assert digest(Path(patch['source'])) == patch['sourceSha256']
                if 'maskLayer' in patch:
                    assert digest(Path(patch['maskSource'])) == patch['maskSha256']
                if 'backgroundMaskSource' in patch:
                    assert digest(Path(patch['backgroundMaskSource'])) == patch['backgroundMaskSha256']
            assert digest(staged / f"{layer['name']}.png") == layer['sha256']
        for label in ('all-visible', 'dynamic-hidden', 'recorded-start', 'recorded-dest'):
            assert (config_path.parent / f'{label}.png').is_file()
        method = 'native-depth-masks-with-reviewed-shared-scenery' if record.get('sharedSceneryLayers') else 'independent-layer-plates'
    else:
        assert evidence['allObservedLayersFixedVisible']
        assert json.loads((source / 'placement.json').read_text())['stationary_layers_verified'] is True
        validation = staged / 'validation.json'
        record = json.loads(validation.read_text())
        assert len(record) == manifest['foreground_layers'] + 1
        assert all(layer['exactOriginalMask'] and (layer['width'], layer['height']) == size for layer in record)
        method = 'strict-static-with-script-evidence'
    files = []
    for name in ['background'] + [f'foreground-{i}' for i in range(manifest['foreground_layers'])]:
        with Image.open(original / f'{name}.png') as image:
            alpha = image.getchannel('A').resize(size, Image.Resampling.NEAREST)
        for directory in (staged, source / 'asset-preview'):
            with Image.open(directory / f'{name}.png') as image:
                assert image.size == size and image.mode == 'RGBA'
                assert ImageChops.difference(image.getchannel('A'), alpha).getbbox() is None
        path = staged / f'{name}.png'
        files.append({'name': name, 'bytes': path.stat().st_size, 'sha256': digest(path)})
    if cut in installed:
        assert {item['name']:item['sha256'] for item in files} == {item['name']:item['sha256'] for item in installed[cut]['files']}
        for item in files:
            assert digest(source.parent / (item['name']+'.png')) == item['sha256']
    elif not job.get('wasInstalled'):
        assert not (source.parent / 'background.png').exists(), 'Step 4 stages artwork; installed packs remain protected'
    reports.append({'cut': cut, 'packagingPrepared': True, 'assetChecksPassed': True,
                    'trace': evidence['trace'], 'validation': validation.as_posix(), 'validationSha256': digest(validation),
                    'reviewEvidence': report_path.as_posix(), 'method': method, 'files': files,
                    'runtimeInstalled': cut in installed, 'runtimeChecksPassed': bool(runtime.get('checksPassed')) if cut in installed else False, 'gameplayVerified': False})
passed = [row for row in reports if row['packagingPrepared']]
summary = {'step': 4, 'complete': len(passed) == len(plan['screens']), 'resolvedScreens': len(passed),
           'remainingScreens': len(plan['screens']) - len(passed),
           'scope': 'Reviewed geometry, state plates and explicitly reviewed shared scenery with exact retail masks. Runtime installation/checks tracked separately. Full story/traversal deferred.',
           'screens': reports}
(root / 'summary.json').write_text(json.dumps(summary, indent=2) + '\n')
print({key: value for key, value in summary.items() if key != 'screens'})
