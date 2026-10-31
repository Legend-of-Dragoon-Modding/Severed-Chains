"""Worker 2 source preservation and asset preview; never installs runtime files."""
import json, pathlib, re, shutil, subprocess, sys
from PIL import Image
cut = int(sys.argv[1])
d = pathlib.Path(f'gfx/backgrounds/disc1/cut-{cut}/source')
p = json.loads((d / 'prompt.json').read_text())
output = re.search(r' as (.+?\.png) by default', p['generatedOutput']).group(1)
assert not (d / 'base.png').exists(), 'Never overwrite earlier source'
shutil.copy2(output, d / 'base.png')
w, h = Image.open(d / 'base.png').size
m = json.loads(pathlib.Path(f'build/disc1/original/cut-{cut}/manifest.json').read_text())
(d / 'placement.json').write_text(json.dumps(dict(mapping='full-canvas', width=w, height=h, stationary_layers_verified=False, gameplay='deferred', targetWidth=m['bounds'][2]*4, targetHeight=m['bounds'][3]*4), indent=2))
subprocess.run([sys.executable, 'tools/preview_disc1_artwork.py', str(cut)], check=True)
report_path = pathlib.Path('docs/disc1-backgrounds/worker-2.json')
report = json.loads(report_path.read_text())
row = next(x for x in report['screens'] if x['cut'] == cut)
row.update(status='artwork-preview-generated-awaiting-visual-review', source='base.png', prompt='prompt.json', preview='asset-preview/comparison.png', canvas=[m['bounds'][2]*4,m['bounds'][3]*4])
report_path.write_text(json.dumps(report, indent=2))
