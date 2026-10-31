// node tools/package_seles.cjs <sharp module path> [--install]
// Explicit whole-canvas source sampling, never silhouette/bounding-box fitting.
const sharp = require(process.argv[2]);
const fs = require('node:fs/promises');
const assert = require('node:assert/strict');
const W = 2048, H = 1424;
const source = 'gfx/backgrounds/seles-overlook/source';
const staged = 'build/seles/modern-pack';
const names = ['background', 'foreground-0', 'foreground-1', 'foreground-2', 'foreground-3'];

async function checked(file, width, height) {
  const metadata = await sharp(file).metadata();
  assert.equal(metadata.width, width, `Unexpected source width: ${file}`);
  assert.equal(metadata.height, height, `Unexpected source height: ${file}`);
  return sharp(file);
}

// Extend RGB into uncovered edge texels without changing placement or alpha.
function bleed(data, width, height) {
  const count = width * height, queue = new Int32Array(count), seen = new Uint8Array(count);
  let head = 0, tail = 0;
  for(let i = 0; i < count; i++) {
    const p = i * 4;
    if(data[p + 3] >= 250 && !(data[p] > 2 * data[p + 1] && data[p] > 2 * data[p + 2])) {
      seen[i] = 1; queue[tail++] = i;
    }
  }
  assert(tail > 0, 'Source contains no opaque artwork');
  while(head < tail) {
    const i = queue[head++], x = i % width;
    for(const n of [x > 0 ? i - 1 : -1, x < width - 1 ? i + 1 : -1, i - width, i + width]) {
      if(n < 0 || n >= count || seen[n]) continue;
      seen[n] = 1; queue[tail++] = n;
      data[n * 4] = data[i * 4]; data[n * 4 + 1] = data[i * 4 + 1]; data[n * 4 + 2] = data[i * 4 + 2];
    }
  }
  return data;
}

(async () => {
  await fs.mkdir(staged, {recursive: true});
  const transparent = () => sharp({create: {width: W, height: H, channels: 4, background: '#00000000'}});
  const bg = await (await checked(source + '/background-plate.png', 1536, 1024)).ensureAlpha().png().toBuffer();
  const inputs = [await transparent().composite([{input: bg, left: 0, top: 0}]).raw().toBuffer()];
  // All four layers share identical scripted XY motion. One coherent master makes
  // every overlap pixel identical while the original masks retain separate depths.
  // Explicit full-canvas mapping: 1504x1046 -> 2048x1424; never fit detected bounds.
  const master = bleed(await (await checked(source + '/foreground-master.png', 1504, 1046))
    .ensureAlpha().resize(W, H, {fit: 'fill'}).raw().toBuffer(), W, H);
  for(let i = 0; i < 4; i++) inputs.push(Buffer.from(master));
  const report = [];
  for(let i = 0; i < names.length; i++) {
    const original = `build/seles/original/${names[i]}.png`;
    await checked(original, 512, 356);
    const mask = await sharp(original).resize(W, H, {kernel: 'nearest'}).extractChannel(3).raw().toBuffer();
    const data = inputs[i];
    for(let p = 0; p < mask.length; p++) {
      data[p * 4 + 3] = mask[p];
      if(!mask[p]) data.fill(0, p * 4, p * 4 + 3);
    }
    const output = `${staged}/${names[i]}.png`;
    await sharp(data, {raw: {width: W, height: H, channels: 4}}).png().toFile(output);
    const saved = await checked(output, W, H);
    const actual = await saved.extractChannel(3).raw().toBuffer();
    assert(mask.equals(actual), `Mask changed: ${names[i]}`);
    report.push({name: names[i], width: W, height: H, exactOriginalMask: true});
  }
  await fs.writeFile(staged + '/validation.json', JSON.stringify(report, null, 2));
  // RGB is copied unchanged from the same master beneath every foreground mask.
  for(let i = 1; i < inputs.length; i++) {
    for(let j = i + 1; j < inputs.length; j++) {
      for(let p = 0; p < W * H * 4; p += 4) {
        if(inputs[i][p + 3] && inputs[j][p + 3]) {
          assert(inputs[i].subarray(p, p + 3).equals(inputs[j].subarray(p, p + 3)), 'Foreground overlap seam');
        }
      }
    }
  }
  if(process.argv.includes('--install')) {
    for(const name of names) await fs.copyFile(`${staged}/${name}.png`, `gfx/backgrounds/seles-overlook/${name}.png`);
  }
  console.log(JSON.stringify(report));
})().catch(error => {console.error(error); process.exitCode = 1;});
