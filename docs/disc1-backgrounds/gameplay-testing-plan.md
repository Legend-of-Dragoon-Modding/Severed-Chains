# Focused completion testing — completed (2026-10-31)

The approved shortened testing scope is complete. All 72 resolution packs passed
GPU texture loading checks. There are 89 focused screen-load recordings and 34
selected state profiles across the final accepted batches. Five fresh pack and
fallback tests passed; packaging and installer fixtures also passed (3 + 4 tests).
See `resolution/focused-verification.json` for hashes, captures and limitations.

Only the two entrance-floor repairs required final targeted reruns. Full unchanged
batches were not repeated. The strict gameplay comparator remains unchanged.
Original 65-screen route coverage is retained without expanding its claims.

## Optional later full gameplay

1. Walk real entrances, exits and stairs, prioritizing geometry-heavy scenes and
   moving/hidden doors. Record matching original/modern routes and compare traces.
2. Exercise later story variants and transitions that synthetic layer probes did
   not cover. Resolve uncertain variant first-visit order from actual transitions.
3. Run the existing strict comparator for those matched routes. Investigate only
   concrete failures; preserve fallback and the accepted 65-screen evidence.

This optional work has no current completion estimate: duration depends on story
save coverage and route selection. Synthetic endpoint probes establish rendering
only, and paused scripts do not freeze every actor, camera or effect.

## Historical broader testing plan

The following describes the earlier exhaustive scope. Its counts and approval
requirements are superseded by the focused completion scope above.

Production does not establish gameplay verification. Keep the 65 previously
accepted screen records and their exact route coverage. Step 3 installed 88 new
packs after strict packaging and batch loading checks, bringing the installed
total to 153. Full gameplay traversal verification remains deferred for these
88 packs. The remaining 64 unresolved screens use original retail backgrounds.
`testing-queue.json` lists each remaining candidate, worker findings and exact
script references for potential foreground changes.

1. Resolve asset defects and inspect the 64 unresolved candidates with alignment or dynamic
   foreground risks using the saved scripts, masks and existing recordings.
   Camera-mode calls with mode below 3 do not hide overlays. Identify independent
   door plates, moving layers, animation frames and story-dependent visibility.
   Split artwork where needed; do not use a single composite to bake a disappearing
   object into the permanent background. Cut 55 has fully transparent retail
   layers and requires no replacement scenery.
   Cut 80's central floor platform remains misaligned after its focused correction.
   Cut 109's lower broken arch and stepping stones remain uncertain after its
   focused correction. Resolve both against the original guides before acceptance.
   Cut 111 also needs a targeted original GPU/export alignment check for the
   exporter's special placement correction (384×385 original, 1536×1540 modern).
2. Obtain only the missing original layer evidence needed by the strict existing
   packagers. The parent runs bounded, targeted sessions, one engine at a time,
   confirming the previous process exited and restoring disabled artwork in
   every exit path. Do not repeat existing completed captures. First-visit order
   for uncertain variants must be supported by scripts or actual transitions.
3. Package supported screens with the existing tools, checking exact 4× canvases,
   all layer names/counts, exact original alpha and placement metadata. Integrate
   runtime mappings in batches; run the relevant non-game pack loading, malformed
   asset fallback and resource cleanup checks once per batch. Preserve original
   fallback and unrelated Dart/TwinShot changes.
4. Compare original and modern routes using the unchanged strict comparator.
   Cover first arrivals, normal traversal, doorway thresholds, stairs/landings,
   camera scroll, foreground changes and relevant story variants. Test moving
   and hidden layers in each relevant state. Review actual matching game captures
   at the geometric boundaries that asset previews cannot establish.
5. Close existing coverage gaps as well as the new queue: previously accepted
   screens include unvisited side rooms, entrances and later variants. Retain
   README/progress limits, including cut 27's storage-door frame, cut 50's upper
   islands and creature interactions, and the unrecorded Bale/Indels routes.
   Promote a screen's verification status only for routes actually checked.

Record exact results, failures and remaining gaps. Rebuild the gallery with
asset previews and verified game captures clearly distinguished. Mark Disc 1
fully verified only when the agreed scene/variant scope has passing evidence.

Current step-2 evidence: all 100 selected originals were inspected; 88 meet the
static packager gate, and 12 additional cases have actual hidden/offset layers.
See `preparation/summary.json` for exact affected layers. There are now 64
unresolved screens. The inspection recordings are not traversal verification.
Step 3 requires explicit approval under the requested step-exec workflow.
