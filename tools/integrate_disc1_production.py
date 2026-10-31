"""Merge exclusive worker reports without changing accepted gameplay evidence."""
import csv
import json
from pathlib import Path


root = Path('docs/disc1-backgrounds')
plan = json.loads((root / 'production-plan.json').read_text())
progress = json.loads((root / 'progress.json').read_text())
inventory = json.loads((root / 'inventory.json').read_text())
preparation_path = root / 'preparation/summary.json'
preparation = json.loads(preparation_path.read_text()) if preparation_path.exists() else {}
preparation_blocked = {item['cut']: item for item in preparation.get('blocked', [])}
prepared = {item['cut']: item for item in preparation.get('evidence', [])}
resolution_path = root / 'resolution/summary.json'
resolution = json.loads(resolution_path.read_text()) if resolution_path.exists() else {}
resolved = {item['cut']: item for item in resolution.get('screens', []) if item.get('packagingPrepared') and item.get('assetChecksPassed')}
quarantine_path = root / 'resolution/quarantined-installed.json'
quarantined = set(json.loads(quarantine_path.read_text())['cuts']) if quarantine_path.exists() else set()
installed = {}
for path in (root / 'installation').glob('batch-*.json'):
    batch = json.loads(path.read_text())
    if batch.get('registered') and batch.get('checksPassed'):
        for pack in batch['packaged']:
            assert pack['copied']
            if pack['cut'] not in quarantined:
                installed[pack['cut']] = batch
runtime_path = root / 'resolution/runtime-integration.json'
if runtime_path.exists():
    runtime = json.loads(runtime_path.read_text())
    if runtime.get('registered'):
        assert runtime['complete'] and runtime['checksPassed'], 'Run fresh resolution runtime checks before publishing installation progress'
        for pack in runtime['packs']:
            assert pack['copied']
            installed[pack['cut']] = {'batch':'resolution', 'loadingSmokeCuts':runtime['loadingSmokeCuts']}
accepted = {screen['cut'] for screen in progress['screens']}
pending = {screen['cut'] for screen in plan['screens']}
assert len(accepted) == plan['acceptedAtStart'] and len(pending) == plan['pendingAtStart']
assert not accepted & pending
reports = {}
review_order = {}
chronology = {}
for worker in (1, 2, 3):
    path = root / f'worker-{worker}.json'
    if not path.exists():
        continue
    report = json.loads(path.read_text())
    assert report['worker'] == worker
    owned = {screen['cut'] for screen in plan['screens'] if screen['worker'] == worker}
    order = report.get('productionOrder', report.get('assignedCuts', []))
    assert len(order) == len(set(order)) and set(order) <= owned
    review_order.update({cut: position for position, cut in enumerate(order)})
    chronology[worker] = report.get('chronologyNote', 'Production review order; uncertain variant first visits are not added to the confirmed story prefix.')
    for screen in report['screens']:
        assert screen['cut'] in owned and screen['cut'] not in reports
        assert screen.get('gameplayVerified', False) is False
        reports[screen['cut']] = screen

