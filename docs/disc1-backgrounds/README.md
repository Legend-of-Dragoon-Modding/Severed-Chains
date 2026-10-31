# Disc 1 background remake — work in progress

Runtime PNGs are now local-only in `C:\Users\20te\Severed-Chains-Backgrounds`,
with hard links at the game's existing paths. They are excluded from Git staging.
See [local asset setup](local-assets.md) before running or packaging a fresh clone.

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

Current production uses three exclusive workers; see `production-plan.json` for
all 153 pending candidate assignments. `progress.json` retains the 65 previously
accepted screens separately from `productionScreens`. New `artwork-preview-ready`
entries are reviewed, uninstalled asset previews with gameplay deferred. They do
not satisfy the existing recorded-layer runtime packaging gate. The gallery
labels them separately from verified actual-game captures. See
`gameplay-testing-plan.md` and `testing-queue.json` for later verification.

Scope: all Disc 1 non-battle backgrounds. The user requested production in story
order and one final review of every screen. Do not request individual art approvals.
The approved modern Seles style and existing gameplay/layer alignment are required.

`inventory.json` lists 224 candidate cuts backed by 223 environment directories.
This is a retail-data inventory, not proof that every entry is visited in the story.
`progress.json` tracks produced artwork and the confirmed chronological prefix.
Pending inventory entries must not be described as completed.
Cuts 0–4 and their cut 791 alias are developer chapter-selection maps and are marked excluded-debug-menu.

The opening order was established from actual scripted transitions:
675 → 676 → 677 → 703 → 704 → 9 → 10 (entrance 2).
After this, optional rooms and later story variants need first-visit ordering.
The cut 10 player script selects cut 725, entrance 6, for the first commander
encounter while global flag 2 `0x281` is unset. Its later branch selects cut 12.
Cut 725 is verified through its commander battle transition. All 3,859 recorded
frames match between original and modern runs, including all nine layer traces.
Its post-battle script next selects cut 12, entrance 18. That pack is verified
over a 982-frame walk past the well and graveyard to the northern exit, which
triggers cut 742's Hellena Prison interlude. Cut 12's test fixture sets only the completed
opening encounter flags (`0x280`, `0x281`); the story scripts still run normally.
The Hellena interlude continues 742 → 694 → 695 → world map. Those three packs
have matching scripted recordings and visual checks. Cut 742's first draft was
rejected because it depicted the distant prison as a rock spire; the corrected
fortress source and correction prompt are retained. The following forest entry
from Seles is cut 624, entrance 10, as specified by the two ends of world-map path 2.
Its walking route reaches cut 5, entrance 14. The ravine pack preserves all three
foreground masks and matches the original 419-frame log crossing and exit to 625.
The first draft's extra stump on walkable ground was removed; both sources and
the correction prompt are retained. Frame 180 records Dart stepping onto the log.

`tools/Disc1ScriptDump.java` uses the existing script-recompiler dependency to
write readable scripts under `build/disc1/scripts` for story-order research.
Pass DRGN21 script-directory numbers, not cut IDs. It does not modify retail files.

## Sources and placement

