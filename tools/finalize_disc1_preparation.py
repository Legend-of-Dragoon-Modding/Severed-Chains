"""Step 2 only: validate recorded original layers and annotate packaging inputs."""
import csv
import json
import xml.etree.ElementTree as ET
from pathlib import Path

root = Path('docs/disc1-backgrounds/preparation')
audit = json.loads((root / 'asset-audit.json').read_text())
progress_path = Path('docs/disc1-backgrounds/progress.json')
progress = json.loads(progress_path.read_text())
plans = {screen['cut']: screen for screen in json.loads(Path('docs/disc1-backgrounds/production-plan.json').read_text())['screens']}
production = {screen['cut']: screen for screen in progress['productionScreens']}
assert audit['candidateCount'] == len(audit['screens']) == 100
evidence = []
blocked = []
placements = {}
for screen in audit['screens']:
    cut = screen['cut']
    assert production[cut]['status'] == 'artwork-preview-ready' and not plans[cut]['layerRisks']
    directory = Path(f'build/disc1/captures/cut-{cut}/original')
    violations = []
    try:
        assert (directory / 'completion.txt').read_text().startswith('Inspection only;'), 'No successful inspection completion'
        gpu = directory / 'original-gpu-export.txt'
        if cut == 78 and not gpu.exists():
            suite = ET.parse(root / 'cut-78-initial-inspection.xml').getroot()
            assert suite.attrib['failures'] == suite.attrib['errors'] == '0'
            assert suite.attrib['tests'] == '1'
            gpu_evidence = (root / 'cut-78-initial-inspection.xml').as_posix()
        else:
            assert gpu.read_text() == f'Original GPU textures exactly match exported PNGs for cut {cut}'
            gpu_evidence = gpu.as_posix()
        with (directory / 'alignment.csv').open() as file:
            rows = list(csv.DictReader(file))
        assert rows, 'An actual original recording is required'
        frames = sorted({int(row['frame']) for row in rows})
        assert frames == list(range(len(frames)))
        foregrounds = len(screen['layers']) - 1
        assert len(rows) == len(frames) * foregrounds
        for frame in frames:
            indices = [int(row['layer']) for row in rows if int(row['frame']) == frame]
            assert indices == list(range(foregrounds)), 'Missing or repeated foreground layer'
        for row in rows:
            assert int(row['cut']) == cut
        for layer in range(foregrounds):
            samples = [row for row in rows if int(row['layer']) == layer and
                       (row['hidden'] != 'false' or any(row[key] != '0' for key in ('startX', 'startY', 'destX', 'destY')))]
            if samples:
                violations.append({'layer': layer, 'firstFrame': int(samples[0]['frame']),
                                   'lastFrame': int(samples[-1]['frame']),
                                   'states': [list(state) for state in sorted({tuple(row[key] for key in
                                       ('startX', 'startY', 'destX', 'destY', 'hidden')) for row in samples})]})
        assert not violations, 'Hidden or offset foreground layers: ' + ', '.join(str(item['layer']) for item in violations)
        end_file = directory / 'original-inspection-end.txt'
        assert len(frames) == 121 or end_file.is_file(), 'Short inspection needs an observed original exit'
        item = {'cut': cut, 'entry': screen['scene'], 'frames': len(frames),
                'trace': (directory / 'alignment.csv').as_posix(),
                'gpuExportEvidence': gpu_evidence, 'allRecordedLayersFixedVisible': True,
                'assetChecksPassed': screen['assetChecksPassed'],
                'entryEvidence': screen['entryEvidence'],
                'inspectionEnd': end_file.read_text() if end_file.exists() else '121-frame inspection limit',
                'fullTraversalVerified': False, 'gameplayVerified': False, 'runtimeInstalled': False}
        evidence.append(item)
        source = Path(production[cut]['source'])
        placements[(source / 'placement.json').as_posix()] = (source / 'placement.json').read_text()
    except (AssertionError, OSError, ValueError) as error:
        blocked.append({'cut': cut, 'reason': str(error), 'trace': (directory / 'alignment.csv').as_posix(),
                        'violations': violations})

summary = {'step': 2, 'candidateCount': 100, 'assetAuditsPassed': 100,
           'originalLayerInspectionsCompleted': 100,
           'staticPackagingGatePassed': len(evidence), 'blocked': blocked,
           'complete': not blocked, 'packaged': 0, 'newRuntimeInstallations': 0,
           'newGameplayVerifications': 0, 'evidence': evidence}
(root / 'summary.json').write_text(json.dumps(summary, indent=2) + '\n')
assert len(evidence) + len(blocked) == 100
backup = root / 'placement-before.json'
assert not backup.exists(), 'Do not overwrite the original preparation metadata backup'
backup.write_text(json.dumps(placements, indent=2) + '\n')
for item in evidence:
    screen = production[item['cut']]
    path = Path(screen['source']) / 'placement.json'
    placement = json.loads(path.read_text())
    placement['stationary_layers_verified'] = True
    placement['layerInspection'] = item
    placement['evidence'] = f"Original GPU/export comparison and {item['frames']} recorded frames; inspection only, not traversal/gameplay verification. See {item['trace']}."
    path.write_text(json.dumps(placement, indent=2) + '\n')
    screen['packagingPrepared'] = True
    screen['packagingBlocker'] = None
    screen['originalLayerEvidence'] = item['trace']
progress['preparationStep2'] = {'complete': not blocked, 'state': 'blocked' if blocked else 'complete',
                              'candidates': 100, 'assetAuditsPassed': 100,
                              'originalLayerInspectionsCompleted': 100,
                              'staticPackagingGatePassed': len(evidence), 'blockedCuts': [item['cut'] for item in blocked],
                              'newRuntimeInstallations': 0,
                              'newGameplayVerifications': 0, 'report': (root / 'summary.json').as_posix()}
progress_path.write_text(json.dumps(progress, indent=2) + '\n')
(root / 'step-ledger.json').write_text(json.dumps({'completed': [] if blocked else [2], 'current': 2 if blocked else None,
    'state': 'blocked-awaiting-scope-decision' if blocked else 'awaiting-next-step-approval',
    'next': {'step': 3, 'title': 'Package and install those screens in small batches'},
    'remaining': [3, 4, 5, 6, 7], 'step1': 'Deferred: user explicitly started at step 2',
    'requiresExplicitUserApproval': True}, indent=2) + '\n')
print(json.dumps({'assetAudits': 100, 'originalInspections': 100, 'prepared': len(evidence),
                  'blocked': blocked, 'nextStepStarted': False}, indent=2))
