"""Verify alpha, partially transparent colors and decoded composites are preserved."""
import tempfile
import unittest
from pathlib import Path

from PIL import Image
from optimize_disc1_textures import optimize_image


class OptimizeTest(unittest.TestCase):
    def test_invisible_rgb_only_and_idempotence(self):
        scratch = Path('build/disc1/optimization-tests')
        scratch.mkdir(parents=True, exist_ok=True)
        with tempfile.TemporaryDirectory(dir=scratch) as directory:
            path = Path(directory) / 'texture.png'
            image = Image.new('RGBA', (64, 64))
            image.putdata([((i * 13) % 256, (i * 23) % 256, (i * 31) % 256, (0, 1, 127, 255)[i % 4]) for i in range(4096)])
            image.save(path, compress_level=0)
            original = image.tobytes()
            report = optimize_image(path)
            self.assertTrue(report['rewritten'])
            self.assertLess(report['afterBytes'], report['beforeBytes'])
            with Image.open(path) as decoded:
                actual = decoded.tobytes()
            for offset in range(0, len(original), 4):
                self.assertEqual(original[offset + 3], actual[offset + 3])
                self.assertEqual(actual[offset:offset + 3], original[offset:offset + 3] if original[offset + 3] else bytes(3))
            first_hash = report['afterSha256']
            second = optimize_image(path)
            self.assertFalse(second['rewritten'])
            self.assertEqual(first_hash, second['afterSha256'])


if __name__ == '__main__':
    unittest.main()
