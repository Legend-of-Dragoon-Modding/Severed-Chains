// Assemble the existing original foreground layers as a geometry guide for ImageGen.
const sharp = require(process.argv[2]);
const fs = require('node:fs/promises');
(async () => {
  await fs.mkdir('build/seles/modern-source', {recursive: true});
  await sharp({create: {width: 512, height: 356, channels: 4, background: '#00000000'}})
    .composite([2, 3, 0, 1].map(i => ({input: `build/seles/original/foreground-${i}.png`, left: 0, top: 0})))
    .png().toFile('build/seles/modern-source/foreground-layout-guide.png');
})().catch(e => {console.error(e); process.exitCode = 1;});
