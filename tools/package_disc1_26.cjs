// Disappearing door overlays need complete recesses in the background.
const sharp = require(process.argv[2]);
const fs = require('node:fs/promises');
const assert = require('node:assert/strict');
const cut = process.argv[3] && process.argv[3] !== '--install' ? Number(process.argv[3]) : 26;
assert([24, 26, 27, 29, 41].includes(cut));
const doorLayers = cut === 41 ? [7, 8] : cut === 24 ? [6] : cut === 26 ? [5, 13] : cut === 27 ? [6, 13] : [5];
const root = `gfx/backgrounds/disc1/cut-${cut}`;
const original = `build/disc1/original/cut-${cut}`;
const staged = `build/disc1/staged/cut-${cut}`;
const W = 1536, H = 1024;
async function plate(name) {
  const file = `${root}/source/${name}.png`;
  const info = await sharp(file).metadata();
  assert.equal(info.width, W); assert.equal(info.height, H);
  return sharp(file).ensureAlpha().raw().toBuffer();
}
async function mask(name) {
  return sharp(`${original}/${name}.png`).resize(W, H, {kernel: 'nearest'}).extractChannel(3).raw().toBuffer();
}
(async () => {
  const trace = (await fs.readFile(`build/disc1/captures/cut-${cut}/original/alignment.csv`, 'utf8')).trim().split(/\r?\n/).slice(1);
  if(cut === 27) for(const variant of ['post-battle', 'reunion-return', 'escape', 'storage-door']) trace.push(...(await fs.readFile(`build/disc1/captures/cut-27-${variant}/original/alignment.csv`, 'utf8')).trim().split(/\r?\n/).slice(1));
  assert(trace.length > 0);
  if(cut === 24) for(const variant of ['post-battle', 'side-cell']) trace.push(...(await fs.readFile(`build/disc1/captures/cut-24-${variant}/original/alignment.csv`, 'utf8')).trim().split(/\r?\n/).slice(1));
  for(const row of trace) {
    const m = row.match(/^\d+,\d+,"[^"]*","[^"]*",(\d+),(-?\d+),(-?\d+),(-?\d+),(-?\d+),(true|false),/);
    assert(Number(row.split(',')[1]) === cut);
    assert(m && m.slice(2, 6).every(v => v === '0') && (m[6] === 'false' || doorLayers.includes(Number(m[1]))), 'Unexpected moving layer');
  }
  const closed = await plate('base'), opened = await plate('open-doors');
  const doors = await Promise.all(doorLayers.map(layer => mask(`foreground-${layer}`)));
  // Match the floor RGB at the prop-mask boundary without altering its alpha.
  const propEdge = cut === 41 ? await sharp(Buffer.from(doors[0].map((v, p) => Math.max(v, doors[1][p]))), {raw:{width:W,height:H,channels:1}}).blur(8).greyscale().raw().toBuffer() : null;
  if(propEdge) assert.equal(propEdge.length, W * H);
  await fs.mkdir(staged, {recursive:true});
  const report = [];
  for(let i = -1; i < (cut === 41 ? 9 : cut === 24 ? 15 : 16); i++) {
    const name = i < 0 ? 'background' : `foreground-${i}`;
    // Shed props are replaced by animated models; use a continuous floor plate
    // for the whole room so the old prop silhouette cannot remain at mask edges.
    const alpha = await mask(name), data = Buffer.from(cut === 41 && !doorLayers.includes(i) ? opened : closed);
    for(let p = 0; p < alpha.length; p++) {
      if(propEdge && doorLayers.includes(i)) {
        const weight = Math.max(0, Math.min(1, (propEdge[p] - 128) / 64));
        for(let c = 0; c < 3; c++) data[p*4+c] = Math.round(closed[p*4+c] * weight + opened[p*4+c] * (1-weight));
      }
      if(cut !== 41 && i === -1 && doors.some(door => door[p])) opened.copy(data, p*4, p*4, p*4+3);
      data[p*4+3] = alpha[p];
      if(!alpha[p]) data.fill(0, p*4, p*4+3);
    }
    const file = `${staged}/${name}.png`;
    await sharp(data, {raw:{width:W,height:H,channels:4}}).png().toFile(file);
    assert(alpha.equals(await sharp(file).extractChannel(3).raw().toBuffer()));
    report.push({name,width:W,height:H,exactOriginalMask:true});
  }
  await fs.writeFile(staged+'/validation.json', JSON.stringify(report,null,2));
  if(process.argv.includes('--install')) for(const {name} of report) await fs.copyFile(`${staged}/${name}.png`, `${root}/${name}.png`);
  console.log(JSON.stringify(report));
})().catch(error => {console.error(error);process.exitCode=1;});
