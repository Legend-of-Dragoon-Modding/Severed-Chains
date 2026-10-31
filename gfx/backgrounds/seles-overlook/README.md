# Modern Seles overlook — Disc 1, cut 9

The five RGBA PNGs are 2048×1424 (4× the original 512×356 canvas).
Background art occupies the top-left 1536×1024; the remaining padding is transparent.
All four foreground alpha masks are exact nearest-neighbour 4× copies of the originals.
The scene-specific replacement hook retains original fallback and existing texture ownership.
No movement, camera, collision, character, battle, or story-script changes are needed.

## Try or disable

Run the rebuilt game from the repository root and reach the opening Seles overlook.
For a direct live preview, set `SELES_CAPTURE=preview` and run `SelesSequenceTest`
using the command below. It pauses with Dart overlooking the village and keeps the
game window open until you close it. Preview captures use a separate folder.
Rename this folder to `seles-overlook-disabled` to restore original scenery on
scene re-entry or restart. Missing, malformed, wrong-size, or non-alpha packs fall
back to originals. The entire pack is validated before replacing any layer.

## Artwork and placement

The approved modern preview determines lighting and material quality. Newly invented
foreground obstacles were removed from the background, leaving a fully painted slope.
The four foregrounds use a single coherent modern surface under their separate original
masks. Recorded scripts move all four by identical XY offsets; their original depth,
visibility, and object overlap behavior remain separate. Shared RGB at every overlapping
pixel avoids seams between layers.

Placement is explicit, never inferred from visible bounding boxes:
- Background source: exactly 1536×1024, placed at (0,0).
- Foreground master: exactly 1504×1046, sampled across the entire 2048×1424 canvas.
  ImageGen returned this near-identical aspect ratio despite the requested output size.
- Original masks: exactly 512×356, nearest-neighbour 4×.
- Unexpected source and installed dimensions are rejected.

The built-in ImageGen tool produced the artwork; exact final prompts are in
[prompts.json](prompts.json). Transparent edge colour is extended before original masks
are applied. No original low-resolution colour is composited into the modern foreground.

## Reproduction

Run from the repository root with Python/Pillow, Node/sharp, and Java 25.
Commands here use the project's RTK wrapper.

```powershell
rtk proxy python tools/export_seles.py
rtk proxy node tools/seles_foreground_guide.cjs <sharp-module-path>
rtk proxy node tools/package_seles.cjs <sharp-module-path>
```

The exporter reads `files/SECT/DRGN21.BIN/31` and writes `build/seles/original`.
Source artwork is retained in this folder's `source/background-plate.png`
and `foreground-master.png`. The guide uses the originals only as geometry references.
Packaging stages five textures in `build/seles/modern-pack`, verifies exact masks and
identical foreground overlap colours, and writes `validation.json`.
Use `--install` after reviewing alignment to copy the staged files into this folder.
Do not package while a game test is temporarily disabling an asset.

The approved preview remains in this folder's `references/approved-modern.png`;
the previous five-texture prototype and its README remain in
`references/prototype-2x`. These references and the final source plates are retained
alongside the assets so a Gradle clean cannot remove them.

## Verification and real game captures

```powershell
rtk proxy ./gradlew.bat test --tests legend.lodmod.SelesBackgroundsTest --tests legend.game.SelesBackgroundTest -PrunTests
$env:SELES_CAPTURE = "original"
rtk proxy ./gradlew.bat test --tests legend.game.SelesSequenceTest -PrunTests --rerun
$env:SELES_CAPTURE = "modern"
rtk proxy ./gradlew.bat test --tests legend.game.SelesSequenceTest -PrunTests --rerun
rtk proxy node tools/compare_seles.cjs
```

Run the sequence and smoke test in separate invocations: the engine has a single-process
lifecycle. Tests create a fresh campaign, never load a user save, and restore disabled
artwork in finally blocks.

The smoke test checks all five enhanced textures, missing-file and malformed-file
fallback, pixel-for-pixel original GPU texture export, reload, exit to cut 10 and
re-entry, and release of previous scene textures. Unit checks also reject wrong sizes
and missing alpha.

The sequence runs the opening scripts with automatic dialogue advancement and captures
the actual final game framebuffer before window swap. These are real game captures,
not standalone artwork or reconstructed compositions:
`build/seles/captures/{original,modern}/frame-0000.png` through `frame-0720.png`,
at 120-frame intervals. `alignment.csv` records camera, Dart position, each layer's
position, visibility and depth every 30 frames. Both runs exit naturally to cut 10
after 748 frames. Matching traces are checked by `tools/compare_seles.cjs`.

Reviewed opening pan, moving foreground, Dart arrival/footing, and departure captures.
Exact original masks retain pixel-stepped edges; original character models, bird polygons,
and smoke/fire effects remain visible against the modern artwork.