Final sources and ImageGen prompts are under `gfx/backgrounds/disc1/cut-N/source`
and `prompts.json`. The existing Seles pack stays in `gfx/backgrounds/seles-overlook`.
`tools/find_disc1_reuse.py` compares decoded original RGBA textures, including
every foreground. `reuse-candidates.json` records exact matches only; shared art
does not establish shared gameplay verification. Cut 13 reuses the corrected
742 fortress source and has its own verified 2,431-frame approach/cart recording.
Cut 14 continues through cart boarding and the drawbridge crossing into cut 37;
its 2,419 frames and all 7,257 foreground samples match exactly. Use entrance 1;
the fixture presses the normal action button when the boarding prompt appears.
Cut 37's first cart arrival uses entrance 0 and exits to cut 16 entrance 9 after
484 matching frames. All 6,292 foreground samples match. Its later explorable
variant still needs its own walking verification when reached.
Cut 16 uses entrance 9 for the cart arrival, then ordinary directional input
through the storeroom to the guard encounter. Its 1,698 frames and 15,282 layer
samples match exactly. Post-encounter recordings use `DISC1_POST_BATTLE=true`,
entrance 21 and a separate `cut-16-post-battle` capture folder; the disposable
fixture sets the original guard-return flag (1:0xd3). Compare these separately
with `tools/compare_disc1.cjs 16 --post-battle`.
The post-encounter route also passes: 1,239 matching frames and 11,151 foreground
samples, through dialogue and the original stairs to cut 15 entrance 4.
The upper walkway starts at entrance 45, a separate visit; its verified captures
are retained in `cut-15-upper-walkway`. Cut 23's storage spur has matching captures
under `cut-23-storage-spur`; use `DISC1_ROUTE=storage-spur` and entrance 23.
The eastern route from cut 15 entrance 4 reaches cut 23 entrance 0 (413 matching
frames and 5,369 samples); reproduce it with `DISC1_ROUTE=east-passage`.
Cut 23's main arrival matches through the guard battle (220 frames, 2,420 samples).
Its post-encounter visit starts at entrance 0 with `DISC1_POST_BATTLE=true` and
the script's defeated guard48 flag: 535 matching frames and 5,885 samples cover
the action-button climb, camera ascent and exit to cut 20 entrance 22.
Cut 15's lower-floor entrance 4 matches through its guard encounter (262 frames,
3,406 samples). A separate post-encounter capture at entrance 3 sets only the
recorded guard47 flag and follows the original ledge to cut 17 entrance 11
(195 matching frames, 2,535 samples). Its guard46 flag remains false.
Cut 17 entrance 11 includes the complete failed-lift script and return walk:
1,049 matching frames and 10,490 layer samples. The fixture presses the normal
action button at the lift and waits for the script to release movement.
Use `DISC1_ROUTE=upper-walkway` with cut 15 entrance 45 to reproduce its separate
upper-route capture. Use `DISC1_ROUTE=rear-exit` with cut 16 entrance 24 for the
rear doorway after the first guard is defeated. These write distinct capture
folders; pass the matching `--upper-walkway` or `--rear-exit` to the comparator.
The rear-exit route passes with 1,587 matching frames and 14,283 layer samples,
reaching cut 19 entrance 15. Matching game captures confirm the rear doorway,
merchant overlap and foreground bridge remain aligned.
Cut 19 entrance 15 passes the lower chest passage and return to cut 16 entrance
10: 249 matching frames, 3,237 layer samples, all 13 exact foreground masks.
Its upper bridge and stair routes remain pending for the later story visit.
Cut 20 entrance 22 passes the lower bridge crossing to cut 21 entrance 28:
365 matching frames and 5,840 layer samples, with all 16 exact masks.
Cut 21 entrance 28 passes the ladder descent and outer walkway through its
guard encounter: 656 matching frames and 13,776 samples, with all 21 exact masks.
The rest room's post-encounter traversal remains pending. The gallery includes
verified alternate routes alongside the initial visit, with each pair labelled
as an actual game capture.
Cut 20's `lift-ascent` route begins at entrance 28, approaches the basket with
normal movement, presses the action button and waits for the original ascent.
Its 795 frames and 12,720 samples match through the middle platform to cut 25
entrance 18. Compare using `--lift-ascent`; the gallery includes the ride.
Cut 25 entrance 18 matches the scripted arrival and transition to cut 24:
172 frames and 2,236 samples. Its first bridge reconstruction is retained as
`source/bridge-drift-reference.png`; the installed source uses the documented
bridge correction. The later free-walking route is still pending.
Cut 24 entrance 0 matches the entire initial Lavitz sequence through the battle
handoff: 3,567 frames and 53,505 layer samples. Its post-battle replay uses the
original negative battle-return variable in the disposable fixture; story code
and production game state are unchanged.
All artwork uses the built-in ImageGen tool. Full-canvas placement uses explicitly
declared source dimensions; runtime files must be exactly 4× their original canvas.
The existing scene-specific event hook retains original-texture fallback.

`tools/export_disc1.py` exports original backgrounds and masks into
`build/disc1/original/cut-N`, using `NEWROOT.RDT` to resolve assets.
The exporter preserves the engine's cut 111 and 642 placement corrections, clips
foreground source rectangles like the renderer, and records unused/empty slots.
These special cases still require GPU verification when their scene is processed.

`tools/package_disc1_static.cjs` is restricted to scenes with recorded fixed,
visible layers. It refuses moving/hidden layers, checks full-canvas dimensions,
and restores exact original masks at 4×. Cut 675 uses its own packager because
four layers are alternate lightning frames. Its seven original layer identities
and scripted animation remain intact. New moving layers require separate plates.

