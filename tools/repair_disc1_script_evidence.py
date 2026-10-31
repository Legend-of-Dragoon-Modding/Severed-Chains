"""Associate each environment with the scripts the retail loader actually reads."""
import hashlib
import json
import re
from pathlib import Path

POSITION = 'SMap::scriptSetEnvForegroundPosition,'
MODE = 'SMap::scriptSetModeParamForNextCallToScriptSetCameraOffsetOrHideSubmapForegroundObject,'


def number(value):
    try:
        return int(value, 0)
    except ValueError:
        return None


def classify(line, foregrounds):
    if POSITION in line:
        args = line.split(POSITION, 1)[1].split(';', 1)[0].strip().split(', ')
        assert len(args) == 3, line
        x, y, index = map(number, args)
        kind = 'show-at-origin' if x == y == 0 else 'foreground-hide' if x == y == 1024 else 'foreground-move'
    elif MODE in line:
        args = line.split(MODE, 1)[1].split(';', 1)[0].strip().split(', ')
        assert len(args) == 3, line
        mode, index = map(number, args[:2])
        kind = 'foreground-hide' if mode == 3 else 'foreground-state-unknown' if mode is None else 'camera-mode-only'
    else:
        return None
    if index is not None and index >= foregrounds and kind.startswith('foreground-'):
        kind = 'untextured-slot-only'
    return kind


def repair():
    root = Path('docs/disc1-backgrounds')
    path = root / 'production-plan.json'
    plan = json.loads(path.read_text())
    installation = json.loads((root / 'installation/plan.json').read_text())
    installed = {row['cut'] for row in installation['jobs']}
    override_path = root / 'resolution/script-review-overrides.json'
    overrides = {row['cut']: row for row in json.loads(override_path.read_text())} if override_path.exists() else {}
    changed = []
    for row in plan['screens']:
        cut = row['cut']
        manifest = json.loads(Path(f'build/disc1/original/cut-{cut}/manifest.json').read_text())
        index = int(Path(manifest['source']).name) + 2
        directory = Path(f'build/disc1/scripts/{index}')
        assert (directory / '0.txt').is_file()
        calls = []
        hashes = {}
        for file in sorted(directory.glob('*.txt')):
            hashes[file.as_posix()] = hashlib.sha256(file.read_bytes()).hexdigest()
            for line, text in enumerate(file.read_text().splitlines(), 1):
                kind = classify(text, manifest['foreground_layers'])
                if kind is not None:
                    calls.append({'file': file.as_posix(), 'line': line, 'call': text, 'classification': kind})
        previous = row['scriptDirectory']
        row.update(scriptDirectory=directory.as_posix(), layerCalls=calls,
                   layerRisks=[call for call in calls if call['classification'].startswith('foreground-')],
                   scriptAssociationEvidence='RetailSubmap.loadAssets: scriptsFuture loads environment fileIndex+2; prepareSobjs uses the same +2 path',
                   scriptFileHashes=hashes)
        if cut in overrides:
            review = overrides[cut]
            script = Path(review['file'])
            assert script.parent == directory and hashes[script.as_posix()] == review['sha256']
            text = script.read_text()
            assert re.search(r'^' + review['tableLabel'] + r':\r?\ndata 0xff\r?\n', text, re.M)
            assert all(risk['file'] == script.as_posix() and POSITION in risk['call'] for risk in row['layerRisks'])
            row['reviewedFalsePositiveRisks'] = row['layerRisks']
            row['layerRisks'] = []
            row['layerRiskReview'] = review
        changed.append({'cut': cut, 'previousDirectory': previous, 'actualDirectory': directory.as_posix(),
                        'potentialLayerRisks': len(row['layerRisks']), 'runtimeInstalled': cut in installed})
    plan['scriptAssociationCorrected'] = True
    path.write_text(json.dumps(plan, indent=2) + '\n')
    report = {'affectedMetadata': len(changed), 'newlyInstalledNeedingScriptReview': [row['cut'] for row in changed if row['runtimeInstalled'] and row['potentialLayerRisks']],
              'screens': changed, 'warning': 'Generic calls and variable indices require table/path analysis. Scanner hits are potential risks, not confirmed dynamic objects.'}
    (root / 'resolution/script-association-repair.json').write_text(json.dumps(report, indent=2) + '\n')
    print({key: value for key, value in report.items() if key != 'screens'})


if __name__ == '__main__':
    repair()
