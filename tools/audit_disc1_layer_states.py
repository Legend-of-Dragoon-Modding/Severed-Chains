"""Describe recorded original layer states without treating them as route coverage."""
import csv
import json
from pathlib import Path

root = Path('docs/disc1-backgrounds/resolution')
plan = json.loads((root / 'plan.json').read_text())
screens = []
for job in plan['screens']:
    cut = job['cut']
    capture = Path(f'build/disc1/captures/cut-{cut}/original')
    trace = capture / 'alignment.csv'
    if not trace.is_file():
        continue
    manifest = json.loads(Path(f'build/disc1/original/cut-{cut}/manifest.json').read_text())
    layers = {i: set() for i in range(manifest['foreground_layers'])}
    frames = {}
    with trace.open(newline='') as stream:
        for row in csv.DictReader(stream):
            assert int(row['cut']) == cut
            layer, frame = int(row['layer']), int(row['frame'])
            assert layer in layers and row['hidden'] in ('true', 'false')
            state = tuple(int(row[k]) for k in ('startX', 'startY', 'destX', 'destY')) + (row['hidden'] == 'true',)
            layers[layer].add(state)
            assert layer not in frames.setdefault(frame, set())
            frames[frame].add(layer)
    complete = bool(frames) and all(len(ids) == len(layers) for ids in frames.values())
    evidence = capture / 'original-gpu-export.txt'
    gpu = evidence.is_file() and evidence.read_text() == f'Original GPU textures exactly match exported PNGs for cut {cut}'
    completion = (capture / 'completion.txt').read_text() if (capture / 'completion.txt').exists() else ''
    passed = complete and gpu and completion.startswith('Inspection only;')
    screens.append({'cut': cut, 'inspectionPassed': passed, 'frames': len(frames),
                    'trace': trace.as_posix(), 'gpuExportMatches': gpu,
                    'allObservedLayersFixedVisible': passed and all(states == {(0, 0, 0, 0, False)} for states in layers.values()),
                    'layers': [{'layer': i, 'states': [dict(zip(('startX', 'startY', 'destX', 'destY', 'hidden'), state))
                                                      for state in sorted(states)]} for i, states in layers.items()],
                    'fullGameplayVerified': False})
summary = {'inspectionsPresent': len(screens), 'passedInspections': sum(s['inspectionPassed'] for s in screens),
           'totalCandidates': len(plan['screens']), 'screens': screens}
(root / 'original-states.json').write_text(json.dumps(summary, indent=2) + '\n')
print({key: value for key, value in summary.items() if key != 'screens'})