## Validation

Select Java 25 and run from the repository root with the project's RTK wrapper:

```powershell
$env:DISC1_CUT = "676"
$env:DISC1_SCENE = "0"
$env:SELES_CAPTURE = "original"
rtk proxy ./gradlew.bat test --tests legend.game.SelesSequenceTest -PrunTests --rerun
$env:SELES_CAPTURE = "modern"
rtk proxy ./gradlew.bat test --tests legend.game.SelesSequenceTest -PrunTests --rerun
rtk proxy node tools/compare_disc1.cjs 676
```

Use entrance 2 for cut 10. Each invocation starts a fresh campaign. Run these
engine tests sequentially; fallback captures temporarily disable one texture and
restore it in finally. Do not package files while a capture test is running.
Forest walking captures (624 entrance 10, 5 entrance 14, 625 entrance 20) use the existing
`EncounterRateMode.NONE` option in the disposable test session, so random battles
cannot interrupt the route. Production encounter rules and scripts are unchanged.
The maze test steers with ordinary directional keys toward original walkable
polygon centers. Retail scripts connect polygon 25 to 31 and polygon 17 to 6;
the test leaves both jumps running normally. It reads geometry without changing
it. Cut 625 is verified through its 843-frame exit to cut 6: all 8,430 layer
samples match exactly, including both scripted jumps and the camera scroll.
Cut 6 follows the original connected path with directional keys, including the
full childhood flashback, and exits to cut 7 after 1,643 identical frames. Cut 7
continues to the world map in 433 identical frames. Its recordings start after
the entrance camera settles. These three scenes retain their original collision.

Recordings are actual final game framebuffer captures, not standalone artwork.
They live in `build/disc1/captures/cut-N/{original,modern}`. Every render frame is
logged for camera, main character position, all layer positions, visibility and depth.
The comparison requires exact equality and matching completion; a generated image
or a successful export alone is not acceptance. Original recordings also compare
exported texture pixels with GPU textures; modern recordings assert 4× dimensions.
The standard Seles smoke test covers missing/malformed fallback and texture cleanup.

Original character models, birds, dragons, scripted effects and UI remain unchanged.
Exact original transparency silhouettes retain their pixel-stepped edges.

Cut 24's post-battle replay now covers the full Lavitz conversation and walk to
cut 26 entrance 2: 2,927 identical frames and 43,905 layer samples. Reproduce with
`DISC1_CUT=24`, `DISC1_SCENE=0`, `DISC1_POST_BATTLE=true`; compare with
`tools/compare_disc1.cjs 24 --post-battle`. The fixture initializes the original
battle-return variable only in this disposable replay.

Cut 26 enters at 2 and reaches the side room (642 entrance 1) after 169 identical
frames and 2,704 layer samples. Door overlays 5 and 13 hide independently, so use
`tools/package_disc1_26.cjs <sharp-path> --install`, not the static packager.
Its original masks select the painted open-door recesses into the background;
the door overlays retain the closed artwork. Frame 168 records the open door.
The second door is also checked: set DISC1_ROUTE=cell-spur and entrance 15. Its 209-frame route to cut 641 matches all 3,344 layer samples; frame 208 shows the open doorway. The tower exit from entrance 0 is verified through cut 25 entrance 2: 187 matching frames and 2,992 layer samples.

Cut 642 (prisoner workroom, entrance 1) follows the right corridor door and returns
to cut 26 entrance 15 after 321 identical frames and 1,605 layer samples. Its five
foregrounds remain fixed and visible. The exported original is 385x256, including
its edge column; the installed complete pack is 1540x1024. Use the static packager
with the saved full-canvas placement and prompts. The route follows the clear
floor, approaches the bench, and avoids the two original prisoner collision areas.

Cut 641 (crate room, entrance 2) replays the original automatic jumps across the
crate stacks, approaches the chest, then returns to the corridor. It passes 485
frames and 3,880 layer samples. Capture starts with the camera settled at (0,-8),
on a script tick; frames are also saved every 60 frames for individual landings.
The initial raised-crate artwork is retained as a reference, with the corrected
source and its generation prompt alongside it. Package using the static packager.

