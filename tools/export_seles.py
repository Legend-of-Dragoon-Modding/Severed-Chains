"""Export cut 9 exactly as RetailSubmap.prepareEnv assembles it (requires Pillow).

Run from the repository root. Only reads extracted retail data; outputs go to build/.
"""
import json
import struct
from pathlib import Path
from PIL import Image


def export(cut=9, output=None, entry_override=None):
    root = Path('files/SUBMAP/NEWROOT.RDT').read_bytes()
    entry = struct.unpack_from('<H', root, cut * 8)[0] if entry_override is None else entry_override
    disk = (entry >> 13) + 1
    file_index = (entry & 0x1fff) * 3 + (4 if disk <= 4 else 0)
    source = Path(f'files/SECT/DRGN2{disk}.BIN/{file_index}')
    output = Path(output or 'build/seles/original')
    output.mkdir(parents=True, exist_ok=True)
    env = (source / '0').read_bytes()
    count, backgrounds, foregrounds = env[20:23]
    assert count == backgrounds + foregrounds and backgrounds > 0
    textures = []
    for number in sorted(int(p.name) for p in source.iterdir() if p.name.isdigit() and int(p.name) >= 3):
        data = (source / str(number)).read_bytes()
        magic, flags, clut_size = struct.unpack_from('<III', data)
        assert magic == 16 and flags & 8
        depth = flags & 7
        assert depth in (0, 1)
        x, y, words, height = struct.unpack_from('<4H', data, clut_size + 12)
        palette_width = struct.unpack_from('<H', data, 16)[0]
        palette = struct.unpack_from(f'<{palette_width}H', data, 20)
        width = words * (4 if depth == 0 else 2)
        raw = data[clut_size + 20:]
        indices = ([n for b in raw for n in (b & 15, b >> 4)] if depth == 0 else list(raw))
        assert len(indices) == width * height
        pixels = []
        for index in indices:
            colour = palette[index]
            pixels.append(((colour & 31) * 8, ((colour >> 5) & 31) * 8,
                           ((colour >> 10) & 31) * 8, 255 if colour else 0))
        image = Image.new('RGBA', (width, height))
        image.putdata(pixels)
        textures.append(((x, y, words, height), image))

    pieces = []
    rectangles = []
    notes = []
    for index in range(count):
        offset = 24 + index * 36
        u, v, width, height, x, y = struct.unpack_from('<6h', env, offset + 8)
        # Keep the engine's existing retail alignment fixes in exported references.
        if cut == 111 and index == 8:
            y += 1
        if cut == 642 and index == 2:
            width -= 1
        page = struct.unpack_from('<H', env, offset + 32)[0]
        tx, ty = (page & 15) * 64, ((page >> 4) & 1) * 256
        texture = next((im for (px, py, pw, ph), im in textures
                        if px <= tx < px + pw and py <= ty < py + ph), None)
        if texture is None:
            assert index >= backgrounds and width == height == 0, 'Missing nonempty source texture'
            pieces.append((x, y, Image.new('RGBA', (1, 1))))
            notes.append(f'Unused foreground slot {index - backgrounds}: no retail texture')
            continue
        rectangles.append((x, y, width, height))
        assert 0 <= u <= texture.width and 0 <= v <= texture.height
        # RetailSubmap clips malformed foreground source rectangles too.
        if index >= backgrounds:
            width, height = min(width, texture.width - u), min(height, texture.height - v)
            if width == 0 or height == 0:
                pieces.append((x, y, Image.new('RGBA', (1, 1))))
                notes.append(f'Empty foreground slot {index - backgrounds}: clipped by renderer')
                continue
        else:
            assert u + width <= texture.width and v + height <= texture.height
        pieces.append((x, y, texture.crop((u, v, u + width, v + height))))
    left = min(r[0] for r in rectangles)
    top = min(r[1] for r in rectangles)
    width = max(x + w for x, y, w, h in rectangles) - left
    height = max(y + h for x, y, w, h in rectangles) - top
    background = Image.new('RGBA', (width, height))
    for x, y, im in pieces[:backgrounds]:
        background.paste(im, (x - left, y - top))
    background.save(output / 'background.png')
    for index, (x, y, im) in enumerate(pieces[backgrounds:]):
        layer = Image.new('RGBA', (width, height))
        layer.paste(im, (x - left, y - top))
        layer.save(output / f'foreground-{index}.png')
    metadata = dict(cut=cut, disk=disk, source=str(source), bounds=[left, top, width, height],
                    notes=notes,
                    background_fragments=backgrounds, foreground_layers=foregrounds,
                    pieces=[dict(x=x, y=y, width=im.width, height=im.height)
                            for x, y, im in pieces])
    (output / 'manifest.json').write_text(json.dumps(metadata, indent=2))
    print(json.dumps(metadata))
    return metadata


if __name__ == '__main__':
    import argparse
    parser = argparse.ArgumentParser()
    parser.add_argument('--cut', type=int, default=9)
    parser.add_argument('--output')
    args = parser.parse_args()
    export(args.cut, args.output)
