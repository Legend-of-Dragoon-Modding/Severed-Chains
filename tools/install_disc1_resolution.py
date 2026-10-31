"""Install the fully reviewed resolution batch, retaining backups of replaced packs."""
import hashlib
import json
import os
import re
import shutil
import tempfile
from pathlib import Path

from PIL import Image, ImageChops
from optimize_disc1_textures import optimize_image


def digest(path):
    with Path(path).open('rb') as stream:
        return hashlib.file_digest(stream, 'sha256').hexdigest()


def atomic_copy(source, target):
    with tempfile.NamedTemporaryFile(dir=target.parent, suffix='.install.tmp', delete=False) as stream:
        temporary = Path(stream.name)
    try:
        shutil.copyfile(source, temporary)
        assert digest(source) == digest(temporary)
        os.replace(temporary, target)
    finally:
        temporary.unlink(missing_ok=True)


def install():
    root = Path('docs/disc1-backgrounds/resolution')
    report_path = root / 'runtime-integration.json'
    assert not report_path.exists(), 'Inspect the existing installation report before retrying'
    summary_path = root / 'summary.json'
    summary = json.loads(summary_path.read_text())
    plan = json.loads((root / 'plan.json').read_text())
    assert summary['complete'] and summary['resolvedScreens'] == len(plan['screens']) == 72
    cuts = {row['cut'] for row in summary['screens']}
    assert len(summary['screens']) == len(cuts) == 72
    assert cuts == {job['cut'] for job in plan['screens']}, 'Audited summary does not match the installation plan'
    assert all(row['packagingPrepared'] and row['assetChecksPassed'] for row in summary['screens'])
    quarantine = json.loads((root / 'quarantined-installed.json').read_text())
    replaced = set(quarantine['cuts'])
    assert replaced == {job['cut'] for job in plan['screens'] if job.get('wasInstalled')}
    hook = Path('src/main/java/legend/lodmod/SelesBackgrounds.java')
    before_hook = hook.read_text()
    current_cases = set(map(int, re.findall(r'^      case (\d+) ->', before_hook, re.M)))
    assert len(current_cases) == 145 and not cuts & current_cases
    assert len(current_cases | cuts) == 217
    protected = json.loads((root / 'preservation-baseline.json').read_text())
    for path, expected in protected.items():
        if path != hook.as_posix():
            assert digest(path) == expected, f'Protected file changed before install: {path}'
    backup = Path('build/disc1/resolution-runtime-backup')
    assert not backup.exists(), 'Do not overwrite a previous runtime backup'
    packs = []
    # Validate every input before changing any runtime texture or mapping.
    for row in summary['screens']:
        cut = row['cut']
        manifest = json.loads(Path(f'build/disc1/original/cut-{cut}/manifest.json').read_text())
        size = tuple(value * 4 for value in manifest['bounds'][2:])
        names = ['background'] + [f'foreground-{i}' for i in range(manifest['foreground_layers'])]
        assert [entry['name'] for entry in row['files']] == names
        assert digest(row['validation']) == row['validationSha256']
        for entry in row['files']:
            staged = Path(f'build/disc1/staged/cut-{cut}/{entry["name"]}.png')
            target = Path(f'gfx/backgrounds/disc1/cut-{cut}/{entry["name"]}.png')
            assert digest(staged) == entry['sha256']
            assert target.exists() == (cut in replaced)
            with Image.open(staged) as image, Image.open(f'build/disc1/original/cut-{cut}/{entry["name"]}.png') as original:
                assert image.mode == 'RGBA' and image.size == size
                assert ImageChops.difference(image.getchannel('A'), original.getchannel('A').resize(size, Image.Resampling.NEAREST)).getbbox() is None
        packs.append({'cut':cut, 'width':size[0], 'height':size[1], 'layers':len(names)-1, 'files':row['files'], 'copied':False})
    backup.mkdir(parents=True)
    (backup / 'SelesBackgrounds.java').write_text(before_hook)
    report = {'complete':False, 'registered':False, 'checksPassed':False, 'gameplayVerified':False,
              'replacedCuts':sorted(replaced), 'backupDirectory':backup.as_posix(), 'packs':packs,
              'scope':'Runtime installation; focused GPU/state checks pending. No full story/traversal claim.'}
    report_path.write_text(json.dumps(report,indent=2)+'\n')
    for index, (pack, row) in enumerate(zip(packs, summary['screens'])):
        cut = pack['cut']
        validation_path = Path(row['validation'])
        validation = json.loads(validation_path.read_text())
        for entry in pack['files']:
            name = entry['name']+'.png'
            staged = Path(f'build/disc1/staged/cut-{cut}/{name}')
            target = Path(f'gfx/backgrounds/disc1/cut-{cut}/{name}')
            if cut in replaced:
                saved = backup / f'cut-{cut}' / name
                saved.parent.mkdir(exist_ok=True)
                atomic_copy(target, saved)
                entry['previousSha256'] = digest(saved)
            optimization = optimize_image(staged)
            assert optimization['decodedPixelsIdentical']
            entry.update(sha256=digest(staged), bytes=staged.stat().st_size, optimization=optimization)
            if isinstance(validation, dict):
                next(layer for layer in validation['layers'] if layer['name'] == entry['name'])['sha256'] = entry['sha256']
            atomic_copy(staged, target)
        if isinstance(validation, dict):
            validation_path.write_text(json.dumps(validation,indent=2)+'\n')
        row['validationSha256'] = digest(validation_path)
        summary_path.write_text(json.dumps(summary,indent=2)+'\n')
        pack['copied'] = True
        report_path.write_text(json.dumps(report,indent=2)+'\n')
        if (index+1)%15 == 0 or index+1 == len(packs):
            print(f'Installed {index+1}/{len(packs)} corrected/new packs',flush=True)
    after_hook = before_hook
    for pack in packs:
        entry = f'      case {pack["cut"]} -> {{ directory = Path.of("gfx/backgrounds/disc1/cut-{pack["cut"]}"); width = {pack["width"]}; height = {pack["height"]}; layers = {pack["layers"]}; }}'
        assert after_hook.count('      default -> { return; }') == 1
        after_hook = after_hook.replace('      default -> { return; }',entry+'\n      default -> { return; }')
    assert set(map(int,re.findall(r'^      case (\d+) ->',after_hook,re.M))) == current_cases | cuts
    assert '\n'.join(line for line in after_hook.splitlines() if not any(line.startswith(f'      case {cut} ->') for cut in cuts))+'\n' == before_hook
    assert hook.read_text() == before_hook, 'Runtime mappings changed during installation'
    (backup/'SelesBackgrounds-after.java').write_text(after_hook)
    atomic_copy(backup/'SelesBackgrounds-after.java', hook)
    report.update(registered=True, complete=True, hookSha256=digest(hook), activePacks=217)
    report_path.write_text(json.dumps(report,indent=2)+'\n')
    quarantine.update(cuts=[], repairedCuts=sorted(replaced), reason='Corrected independent plates installed; focused renderer verification pending.')
    (root/'quarantined-installed.json').write_text(json.dumps(quarantine,indent=2)+'\n')
    Path('docs/disc1-backgrounds/installation/active-packs.csv').write_text(''.join(f'{pack["cut"]},{pack["width"]},{pack["height"]},{pack["layers"]}\n' for pack in packs))
    print('217 packs registered; run fresh pack/fallback and focused renderer checks next',flush=True)


if __name__ == '__main__':
    install()
