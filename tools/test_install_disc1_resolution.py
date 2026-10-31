"""Exercise installation integrity only inside disposable fixture workspaces."""
import contextlib
import io
import json
import os
import re
import shutil
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

from PIL import Image
import install_disc1_resolution as installer

SCRATCH = Path(__file__).resolve().parents[1] / 'build/disc1/installer-tests'


@contextlib.contextmanager
def fixture():
    previous = Path.cwd()
    SCRATCH.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory(dir=SCRATCH) as directory:
        os.chdir(directory)
        try:
            root = Path('docs/disc1-backgrounds/resolution')
            root.mkdir(parents=True)
            Path('docs/disc1-backgrounds/installation').mkdir()
            hook = Path('src/main/java/legend/lodmod/SelesBackgrounds.java')
            hook.parent.mkdir(parents=True)
            hook.write_text(''.join(f'      case {cut} -> {{ return; }}\n' for cut in range(1000, 1145)) + '      default -> { return; }\n')
            plan, rows = [], []
            for cut in range(1, 73):
                original = Path(f'build/disc1/original/cut-{cut}')
                staged = Path(f'build/disc1/staged/cut-{cut}')
                target = Path(f'gfx/backgrounds/disc1/cut-{cut}')
                for path in (original, staged, target):
                    path.mkdir(parents=True)
                (original / 'manifest.json').write_text(json.dumps({'bounds': [0, 0, 1, 1], 'foreground_layers': 0}))
                Image.new('RGBA', (1, 1), (80, 90, 100, 255)).save(original / 'background.png')
                Image.new('RGBA', (4, 4), (80, 90, 100, 255)).save(staged / 'background.png', compress_level=0)
                if cut == 1:
                    (target / 'background.png').write_bytes(b'previous runtime pack')
                entry = {'name': 'background', 'sha256': installer.digest(staged / 'background.png'), 'bytes': (staged / 'background.png').stat().st_size}
                validation = staged / ('layered-validation.json' if cut % 2 else 'validation.json')
                validation.write_text(json.dumps({'layers': [dict(entry)]} if cut % 2 else [{'exactOriginalMask': True}]))
                rows.append({'cut': cut, 'packagingPrepared': True, 'assetChecksPassed': True, 'validation': validation.as_posix(), 'validationSha256': installer.digest(validation), 'files': [entry]})
                plan.append({'cut': cut, 'wasInstalled': cut == 1})
            (root / 'plan.json').write_text(json.dumps({'screens': plan}))
            (root / 'summary.json').write_text(json.dumps({'complete': True, 'resolvedScreens': 72, 'screens': rows}))
            (root / 'quarantined-installed.json').write_text(json.dumps({'cuts': [1]}))
            (root / 'preservation-baseline.json').write_text('{}')
            yield root, hook
        finally:
            os.chdir(previous)


class InstallTest(unittest.TestCase):
    def test_atomic_copy_failure_preserves_target_backup_and_removes_temporary(self):
        SCRATCH.mkdir(parents=True, exist_ok=True)
        with tempfile.TemporaryDirectory(dir=SCRATCH) as directory:
            root = Path(directory)
            source, target, backup = (root / name for name in ('source', 'target', 'backup'))
            source.write_bytes(b'new pack')
            target.write_bytes(b'old pack')
            installer.atomic_copy(target, backup)

            def interrupted_copy(source, temporary):
                Path(temporary).write_bytes(b'partial copy')
                raise OSError('injected interrupted copy')

            with patch.object(shutil, 'copyfile', side_effect=interrupted_copy):
                with self.assertRaisesRegex(OSError, 'injected'):
                    installer.atomic_copy(source, target)
            self.assertEqual(target.read_bytes(), b'old pack')
            self.assertEqual(backup.read_bytes(), b'old pack')
            self.assertEqual(sorted(path.name for path in root.iterdir()), ['backup', 'source', 'target'])

    def test_install_updates_optimized_hashes_and_preserves_static_validation(self):
        with fixture() as (root, hook), contextlib.redirect_stdout(io.StringIO()):
            static_path = Path('build/disc1/staged/cut-2/validation.json')
            static_before = static_path.read_bytes()
            installer.install()
            summary = json.loads((root / 'summary.json').read_text())
            for row in summary['screens']:
                staged = Path(f'build/disc1/staged/cut-{row["cut"]}/background.png')
                self.assertTrue(row['files'][0]['optimization']['rewritten'])
                self.assertEqual(row['files'][0]['sha256'], installer.digest(staged))
                self.assertEqual(row['validationSha256'], installer.digest(row['validation']))
                validation = json.loads(Path(row['validation']).read_text())
                if isinstance(validation, dict):
                    self.assertEqual(validation['layers'][0]['sha256'], installer.digest(staged))
            self.assertEqual(static_before, static_path.read_bytes())
            self.assertEqual(len(re.findall(r'^      case (\d+) ->', hook.read_text(), re.M)), 217)
            self.assertEqual(Path('build/disc1/resolution-runtime-backup/cut-1/background.png').read_bytes(), b'previous runtime pack')

    def test_failed_pack_copy_leaves_mappings_unregistered(self):
        with fixture() as (root, hook), contextlib.redirect_stdout(io.StringIO()):
            before = hook.read_bytes()
            atomic_copy = installer.atomic_copy

            def copy_until_second_pack(source, target):
                if target == Path('gfx/backgrounds/disc1/cut-2/background.png'):
                    raise OSError('injected second pack failure')
                atomic_copy(source, target)

            with patch.object(installer, 'atomic_copy', side_effect=copy_until_second_pack):
                with self.assertRaisesRegex(OSError, 'injected'):
                    installer.install()
            self.assertEqual(hook.read_bytes(), before)
            report = json.loads((root / 'runtime-integration.json').read_text())
            self.assertTrue(report['packs'][0]['copied'])
            self.assertFalse(report['registered'])
            self.assertEqual(Path('build/disc1/resolution-runtime-backup/cut-1/background.png').read_bytes(), b'previous runtime pack')
            self.assertFalse(list(Path('.').rglob('*.install.tmp')))

    def test_changed_plan_is_rejected_before_installation(self):
        with fixture() as (root, hook):
            before = hook.read_bytes()
            plan = json.loads((root / 'plan.json').read_text())
            plan['screens'][-1]['cut'] = 999
            (root / 'plan.json').write_text(json.dumps(plan))
            with self.assertRaisesRegex(AssertionError, 'does not match'):
                installer.install()
            self.assertEqual(hook.read_bytes(), before)
            self.assertFalse((root / 'runtime-integration.json').exists())
            self.assertFalse(Path('build/disc1/resolution-runtime-backup').exists())


if __name__ == '__main__':
    unittest.main()
