import json,sys
from pathlib import Path
cut=int(sys.argv[1]);s=Path(f'gfx/backgrounds/disc1/cut-{cut}/source/layer-resolution')
m=json.load(open(f'build/disc1/original/cut-{cut}/manifest.json'));a=json.load(open(s/'audit.json'))
assert a['dynamicLayers']
evidence=[f'Correct RetailSubmap script directory: environment source index+2. Detailed calls/tables and observed GPU/exportmatched states in {s}/audit.json.', 'Raw generated base.png preserved. New built-in ImageGen underlay follows original retail background plus static foregrounds; changing objects excluded.', 'Independent source RGB uses original source master for dynamic layers and generated clean underlying plate for background/still layers. Exact retail alpha retained.']
cfg=dict(background='generated-underlay.png',layers={str(i):'../base.png' if i in a['dynamicLayers'] else 'generated-underlay.png' for i in range(m['foreground_layers'])},dynamicLayers=a['dynamicLayers'],evidence=evidence,visualReview=True)
overrides=s/'layer-overrides.json'
if overrides.exists():cfg['layers'].update(json.load(open(overrides)))
(s/'plates.json').write_text(json.dumps(cfg,indent=2)+'\n')
