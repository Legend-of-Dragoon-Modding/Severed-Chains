// Compare matching actual-game recordings, not artwork previews.
const fs = require('node:fs');
const assert = require('node:assert/strict');
const root = 'build/seles/captures/';
fs.rmSync(root + 'verification.txt', {force: true});
const original = fs.readFileSync(root + 'original/alignment.csv', 'utf8');
const modern = fs.readFileSync(root + 'modern/alignment.csv', 'utf8');
assert(modern === original, 'Camera, player, or foreground alignment changed; inspect alignment.csv');
const completion = fs.readFileSync(root + 'original/completion.txt', 'utf8');
assert.match(completion, /^Exited cut 9 to 10 after \d+ frames$/);
assert.equal(fs.readFileSync(root + 'modern/completion.txt', 'utf8'), completion);
const report = `${original.trim().split('\n').length - 1} matching layer samples; ${completion}\n`;
fs.writeFileSync(root + 'verification.txt', report);
console.log(report);
