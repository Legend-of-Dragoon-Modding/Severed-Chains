import pathlib,json,subprocess,shutil
p=pathlib.Path('docs/disc1-backgrounds/resolution/middle.json');r=json.loads(p.read_text())
statics=[109,121,134,141,146,149,150,151,637,647,155,159,166]
for c in statics:
 row=next(x for x in r['screens'] if x['cut']==c);assert row['originalInspection']['allObservedLayersFixedVisible']
 d=pathlib.Path(f'gfx/backgrounds/disc1/cut-{c}/source');placement=json.load(open(d/'placement.json'));backup=d/'placement-before-step4.json'
 if not backup.exists():shutil.copy2(d/'placement.json',backup)
 placement['stationary_layers_verified']=True;placement['evidence']=f'Actual script directory environment+2 ({row["actualScriptDirectory"]}) inspected: no changing nonempty overlay reached; generic door helpers terminate at 0xff where present. Parent original-only121frame exactGPU/export fixed-visible trace. Traversal gameplay deferred.'
 (d/'placement.json').write_text(json.dumps(placement,indent=2)+'\n')
 run=subprocess.run(['node','tools/package_disc1_static.cjs','C:/Users/20te/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp',str(c)],capture_output=True,text=True)
 (d/'step4-package.log').write_text(run.stdout+run.stderr)
 assert run.returncode==0,(c,run.stderr)
 row.update(ready=True,status='strict-static-staged',staged=f'build/disc1/staged/cut-{c}',evidence=[placement['evidence']],previousScriptAttributionInvalid=True)
 p.write_text(json.dumps(r,indent=2));print(f'CORRECTED STAGED {c}',flush=True)
