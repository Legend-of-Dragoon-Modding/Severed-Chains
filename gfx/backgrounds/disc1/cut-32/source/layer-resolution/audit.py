import json, re, csv
from pathlib import Path
from PIL import Image, ImageDraw, ImageChops

cuts=[32,30,172,175,176,177,180,181,192,193,194,195,197,198,658,659,660,57,171]
states={s['cut']:s for s in json.load(open('docs/disc1-backgrounds/resolution/original-states.json'))['screens']}
for cut in [57,171]:
    by_layer={}
    trace=Path(f'build/disc1/captures/cut-{cut}/original/alignment.csv')
    for row in csv.DictReader(trace.open()):
        ix=int(row['layer']);state={k:int(row[k]) for k in ['startX','startY','destX','destY']};state['hidden']=row['hidden']=='true'
        if state not in by_layer.setdefault(ix,[]):by_layer[ix].append(state)
    states[cut]=dict(cut=cut,trace=str(trace),layers=[dict(layer=i,states=s) for i,s in by_layer.items()])
result=[]
for cut in cuts:
    original=Path(f'build/disc1/original/cut-{cut}')
    root=Path(f'gfx/backgrounds/disc1/cut-{cut}/source/layer-resolution')
    root.mkdir(parents=True,exist_ok=True)
    m=json.load(open(original/'manifest.json'))
    count=m['foreground_layers']
    script=Path('build/disc1/scripts')/str(int(Path(m['source']).name)+2)
    evidence=[]; dynamic=set()
    for file in script.glob('*.txt'):
        for n,line in enumerate(file.read_text().splitlines(),1):
            if 'call SMap::scriptSetEnvForegroundPosition,' in line or ('call SMap::scriptSetModeParamForNextCallToScriptSetCameraOffsetOrHideSubmapForegroundObject, 0x3,' in line):
                args=line.split(',',1)[1].split(';')[0].strip().split(', ')
                ix=args[2] if 'scriptSetEnvForegroundPosition' in line else args[1]
                if ix.startswith('0x') and int(ix,16)<count:dynamic.add(int(ix,16))
                evidence.append(dict(file=str(file),line=n,call=line,index=ix,inRange=int(ix,16)<count if ix.startswith('0x') else None))
    for layer in states[cut]['layers']:
        if any(s['hidden'] or any(s[k] for k in ['startX','startY','destX','destY']) for s in layer['states']):dynamic.add(layer['layer'])
    # The generic five-word door tables use their fourth word as overlay index.
    generic=[]
    for file in script.glob('*.txt'):
        txt=file.read_text()
        for match in re.finditer(r'mov inl\[:(LABEL_\d+)\[stor\[25\]\]\], stor\[26\] ; source, dest\ncall SMap::scriptSetEnvForegroundPosition, 0x400',txt):
            label=match.group(1);tail=txt.split(label+':',1)[1];v=[]
            for line in tail.splitlines()[1:]:
                if not line.startswith('data 0x'):break
                v.append(int(line[5:],16))
            ids=v[3::5]
            generic.append(dict(file=str(file),table=label,indices=ids))
            dynamic.update(i for i in ids if i<count)
    base=Image.open(original/'background.png').convert('RGBA')
    sheet=Image.new('RGB',(1000,((count+3)//4)*180),'#222222');draw=ImageDraw.Draw(sheet)
    layers=[]
    for i in range(count):
        im=Image.open(original/f'foreground-{i}.png').convert('RGBA');a=im.getchannel('A');bbox=a.getbbox();overlap=ImageChops.multiply(a,base.getchannel('A'));ov=sum(1 for x in overlap.getdata() if x)
        layers.append(dict(layer=i,bbox=bbox,opaquePixels=sum(1 for x in a.getdata() if x),backgroundOverlapPixels=ov,dynamic=i in dynamic))
        if bbox:im=im.crop(bbox);im.thumbnail((245,155))
        else:im=Image.new('RGBA',(1,1))
        x=i%4*250;y=i//4*180;sheet.paste(im,(x,y+22),im);draw.text((x+3,y+3),f'{i}'+(' DYNAMIC' if i in dynamic else '')+f' overlap {ov}',fill='yellow' if i in dynamic else 'white')
    sheet.save(root/'original-layer-audit.png')
    item=dict(cut=cut,dynamicLayers=sorted(dynamic),layers=layers,scriptEvidence=evidence,genericDoorTables=generic,observed=states[cut])
    (root/'audit.json').write_text(json.dumps(item,indent=2)+'\n')
    result.append(item)
print(json.dumps([dict(cut=x['cut'],dynamicLayers=x['dynamicLayers'],dynamicInfo=[l for l in x['layers'] if l['dynamic']]) for x in result]))
