"""Identify exact retail texture matches; gameplay still needs per-cut verification."""
import hashlib
import json
from pathlib import Path
from PIL import Image

groups = {}
for directory in Path('build/disc1/original').glob('cut-*'):
    manifest = json.loads((directory / 'manifest.json').read_text())
    digest = hashlib.sha256()
    names = ['background.png'] + [f'foreground-{i}.png' for i in range(manifest['foreground_layers'])]
    for name in names:
        with Image.open(directory / name) as image:
            image = image.convert('RGBA')
            digest.update(f'{name}:{image.size}:'.encode())
            digest.update(image.tobytes())
    groups.setdefault(digest.hexdigest(), []).append(manifest['cut'])
matches = [{'cuts': sorted(cuts), 'rgba_sha256': digest} for digest, cuts in groups.items() if len(cuts) > 1]
matches.sort(key=lambda group: group['cuts'][0])
Path('docs/disc1-backgrounds/reuse-candidates.json').write_text(json.dumps(matches, indent=2) + '\n')
print(json.dumps({'exactMatchGroups': len(matches), 'cutsInGroups': sum(len(group['cuts']) for group in matches)}))
