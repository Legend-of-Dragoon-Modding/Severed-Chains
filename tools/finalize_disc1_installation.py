"""Finish approved step 3; do not promote loading checks to gameplay verification."""
import hashlib
import json
import re
import xml.etree.ElementTree as ET
from pathlib import Path

root = Path('docs/disc1-backgrounds/installation')
plan = json.loads((root / 'plan.json').read_text())
installed = []
smokes = []
test_count = 0
for batch in range(1, 6):
    report = json.loads((root / f'batch-{batch}.json').read_text())
    assert report['registered'] and report['checksPassed'] and report['packValidationPassed']
    expected = {job['cut']: job for job in plan['jobs'] if job['batch'] == batch}
    assert len(report['packaged']) == len(expected)
    suite = ET.parse(root / f'batch-{batch}-pack-validation.xml').getroot()
    assert int(suite.attrib['tests']) > int(suite.attrib.get('skipped', 0))
    assert int(suite.attrib['failures']) == int(suite.attrib['errors']) == 0
    test_count += int(suite.attrib['tests']) - int(suite.attrib.get('skipped', 0))
    for cut in report['loadingSmokeCuts']:
        assert cut in expected
        suite = ET.parse(root / f'batch-{batch}-loading-smoke-{cut}.xml').getroot()
        assert int(suite.attrib['tests']) == 1 and int(suite.attrib['failures']) == int(suite.attrib['errors']) == int(suite.attrib.get('skipped', 0)) == 0
        test_count += 1
        for name in ('modern-loaded.png', 'original-fallback.png', 'modern-restored.png'):
            assert Path(f'build/disc1/installation/smoke-cut-{cut}/{name}').is_file()
        smokes.append(cut)
    for pack in report['packaged']:
        job = expected.pop(pack['cut'])
        assert pack['copied']
        for layer in pack['files']:
            path = Path(job['directory']) / layer['name']
            assert layer['exactOriginalAlpha'] and path.stat().st_size == layer['bytes']
            assert hashlib.file_digest(path.open('rb'), 'sha256').hexdigest() == layer['sha256']
        installed.append(pack)
    assert not expected
assert len(installed) == 88 and set(smokes) == {78, 68, 101, 140, 171, 173}
baseline = json.loads((root / 'preservation-baseline.json').read_text())
changed = [name for name, digest in baseline.items() if not Path(name).is_file() or
           hashlib.file_digest(Path(name).open('rb'), 'sha256').hexdigest() != digest]
assert not changed, changed
assert not list(Path('gfx/backgrounds').rglob('*test-disabled*'))
assert not list(Path('gfx/backgrounds').rglob('*capture-disabled*'))
assert not list(Path('gfx/backgrounds').rglob('*.install.tmp'))
path = Path('src/main/java/legend/lodmod/SelesBackgrounds.java')
current = path.read_text()
ids = {job['cut'] for job in plan['jobs']}
cases = {int(value) for value in re.findall(r'case (\d+) ->', current)}
assert cases == set(plan['previouslyInstalled']) | ids
assert not cases.intersection(plan['blockedCuts'])
without_new_cases = '\n'.join(line for line in current.split('\n')
                              if not ((match := re.search(r'case (\d+) ->', line)) and int(match[1]) in ids))
without_new_cases = without_new_cases.replace('/** Disc 1 artwork packs; original assets remain the fallback. */',
                                             '/** Verified Disc 1 art packs; original assets remain the fallback. */')
assert without_new_cases == (root / 'SelesBackgrounds-before.java').read_text(), 'Existing mappings or fallback logic changed'
for cut in plan['blockedCuts']:
    assert not Path(f'gfx/backgrounds/disc1/cut-{cut}/background.png').exists()
summary = {'step': 3, 'complete': True, 'newRuntimePacks': 88, 'totalInstalledDisc1Packs': 153,
           'previouslyGameplayVerified': 65, 'newGameplayVerifications': 0,
           'unresolvedScreens': 64, 'texturePngs': sum(len(pack['files']) for pack in installed),
           'runtimeTextureBytes': sum(layer['bytes'] for pack in installed for layer in pack['files']),
           'loadingSmokeCuts': smokes, 'freshPassingTestCases': test_count,
           'protectedFilesUnchanged': len(baseline),
           'scope': 'Strict packaging, optimized textures, runtime registration, loading/fallback/restoration/cleanup checks. No full traversal comparison.',
           'screens': [{'cut': pack['cut'], 'batch': pack['batch']} for pack in installed]}
(root / 'summary.json').write_text(json.dumps(summary, indent=2) + '\n')
ledger_path = Path('docs/disc1-backgrounds/preparation/step-ledger.json')
ledger = json.loads(ledger_path.read_text())
ledger.update(completed=[2, 3], current=None, state='awaiting-step-4-approval',
              next={'step': 4, 'title': 'Resolve the remaining 64 screens'}, remaining=[4, 5, 6, 7])
ledger_path.write_text(json.dumps(ledger, indent=2) + '\n')
progress_path = Path('docs/disc1-backgrounds/progress.json')
progress = json.loads(progress_path.read_text())
progress['installationStep3'] = {key: value for key, value in summary.items() if key != 'screens'}
progress_path.write_text(json.dumps(progress, indent=2) + '\n')
section = f'''## Step 3 installed — awaiting step 4 approval

The user approved proceeding with 88 prepared screens and deferring the 12 new
layer blockers to step 4. All 88 are now strictly packaged, PNG-optimized,
registered and installed. Disc 1 has 153 installed packs: the 65 previously
verified packs plus 88 new packs with full gameplay verification still deferred.
The 64 unresolved screens remain on original retail backgrounds.

All five batches passed fresh, non-skipped pack-validation/fallback tests.
Renderer loading, missing-file fallback, malformed-file fallback, restoration and
texture-release checks passed for cuts {', '.join(map(str, smokes))}.
These are loading smoke checks, not original/modern traversal comparisons.
Existing background mappings and fallback logic were preserved. All
{len(baseline):,} protected files retained their hashes; the only runtime source
change is the addition of the 88 approved mappings in `SelesBackgrounds.java`.
No Dart/TwinShot code was changed, no comparator was relaxed, and nothing was
committed or pushed. The test-only Gradle init configuration enables test
discovery and the runner rejects skipped/stale results.

See `installation/summary.json`, per-batch reports and JUnit evidence for details.
The new runtime textures total {summary['runtimeTextureBytes'] / 1e6:.2f} MB.
Step 3 is complete. Do not begin step 4 without explicit user validation under
the requested step-exec workflow. Progress updates remain every 15–20 screens
with remaining work and a revised time estimate.

'''
for name in ('README.md', 'continuation.md'):
    path = Path('docs/disc1-backgrounds') / name
    heading, remaining = path.read_text(encoding='utf-8').split('\n', 1)
    assert '## Step 3 installed' not in remaining
    path.write_text(heading + '\n\n' + section + remaining.lstrip(), encoding='utf-8')
print(json.dumps({key: value for key, value in summary.items() if key != 'screens'}, indent=2))