Cut 31 (central cavern hall, entrance 0) covers the key conversation and return to cut 20 entrance 20: 2,207 identical frames and 37,519 foreground samples. Its 17 layers remain stationary and visible. The 2048x1536 pack uses explicit full-canvas placement and exact original masks. Recordings anchor after the entrance camera settles at (-64,-72). Upper stairs leading to other rooms remain for later visits.

Cut 20 upper basket route: DISC1_SCENE=69 and DISC1_ROUTE=upper-ascent, 723 matching frames and 11,568 layer samples to cut 31 entrance 0. It follows the direct story route to the key hall. DISC1_SCENE=7 and DISC1_ROUTE=tower-guards covers the preceding 232-frame guard encounter. Both use normal scripts and controls.

Cut 20 key return: DISC1_SCENE=20 and DISC1_ROUTE=key-return. The fixture restores the earned key and completed lift/guard flags, then walks and interacts normally. Verified 403 matching frames and 6,448 layer samples through entry to cut 28 entrance 4.

Cut 28 (upper cell gallery, entrance 4) reaches cut 29 entrance 0 in 199 matching frames and 2,587 foreground samples. Its 13 layers remain fixed and visible. Original smoke, flame and model effects remain unchanged. Extra generated props were removed before packaging. Capture every 30 frames to inspect the short upper walkway.

Cut 29 (inner cell corridor, entrance 0) opens the side-room door and reaches cut 644 entrance 0 in 171 matching frames and 2,736 foreground samples. Package with tools/package_disc1_26.cjs <sharp-path> 29 --install; overlay 5 selects the open recess into the background. Default cut 26 packaging remains byte-identical for all 17 installed files. Frame 168 shows the opened door.

Cut 644 (timber storage room, entrance 0) returns to cut 29 entrance 1 in 239 matching frames and 717 foreground samples. The route walks around occupied prisoner positions and behind the foreground beams. Its three layers remain fixed and visible; original chest and prisoner models remain unchanged. Camera starts at (-4,-8).

Cut 29 hall-exit: entrance 1 returns from storage and reaches cut 27 entrance 2 in 241 matching frames and 3,856 samples.

Cut 27 (rescue cell block, entrance 2): guard confrontation matches 234 frames and 3,744 samples. DISC1_POST_BATTLE=true restores the guard victory and key, then runs the original gate opening into cut 643: 587 frames and 9,392 samples. Package with tools/package_disc1_26.cjs <sharp-path> 27 --install. Foregrounds 6 and 13 have separately painted open recesses; the cell opening is timed during the original fade.

Cut 643 (Shana reunion cell, entrance 0): 2,017 matching frames and 4,034 foreground samples through the complete reunion and departure to cut 27 entrance 2. Its original RGBA textures exactly match cut 694, so the same modern master is reused with independent mask and gameplay verification. The fixture restores rescue flags d0/d1, key and Lavitz flags, and temporary reunion flag 13. Package using the static packager.

Cut 27 return variants: DISC1_ROUTE=reunion-return restores the reunion flags and anchors at the original scripted pose (-261,-38,37); 242 frames and 3,872 samples match through the second guard battle. DISC1_ROUTE=escape adds the victory flag d2 and runs the 2,003-frame conversation into cut 22 entrance 0, matching 32,048 samples. Both use entrance 2.

Cut 22 (commander escape interlude, entrance 0): 1,471 identical frames and 25,007 layer samples through return to cut 27 entrance 26. Original RGBA textures match cut 695 for background and all 17 foregrounds. Reuse its source with per-cut masks, static packaging and independent gameplay verification. Capture starts on a script tick after depth flags for layers 1 and 11 initialize.

Cut 645 (wheel and barrel storehouse, entrance 0): 299 identical frames and 598 foreground samples through the clear aisle, chest approach and return to cut 27 entrance 39. The route avoids the original prisoner and chest collision areas. Both foregrounds remain stationary; package with the static tool from the saved 1536x1024 full-canvas master and prompt.

Additional escape-route comparisons: cut 27 lower-bridge (entrance 39) to cut 29 entrance 13: 257 frames / 4,112 samples; cut 29 lower-bridge (entrance 13) to cut 28 entrance 21: 167 / 2,672; cut 28 lower-bridge (entrance 21) to cut 20 entrance 0: 227 / 2,951. Cut 20 upper-descent (entrance 0) takes the original basket down and exits to cut 25 entrance 18: 691 / 11,056. Cut 25 cell-return (entrance 18) reaches cut 24 entrance 2: 199 / 2,587.

