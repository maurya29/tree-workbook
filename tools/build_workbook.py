"""Build the six selected topic modules and question-only README files."""
import json
import re
from pathlib import Path

ROOT=Path(__file__).resolve().parents[1]
MODULES=[(12,'trees','Trees'),(13,'bst','BinarySearchTree'),(16,'tries','Trie'),
         (14,'graphs','Graphs'),(27,'segment-tree','SegmentTreeFenwick'),(28,'advanced-graphs','AdvancedGraphs')]

def adapt(code,topic,n,variant):
    if (topic,n,variant)==('bst',10,'optimized'):
        code=code.replace('stack.push(root);','if (root != null) stack.push(root);')
        code=code.replace('if (node.val > low)', 'if (node.val > low && node.left != null)')
        code=code.replace('if (node.val < high)', 'if (node.val < high && node.right != null)')
    if variant=='recursive':
        resets={('trees',6):('int diameterOfBinaryTree(TreeNode root)','best = 0;'),
                ('trees',10):('int maxPathSum(TreeNode root)','best = Integer.MIN_VALUE;'),
                ('trees',17):('TreeNode buildTree(int[] preorder, int[] inorder)','preIndex = 0; inorderIndex.clear();'),
                ('bst',9):('int getMinimumDifference(TreeNode root)','previous = null; best = Integer.MAX_VALUE;'),
                ('bst',12):('void recoverTree(TreeNode root)','first = second = previous = null;'),
                ('bst',15):('TreeNode bstFromPreorder(int[] preorder)','index = 0;')}
        if (topic,n) in resets:
            signature,reset=resets[(topic,n)]
            code=code.replace('public '+signature+' {','public '+signature+' {\n    '+reset)
        if (topic,n)==('trees',19):
            code=code.replace('flatten(', 'flattenDfs(').replace('public void flattenDfs(TreeNode root) {',
                'public void flatten(TreeNode root) {\n    previous = null;\n    flattenDfs(root);\n  }\n\n  private void flattenDfs(TreeNode root) {')
    return code

def extract(p,topic,n,variant):
    code=adapt(p[variant+'Code'],topic,n,variant)
    classes=[m for m in re.finditer(r'^(?:public )?class (\w+)\s*\{',code,re.M) if m.group(1) not in ('TreeNode','Node')]
    assert len(classes)==1,(topic,n,variant)
    match=classes[0];original=match.group(1)
    code=code[match.start():].strip()
    name='Q'+str(n).zfill(2)+variant.capitalize()
    code=re.sub(r'\b'+original+r'\b',name,code)
    code=re.sub(r'^(?:public )?class '+name,r'public static class '+name,code, count=1)
    methods=[]
    for m in re.finditer(r'^  public\s+(?:static\s+)?([\w<>\[\], ?]+)\s+(\w+)\(([^)]*)\)',code,re.M):
        methods.append(dict(returnType=m.group(1).strip(),name=m.group(2),parameters=m.group(3).strip()))
    assert methods,(topic,n,variant)
    return name,code,methods,original

def comment(p,n,url):
    lines=[f'Question {n}: {p["name"]}','','Question: '+p['question'],'','Constraints: '+p['constraints'],
        '', 'Optimized complexity: '+p['optimizedComplexity'],'Recursive complexity: '+p['recursiveComplexity']]
    for i,e in enumerate(p['examples'],1):
        lines+=['',f'Example {i}:','Input: '+e['input'],'Output: '+e['output'],'Explanation: '+e.get('explanation','')]
    lines+=['','Source: '+url]
    return '  /*\n'+'\n'.join('   * '+line.replace('*/','* /') for line in lines)+'\n   */\n'

def main():
    docs={tier:['# Tree Workbook - '+tier+' Questions and Solutions',''] for tier in ['Basic','Moderate']}
    index=[]
    for topic_number,topic,base in MODULES:
        data=json.loads((ROOT/'workbook-data'/(topic+'.json')).read_text(encoding='utf-8'))
        assert len(data['problems'])==20
        for lines in docs.values():lines+=['## Topic '+str(topic_number)+': '+data['name'],'']
        for tier,lo,hi in [('Basic',1,12),('Moderate',13,20)]:
            class_name=base+tier;sections=[]
            docs[tier]+=['### '+tier+f' ({lo}-{hi})','']
            for n in range(lo,hi+1):
                p=data['problems'][n-1]
                entry=dict(topic=topic,topicNumber=topic_number,number=n,title=p['name'],className=class_name,variants={},examples=p['examples'])
                sections.append(comment(p,n,data['sourceUrl']))
                for variant in ['optimized','recursive']:
                    name,code,methods,original=extract(p,topic,n,variant)
                    sections.append('\n'.join('  '+line for line in code.splitlines()))
                    entry['variants'][variant]=dict(className=name,methods=methods,originalClass=original)
                    source=p.get('source',{});label=source.get('label','')
                    lc=re.search(r'LeetCode\s*#?\s*(\d+)',label,re.I)
                    link=' - ['+('LeetCode '+lc.group(1) if lc else label)+']('+source['url']+')' if source.get('url') else ''
                    if variant=='optimized':
                        docs[tier]+=['#### '+str(n)+'. '+p['name']+link,'',p['question'],'']
                    docs[tier]+=['##### '+('Optimized / Iterative Solution' if variant=='optimized' else 'Recursive Solution'),'','```java',code,'```','']
                index.append(entry)
            folder=ROOT/'src/main/java/com/interview/tree/modules';folder.mkdir(parents=True,exist_ok=True)
            (folder/(class_name+'.java')).write_text('package com.interview.tree.modules;\n\nimport java.util.*;\nimport com.interview.tree.model.*;\n\n'
                +f'/** Topic {topic_number}: {data["name"]}. {tier} questions {lo}-{hi}. */\npublic class {class_name} {{\n\n'
                +'\n\n'.join(sections)+'\n}\n',encoding='utf-8')
    assert len(index)==120
    (ROOT/'workbook-index.json').write_text(json.dumps(index,indent=2),encoding='utf-8')
    for tier,lines in docs.items():
        filename='README-'+tier.upper()+'.md'
        (ROOT/filename).write_text('\n'.join(lines),encoding='utf-8')
    print('Generated 12 module classes, 120 questions, 240 solution variants, and two README files.')

if __name__=='__main__': main()
