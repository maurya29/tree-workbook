"""Prepare one example/operation sequence for each of the 240 variants."""
import json,re
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]

def op(name,args,expected=None): return dict(method=name,args=args,expected=expected)
stateful={
 ('bst',20): ([[7,3,15,None,None,9,20]],[op('next',[],3),op('next',[],7),op('hasNext',[],True),op('next',[],9),op('next',[],15),op('next',[],20),op('hasNext',[],False)]),
 ('tries',1): ([],[op('insert',['apple']),op('search',['apple'],True),op('search',['app'],False),op('startsWith',['app'],True),op('insert',['app']),op('search',['app'],True)]),
 ('tries',2): ([],[op('addWord',['bad']),op('addWord',['dad']),op('search',['pad'],False),op('search',['.ad'],True),op('search',['b..'],True)]),
 ('tries',5): ([],[op('insert',['apple',3]),op('sum',['ap'],3),op('insert',['app',2]),op('sum',['ap'],5),op('insert',['apple',4]),op('sum',['ap'],6)]),
 ('tries',7): ([['cd','f','kl']],[op('query',[c],v) for c,v in zip('abcdefghijkl',[False,False,False,True,False,True,False,False,False,False,False,True])]),
 ('tries',14): ([['apple']],[op('f',['a','e'],0),op('f',['b','e'],-1)]),
 ('tries',15): ([],[op('buildDict',[['hello','leetcode']]),op('search',['hello'],False),op('search',['hhllo'],True),op('search',['hell'],False)]),
 ('tries',20): ([['i love you','island','iroman','i love leetcode'],[5,3,2,2]], [op('input',['i'],['i love you','island','i love leetcode']),op('input',[' '],['i love you','i love leetcode']),op('input',['a'],[]),op('input',['#'],[]),op('input',['i'],['i love you','island','i love leetcode'])]),
 ('segment-tree',1): ([[1,3,5]],[op('sumRange',[0,2],9),op('update',[1,2]),op('sumRange',[0,2],8)]),
 ('segment-tree',2): ([[[3,0,1,4,2],[5,6,3,2,1],[1,2,0,1,5],[4,1,0,1,7],[1,0,3,0,5]]],[op('sumRegion',[2,1,4,3],8),op('update',[3,2,2]),op('sumRegion',[2,1,4,3],10)]),
 ('segment-tree',7): ([],[op('addRange',[10,20]),op('removeRange',[14,16]),op('queryRange',[10,14],True),op('queryRange',[13,15],False),op('queryRange',[16,17],True)]),
 ('segment-tree',8): ([],[op('book',[a,b],r) for a,b,r in [(10,20,1),(50,60,1),(10,40,2),(5,15,3),(5,10,3),(25,55,3)]]),
 ('segment-tree',13): ([],[op('add',[2,3]),op('add',[7,10]),op('count',[],6),op('add',[5,8]),op('count',[],8)]),
 ('segment-tree',15): ([2,5],[op('gather',[4,0],[0,0]),op('gather',[2,0],[]),op('scatter',[5,1],True),op('scatter',[5,1],False)]),
 ('segment-tree',16): ([[12,33,4,56,22,2,34,33,22,12,34,56]],[op('query',[1,2,4],1),op('query',[0,11,33],2)]),
 ('segment-tree',17): ([[5,2,7,3]],[op('rangeMin',[1,3],2),op('update',[1,8]),op('rangeMin',[1,3],3)]),
 ('segment-tree',18): ([[5,2,7,3]],[op('rangeMax',[1,3],7),op('update',[2,1]),op('rangeMax',[1,3],3)]),
 ('segment-tree',19): ([[1,2,3,4]],[op('addRange',[1,3,2]),op('sumRange',[0,3],16),op('addRange',[0,1,-1]),op('sumRange',[0,3],14)]),
 ('segment-tree',20): ([[1,2,3,4]],[op('assignRange',[1,2,5]),op('sumRange',[0,3],15),op('assignRange',[0,3,0]),op('sumRange',[0,3],0)]),
}

def main():
 cases=[]
 for p in json.loads((ROOT/'workbook-index.json').read_text()):
  key=(p['topic'],p['number']);ex=p['examples'][0]
  for variant,v in p['variants'].items():
   case=dict(id=p['topic']+'-'+str(p['number'])+'-'+variant,className=p['className']+'$'+v['className'])
   if key in stateful:
    case['constructorArgs'],case['operations']=stateful[key];cases.append(case);continue
   text=ex['input'];fields={}
   if key==('advanced-graphs',8): fields={'tickets':[['JFK','SFO'],['JFK','ATL'],['SFO','ATL'],['ATL','JFK'],['ATL','SFO']]}
   elif key==('advanced-graphs',9): fields={'words':['wrt','wrf','er','ett','rftt']}
   elif key==('segment-tree',14): fields={'nums':[3,1,4,2,5],'queries':[[1,0,4]]}
   else:
    while text.strip():
     m=re.match(r'\s*,?\s*(\w+)\s*=\s*',text);assert m,(key,text)
     val,end=json.JSONDecoder().raw_decode(text[m.end():]);fields[m.group(1)]=val;text=text[m.end()+end:]
   aliases={'passingFees':'fees','distanceThreshold':'threshold','node':'adjList'}
   method=v['methods'][0];names=[x.strip().rsplit(' ',1)[-1] for x in method['parameters'].split(',')]
   args=[fields[name] if name in fields else fields[aliases[name]] for name in names]
   mode='equal';expected=ex['output']
   if key==('trees',18):mode='roundTrip';expected=json.dumps(fields['root'])
   elif key in [('trees',9),('bst',6)]:mode='lca'
   elif key in [('bst',7),('bst',13)]:mode='balanced';expected=json.dumps(fields.get('nums',[1,2,3,4]))
   elif key==('bst',17):mode='uniqueTrees';expected='5'
   elif key==('graphs',2):mode='cloneGraph'
   elif key in [('graphs',9),('advanced-graphs',10)]:mode='topological'
   elif key==('advanced-graphs',5):mode='innerSets'
   elif key==('advanced-graphs',14):mode='edges'
   elif key in [('trees',12),('bst',19),('tries',3),('tries',10),('tries',11),('tries',12),('graphs',4)]:mode='unordered'
   elif method['returnType']=='void':mode='mutated'
   if key==('advanced-graphs',8):expected=json.dumps(['JFK','ATL','JFK','SFO','ATL','SFO'])
   if key==('advanced-graphs',9):expected=json.dumps('wertf')
   case.update(method=method['name'],args=args,mode=mode,expected=json.loads(expected))
   cases.append(case)
 assert len(cases)==240
 out=ROOT/'src/test/resources';out.mkdir(parents=True,exist_ok=True)
 (out/'examples.json').write_text(json.dumps(cases,indent=2),encoding='utf-8')
 print('Prepared 240 variant examples, including stateful operation sequences.')

if __name__=='__main__':main()
