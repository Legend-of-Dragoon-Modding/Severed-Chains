"""Audit exclusive optimization reports and save the consolidated storage result."""
import hashlib
import json
from pathlib import Path


root = Path('docs/disc1-backgrounds/storage-optimization')
plan = json.loads((root / 'plan.json').read_text())
screens = []
for worker in (1, 2, 3):
    report = json.loads((root / f'worker-{worker}.json').read_text())
    assert report['worker'] == worker and report['complete']
    jobs = {job['cut']: job for job in plan['jobs'] if job['worker'] == worker}
    assert len(report['screens']) == len(jobs)
    for screen in report['screens']:
        job = jobs.pop(screen['cut'])
        assert screen['directory'] == job['directory'] and screen['kind'] == job['kind']
        assert screen['beforeBytes'] == job['beforeBytes'] and screen['compositePixelsIdentical']
        assert [Path(layer['path']).name for layer in screen['layers']] == job['names']
        for layer in screen['layers']:
            path = Path(layer['path'])
            assert path.parent.as_posix() == job['directory']
            assert layer['dimensions'] == job['dimensions']
            assert layer['alphaExact'] and layer['visibleRgbExact']
            assert path.stat().st_size == layer['afterBytes'] <= layer['beforeBytes']
            assert hashlib.file_digest(path.open('rb'), 'sha256').hexdigest() == layer['afterSha256']
        screens.append(screen)
    assert not jobs
assert len(screens) == 217 and len({screen['cut'] for screen in screens}) == 217
baseline = json.loads((root / 'preservation-baseline.json').read_text())
changed = [name for name, expected in baseline.items()
           if not Path(name).is_file() or hashlib.file_digest(Path(name).open('rb'), 'sha256').hexdigest() != expected]
assert not changed, changed
installed_changes = sorted({screen['cut'] for screen in screens if screen['kind'] == 'installed'
                            and any(not layer['decodedPixelsIdentical'] for layer in screen['layers'])})
before = sum(screen['beforeBytes'] for screen in screens)
after = sum(screen['afterBytes'] for screen in screens)
assert before == plan['beforeBytes']
summary = {
    'screens': len(screens), 'texturePngs': sum(len(screen['layers']) for screen in screens),
    'beforeBytes': before, 'afterBytes': after, 'savedBytes': before - after,
    'savedPercent': 100 * (1 - after / before),
    'rewrittenPngs': sum(layer['rewritten'] for screen in screens for layer in screen['layers']),
    'visiblePixelsExact': True, 'alphaMasksExact': True, 'compositesExact': True,
    'protectedFilesUnchanged': len(baseline),
    'existingInstalledDecodedPixelsIdentical': not installed_changes,
    'installedScreensNeedingFilterCheck': installed_changes,
    'newRuntimeInstallations': 0, 'newGameplayVerifications': 0,
    'inGameFilterTestingPerformed': False,
    'byKind': {kind: {
        'screens': sum(screen['kind'] == kind for screen in screens),
        'beforeBytes': sum(screen['beforeBytes'] for screen in screens if screen['kind'] == kind),
        'afterBytes': sum(screen['afterBytes'] for screen in screens if screen['kind'] == kind),
    } for kind in ('installed', 'preview')},
}
assert summary['texturePngs'] == 2275
(root / 'summary.json').write_text(json.dumps(summary, indent=2) + '\n')
progress_path = Path('docs/disc1-backgrounds/progress.json')
progress = json.loads(progress_path.read_text())
assert len(progress['screens']) == 65 and progress['productionSummary']['reviewedSourcesWithUnresolvedIssues'] == 52
progress['storageOptimization'] = summary
progress_path.write_text(json.dumps(progress, indent=2) + '\n')
section = f'''## Texture storage optimization

All 217 visible Disc 1 screens (2,275 texture PNGs) were optimized in place after user approval.
Texture storage changed from {before / 1e9:.3f} GB to {after / 1e9:.3f} GB,
saving {(before - after) / 1e9:.3f} GB ({summary['savedPercent']:.1f}%).
Visible pixels, alpha masks, dimensions and all composited previews were checked exactly.
The 65 previously installed packs have decoded pixel identity: {not installed_changes}.
All {len(baseline):,} protected source/reference/code files retain their original hashes.

This intentionally updates texture PNG bytes from the earlier production preservation baseline.
Raw artwork, prompts, references, comparison images and gameplay code were preserved.
No new packs were installed and no new gameplay verification was performed.
The 100 reviewed source previews and 52 unresolved cases retain their existing production status.
In-game filtering for the new assets remains part of the deferred testing phase.
Exact per-file sizes and validation hashes are in `storage-optimization/worker-1.json`,
`worker-2.json`, `worker-3.json`; the consolidated result is `storage-optimization/summary.json`.
Future preview generation now clears transparent RGB and uses optimized PNG compression by default.

'''
for name in ('README.md', 'continuation.md'):
    path = Path('docs/disc1-backgrounds') / name
    text = path.read_text(encoding='utf-8')
    heading, rest = text.split('\n', 1)
    assert '## Texture storage optimization' not in rest
    path.write_text(heading + '\n\n' + section + rest.lstrip(), encoding='utf-8')
print(json.dumps(summary, indent=2))
