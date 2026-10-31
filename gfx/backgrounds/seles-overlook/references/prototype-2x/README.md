# Seles overlook prototype

Disc 1, cut 9: Dart overlooking Seles. The loader accepts this complete set of five
1024x712 RGBA PNGs. They replace a 512x356 canvas (the background itself occupies
384x256). Four foreground cutouts retain their original alpha silhouettes at 2x.
The original extracted files are never overwritten.

## Try it

Run the rebuilt game from the repository root. The replacement loads automatically
when reaching the Seles overlook in the opening story. For a direct smoke test,
use Java 25 and run:

```powershell
./gradlew.bat test --tests legend.game.SelesBackgroundTest --tests legend.lodmod.SelesBackgroundsTest -PrunTests
```

Run `tools/export_seles.py` with Python and Pillow first: the smoke test compares
the exported originals against the renderer's GPU textures pixel for pixel. It
creates a fresh test campaign, enters cut 9 scene 0, tests all five enhanced
textures, removes one file temporarily to exercise original fallback, then restores
the file and reloads. It does not validate the complete opening story sequence.
Do not run the packaging tool at the same time as the smoke test.

To disable the artwork, rename this directory to `seles-overlook-disabled`, then
leave/re-enter the scene or restart. To restore it, rename it back. A partial pack
is ignored. Invalid images produce a warning and fall back to the originals.

## Sources and reproduction

`tools/export_seles.py` reads `files/SECT/DRGN21.BIN/31` and writes the assembled
original and four foregrounds to `build/seles/original`. The first CLUT and
nonzero-colour alpha rules match `RetailSubmap.prepareEnv`.

Artwork was edited with the built-in ImageGen tool, one call per texture. The raw
generated outputs were copied into `build/seles/generated`. The tool did not honor
exact output dimensions and subtly changed internal detail. These are experimental
enhancements, not recovered original high-resolution artwork.

`tools/package_seles.cjs <sharp-module-path> <generated-image-directory>` packages
the five generated files named in the script. It fits each visible artwork region
to the original bounds, composites it over the original as an edge fallback, and
restores the original binary alpha mask. It asserts identical masks and writes the
PNG set here, a nearest-neighbour baseline under `build/seles/baseline`, and a
labelled comparison at `build/seles/background-comparison.png`.

## ImageGen prompts

Background (transparent output, original background as the edit target):

> Edit target: the supplied original Legend of Dragoon Seles overlook background texture. Produce a faithful cleanup/upscale at exactly 1024x712 (2x original 512x356). Reduce dithering and pixelation subtly; retain original muted dusk colors, lighting, shapes, every tree, mountains and small burning village. This is an aligned game texture, not a reinterpretation. CRITICAL: retain identical full canvas composition and transparent padding: artwork occupies only top-left 768x512 of output; remaining right and bottom padding stays transparent. Do not fill empty areas, extend scene, crop, reframe, move landmarks, add objects or text. Preserve the original painterly pre-rendered appearance. Only improve clarity.

Foregrounds (four separate calls, each original layer as edit target; substitute
its index 0 through 3 for N; transparent output):

> Faithful cleanup of supplied game foreground texture, layer N. Edit the provided image only: subtly reduce dithering and pixelation in the grass and rocks while keeping exact original ochre/olive colors, shading, vegetation, rock shapes, and every silhouette. Output exactly 1024x712, twice original 512x356. Preserve full canvas, all transparent padding and object positions to match original at 2x. Do not recenter, crop, expand the objects, fill gaps, or add details outside existing silhouettes. Transparent background, no text. This texture must overlay the original game background with exact alignment; original composition is mandatory.
