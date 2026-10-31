# Local Disc 1 runtime images

The 2,275 runtime PNGs are stored outside Git at:

`C:\Users\xxx\Severed-Chains-Backgrounds`

The folder contains `disc1/cut-<number>/`, `seles-overlook/` and a SHA-256
`manifest.json`. The existing workspace image paths are NTFS hard links to those
files: they use the same file data without allocating a second image copy. The
background loader and its expected paths are unchanged. Source artwork, prompts,
placement metadata and review files were not moved by this operation.

Runtime PNG paths are ignored by Git. Code, tests, tools and selected metadata
remain staged separately. Earlier installation and Git-readiness reports are
historical snapshots; their image manifests describe local assets, not files to
commit. Large source/reference images and gallery previews also remain local.

A fresh clone will not contain the remade backgrounds. Copy the runtime folders
from this local asset folder into `gfx/backgrounds/` before running or packaging
that clone. Without the images, the game uses original-background fallback.
The usual build copies the available workspace `gfx/` tree, so this workspace can
still package the linked images; a clean clone needs the image copy first.

Hard links are local filesystem relationships and are not recreated by Git. The
two paths refer to the same data and are not independent backups. A tool that
replaces a PNG atomically can detach its workspace link; synchronize the final
runtime PNG back to the external folder after any future regeneration or repair.

The known fullscreen coverage issue and incomplete full-story verification
remain unchanged. No commit or push was made during local-asset separation.