screens = []
queue = []
for item in plan['screens']:
    cut = item['cut']
    report = reports.get(cut, {})
    source = Path(f'gfx/backgrounds/disc1/cut-{cut}/source')
    manifest = json.loads(Path(f'build/disc1/original/cut-{cut}/manifest.json').read_text())
    assert manifest['cut'] == cut
    canvas = [dimension * 4 for dimension in manifest['bounds'][2:]]
    layer_names = ['background'] + [f'foreground-{i}' for i in range(manifest['foreground_layers'])]
    issues = report.get('issues', report.get('visualIssues', []))
    assert isinstance(issues, list)
    issues = list(issues)
    status = report.get('status', 'pending-generation')
    if status == 'source-preview-ready':
        status = 'artwork-preview-ready'
    reviewed = report.get('visualReview', False)
    if reviewed and status == 'artwork-preview-ready' and item['layerRisks']:
        status = 'source-preview-ready-exception'
        kinds = ', '.join(sorted({risk['classification'] for risk in item['layerRisks']}))
        issues.append(f'Scripted foreground states require independent plate/state review ({kinds}); exact script evidence is in production-plan.json for cut {cut}.')
    if cut in preparation_blocked:
        status = 'source-preview-ready-exception'
        issues.append('Original-renderer inspection: ' + preparation_blocked[cut]['reason'])
    if cut in resolved:
        assert resolved[cut]['gameplayVerified'] is False
        status = 'artwork-preview-ready'
        issues = []
    if reviewed and status != 'no-visible-environment':
        assert (source / 'base.png').is_file()
        validation = json.loads((source / 'asset-preview/validation.json').read_text())
        assert validation['cut'] == cut and validation['scale'] == 4
        assert validation['runtimeInstalled'] is False and validation['gameplayVerified'] is False
        assert [layer['name'] for layer in validation['layers']] == layer_names
        assert all([layer['width'], layer['height']] == canvas for layer in validation['layers'])
        assert all(layer['exactOriginalMask'] for layer in validation['layers'])
        assert (source / 'asset-preview/comparison.png').is_file()
        assert (source / 'asset-preview/composite.png').is_file()
    if status == 'artwork-preview-ready':
        assert reviewed and not issues
    entry = {
        'cut': cut, 'area': item['area'], 'worker': item['worker'],
        'status': status, 'visualReview': reviewed, 'issues': issues,
        'source': source.as_posix(), 'runtimeInstalled': False, 'gameplayVerified': False,
        'targetCanvas': canvas, 'foregroundCount': manifest['foreground_layers'],
        'packagingBlocker': report.get('packagingBlocker', 'Production pending; strict runtime packaging gate retained.'),
        'chronologyNote': chronology.get(item['worker'], 'First-visit order pending evidence.'),
    }
    if cut in prepared:
        entry.update(packagingPrepared=True, packagingBlocker=None, originalLayerEvidence=prepared[cut]['trace'])
    if cut in preparation_blocked:
        entry.update(packagingPrepared=False, packagingBlocker=preparation_blocked[cut]['reason'],
                     originalLayerEvidence=preparation_blocked[cut]['trace'])
    if cut in resolved:
        entry.update(packagingPrepared=True, packagingBlocker=None,
                     originalLayerEvidence=resolved[cut]['trace'],
                     resolutionEvidence=resolved[cut]['validation'],
                     resolutionMethod=resolved[cut]['method'])
    if cut in installed:
        assert cut in prepared or cut in resolved
        entry.update(runtimeInstalled=True, packagingBlocker=None,
                     installationBatch=installed[cut]['batch'], loadingChecksPassed=True,
                     representativeSmokeCuts=installed[cut]['loadingSmokeCuts'])
        if issues:
            status = 'installed-needs-layer-review'
            entry.update(status=status, packagingPrepared=False,
                         packagingBlocker='Corrected script association requires independent layer review; installed pack has bounded loading evidence only.')
    if cut in quarantined:
        entry.update(runtimeInstalled=False, runtimeQuarantined=True,
                     packagingBlocker=None if cut in resolved else 'Runtime mapping disabled after corrected script evidence found independent layer changes; original fallback retained.')
        if cut not in resolved:
            entry['packagingPrepared'] = False
    screens.append(entry)
    queue.append({
        'cut': cut, 'area': item['area'], 'productionStatus': status,
        'uncertainty': entry['packagingBlocker'], 'visualIssues': issues,
        'layerRiskEvidence': item['layerRisks'],
        'recordedLayerEvidence': resolved.get(cut, preparation_blocked.get(cut, prepared.get(cut))),
        'runtimeInstalled': entry['runtimeInstalled'],
        'pendingRuntimeMapping': None if status == 'no-visible-environment' else {'directory': source.parent.as_posix(), 'width': canvas[0], 'height': canvas[1], 'layers': manifest['foreground_layers'], 'installed': entry['runtimeInstalled']},
        'smallestCheck': report.get('laterCheck', 'Inspect recorded original layer states, then use the unchanged supported packager; compare original/modern traces and check entrances, stairs and walkable surfaces.'),
    })

