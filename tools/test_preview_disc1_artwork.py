"""Asset preview checks using synthetic masks, without touching production packs."""
import json
import os
import tempfile
import unittest
from pathlib import Path

from PIL import Image
from preview_disc1_artwork import preview


class PreviewTest(unittest.TestCase):
    def test_masks_scale_and_no_runtime_install(self):
        previous = Path.cwd()
        scratch = previous / 'build/disc1/preview-tests'
        scratch.mkdir(parents=True, exist_ok=True)
        with tempfile.TemporaryDirectory(dir=scratch) as directory:
            assert Path(directory).resolve().parent == scratch.resolve()
            try:
                os.chdir(directory)
                original = Path('build/disc1/original/cut-67')
                source = Path('gfx/backgrounds/disc1/cut-67/source')
                original.mkdir(parents=True)
                source.mkdir(parents=True)
                (original / 'manifest.json').write_text(json.dumps({'cut': 67, 'bounds': [0, 0, 2, 2], 'foreground_layers': 1}))
                (source / 'placement.json').write_text(json.dumps({'mapping': 'full-canvas', 'width': 3, 'height': 3}))
                Image.new('RGBA', (3, 3), 'red').save(source / 'base.png')
                Image.new('RGBA', (2, 2), 'blue').save(original / 'background.png')
                foreground = Image.new('RGBA', (2, 2), 'green')
                mask = Image.new('L', (2, 2))
                mask.putdata([0, 64, 128, 255])
                foreground.putalpha(mask)
                foreground.save(original / 'foreground-0.png')
                preview(67)
                with Image.open(source / 'asset-preview/foreground-0.png') as image:
                    self.assertEqual(image.size, (8, 8))
                    self.assertEqual(image.getchannel('A').tobytes(), mask.resize((8, 8), Image.Resampling.NEAREST).tobytes())
                    self.assertEqual(image.getpixel((0, 0)), (0, 0, 0, 0))
                    self.assertEqual(image.getpixel((4, 0)), (255, 0, 0, 64))
                self.assertFalse((source.parent / 'background.png').exists())
                report = json.loads((source / 'asset-preview/validation.json').read_text())
                self.assertFalse(report['gameplayVerified'])
                self.assertFalse(report['runtimeInstalled'])
                self.assertTrue(report['transparentRgbCleared'])
                (original / 'manifest.json').write_text(json.dumps({'cut': 67, 'bounds': [0, 0, 2, 2], 'foreground_layers': 2}))
                Image.new('RGBA', (2, 2), 'yellow').save(original / 'foreground-1.png')
                staged = Path('build/disc1/staged/cut-67')
                staged.mkdir(parents=True)
                Image.new('RGBA', (8, 8), 'black').save(staged / 'background.png')
                near = Image.new('RGBA', (8, 8), 'red')
                near.putalpha(mask.resize((8, 8), Image.Resampling.NEAREST))
                near.save(staged / 'foreground-0.png')
                Image.new('RGBA', (8, 8), 'blue').save(staged / 'foreground-1.png')
                capture = Path('build/disc1/captures/cut-67/original')
                capture.mkdir(parents=True)
                (capture / 'alignment.csv').write_text('frame,layer,z,zFlags\n0,0,370,0\n0,1,469,0\n')
                preview(67, plate_directory=staged)
                with Image.open(source / 'asset-preview/composite.png') as image:
                    self.assertEqual(image.getpixel((7, 7)), (255, 0, 0, 255))
                (source / 'placement.json').write_text(json.dumps({'mapping': 'full-canvas', 'width': 4, 'height': 3}))
                with self.assertRaises(AssertionError):
                    preview(67)
            finally:
                os.chdir(previous)


if __name__ == '__main__':
    unittest.main()
