# Continuation state

## Current state — approved focused scope complete (2026-10-31)

All 217 visible Disc 1 background packs are installed locally. All 72 resolution
cases are resolved, including the eight corrected previously installed packs.
Cut 55 has no visible environment and retains original fallback. There are no
remaining blocking asset findings or required steps in the approved focused scope.

The final checks cover exact GPU texture bytes for all 72 resolution packs,
89 screen-load recordings, 34 selected state profiles and targeted camera views.
Five fresh pack/fallback tests passed. Native 4× alpha masks and source/trace
hashes were audited. Original matched-route evidence for 65 earlier screens is
preserved separately; these focused checks do not establish exhaustive story
traversal. Cut 175's shared scenery is explicitly reviewed and hash bound.

Runtime artwork totals 2,275 PNGs, approximately 1.20 GB / 1.12 GiB. Sources,
generation prompts, reference paths and review images are additional storage.
The runtime preservation audit confirms 3,025 protected files unchanged outside
the intended replacements and mapping update. Dart/TwinShot work is preserved.

See [completion.md](completion.md), [review.html](review.html),
`resolution/focused-verification.json`, `resolution/runtime-preservation.json`
and `preparation/step-ledger.json`. Nothing is staged, committed or pushed.
Full story traversal, later variants and remaining route checks are optional
follow-up work in `gameplay-testing-plan.md` and `testing-queue.json`.

The sections below are historical snapshots. Their counts, pending tasks and
approval gates are superseded by this current state.

## Historical snapshots
## Step 3 installed — awaiting step 4 approval

The user approved proceeding with 88 prepared screens and deferring the 12 new
layer blockers to step 4. All 88 are now strictly packaged, PNG-optimized,
registered and installed. Disc 1 has 153 installed packs: the 65 previously
verified packs plus 88 new packs with full gameplay verification still deferred.
The 64 unresolved screens remain on original retail backgrounds.

All five batches passed fresh, non-skipped pack-validation/fallback tests.
Renderer loading, missing-file fallback, malformed-file fallback, restoration and
texture-release checks passed for cuts 78, 68, 101, 140, 171, 173.
These are loading smoke checks, not original/modern traversal comparisons.
Existing background mappings and fallback logic were preserved. All
5,549 protected files retained their hashes; the only runtime source
change is the addition of the 88 approved mappings in `SelesBackgrounds.java`.
No Dart/TwinShot code was changed, no comparator was relaxed, and nothing was
committed or pushed. The test-only Gradle init configuration enables test
discovery and the runner rejects skipped/stale results.

See `installation/summary.json`, per-batch reports and JUnit evidence for details.
The new runtime textures total 464.42 MB.
Step 3 is complete. Do not begin step 4 without explicit user validation under
the requested step-exec workflow. Progress updates remain every 15–20 screens
with remaining work and a revised time estimate.

## Step 2 preparation — awaiting user decision

All 100 selected candidates passed the asset audit and completed original-renderer
inspections. 88 satisfy the existing fixed-visible layer gate; 12 have recorded
hidden or offset layers and remain blocked. The current readiness totals are
88 reviewed candidates for static packaging and 64 unresolved screens. Earlier
100/52 counts below are historical production snapshots.

No new screen was packaged or installed. No full traversal or original/modern
gameplay comparison was performed. The 65 installed packs and their existing
verification records are unchanged. Inspection-only recordings must not be
presented as gameplay verification.

| Newly blocked cut | Actual recorded layer state |
| --- | --- |
| 631 | foreground 3 hidden |
| 99 | foreground 4 hidden; foreground 5 hidden |
| 102 | foreground 6 hidden |
| 626 | foreground 6 hidden |
| 120 | foreground 7 hidden; foreground 8 hidden |
| 636 | foreground 14 offset (0, -140); foreground 15 offset (0, -140) |
| 158 | foreground 1 hidden; foreground 6 hidden; foreground 7 hidden; foreground 8 hidden; foreground 11 hidden; foreground 13 hidden; foreground 15 hidden |
| 165 | foreground 4 hidden |
| 175 | foreground 2 hidden |
| 180 | foreground 5 offset (0, -120) |
| 192 | foreground 15 hidden |
| 197 | foreground 2 hidden; foreground 3 hidden; foreground 4 hidden; foreground 6 hidden |

The exact traces, frame intervals, entries and GPU/export checks are recorded in
`preparation/summary.json`. Prepared placement metadata for the 88 passing screens
references those real recordings; previous metadata is saved in
`preparation/placement-before.json`. Foreground count and names, exact 4× canvas,
original alpha masks and source placement dimensions passed for all 100 candidates.

Step 2 is partially complete, awaiting a decision about the 12 blockers. Proposed
scope adjustment: use the 88 prepared screens in step 3 and defer the additional
12 layer cases to step 4. Do not package, install, change runtime mappings or
start step 3 without explicit user approval. The requested step-exec workflow
requires validation at each step boundary. Future progress updates are requested
every 15–20 completed screens, with remaining work and a revised time estimate.

## Texture storage optimization

All 217 visible Disc 1 screens (2,275 texture PNGs) were optimized in place after user approval.
Texture storage changed from 8.826 GB to 1.228 GB,
saving 7.598 GB (86.1%).
Visible pixels, alpha masks, dimensions and all composited previews were checked exactly.
The 65 previously installed packs have decoded pixel identity: True.
All 3,293 protected source/reference/code files retain their original hashes.

