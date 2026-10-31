# Disc 1 background completion — approved focused scope

Completed locally on 2026-10-31 on `main-background-rework-disc-1`.

- 217 visible background packs installed; transparent cut 55 retains retail fallback.
- All 72 resolution cases repaired and audited; zero remaining blocking asset findings.
- 2,275 runtime PNGs: 1,200,671,421 bytes (1.20 GB / 1.12 GiB).
- Five fresh pack/fallback tests passed. Packaging and installer fixtures passed (3 + 4).
- 72 packs passed exact installed-PNG/GPU texture checks, with 89 focused loads,
  34 selected state profiles, and targeted camera followups.
- Exact 4× retail alpha masks, runtime/source hashes and registration were audited.
  The preservation audit confirms 3,025 protected files unchanged outside intended
  replacements. Eight replaced packs have backups in `build/disc1/resolution-runtime-backup`.

[Open the review gallery](review.html). It separates the earlier 65 matched-route
reviews, 72 focused GPU reviews and 152 production asset previews. Generation
requests, references and used raw outputs are retained alongside the sources.

The corrected script association is environment fileIndex + 2. Previously hidden
layer changes now have independent plates, except cut 175's evidence-reviewed
shared floor/wall scenery, with unchanged native depth masks. Cuts 102/626 received
supporting-floor RGB repairs and final door-state GPU review; native mask contours
remain pixel stepped. Cut 197's phase textures vary more strongly than native art.

The approved implementation steps are finished. Runtime artwork is installed in
the current workspace. Branch and remotes are recorded in `resolution/git-readiness.json`.
Nothing is staged, committed or pushed. The source/reference/review assets occupy
additional space beyond the runtime pack; selective delivery details are in that
Git readiness report.

Full story traversal and unexercised variants remain unverified. Focused synthetic
states and camera probes do not establish normal story progression. Existing
65-screen route coverage limits remain in progress.json and the historical README.
Optional follow-up routes are recorded in gameplay-testing-plan.md/testing-queue.json.
No additional full test pass is required for the approved focused scope.
