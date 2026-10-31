// Cut 92 keeps floor patch 1 hidden; all other layers are fixed and visible.
const sharp = require(process.argv[2]);
const fs = require('node:fs/promises');
const assert = require('node:assert/strict');
const root = 'gfx/backgrounds/disc1/cut-92';
const original = 'build/disc1/original/cut-92';
const staged = 'build/disc1/staged/cut-92';
(async () => {
  const manifest = JSON.parse(await fs.readFile(original + '/manifest.json'));
  assert.equal(manifest.foreground_layers, 19);
  const placement = JSON.parse(await fs.readFile(root + '/source/placement.json'));
  assert.equal(placement.mapping, 'full-canvas');
  const rows = (await fs.readFile('build/disc1/captures/cut-92/original/alignment.csv', 'utf8')).trim().split(/\r?\n/).slice(1);
  const completion = await fs.readFile('build/disc1/captures/cut-92/original/completion.txt', 'utf8');
  assert(completion.startsWith('Exited cut 92 to '));
  assert(rows.length > 0 && rows.length % 19 === 0);
  for(const row of rows) {
    const m = row.match(/^\d+,92,"[^"]*","[^"]*",(\d+),(-?\d+),(-?\d+),(-?\d+),(-?\d+),(true|false),/);
    assert(m && m.slice(2, 6).every(v => v === '0'), 'Moving layers need separate plates');
    assert.equal(m[6], Number(m[1]) === 1 ? 'true' : 'false', 'Unexpected visibility; inspect the new variant');
  }
  const W = manifest.bounds[2] * 4, H = manifest.bounds[3] * 4;
  assert.equal(W, 2560); assert.equal(H, 1536);
  const source = root + '/source/base.png';
  const info = await sharp(source).metadata();
  assert.equal(info.width, placement.width); assert.equal(info.height, placement.height);
  // One continuous floor supplies the hidden patch and the exposed background.
  const master = await sharp(source).ensureAlpha().resize(W, H, {fit:'fill'}).raw().toBuffer();
  await fs.mkdir(staged, {recursive:true});
  const report = [];
  for(let i = -1; i < 19; i++) {
    const name = i < 0 ? 'background' : `foreground-${i}`;
    const mask = await sharp(`${original}/${name}.png`).resize(W, H, {kernel:'nearest'}).extractChannel(3).raw().toBuffer();
    const data = Buffer.from(master);
    for(let p = 0; p < mask.length; p++) {
      data[p*4+3] = mask[p];
      if(!mask[p]) data.fill(0, p*4, p*4+3);
    }
    const file = `${staged}/${name}.png`;
    await sharp(data, {raw:{width:W,height:H,channels:4}}).png().toFile(file);
    assert(mask.equals(await sharp(file).extractChannel(3).raw().toBuffer()));
    report.push({name,width:W,height:H,exactOriginalMask:true});
  }
  await fs.writeFile(staged + '/validation.json', JSON.stringify(report,null,2));
  if(process.argv.includes('--install')) for(const {name} of report) await fs.copyFile(`${staged}/${name}.png`, `${root}/${name}.png`);
  console.log(JSON.stringify(report));
})().catch(error => {console.error(error);process.exitCode=1;});
