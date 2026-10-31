// Assemble original retail layers as an ImageGen geometry reference.
const sharp = require(process.argv[2]);
const fs = require('node:fs/promises');
const cut = Number(process.argv[3]);
const input = `build/disc1/original/cut-${cut}`;
(async () => {
  const manifest = JSON.parse(await fs.readFile(input + '/manifest.json'));
  // Cut 675 layers 0..3 are alternate lightning animation frames.
  const indices = cut === 675 ? [0, 4, 5, 6] : Array.from({length: manifest.foreground_layers}, (_, i) => i);
  await sharp(input + '/background.png').composite(indices.map(i => ({input: input + `/foreground-${i}.png`, left: 0, top: 0})))
    .png().toFile(input + '/geometry-guide.png');
})().catch(error => {console.error(error); process.exitCode = 1;});
