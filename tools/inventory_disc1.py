"""Inventory retail Disc 1 environments; cut IDs are NOT chronological order."""
import csv
import json
import re
import struct
from pathlib import Path

root = Path('files/SUBMAP/NEWROOT.RDT').read_bytes()
java = Path('src/main/java/legend/game/submap/RetailSubmap.java').read_text()
areas = list(map(int, re.search(r'cutToSubmap_800d610c = \{(.*?)\};', java, re.S)[1].replace('\n', '').replace(' ', '').strip(',').split(',')))
names_java = Path('src/main/java/legend/game/SItem.java').read_text()
names = re.findall(r'"([^"]*)"', re.search(r'submapNames_8011c108 = \{(.*?)\};', names_java, re.S)[1])
rows = []
progress_path = Path('docs/disc1-backgrounds/progress.json')
state = json.loads(progress_path.read_text()) if progress_path.exists() else {}
progress = {row['cut']: row for row in state.get('screens', [])}
production = {row['cut']: row for row in state.get('productionScreens', [])}
for cut, area in enumerate(areas):
    early, late, offset, transitions = struct.unpack_from('<HHhH', root, cut * 8)
    if early >> 13 != 0 or area == 58:
        continue
    index = (early & 8191) * 3 + 4
    source = Path(f'files/SECT/DRGN21.BIN/{index}')
    if not (source / '0').is_file():
        continue
    env = (source / '0').read_bytes()
    if len(env) < 24:
        continue
    total, backgrounds, foregrounds = env[20:23]
    if not backgrounds or total != backgrounds + foregrounds or len(env) < 24 + total * 36:
        continue
    exits = []
    for i in range(transitions if offset >= 0 else 0):
        value = struct.unpack_from('<I', root, 8192 + offset + i * 4)[0]
        if value & 16:
            exits.append({'cut': value >> 22, 'scene': value >> 16 & 63})
    rows.append(dict(cut=cut, area=names[area], area_id=area, source=str(source),
                     backgrounds=backgrounds, foregrounds=foregrounds, exits=exits,
                     status='excluded-debug-menu' if cut <= 4 or cut == 791 else 'installed' if production.get(cut, {}).get('runtimeInstalled') else progress.get(cut, {}).get('artwork', 'pending'),
                     chronological_order=progress.get(cut, {}).get('order')))
    if cut in production:
        rows[-1]['production_status'] = production[cut]['status']
        if production[cut].get('runtimeInstalled'):
            rows[-1]['gameplay_verification'] = 'deferred'
output = Path('docs/disc1-backgrounds')
output.mkdir(parents=True, exist_ok=True)
(output / 'inventory.json').write_text(json.dumps(rows, indent=2))
with (output / 'inventory.csv').open('w', newline='') as stream:
    writer = csv.DictWriter(stream, fieldnames=['cut', 'area', 'source', 'backgrounds', 'foregrounds', 'status', 'production_status', 'gameplay_verification'])
    writer.writeheader()
    writer.writerows({key: row.get(key, '') for key in writer.fieldnames} for row in rows)
print(json.dumps({'cuts': len(rows), 'unique_environments': len({r['source'] for r in rows}),
                  'areas': {name: sum(r['area'] == name for r in rows) for name in dict.fromkeys(r['area'] for r in rows)},
                  'intro': [r['cut'] for r in rows if r['area_id'] == 57]}))
