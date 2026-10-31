import json, pathlib, subprocess, sys, shutil
from PIL import Image
root=pathlib.Path('.')
cuts=[109,111,120,121,134,141,142,143,144,145,146,147,148,149,150,151,637,636,647,155,158,159,165,166]
states={x['cut']:x for x in json.load(open('docs/disc1-backgrounds/resolution/original-states.json'))['screens']}
plan={x['cut']:x for x in json.load(open('docs/disc1-backgrounds/production-plan.json'))['screens']}
out=pathlib.Path('docs/disc1-backgrounds/resolution/middle.json')
report={'worker':'middle','screens':[],'gameplayVerified':False}
false={141:'LABEL_165',142:'LABEL_192',147:'LABEL_127',150:'LABEL_277',151:'LABEL_113',647:'LABEL_155'}
for c in cuts:
 row={'cut':c,'status':'independent-layer-inspection-pending','gameplayVerified':False,'originalInspection':states[c],'scriptRisks':plan[c]['layerRisks'],'ready':False}
 if c in false:row['evidence']=[f'Generic door helper lookup table {false[c]} contains only data 0xff, so no door overlay index reaches its hide call.']
 if c==111:row.update(status='parent-resolves-gpu-placement',evidence=['Parent confirmed original GPU/export exact and all121frames fixed/visible; do not edit.'])
 report['screens'].append(row)
out.write_text(json.dumps(report,indent=2))
for c in [109,141,142,150,151,647]:
 row=next(x for x in report['screens'] if x['cut']==c)
 assert states[c]['gpuExportMatches'] and states[c]['allObservedLayersFixedVisible']
 d=pathlib.Path(f'gfx/backgrounds/disc1/cut-{c}/source');p=d/'placement.json';backup=d/'placement-before-step4.json'
 if not backup.exists():shutil.copy2(p,backup)
 placement=json.load(open(p));placement['stationary_layers_verified']=True;placement['evidence']='Original-only121frame trace exactGPU/export fixed visible; generic door table terminates before any hidecall.'
 if c==109:
  with Image.open(d/'geometry-candidate-step4.png') as im:placement.update(width=im.width,height=im.height,source_image='geometry-candidate-step4.png')
  placement['evidence']='Original-only121frame trace exactGPU/export fixed visible; corrected geometry candidate independently compared with original; gameplay footing deferred.'
 p.write_text(json.dumps(placement,indent=2)+'\n')
 run=subprocess.run(['node','tools/package_disc1_static.cjs','C:/Users/20te/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp',str(c)],capture_output=True,text=True)
 (d/'step4-package.log').write_text(run.stdout+run.stderr)
 if run.returncode:row.update(status='strict-static-package-failed',error=run.stderr);out.write_text(json.dumps(report,indent=2));raise RuntimeError(f'Failed cut{c}')
 if c==109:
  sys.path.insert(0,'tools');from preview_disc1_artwork import preview;preview(c)
 row.update(status='strict-static-staged',ready=True,staged=f'build/disc1/staged/cut-{c}',evidence=row.get('evidence',[])+[placement['evidence'],'Strict existing CJS trace/mask/dimension gates passed; not installed.'])
 out.write_text(json.dumps(report,indent=2));print(f'STAGED {c}',flush=True)
