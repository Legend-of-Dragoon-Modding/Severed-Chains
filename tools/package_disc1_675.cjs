// Full-canvas placement for the opening cliff, retaining all seven retail masks.
const sharp = require(process.argv[2]);
const fs = require('node:fs/promises');
const assert = require('node:assert/strict');
const root = 'gfx/backgrounds/disc1/cut-675';
const original = 'build/disc1/original/cut-675';
const staged = 'build/disc1/staged/cut-675';
const W = 2048, H = 1024;
async function plate(name) {
  const file = `${root}/source/${name}.png`;
  const info = await sharp(file).metadata();
  assert.equal(info.width, 1774);
  assert.equal(info.height, 887);
  return sharp(file).ensureAlpha().resize(W, H, {fit: 'fill'}).raw().toBuffer();
}
(async () => {
  await fs.mkdir(staged, {recursive: true});
  const base = await plate('base'), lightning = await plate('lightning');
  const report = [];
  for(let i = -1; i < 7; i++) {
    const name = i < 0 ? 'background' : `foreground-${i}`;
    const info = await sharp(`${original}/${name}.png`).metadata();
    assert.equal(info.width, 512); assert.equal(info.height, 256);
    const mask = await sharp(`${original}/${name}.png`).resize(W, H, {kernel: 'nearest'}).extractChannel(3).raw().toBuffer();
    const data = Buffer.from(base);
    // Alternate flash frames retain the original timing, depth and visibility.
    // Feather only the generated lightning patch inside the original sky rectangle.
    const flash = [0, 0.25, 0.65, 1][i] || 0;
    for(let y = 0; y < H; y++) {
      for(let x = 0; x < W; x++) {
        const p = y * W + x, b = p * 4;
        if(flash && mask[p]) {
          const edge = Math.max(0, Math.min(1, (x - 256) / 32, (840 - x) / 32, (y - 20) / 32, (276 - y) / 32));
          const weight = edge * flash;
          for(let c = 0; c < 3; c++) data[b + c] = Math.round(base[b + c] * (1 - weight) + lightning[b + c] * weight);
        }
        data[b + 3] = mask[p];
        if(!mask[p]) data.fill(0, b, b + 3);
      }
    }
    const file = `${staged}/${name}.png`;
    await sharp(data, {raw: {width: W, height: H, channels: 4}}).png().toFile(file);
    assert(mask.equals(await sharp(file).extractChannel(3).raw().toBuffer()));
    report.push({name, width: W, height: H, exactOriginalMask: true});
  }
  await fs.writeFile(staged + '/validation.json', JSON.stringify(report, null, 2));
  if(process.argv.includes('--install')) {
    for(const {name} of report) await fs.copyFile(`${staged}/${name}.png`, `${root}/${name}.png`);
  }
  console.log(JSON.stringify(report));
})().catch(error => {console.error(error); process.exitCode = 1;});