screens.sort(key=lambda screen: (screen['worker'], review_order.get(screen['cut'], 10000 + screen['cut'])))
rank = {screen['cut']: position for position, screen in enumerate(screens)}
queue.sort(key=lambda screen: rank[screen['cut']])
progress['productionScreens'] = screens
progress['remainingOrder'] = 'Production review order is recorded separately; uncertain variant first visits require transition evidence during deferred testing. Numeric cut order is not story order.'
progress['productionSummary'] = {
    'previouslyAcceptedScreens': len(accepted),
    'reviewedAssetPreviews': sum(screen['status'] == 'artwork-preview-ready' for screen in screens),
    'sourceArtworkCount': sum((Path(screen['source']) / 'base.png').is_file() for screen in screens),
    'reviewedSourceArtworkCount': sum(screen['visualReview'] and screen['status'] != 'no-visible-environment' for screen in screens),
    'reviewedSourcesWithUnresolvedIssues': sum(screen['visualReview'] and screen['status'] not in ('artwork-preview-ready', 'no-visible-environment') for screen in screens),
    'noVisibleEnvironment': sum(screen['status'] == 'no-visible-environment' for screen in screens),
    'newRuntimePacks': sum(screen['runtimeInstalled'] for screen in screens), 'newGameplayVerifiedScreens': 0,
    'note': 'Runtime installation and asset previews are tracked separately. Existing recorded-layer packaging gates and strict gameplay comparator remain unchanged; new installs are not full gameplay verification.',
}
summary = progress['productionSummary']
summary['remainingSourceProduction'] = len(screens) - summary['noVisibleEnvironment'] - summary['sourceArtworkCount']
summary['pendingVisualReviews'] = summary['sourceArtworkCount'] - summary['reviewedSourceArtworkCount']
assert summary['remainingSourceProduction'] >= 0 and summary['pendingVisualReviews'] >= 0
summary['sourceProductionComplete'] = summary['remainingSourceProduction'] == summary['pendingVisualReviews'] == 0
progress['complete'] = False
(root / 'progress.json').write_text(json.dumps(progress, indent=2) + '\n')
by_cut = {screen['cut']: screen for screen in screens}
for row in inventory:
    if row['cut'] in by_cut:
        row['production_status'] = by_cut[row['cut']]['status']
        if by_cut[row['cut']]['runtimeInstalled']:
            row['status'] = 'installed'
            row['gameplay_verification'] = 'deferred'
        elif row['cut'] in quarantined:
            row['status'] = 'original-fallback'
            row['gameplay_verification'] = 'deferred'
(root / 'inventory.json').write_text(json.dumps(inventory, indent=2) + '\n')
with (root / 'inventory.csv').open('w', newline='') as stream:
    fields = ['cut', 'area', 'source', 'backgrounds', 'foregrounds', 'status', 'production_status', 'gameplay_verification']
    writer = csv.DictWriter(stream, fieldnames=fields)
    writer.writeheader()
    writer.writerows({key: row.get(key, '') for key in fields} for row in inventory)
(root / 'testing-queue.json').write_text(json.dumps({
    'policy': 'Parent only; one engine at a time; bounded targeted sessions; restore disabled artwork. Do not treat asset previews as game captures.',
    'previouslyAcceptedCoverageLimits': 'Preserve per-screen orderEvidence/additionalVerification and README route limits. Previously accepted screens do not establish all later variants.',
    'screens': queue,
}, indent=2) + '\n')
print(json.dumps(progress['productionSummary']))