Cut 24 side-cell (entrance 2) opens the barred doorway into cut 640 entrance 0: 177 frames / 2,655 samples. Use package_disc1_26.cjs <sharp-path> 24 --install for this pack now. Separate recess under foreground 6 changes only fully covered pixels when the door is closed; the previous background is retained in source/background-before-door.png.

For initial layout exploration only, DISC1_INSPECT=true ends after frame 120 and exports the walkmesh when supported. Its completion report explicitly says inspection only and is rejected by compare_disc1.cjs. Full traversal without this flag remains required for acceptance.

Cut 640 (skeleton side cell, entrance 0): 249 matching frames and 498 foreground samples through the open floor and back to cut 24 entrance 14. Original skeleton remains a separate model. Both foreground masks retained exactly; static 1536x1024 pack from saved full-canvas source and prompt.

Cut 24 cell-exit (entrance 14): 179 matching frames / 2685 samples to cut 25 entrance 33. Cut 25 lift-after-cell (entrance 33): 195 matching frames / 2535 samples to cut 20 entrance 7. Both are included as paired gameplay review variants.

Cut 20 lower-descent (entrance 7): 831 matching frames / 13296 samples to cut 23 entrance 20. The capture starts after camera initialization at y=34.19; basket interaction and travel use normal controls and scripts.

Escape checks: cut 23 escape-stairs (entrance 20), 491 frames / 5401 samples to cut 15 entrance 33; capture waits for foreground 7 depth 33208. Cut 15 escape-hall is a free-traversal comparison (411 / 5343), not the rescued-party story variant. Cut 15 escape-boss restores rescue flags 7/9/a and verifies the Fruegel confrontation (1656 / 21528). Escape-victory additionally restores flag b, then verifies the aftermath into cut 56 entrance 6 (819 / 10647). Both story variants start at entrance 33.

Cut 56 drawbridge escape (entrance 6): 1519 identical frames and 4557 foreground samples to cut 13 entrance 14. Original textures exactly match cut 14; reuse proof is saved with its copied modern master and prompts. Static packaging independently checks all masks; capture starts on a script tick.

Cut 13 escape (entrance 14): restore flags cd and 39c, follow the cliff exit, and allow world-map completion. Verified 289 matching frames / 1156 samples. This route excludes the original arrival-only upward input at frame 1200.

Cut 38 Prairie coastal chase (entrance 4): 2931 matching frames / 20517 samples through arrival, normal walking and cliff escape to cut 39 entrance 3. Capture starts on a script tick at Dart x=820.75, z=794.75. Walking begins after original arrival flag c8; scripts retain control during the pursuit. Seven stationary foregrounds retain exact masks. Package from the full-canvas source and saved built-in ImageGen prompt.

Cut 39 Prairie crossroads (entrance 3): 2579 matching frames / 28369 samples to cut 43 entrance 6. Full hiding and guard sequence plus party reunion. Walk to river begins after original flag cb; the camera anchor is (-128,-72). Eleven exact foreground masks; use the static packager and saved full-canvas source and prompt.

Cut 43 river first visit (entrance 6): 293 matching frames / 1172 samples through blocked-crossing dialogue and return to cut 39 entrance 33. Camera anchor (-4,8); four exact stationary masks, original animated water retained. Full log crossing is verified below.

Cut 40 farmyard (entrance 5): complete party arrival conversation and left steps to cliff cut 42 entrance 0. 1343 identical frames / 10744 samples. Eight exact fixed masks, full-canvas 1536x1024 master, static packaging. Horse remains an original independent model.

Cut 39 farmhouse route (entrance 33): 408 identical frames / 4488 samples to cut 40 entrance 5. Flags cb/cf restored; exact first movement pose x=-561 avoids variable loading-idle frames.

Cut 42 cliff first visit (entrance 0): 553 identical frames and samples through tree inspection and return to cut 40 entrance 24. Restore cf from blocked river. Single exact foreground mask; generated stump revised to fit original animated tree. Full chopping sequence is verified below.

Cut 40 shed route (entrance 24): 321 matching frames / 2568 samples to cut 41 entrance 1. Restore cc/cf/d2 after tree inspection.

Cut 40 cliff-return (entrance 15): 319 matching frames / 2552 samples to cut 42 entrance 0 after collecting the axe.

