"""Export Disc 1 candidate environments; record failures without hiding them."""
import contextlib
import json
from pathlib import Path
from export_seles import export

inventory = json.loads(Path('docs/disc1-backgrounds/inventory.json').read_text())
root = Path('build/disc1')
root.mkdir(parents=True, exist_ok=True)
results = []
with (root / 'export.log').open('w') as log, contextlib.redirect_stdout(log):
    for row in inventory:
        cut = row['cut']
        try:
            metadata = export(cut, root / 'original' / f'cut-{cut}')
            results.append({'cut': cut, 'status': 'exported', 'bounds': metadata['bounds']})
        except Exception as error:
            results.append({'cut': cut, 'status': 'failed', 'error': repr(error)})
(root / 'export-report.json').write_text(json.dumps(results, indent=2))
print(json.dumps({'exported': sum(r['status'] == 'exported' for r in results),
                  'failed': [r for r in results if r['status'] == 'failed']}))
