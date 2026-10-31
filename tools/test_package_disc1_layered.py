import json
import os
from pathlib import Path
import tempfile
import unittest

from PIL import Image

from package_disc1_layered import package, layer_order, bleed_transparent_rgb


class LayeredPackagingTest(unittest.TestCase):
    def test_atlas_edge_bleed_preserves_visible_pixels_and_alpha(self):
        image = Image.new('RGBA', (5, 3))
        image.putpixel((2, 1), (73, 121, 201, 255))
        image.putpixel((3, 1), (180, 60, 22, 64))
        result = bleed_transparent_rgb(image)
        self.assertEqual(result.getpixel((2, 1)), image.getpixel((2, 1)))
        self.assertEqual(result.getpixel((3, 1)), image.getpixel((3, 1)))
        self.assertEqual(result.getchannel('A').tobytes(), image.getchannel('A').tobytes())
        self.assertNotEqual(result.getpixel((0, 0))[:3], (0, 0, 0))
        with self.assertRaisesRegex(AssertionError, 'entirely transparent'):
            bleed_transparent_rgb(Image.new('RGBA', (2, 2)))
        noisy = Image.new('RGBA', (3, 1))
        noisy.putpixel((0, 0), (73, 121, 201, 255))
        noisy.putpixel((1, 0), (0, 0, 0, 1))
        cleaned = bleed_transparent_rgb(noisy, 128)
        self.assertEqual(cleaned.getpixel((0, 0)), noisy.getpixel((0, 0)))
        self.assertEqual(cleaned.getpixel((1, 0)), (73, 121, 201, 1))
        self.assertEqual(cleaned.getchannel('A').tobytes(), noisy.getchannel('A').tobytes())

    def test_foreground_depth_can_place_a_lower_slot_in_front(self):
        self.assertEqual(layer_order({8: {'z': '370', 'zFlags': '0'}, 10: {'z': '469', 'zFlags': '0'}}), [10, 8])
        self.assertEqual(layer_order({8: {'z': '370', 'zFlags': str(0x8000 | 500)}, 10: {'z': '469', 'zFlags': '0'}}), [8, 10])

    def test_preserves_partial_alpha_and_rejects_unreviewed_motion(self):
        previous = Path.cwd()
        fixtures = (previous / 'build/disc1/layered-tests').resolve()
        fixtures.mkdir(parents=True, exist_ok=True)
        with tempfile.TemporaryDirectory(dir=fixtures) as temporary:
            assert Path(temporary).resolve().parent == fixtures
            try:
                os.chdir(temporary)
                source = Path('gfx/backgrounds/disc1/cut-1/source/layer-resolution')
                original = Path('build/disc1/original/cut-1')
                capture = Path('build/disc1/captures/cut-1/original')
                for directory in (source, original, capture):
                    directory.mkdir(parents=True)
                (original / 'manifest.json').write_text(json.dumps({'cut': 1, 'bounds': [0, 0, 3, 1], 'foreground_layers': 2}))
                retail = Image.new('RGBA', (3, 1))
                retail.putdata([(20, 30, 40, alpha) for alpha in (0, 128, 255)])
                for name in ('background', 'foreground-0', 'foreground-1'):
                    retail.save(original / f'{name}.png')
                Image.new('RGB', (3, 1), (100, 150, 200)).save(source / 'underlay.png')
                config = {'background': 'underlay.png', 'layers': {'0': 'underlay.png', '1': 'underlay.png'},
                          'dynamicLayers': [0], 'visualReview': True, 'evidence': ['Synthetic test fixture']}
                (source / 'plates.json').write_text(json.dumps(config))
                header = 'frame,cut,layer,startX,startY,destX,destY,hidden\n'
                (capture / 'alignment.csv').write_text(header + '0,1,0,0,0,0,-1,true\n0,1,1,0,0,0,0,false\n')
                (capture / 'original-gpu-export.txt').write_text('Original GPU textures exactly match exported PNGs for cut 1')
                package(1)
                with Image.open('build/disc1/staged/cut-1/foreground-0.png') as image:
                    self.assertEqual(list(image.getchannel('A').getdata()), [0] * 4 + [128] * 4 + [255] * 4 + ([0] * 4 + [128] * 4 + [255] * 4) * 3)
                    self.assertEqual(image.getpixel((0, 0)), (0, 0, 0, 0))
                Image.new('RGBA', (1, 1), (255, 0, 0, 128)).save(source / 'patch.png')
                config['background'] = {'source': 'underlay.png', 'patches': [
                    {'source': 'patch.png', 'sourceBounds': [0, 0, 1, 1], 'targetBounds': [1, 0, 2, 1]}]}
                (source / 'plates.json').write_text(json.dumps(config))
                package(1)
                with Image.open('build/disc1/staged/cut-1/background.png') as image:
                    self.assertEqual(image.getpixel((4, 0)), (178, 75, 100, 128))
                    self.assertEqual(image.getpixel((11, 3)), (100, 150, 200, 255))
                mask = retail.copy()
                mask.putalpha(Image.new('L', (3, 1), 255))
                mask.putpixel((1, 0), (20, 30, 40, 0))
                mask.save(original / 'foreground-1.png')
                patch = config['background']['patches'][0]
                patch.update(targetBounds=[0, 0, 3, 1], maskLayer=1)
                (source / 'plates.json').write_text(json.dumps(config))
                package(1)
                with Image.open('build/disc1/staged/cut-1/background.png') as image:
                    self.assertEqual(image.getpixel((4, 0)), (100, 150, 200, 128), 'Native mask hole must preserve the clean room')
                    self.assertEqual(image.getpixel((11, 3)), (178, 75, 100, 255))
                patch['targetBounds'] = [1, 0, 3, 1]
                (source / 'plates.json').write_text(json.dumps(config))
                with self.assertRaisesRegex(AssertionError, 'exact retail alpha bounds'):
                    package(1)
                retail.save(original / 'foreground-1.png')
                black_void = retail.copy()
                black_void.putpixel((1, 0), (24, 16, 16, 128))
                black_void.putpixel((2, 0), (8, 0, 0, 255))
                for path in original.glob('*.png'): black_void.save(path)
                patch.pop('maskLayer')
                patch.update(targetBounds=[0, 0, 3, 1], maskBackgroundRgb=True, exteriorRgb=[[0,0,0],[8,8,8],[16,16,16],[24,16,16]])
                config['layers']['1'] = {'source':'underlay.png','clearRetailExteriorRgb':{'bounds':[0,0,3,1],'colors':[[24,16,16]]}}
                (source / 'plates.json').write_text(json.dumps(config))
                package(1)
                with Image.open('build/disc1/staged/cut-1/background.png') as image:
                    self.assertEqual(image.getpixel((4, 0)), (100, 150, 200, 128), 'Opaque retail checker void must not gain floor RGB')
                    self.assertEqual(image.getpixel((11, 3)), (178, 75, 100, 255))
                with Image.open('build/disc1/staged/cut-1/foreground-1.png') as image:
                    self.assertEqual(image.getpixel((4, 0)), (0, 0, 0, 128), 'Foreground floor-context checker RGB must clear without changing native alpha')
                    self.assertEqual(image.getpixel((11, 3)), (100, 150, 200, 255))
                config['layers']['1']['clearRetailExteriorRgb']['polygon']=[[2,0],[3,0],[3,1],[2,1]]
                (source / 'plates.json').write_text(json.dumps(config))
                package(1)
                with Image.open('build/disc1/staged/cut-1/foreground-1.png') as image:
                    self.assertEqual(image.getpixel((4, 0)), (100, 150, 200, 128), 'Dark wood above the floor polygon must remain unchanged')
                for path in original.glob('*.png'): retail.save(path)
                config['layers']['1'] = 'underlay.png'
                config['background'] = 'underlay.png'
                config['layers']['0'] = {'source': 'underlay.png', 'sourceBounds': [0, 0, 3, 1]}
                (source / 'plates.json').write_text(json.dumps(config))
                package(1)
                with Image.open('build/disc1/staged/cut-1/foreground-0.png') as image:
                    self.assertEqual(image.getpixel((4, 0)), (100, 150, 200, 128))
                    self.assertEqual(image.getpixel((11, 3)), (100, 150, 200, 255))
                (capture / 'alignment.csv').write_text(header + '0,1,0,0,0,0,-1,true\n0,1,1,0,0,0,0,true\n')
                with self.assertRaisesRegex(AssertionError, 'Unexpected dynamic layer'):
                    package(1)
                (capture / 'alignment.csv').write_text(header + '0,1,0,0,0,0,-1,true\n0,1,1,0,0,0,0,false\n')
                retail.putdata([(80, 90, 100, alpha) for alpha in (0, 128, 255)])
                retail.save(original / 'foreground-0.png')
                with self.assertRaisesRegex(AssertionError, 'still baked identically'):
                    package(1)
                from package_disc1_layered import digest
                analysis = source / 'native-states.json'
                analysis.write_text(json.dumps({'cut':1,'states':[{'visible':[]},{'visible':[0]}]}))
                review={'cut':1,'layers':[0],'geometryUnchanged':True,'reviewers':['one','two'],
                        'traceSha256':digest(capture/'alignment.csv'),
                        'nativeHashes':{p.name:digest(p) for p in original.glob('*.png')},
                        'nativeStateAnalysis':'native-states.json','nativeStateAnalysisSha256':digest(analysis)}
                config.update(sharedSceneryLayers=[0],sharedSceneryReview='shared-review.json')
                (source/'shared-review.json').write_text(json.dumps(review))
                (source/'plates.json').write_text(json.dumps(config))
                package(1)
                review['nativeHashes']['foreground-0.png']='changed'
                (source/'shared-review.json').write_text(json.dumps(review))
                with self.assertRaisesRegex(AssertionError,'native evidence changed'):
                    package(1)
            finally:
                os.chdir(previous)


if __name__ == '__main__':
    unittest.main()
