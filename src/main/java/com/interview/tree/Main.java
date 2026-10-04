package com.interview.tree;

import com.interview.tree.model.TreeNode;
import com.interview.tree.modules.*;

/** Run this demo or use the question classes directly. */
public class Main {
    public static void main(String[] args) {
        TreeNode root=new TreeNode(2,new TreeNode(1),new TreeNode(3));
        System.out.println("Tree workbook: 6 topics, 12 modules, 120 questions, 240 solution variants");
        System.out.println("Depth: "+new TreesBasic.Q01Optimized().maxDepth(root)+" / "+new TreesBasic.Q01Recursive().maxDepth(root));
        System.out.println("BST search: "+new BinarySearchTreeBasic.Q01Optimized().searchBST(root,3).val);
        TrieBasic.Q01Optimized trie=new TrieBasic.Q01Optimized();
        trie.insert("tree");System.out.println("Trie search: "+trie.search("tree"));
        System.out.println("Components: "+new GraphsBasic.Q11Optimized().countComponents(5,new int[][]{{0,1},{1,2},{3,4}}));
        SegmentTreeFenwickBasic.Q01Optimized sums=new SegmentTreeFenwickBasic.Q01Optimized(new int[]{1,3,5});
        sums.update(1,2);System.out.println("Mutable range sum: "+sums.sumRange(0,2));
        System.out.println("Network delay: "+new AdvancedGraphsBasic.Q01Optimized().networkDelayTime(new int[][]{{2,1,1},{2,3,1},{3,4,1}},4,2));
    }
}
