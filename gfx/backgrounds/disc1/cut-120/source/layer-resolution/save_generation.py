import json,pathlib,re,shutil,sys
c=int(sys.argv[1]);name=sys.argv[2];d=pathlib.Path(f'gfx/backgrounds/disc1/cut-{c}/source/layer-resolution')
p=json.load(open(d/(name+'-prompt.json')));target=d/(name+'.png');assert not target.exists()
raw=pathlib.Path(re.search(r' as (.+?\.png) by default',p['generatedOutput']).group(1));shutil.copy2(raw,target)
print(str(target))
