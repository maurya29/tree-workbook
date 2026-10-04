package com.interview.tree;

import com.interview.tree.model.TreeNode;
import com.interview.tree.modules.*;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class RegressionTest {
    @Test public void bstRangeHandlesMissingChildren() {
        BinarySearchTreeBasic.Q10Optimized q = new BinarySearchTreeBasic.Q10Optimized();
        assertEquals(0, q.rangeSumBST(null, 0, 9));
        assertEquals(4, q.rangeSumBST(new TreeNode(4), 0, 9));
    }

    @Test public void diameterCanBeReused() {
        TreesBasic.Q06Recursive q = new TreesBasic.Q06Recursive();
        assertEquals(2, q.diameterOfBinaryTree(new TreeNode(2, new TreeNode(1), new TreeNode(3))));
        assertEquals(0, q.diameterOfBinaryTree(new TreeNode(1)));
    }

    @Test public void maximumPathCanBeReused() {
        TreesBasic.Q10Recursive q = new TreesBasic.Q10Recursive();
        assertEquals(6, q.maxPathSum(new TreeNode(2, new TreeNode(1), new TreeNode(3))));
        assertEquals(-5, q.maxPathSum(new TreeNode(-5)));
    }

    @Test public void minimumDifferenceCanBeReused() {
        BinarySearchTreeBasic.Q09Recursive q = new BinarySearchTreeBasic.Q09Recursive();
        assertEquals(1, q.getMinimumDifference(new TreeNode(2, new TreeNode(1), null)));
        assertEquals(10, q.getMinimumDifference(new TreeNode(20, new TreeNode(10), null)));
    }

    @Test public void flattenCanBeReused() {
        TreesModerate.Q19Recursive q = new TreesModerate.Q19Recursive();
        TreeNode first = new TreeNode(2, new TreeNode(1), new TreeNode(3));
        q.flatten(first);
        assertNull(first.left);
        assertEquals(1, first.right.val);
        assertEquals(3, first.right.right.val);
        TreeNode second = new TreeNode(7);
        q.flatten(second);
        assertNull(second.right);
    }

    @Test public void lazyAdditionMatchesReference() {
        SegmentTreeFenwickModerate.Q19Optimized a = new SegmentTreeFenwickModerate.Q19Optimized(new int[17]);
        SegmentTreeFenwickModerate.Q19Recursive b = new SegmentTreeFenwickModerate.Q19Recursive(new int[17]);
        long[] reference = new long[17];
        Random random = new Random(2026);
        for (int step = 0; step < 500; step++) {
            int l = random.nextInt(17), r = random.nextInt(17);
            if (l > r) { int t = l; l = r; r = t; }
            long delta = random.nextInt(101) - 50;
            a.addRange(l, r, delta); b.addRange(l, r, delta);
            for (int i = l; i <= r; i++) reference[i] += delta;
            int x = random.nextInt(17), y = random.nextInt(17);
            if (x > y) { int t = x; x = y; y = t; }
            long sum = 0;
            for (int i = x; i <= y; i++) sum += reference[i];
            assertEquals(sum, a.sumRange(x, y)); assertEquals(sum, b.sumRange(x, y));
        }
    }

    @Test public void lazyAssignmentMatchesReference() {
        SegmentTreeFenwickModerate.Q20Optimized a = new SegmentTreeFenwickModerate.Q20Optimized(new int[17]);
        SegmentTreeFenwickModerate.Q20Recursive b = new SegmentTreeFenwickModerate.Q20Recursive(new int[17]);
        long[] reference = new long[17];
        Random random = new Random(28);
        for (int step = 0; step < 500; step++) {
            int l = random.nextInt(17), r = random.nextInt(17);
            if (l > r) { int t = l; l = r; r = t; }
            long value = step % 3 == 0 ? 0 : random.nextInt(101) - 50;
            a.assignRange(l, r, value); b.assignRange(l, r, value);
            for (int i = l; i <= r; i++) reference[i] = value;
            int x = random.nextInt(17), y = random.nextInt(17);
            if (x > y) { int t = x; x = y; y = t; }
            long sum = 0;
            for (int i = x; i <= y; i++) sum += reference[i];
            assertEquals(sum, a.sumRange(x, y)); assertEquals(sum, b.sumRange(x, y));
        }
    }
}
