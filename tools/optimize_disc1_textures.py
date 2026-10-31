"""Clear fully transparent RGB and recompress PNGs, preserving visible pixels."""
import argparse
import hashlib
import json
import os
import tempfile
from pathlib import Path

from PIL import Image, ImageChops


def optimize_image(path):
    path = Path(path)
    before_bytes = path.read_bytes()
    before_hash = hashlib.sha256(before_bytes).hexdigest()
    with Image.open(path) as image:
        assert image.mode == 'RGBA', (path, image.mode)
        before = image.copy()
    alpha = before.getchannel('A')
    invisible = alpha.point(lambda value: 255 if value == 0 else 0)
    candidate = before.copy()
    candidate.paste((0, 0, 0, 0), mask=invisible)
    pixels_identical = candidate.tobytes() == before.tobytes()
    handle = tempfile.NamedTemporaryFile(dir=path.parent, suffix='.optimization.tmp.png', delete=False)
    temporary = Path(handle.name)
    handle.close()
    try:
        candidate.save(temporary, optimize=True, compress_level=9)
        with Image.open(temporary) as image:
            decoded = image.convert('RGBA')
        assert decoded.size == before.size
        assert decoded.getchannel('A').tobytes() == alpha.tobytes(), f'Alpha changed: {path}'
        difference = ImageChops.difference(before.convert('RGB'), decoded.convert('RGB'))
        difference.paste((0, 0, 0), mask=invisible)
        assert difference.getbbox() is None, f'Visible RGB changed: {path}'
        assert hashlib.sha256(path.read_bytes()).hexdigest() == before_hash, f'Concurrent change: {path}'
        rewritten = temporary.stat().st_size < len(before_bytes)
        if rewritten:
            os.replace(temporary, path)
        result = {
            'path': path.as_posix(), 'beforeBytes': len(before_bytes), 'afterBytes': path.stat().st_size,
            'rewritten': rewritten, 'dimensions': list(before.size), 'alphaExact': True,
            'visibleRgbExact': True, 'decodedPixelsIdentical': pixels_identical if rewritten else True,
            'beforeSha256': before_hash, 'afterSha256': hashlib.sha256(path.read_bytes()).hexdigest(),
        }
        assert result['afterBytes'] <= result['beforeBytes']
        return result
    finally:
        temporary.unlink(missing_ok=True)


def optimize_batch(plan_path, worker):
    plan = json.loads(Path(plan_path).read_text())
    jobs = [job for job in plan['jobs'] if job['worker'] == worker]
    assert jobs
    report_path = Path(plan['reportDirectory']) / f'worker-{worker}.json'
    assert not report_path.exists(), 'Do not rerun a completed/partial batch without inspecting its report'
    report = {'worker': worker, 'complete': False, 'screens': []}
    for job in jobs:
        directory = Path(job['directory'])
        names = job['names']
        assert len(names) == len(set(names))
        before_composite = Image.new('RGBA', tuple(job['dimensions']))
        after_composite = Image.new('RGBA', tuple(job['dimensions']))
        layers = []
        for name in names:
            path = directory / name
            with Image.open(path) as image:
                assert list(image.size) == job['dimensions']
                before_composite.alpha_composite(image.convert('RGBA'))
            result = optimize_image(path)
            layers.append(result)
            with Image.open(path) as image:
                after_composite.alpha_composite(image.convert('RGBA'))
        assert before_composite.tobytes() == after_composite.tobytes(), f'Composite changed: {job["cut"]}'
        screen = {'cut': job['cut'], 'kind': job['kind'], 'directory': job['directory'],
                  'beforeBytes': sum(layer['beforeBytes'] for layer in layers),
                  'afterBytes': sum(layer['afterBytes'] for layer in layers),
                  'compositePixelsIdentical': True, 'layers': layers}
        report['screens'].append(screen)
        report_path.write_text(json.dumps(report, indent=2) + '\n')
        if len(report['screens']) % 10 == 0:
            print(json.dumps({'worker': worker, 'completed': len(report['screens']), 'assigned': len(jobs)}), flush=True)
    report['complete'] = True
    report_path.write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({'worker': worker, 'complete': True, 'screens': len(jobs),
                      'beforeBytes': sum(s['beforeBytes'] for s in report['screens']),
                      'afterBytes': sum(s['afterBytes'] for s in report['screens'])}), flush=True)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--plan', required=True)
    parser.add_argument('--worker', type=int, choices=[1, 2, 3], required=True)
    args = parser.parse_args()
    optimize_batch(args.plan, args.worker)