Cut 41 shed (entrance 1): 3949 identical frames / 35541 samples through full conversation and axe search to cut 40 entrance 15. Restore cc/cd/cf/d2; capture anchors at scripted pose (44,88) in x/z. Use package_disc1_26.cjs <sharp-path> 41 --install. Continuous open-doors floor plate supplies background and stationary layers; rebuilt base supplies disappearing layers 7/8, with an 8px union-mask blur used only to match boundary RGB to the floor. All original alpha masks remain exact. Saved initial artwork, revised prop guide, generation prompts and underlay remain available.

Cut 42 chop (entrance 0): 1953 identical frames and samples through chopping, falling tree, party conversation and return. Restore flag 1 plus cc/cd/ce/cf/d2. Camera anchor (0,-8) prevents initial loading differences. Original tree model and all layer masks retained.

Cut 43 crossing (entrance 6): 773 identical frames / 3092 samples through log arrival, crossing and exit to cut 44 entrance 20. Restore d0 after chopping plus the previous Prairie story flags. Foregrounds remain fixed and visible; animated log and water stay original.

Cut 44 Prairie exit (entrance 20): 2527 identical frames / 17689 samples through refugee family conversation and departure to world map. Restore flags1 d3 and flags2 d. Original interaction opens the blocked route; all seven foregrounds remain fixed and visible. Use static packager, full-canvas 2560x1024 pack and saved prompts.

Cut 45 cave entrance (entrance 0): 491 identical frames / 2455 samples through upper path, central bridge and left-hand descent to cut 46. Five stationary foregrounds retain exact masks. Static full-canvas 1536x2048 pack; original animated sea and chasm water retained.

Cut 46 waterfall cavern (entrance 8): main descent 423 frames / 7191 samples, slide variant 631 / 10727, both exactly matching to cut 47 entrance 12. DISC1_ROUTE=slide runs the original slide and jump to the exit path. Foreground 1 depth 33042 anchors capture initialization. All 17 masks retained; static 1536x2048 pack. Original animated waterfall and sliding effects remain.

Cut 47 stepping stones (entrance 12): 3283 identical frames / 6566 samples through the full party crossing and landing sequence to cut 48 entrance 37. Static 1536x2048 pack, two exact masks. Initial artwork and first alignment revision retained; second correction extends the distant standing shore and lowers stepping stones to fit original landings. Original animated water remains. One recording hit an existing newRoot initialization crash before loading the scene; retry completed without engine changes.

Cut 48 raised platforms (entrance 37): 3259 identical frames / 6518 samples through the healing-spring interaction, full party conversation and exit to cut 49 entrance 38. Restore flags2 e after the stepping-stone scene. Camera anchor (128,72); two exact masks, static 2560x1536 pack.

Cut 49 stairway (entrance 38): 451 identical frames / 2255 samples through terraced pools and central stairs to cut 50 entrance 4. Five exact stationary masks; static full-canvas 1536x2048 pack.

Cut 50 jumping cavern (entrance 4): 779 identical frames / 3895 samples along outer stones and treasure approach, returning to cut 49 entrance 45. Five exact masks, static 1536x1024 pack. This room scripts ordinary movement while ignoreCollision stays set after jumps, so capture input waits on movementType instead. The route approaches the chest within 40 world units to avoid collision. Upper islands and creature interactions remain unrecorded; original creature and mist effects retained.

Cut 49 upper-exit (entrance 45): 607 identical frames / 3035 samples to cut 51 entrance 10, including scripted jump. Capture begins after camera y=64.10 initialization; waypoint 18 steers around the chest. Foreground depth changes are preserved.

Cut 51 stone bridge (entrance 10): 279 identical frames / samples to cut 52 entrance 33. One exact stationary mask; 1536x1024 pack. Revised stone tops fit original standing surfaces; initial art and alignment prompt retained. Capture waits for foreground depth flag 16384. Extra frames 180/210 cover right-hand stepping stones and arch approach. An intermittent newRoot initialization crash occurred on one attempt; retry passed without engine changes.

Cut 52 main cavern (entrance 33): 541 identical frames / 5410 samples across left-hand path, bridge and central pillar to cut 53 entrance 2. Ten exact stationary masks; 2048x1028 textures preserve original 512x257 canvas. Original save-point and ambient effects retained.

