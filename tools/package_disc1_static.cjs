// Only for scenes whose recorded foregrounds remain fixed and visible.
const sharp = require(process.argv[2]);
const fs = require('node:fs/promises');
const assert = require('node:assert/strict');
const cut = Number(process.argv[3]);
const root = `gfx/backgrounds/disc1/cut-${cut}`;
const original = `build/disc1/original/cut-${cut}`;
const staged = `build/disc1/staged/cut-${cut}`;
(async () => {
  const manifest = JSON.parse(await fs.readFile(original + '/manifest.json'));
  const placement = JSON.parse(await fs.readFile(root + '/source/placement.json'));
  assert.equal(placement.mapping, 'full-canvas');
  assert.equal(placement.stationary_layers_verified, true);
  const trace = (await fs.readFile(`build/disc1/captures/cut-${cut}/original/alignment.csv`, 'utf8')).trim().split(/\r?\n/).slice(1);
  assert(trace.length > 0, 'A scripted original recording is required');
  for(const row of trace) {
    const match = row.match(/^\d+,\d+,"[^"]*","[^"]*",\d+,(-?\d+),(-?\d+),(-?\d+),(-?\d+),(true|false),/);
    assert(match && match.slice(1, 5).every(value => value === '0') && match[5] === 'false', 'Moving or hidden layers require separate artwork');
  }
  const W = manifest.bounds[2] * 4, H = manifest.bounds[3] * 4;
  const sourceImage = placement.source_image || 'base.png';
  assert(/^[A-Za-z0-9_.-]+\.png$/.test(sourceImage), 'Source image must be a local PNG filename');
  const source = root + '/source/' + sourceImage;
  const info = await sharp(source).metadata();
  assert.equal(info.width, placement.width); assert.equal(info.height, placement.height);
  const master = await sharp(source).ensureAlpha().resize(W, H, {fit: 'fill'}).raw().toBuffer();
  await fs.mkdir(staged, {recursive: true});
  const report = [];
  for(let i = -1; i < manifest.foreground_layers; i++) {
    const name = i < 0 ? 'background' : `foreground-${i}`;
    const mask = await sharp(`${original}/${name}.png`).resize(W, H, {kernel: 'nearest'}).extractChannel(3).raw().toBuffer();
    const data = Buffer.from(master);
    for(let p = 0; p < mask.length; p++) {
      data[p * 4 + 3] = mask[p];
      if(!mask[p]) data.fill(0, p * 4, p * 4 + 3);
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
