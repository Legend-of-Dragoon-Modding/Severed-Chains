"""Package only approved step-2 candidates; register one complete batch at a time."""
import argparse
import hashlib
import json
import os
import re
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

from PIL import Image, ImageChops
from optimize_disc1_textures import optimize_image

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('--batch', type=int, choices=range(1, 6), required=True)
mode = parser.add_mutually_exclusive_group()
mode.add_argument('--package-only', action='store_true')
mode.add_argument('--register-only', action='store_true')
args = parser.parse_args()
root = Path('docs/disc1-backgrounds/installation')
plan = json.loads((root / 'plan.json').read_text())
jobs = [job for job in plan['jobs'] if job['batch'] == args.batch]
report_path = root / f'batch-{args.batch}.json'
report = json.loads(report_path.read_text()) if report_path.exists() else {'batch': args.batch, 'packaged': [], 'registered': False, 'checksPassed': False}
if args.register_only:
    assert len(report['packaged']) == len(jobs) and all(entry['copied'] for entry in report['packaged'])

def save():
    temporary = report_path.with_suffix('.json.tmp')
    temporary.write_text(json.dumps(report, indent=2) + '\n')
    temporary.replace(report_path)

for job in jobs:
    cut = job['cut']
    prior = next((entry for entry in report['packaged'] if entry['cut'] == cut), None)
    directory = Path(job['directory'])
    staged = Path(f'build/disc1/staged/cut-{cut}')
    if prior is None:
        assert not any((directory / name).exists() for name in job['names']), 'Unexpected existing runtime files'
        command = ['rtk', 'proxy', 'node', 'tools/package_disc1_static.cjs',
                   'C:/Users/20te/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp', str(cut)]
        result = subprocess.run(command, capture_output=True, text=True, timeout=180)
        (root / f'package-cut-{cut}.log').write_text(result.stdout + result.stderr)
        assert result.returncode == 0, f'Strict packaging failed for cut {cut}'
        checks = json.loads((staged / 'validation.json').read_text())
        assert [entry['name'] + '.png' for entry in checks] == job['names']
        layers = []
        for name in job['names']:
            path = staged / name
            optimization = optimize_image(path)
            assert optimization['decodedPixelsIdentical'], 'Strict packager already clears invisible RGB'
            with Image.open(path) as image, Image.open(f'build/disc1/original/cut-{cut}/{name}') as original:
                assert image.mode == 'RGBA' and image.size == (job['width'], job['height'])
                alpha = original.getchannel('A').resize(image.size, Image.Resampling.NEAREST)
                assert ImageChops.difference(image.getchannel('A'), alpha).getbbox() is None
            layers.append({'name': name, 'bytes': path.stat().st_size,
                           'sha256': hashlib.file_digest(path.open('rb'), 'sha256').hexdigest(), 'exactOriginalAlpha': True})
        prior = {'cut': cut, 'batch': args.batch, 'width': job['width'], 'height': job['height'],
                 'layers': job['layers'], 'files': layers, 'copied': False}
        report['packaged'].append(prior)
        save()
    for layer in prior['files']:
        source, target = staged / layer['name'], directory / layer['name']
        assert hashlib.file_digest(source.open('rb'), 'sha256').hexdigest() == layer['sha256']
        if target.exists():
            assert hashlib.file_digest(target.open('rb'), 'sha256').hexdigest() == layer['sha256']
        else:
            handle = tempfile.NamedTemporaryFile(dir=directory, suffix='.install.tmp', delete=False)
            temporary = Path(handle.name)
            handle.close()
            try:
                shutil.copyfile(source, temporary)
                os.replace(temporary, target)
            finally:
                temporary.unlink(missing_ok=True)
    prior['copied'] = True
    save()
    print(f'PACKAGED batch={args.batch} cut={cut} completed={sum(entry["copied"] for entry in report["packaged"])}/{len(jobs)}', flush=True)

if args.package_only:
    print(f'Batch {args.batch} packaged; shared runtime registration left to parent.', flush=True)
    sys.exit(0)

path = Path('src/main/java/legend/lodmod/SelesBackgrounds.java')
text = path.read_text()
for job in jobs:
    entry = f'      case {job["cut"]} -> {{ directory = Path.of("{job["directory"]}"); width = {job["width"]}; height = {job["height"]}; layers = {job["layers"]}; }}'
    matches = re.findall(r'^\s*case ' + str(job['cut']) + r' -> .*$', text, re.M)
    if matches:
        assert len(matches) == 1 and matches[0].strip() == entry.strip()
    else:
        assert text.count('      default -> { return; }') == 1
        text = text.replace('      default -> { return; }', entry + '\n      default -> { return; }')
text = text.replace('/** Verified Disc 1 art packs; original assets remain the fallback. */',
                    '/** Disc 1 artwork packs; original assets remain the fallback. */')
path.write_text(text)
report['registered'] = True
save()
active = jobs  # Test this integration batch; unchanged earlier packs are already covered.
(root / 'active-packs.csv').write_text('\n'.join(f'{job["cut"]},{job["width"]},{job["height"]},{job["layers"]}' for job in active) + '\n')
print(f'Batch {args.batch} registered; loading/fallback verification still required.', flush=True)
