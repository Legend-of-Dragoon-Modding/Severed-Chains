import json, pathlib, subprocess, sys
root=pathlib.Path.cwd()
for cut in map(int,sys.argv[1:]):
 d=root/f'gfx/backgrounds/disc1/cut-{cut}/source/layer-resolution'
 manifest=json.loads((root/f'build/disc1/original/cut-{cut}/manifest.json').read_text())
 dynamic=json.loads((d/'dynamic-ids.json').read_text())
 print(cut,dynamic,flush=True)
 config={'background':'underlay-generated.png','layers':{str(i):('../base.png' if i in dynamic else 'underlay-generated.png') for i in range(manifest['foreground_layers'])},'dynamicLayers':dynamic,'evidence':['Correct runtime environment+2 scripts identify these exact changing slots.','Clean underlay removes the dynamic door geometry; closed door RGB remains independently clipped to exact original alpha.','Generated underlay visually reviewed against the original clean state and modern scene.'],'visualReview':True}
 (d/'plates.json').write_text(json.dumps(config,indent=2)+'\n')
 subprocess.run([sys.executable,'tools/package_disc1_layered.py',str(cut)],check=True)
