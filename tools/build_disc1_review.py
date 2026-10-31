"""Keep matched route captures, focused GPU probes and asset previews distinct."""
import html
import json
import shutil
from pathlib import Path

root = Path('docs/disc1-backgrounds')
progress = json.loads((root / 'progress.json').read_text())
cards = []
for screen in sorted(progress['screens'], key=lambda item: item['order'] or 9999):
    cut = screen['cut']
    capture = Path('build/seles/captures' if cut == 9 else f'build/disc1/captures/cut-{cut}')
    verified = capture / ('verification.txt' if cut == 9 else 'verification.json')
    if not screen['visualReview'] or not verified.is_file() or screen['order'] is None:
        continue
    figures = []
    target = root / 'review-assets' / f'cut-{cut}'
    target.mkdir(parents=True, exist_ok=True)
    recordings = [('', screen['reviewFrames'])] + list(screen.get('reviewVariants', {}).items())
    for variant, frames in recordings:
        recording = capture.with_name(capture.name + '-' + variant) if variant else capture
        if variant:
            assert (recording / 'verification.json').is_file(), f'Unverified review variant: {recording}'
            figures.append(f'<h3>{html.escape(variant.replace("-", " ").title())}</h3>')
        for frame in frames:
            images = []
            for version in ['original', 'modern']:
                source = recording / version / f'frame-{frame:04}.png'
                filename = f'{variant + "-" if variant else ""}{version}-{frame:04}.png'
                shutil.copy2(source, target / filename)
                images.append(f'<figure><figcaption>{version.title()} · game frame {frame}</figcaption><img loading="lazy" src="review-assets/cut-{cut}/{filename}" alt="{version.title()} game capture of {html.escape(screen["name"])}"></figure>')
            figures.append('<div class="pair">' + ''.join(images) + '</div>')
    cards.append(f'<section><h2>{screen["order"]:02} · {html.escape(screen["name"])}</h2><p>Actual game captures. Camera, character and layer traces match; original masks retained.</p>{"".join(figures)}</section>')
preview_cards = []
exception_cards = []
production_cards = []
for screen in progress.get('productionScreens', []):
    if not screen['visualReview'] or screen['status'] == 'no-visible-environment':
        continue
    source = Path(screen['source']) / 'asset-preview'
    assert (source / 'validation.json').is_file()
    comparison = html.escape('../../' + (source / 'comparison.png').as_posix(), quote=True)
    composite = html.escape('../../' + (source / 'composite.png').as_posix(), quote=True)
    title = html.escape(f"Cut {screen['cut']} · {screen['area']}")
    installed = screen.get('runtimeInstalled', False)
    blocker = html.escape(screen['packagingBlocker'] or ('Gameplay verification pending.' if installed else 'Runtime packaging pending.'))
    ready = screen['status'] == 'artwork-preview-ready' and not screen['issues']
    label = 'Reviewed asset preview' if ready else 'UNRESOLVED asset preview'
    issues = ''.join(f'<li>{html.escape(issue)}</li>' for issue in screen['issues'])
    issue_list = f'<ul>{issues}</ul>' if issues else ''
    runtime = 'runtime pack installed; loading checks passed' if installed else 'runtime pack uninstalled'
    card = f'<section><h2>{title}</h2><p>{label} · original left, modern right. Gameplay deferred; {runtime}. {blocker}</p>{issue_list}<img loading="lazy" src="{comparison}" alt="{title} asset comparison"><details><summary>Composite using original masks</summary><img loading="lazy" src="{composite}" alt="{title} masked asset composite"></details></section>'
    (preview_cards if ready else exception_cards).append(card)
    production_cards.append(card)
focused = json.loads((root / 'resolution/focused-verification.json').read_text())
assert focused['complete'] and focused['screenCount'] == 72
focused_cards = []
for screen in focused['screens']:
    target = root / 'review-assets' / f"focused-cut-{screen['cut']}"
    target.mkdir(parents=True, exist_ok=True)
    primary = Path(screen['primaryImage'])
    shutil.copy2(primary, target / 'primary.png')
    views = []
    for recording in screen['recordings']:
        for filename in recording['images']:
            source = Path(filename)
            name = recording['variant'] + '-' + source.name
            shutil.copy2(source, target / name)
            label = html.escape(recording['variant'] + ' / ' + source.stem)
            views.append(f'<figure><figcaption>{label}</figcaption><img loading="lazy" src="review-assets/{target.name}/{name}" alt="{label}"></figure>')
    focused_cards.append(f'<section><h2>Cut {screen["cut"]}</h2><p>Actual renderer captures; exact installed GPU textures checked. Synthetic states and camera probes do not establish story progression.</p><img loading="lazy" src="review-assets/{target.name}/primary.png" alt="Cut {screen["cut"]} focused capture"><details><summary>Layer states and camera views</summary>{"".join(views)}</details></section>')
page = '''<!doctype html><html lang="en"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Disc 1 scenery review — installed packs</title><style>
body{margin:0;background:#14191b;color:#ecf0e8;font:16px system-ui;line-height:1.5}main{max-width:1800px;margin:auto;padding:32px}h1{font-size:32px}h2{margin-top:40px}p{color:#bfc9c3}.pair{display:grid;grid-template-columns:1fr 1fr;gap:16px;margin:24px 0}figure{margin:0}figcaption{padding:8px 0;color:#d6e5d7}img{width:100%;display:block;background:#000}section{border-top:1px solid #42504a;padding-bottom:24px}.status{background:#25322e;padding:16px;border-radius:8px}@media(max-width:800px){.pair{grid-template-columns:1fr}main{padding:16px}}</style><main>
<h1>Disc 1 · Modern scenery</h1><p class="status">217 visible packs installed. All 72 resolution cases passed focused GPU checks. Full story traversal and unexercised variants remain deferred.</p>
<p>The first section preserves the 65 previously accepted matched-route reviews and their recorded coverage limits. Generation sources and prompts are retained beside each installed pack.</p>
'''
(root / 'review.html').write_text(page + ''.join(cards) + '<h1>Focused GPU review · 72 resolution cases</h1><p>Modern loading, layer endpoints and selected states/cameras. These checks supplement the earlier captures; they are not exhaustive gameplay routes.</p>' + ''.join(focused_cards) + '<h1>Artwork production · asset previews</h1><p>These are asset previews, not game captures, shown in production review order. Runtime installation and full gameplay acceptance are recorded separately. Uncertain variant first visits remain documented in progress.json.</p>' + ''.join(production_cards) + '<p>Cut 55 has fully transparent original environment layers; retail fallback is preserved.</p></main></html>', encoding='utf-8')
assert len(cards) == 65 and len(focused_cards) == 72 and len(preview_cards) == 152 and not exception_cards
print(f'Review gallery: {len(cards)} prior matched reviews; {len(focused_cards)} focused GPU reviews; {len(preview_cards)} asset previews. Full traversal deferred.')