Cut 53 guardian chamber: entrance 2 introduction reaches Battle after 1134 identical frames / 7938 samples. Post-battle entrance 22 seeds flags2 f/10 and flag1 76, plays the full party recovery and walks to cut 54 entrance 34 after 1223 frames / 8561 samples. Seven exact stationary masks; static 1536x1024 pack. Original boss model, party and effects remain.

Cut 54 winding exit (entrance 34): 715 identical frames / 5005 samples through complete ascent to world map. Seven exact stationary masks; 1536x2048 pack, original cutaway silhouettes and effects retained. Capture uses the existing no-random-encounters setting and waits for initial camera y approximately -118.7. Exact CSV comparison remains unchanged.

Cut 66 Bale fountain square (world-map entrance 40): 819 identical frames / 13923 samples through full arrival camera sequence and left path to market square cut 69 entrance 20. Seventeen stationary foreground masks match exactly, static 2048x1536 pack. Camera anchor (64,-72) excludes variable loading frames. Original fountain water, townspeople, birds and smoke retained. Other exits remain to be recorded. Built-in image generation source and prompt saved alongside the pack.

Cut 69 Bale market square (entrance 20): 393 identical frames / 6288 layer samples to tower exterior cut 86 entrance 3. Sixteen exact stationary masks; 2560x1536 pack. Capture anchor (-74,-72) excludes loading initialization without relaxing comparison. Actual game frames 120, 240 and 360 visually checked. Pack loading and fallback tests pass. Other exits remain unverified.

Cut 86 fortified tower exterior (entrance 3): stair ascent to cut 91 entrance 0 matches 489 frames / 5868 samples. Lower-door route to cut 90 entrance 1 matches 301 frames / 3612 samples; reproduce with DISC1_ROUTE=lower-door and compare --lower-door. Frame 300 records the lower threshold. Twelve fixed visible foregrounds retain exact masks in 1536x1536 textures. Initial art, alignment revision and built-in prompts retained. Both doors and stair footing visually checked. One stair attempt had a transient three-frame player-position difference; repeat passed exact comparison without changing the route or comparator. Right-hand return and later story variants remain unrecorded.

Cut 91 tower residential interior (entrance 0 from cut 86 upper door): central hall and stairs to cut 92 entrance 11 match 213 frames / 4260 layer samples. Twenty fixed visible foregrounds retain exact original masks, full-canvas 2560x1536 pack. Built-in ImageGen source (1619x971) and prompt retained. Camera anchor (0,-72); actual game frames 120, 180 and 210 inspected. All-pack loading/fallback tests pass. Side rooms and subsequent story variants remain unrecorded. Next confirmed screen is cut 92 entrance 11; its original guide is prepared, but no gameplay or replacement artwork has been produced yet.

Cut 92 upper tower audience chamber (entrance 11): central stair and left chamber approach return to cut 91 entrance 49 after 463 identical frames / 8797 layer samples. Nineteen exact masks in 2560x1536 canvases. Use package_disc1_92.cjs; floor-patch foreground 1 remains hidden in every recorded frame, with all other layers fixed and visible. A continuous master supplies the patch and background. Actual game frames 120, 240 and 360 inspected. Initial extended route timed out against the spiral-chamber NPC; conversation and other side rooms remain unverified. Loading/fallback tests pass. Background tests can use -I build/disc1/background-tests.gradle to isolate result output from the unrelated locked shared results folder.

Cut 90 dragon-research basement (entrance 1 from cut 86 lower door): storage route to cut 91 entrance 35 matches 393 frames / 7467 samples. DISC1_ROUTE=right-exit returns to cut 86 entrance 7 after 449 frames / 8531 samples; compare --right-exit. Nineteen fixed visible masks, static 2560x1536 pack. Original dragon, guards and ambient effects retained. Input updates only on script ticks to prevent SDL stair-input timing variation; exact comparator unchanged. Stair support and painting placement revised from paired gameplay, with original master, diagnostic captures and all built-in prompts retained. Initial interaction-wait route was stopped while stuck; an early laboratory route timed out against a fixture. Passing route detours around it. Ladder routes and other interactions remain unverified. Loading/fallback tests pass.

Cut 86 market-return (entrance 7 after cut 90 right exit): 293 identical frames / 3516 samples to cut 69 entrance 8. Camera starts near y=-24.31; actual frames 120/240 inspected. Reproduce with DISC1_ROUTE=market-return, compare --market-return.