This intentionally updates texture PNG bytes from the earlier production preservation baseline.
Raw artwork, prompts, references, comparison images and gameplay code were preserved.
No new packs were installed and no new gameplay verification was performed.
The 100 reviewed source previews and 52 unresolved cases retain their existing production status.
In-game filtering for the new assets remains part of the deferred testing phase.
Exact per-file sizes and validation hashes are in `storage-optimization/worker-1.json`,
`worker-2.json`, `worker-3.json`; the consolidated result is `storage-optimization/summary.json`.
Future preview generation now clears transparent RGB and uses optimized PNG compression by default.

## Production result

Source production and visual asset reviews are complete for all 152 visible remaining candidates. Cut 55 has fully transparent original layers and needs no replacement scenery. There are 100 reviewed previews without additional source issues and 52 unresolved sources. All 152 new packs remain uninstalled because strict recorded-layer runtime packaging requirements are deferred. No new gameplay verification was performed. The 65 previously accepted packs remain intact; a 916-file preservation hash audit passed.

Sources and exact prompts/references are under `gfx/backgrounds/disc1/cut-N/source`. The built-in ImageGen tool was used, with documented exact-image source reuse. Every produced preview has exact 4× dimensions and original alpha masks. See `review.html` for all sources, clearly separated from verified game captures.

| Unresolved cuts | Reason |
| --- | --- |
| 80 | Central floor platform remains misaligned after a focused correction. |
| 109 | Lower broken arch and stepping-stone alignment remains uncertain after a focused correction. |
| 111 | Special exporter/engine placement needs targeted original GPU/export comparison; 384×385 original, 1536×1540 modern. |
| 30, 32, 58, 67, 70, 71, 88, 89, 93, 94, 95, 96, 97, 100, 103, 104, 121, 134, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 155, 159, 166, 172, 176, 177, 181, 193, 194, 195, 198, 627, 628, 632, 637, 647, 658, 659, 660, 674 | Scripted foreground visibility, movement or dynamic state requires independent plates/underlay inspection and state evidence. |

Exact per-cut script files, lines, uncertainties and smallest checks are in `testing-queue.json`. Follow the single consolidated `gameplay-testing-plan.md`; retain all previously recorded route limits. Disc 1 is not fully gameplay-verified.

Source generation/review has finished. Resume from the unresolved queue and deferred runtime/gameplay phase; do not regenerate completed sources. Worker reports cover all 153 candidates (54/53/46).

The current production request is preserved in the user attachment and production-plan.json. Disk baseline: 65 accepted screens through cut 90, with all 65 runtime backgrounds present. Older notes saying 64 or that cut90 was awaiting acceptance were stale; README/progress contain the passing cut90 and cut86-return evidence.

Exactly three workers own 153 pending candidates: worker1=54 Bale/Indels/Hoax/Marshland cuts; worker2=53 Volcano/Nest/Lohan/Shrine cuts; worker3=46 return variants/Kazas/BlackCastle cuts. Exact exclusive cut assignments, original exit candidates and script evidence are in production-plan.json. Workers edit only owned cut directories and their own worker-N reports. Do not create additional agents.

No engine was running at startup. The stranded cut69 background.capture-disabled.png was restored to background.png before workers began. No game, gameplay test or capture session has been launched during this production continuation. Preserve unrelated Dart/TwinShot and all accepted artwork.

The existing static runtime packager requires original recorded fixed-visible layer traces. These gates and compare_disc1.cjs remain unchanged. Pending scenes generally lack those traces. tools/preview_disc1_artwork.py produces uninstalled source/asset-preview layers at exact4x, restores every original alpha mask exactly, and saves comparison/composite previews plus validation.json. This is asset production, not runtime packaging or gameplay verification. Never fabricate traces or stationary_layers_verified.

Worker reports are integrated with tools/integrate_disc1_production.py. Existing progress.screens and confirmedOpeningOrder remain the65 accepted records; productionScreens separately tracks pending/reviewed artwork. Inventory status continues to describe runtime installation; production_status describes artwork. tools/build_disc1_review.py shows verified captures separately from uninstalled asset previews. Rebuild these in meaningful batches, not after every image.

Read-only original script dumps for all pending source directories exist under build/disc1/scripts (environment source directory minus1). production-plan.json records file/line evidence and distinguishes camera-mode calls from actual foreground hide/offset risks. There are50 candidates with concrete or dynamic layer risks; no detected risk in the other103 is not a gameplay proof. Cut55 has fully transparent original background and foreground (all RGBA extrema zero): no visible artwork to recreate, preserve retail fallback.

Complete the assigned production, retaining original generated outputs, prompts, references and placement metadata. One generation/review is preferred; allow one focused correction for a concrete defect. Do not mark known defective artwork ready. Unsupported runtime packaging must remain explicit.

Later testing is consolidated in gameplay-testing-plan.md and testing-queue.json. Only the parent may launch targeted bounded checks, one engine at a time, restoring any disabled artwork. Existing per-screen route limits in README/progress remain applicable. Do not commit or publish without a separate request.
