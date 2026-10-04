package com.interview.tree;

import com.google.gson.*;
import com.interview.tree.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import static org.junit.Assert.*;

/** One independent source example or operation sequence for every solution variant. */
@RunWith(Parameterized.class)
public class WorkbookExamplesTest {
    static final Gson GSON=new Gson();
    private final JsonObject test;
    public WorkbookExamplesTest(String name,JsonObject test) {this.test=test;}

    @Parameterized.Parameters(name="{0}")
    public static Collection<Object[]> examples() throws IOException {
        try(Reader reader=new InputStreamReader(Objects.requireNonNull(WorkbookExamplesTest.class.getResourceAsStream("/examples.json")),"UTF-8")) {
            List<Object[]> out=new ArrayList<>();
            for(JsonElement value:JsonParser.parseReader(reader).getAsJsonArray()) {
                JsonObject obj=value.getAsJsonObject();out.add(new Object[]{obj.get("id").getAsString(),obj});
            }
            assertEquals(240,out.size());return out;
        }
    }

    @Test(timeout=15000) public void sourceExample() throws Exception {
        Class<?> type=Class.forName("com.interview.tree.modules."+test.get("className").getAsString());
        if(test.has("operations")) {
            JsonArray inputs=test.getAsJsonArray("constructorArgs");Constructor<?> constructor=null;
            for(Constructor<?> c:type.getConstructors()) if(c.getParameterCount()==inputs.size()) {constructor=c;break;}
            assertNotNull(constructor);
            Object obj=constructor.newInstance(convert(inputs,constructor.getGenericParameterTypes()));
            for(JsonElement element:test.getAsJsonArray("operations")) {
                JsonObject operation=element.getAsJsonObject();
                Method method=method(type,operation.get("method").getAsString(),operation.getAsJsonArray("args").size());
                Object result=method.invoke(obj,convert(operation.getAsJsonArray("args"),method.getGenericParameterTypes()));
                if(method.getReturnType()!=void.class) assertEquals(operation.toString(),operation.get("expected"),GSON.toJsonTree(result));
            }
            return;
        }
        Object obj=type.getDeclaredConstructor().newInstance();
        Method method=method(type,test.get("method").getAsString(),test.getAsJsonArray("args").size());
        Object[] args=convert(test.getAsJsonArray("args"),method.getGenericParameterTypes());
        Object result=method.invoke(obj,args);
        String mode=test.get("mode").getAsString();JsonElement expected=test.get("expected");
        if(mode.equals("mutated")) result=args[0];
        if(mode.equals("roundTrip")) result=type.getMethod("deserialize",String.class).invoke(obj,result);
        if(mode.equals("lca")) { assertEquals(expected.getAsInt(),((TreeNode)result).val);assertSame(find((TreeNode)args[0],expected.getAsInt()),result);return; }
        if(mode.equals("balanced")) {
            assertEquals(expected,GSON.toJsonTree(inorder((TreeNode)result)));heightBalanced((TreeNode)result);return;
        }
        if(mode.equals("uniqueTrees")) {
            List<?> trees=(List<?>)result;assertEquals(expected.getAsInt(),trees.size());Set<String> forms=new HashSet<>();
            for(Object tree:trees) {assertEquals(Arrays.asList(1,2,3),inorder((TreeNode)tree));assertTrue(forms.add(treeValues((TreeNode)tree).toString()));}return;
        }
        if(mode.equals("cloneGraph")) {checkClone((Node)args[0],(Node)result);return;}
        if(mode.equals("topological")) {
            int n=(Integer)args[0];int[] order=(int[])result;assertEquals(n,order.length);
            int[] at=new int[n];boolean[] seen=new boolean[n];
            for(int i=0;i<n;i++) {assertTrue(order[i]>=0 && order[i]<n);assertFalse(seen[order[i]]);seen[order[i]]=true;at[order[i]]=i;}
            for(int[] edge:(int[][])args[1]) assertTrue(at[edge[1]]<at[edge[0]]);return;
        }
        if(result instanceof TreeNode) result=treeValues((TreeNode)result);
        JsonElement actual=GSON.toJsonTree(result);
        if(mode.equals("unordered")) assertEquals(canonical(expected,false),canonical(actual,false));
        else if(mode.equals("edges")) assertEquals(canonical(expected,true),canonical(actual,true));
        else if(mode.equals("innerSets")) {
            JsonArray a=actual.getAsJsonArray(),e=expected.getAsJsonArray();assertEquals(e.size(),a.size());
            for(int i=0;i<e.size();i++) assertEquals(canonical(e.get(i),false),canonical(a.get(i),false));
        } else assertEquals(expected,actual);
    }

    static Method method(Class<?> type,String name,int arity) {
        for(Method m:type.getMethods()) if(m.getName().equals(name)&&m.getParameterCount()==arity)return m;
        throw new AssertionError(type.getName()+"."+name);
    }
    static Object[] convert(JsonArray inputs,Type[] types) {
        Object[] out=new Object[types.length];
        for(int i=0;i<types.length;i++) {
            JsonElement input=inputs.get(i);
            if(types[i]==TreeNode.class) out[i]=input.isJsonArray()?tree(input.getAsJsonArray()):find((TreeNode)out[0],input.getAsInt());
            else if(types[i]==Node.class) out[i]=graph(input.getAsJsonArray());
            else out[i]=GSON.fromJson(input,types[i]);
        }
        return out;
    }
    static TreeNode tree(JsonArray values) {
        if(values.size()==0||values.get(0).isJsonNull())return null;
        TreeNode root=new TreeNode(values.get(0).getAsInt());Queue<TreeNode> queue=new ArrayDeque<>();queue.add(root);int i=1;
        while(!queue.isEmpty()&&i<values.size()) {
            TreeNode node=queue.remove();JsonElement left=values.get(i++);
            if(!left.isJsonNull()){node.left=new TreeNode(left.getAsInt());queue.add(node.left);}
            if(i<values.size()){JsonElement right=values.get(i++);if(!right.isJsonNull()){node.right=new TreeNode(right.getAsInt());queue.add(node.right);}}
        }
        return root;
    }
    static TreeNode tree(String json) {return tree(JsonParser.parseString(json).getAsJsonArray());}
    static TreeNode find(TreeNode node,int value) {
        if(node==null||node.val==value)return node;TreeNode left=find(node.left,value);return left!=null?left:find(node.right,value);
    }
    static List<Integer> treeValues(TreeNode root) {
        List<Integer> out=new ArrayList<>();Queue<TreeNode> queue=new LinkedList<>();queue.add(root);
        Set<TreeNode> seen=Collections.newSetFromMap(new IdentityHashMap<TreeNode,Boolean>());
        while(!queue.isEmpty()) {
            TreeNode node=queue.remove();
            if(node==null)out.add(null);
            else {assertTrue("Unexpected cycle/shared node in one tree",seen.add(node));out.add(node.val);queue.add(node.left);queue.add(node.right);}
        }
        while(!out.isEmpty()&&out.get(out.size()-1)==null)out.remove(out.size()-1);return out;
    }
    static List<Integer> inorder(TreeNode root) {
        List<Integer> out=new ArrayList<>();Deque<TreeNode> stack=new ArrayDeque<>();
        while(root!=null||!stack.isEmpty()){while(root!=null){stack.push(root);root=root.left;}root=stack.pop();out.add(root.val);root=root.right;}
        return out;
    }
    static int heightBalanced(TreeNode root) {
        if(root==null)return 0;int l=heightBalanced(root.left),r=heightBalanced(root.right);assertTrue(Math.abs(l-r)<=1);return 1+Math.max(l,r);
    }
    static List<String> canonical(JsonElement array,boolean inner) {
        List<String> out=new ArrayList<>();
        for(JsonElement item:array.getAsJsonArray())out.add(inner?canonical(item,false).toString():item.toString());
        Collections.sort(out);return out;
    }
    static Node graph(JsonArray adj) {
        if(adj.size()==0)return null;Node[] nodes=new Node[adj.size()];
        for(int i=0;i<nodes.length;i++)nodes[i]=new Node(i+1);
        for(int i=0;i<nodes.length;i++)for(JsonElement j:adj.get(i).getAsJsonArray())nodes[i].neighbors.add(nodes[j.getAsInt()-1]);
        return nodes[0];
    }
    static void checkClone(Node original,Node clone) {
        Map<Node,Node> copies=new IdentityHashMap<>();Queue<Node[]> queue=new ArrayDeque<>();queue.add(new Node[]{original,clone});
        while(!queue.isEmpty()) {
            Node[] pair=queue.remove();Node a=pair[0],b=pair[1];assertNotSame(a,b);assertEquals(a.val,b.val);
            if(copies.containsKey(a)){assertSame(copies.get(a),b);continue;}
            assertFalse(copies.containsValue(b));copies.put(a,b);assertEquals(a.neighbors.size(),b.neighbors.size());
            for(int i=0;i<a.neighbors.size();i++)queue.add(new Node[]{a.neighbors.get(i),b.neighbors.get(i)});
        }
        for(Node node:copies.values())assertFalse(copies.containsKey(node));
    }
}
