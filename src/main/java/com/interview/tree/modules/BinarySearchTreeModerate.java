package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 13: Binary Search Tree. Moderate questions 13-20. */
public class BinarySearchTreeModerate {

  /*
   * Question 13: Balance a Binary Search Tree
   * 
   * Question: Given the root of a BST, return a balanced BST containing the same node values.
   * 
   * Constraints: 1 <= number of nodes <= 10000; 1 <= Node.val <= 100000.
   * 
   * Optimized complexity: Time O(n); Space O(n). Iterative inorder plus iterative range construction builds balanced tree.
   * Recursive complexity: Time O(n); Space O(n). Inorder list plus recursive middle construction.
   * 
   * Example 1:
   * Input: root = [1,null,2,null,3,null,4]
   * Output: balanced BST with values [1,2,3,4]
   * Explanation: A skewed tree is rebuilt with smaller height.
   * 
   * Example 2:
   * Input: root = [2,1,3]
   * Output: [2,1,3]
   * Explanation: Already balanced tree may remain unchanged.
   * 
   * Example 3:
   * Input: root = [1]
   * Output: [1]
   * Explanation: One node is balanced.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q13Optimized {
    public TreeNode balanceBST(TreeNode root) {
      List<Integer> values = new ArrayList<>();
      Deque<TreeNode> stack = new ArrayDeque<>();
      TreeNode current = root;
      while (current != null || !stack.isEmpty()) {
        while (current != null) { stack.push(current); current = current.left; }
        current = stack.pop();
        values.add(current.val);
        current = current.right;
      }
      if (values.isEmpty()) return null;
      TreeNode newRoot = new TreeNode(0);
      Deque<State> ranges = new ArrayDeque<>();
      ranges.push(new State(newRoot, 0, values.size() - 1));
      while (!ranges.isEmpty()) {
        State state = ranges.pop();
        int mid = state.left + (state.right - state.left) / 2;
        state.node.val = values.get(mid);
        if (state.left <= mid - 1) { state.node.left = new TreeNode(0); ranges.push(new State(state.node.left, state.left, mid - 1)); }
        if (mid + 1 <= state.right) { state.node.right = new TreeNode(0); ranges.push(new State(state.node.right, mid + 1, state.right)); }
      }
      return newRoot;
    }
  
    private static class State { TreeNode node; int left, right; State(TreeNode node, int left, int right){this.node=node;this.left=left;this.right=right;} }
  }

  public static class Q13Recursive {
    public TreeNode balanceBST(TreeNode root) {
      List<Integer> values = new ArrayList<>();
      inorder(root, values);
      return build(values, 0, values.size() - 1);
    }
  
    private void inorder(TreeNode node, List<Integer> values) {
      if (node == null) return;
      inorder(node.left, values);
      values.add(node.val);
      inorder(node.right, values);
    }
  
    private TreeNode build(List<Integer> values, int left, int right) {
      if (left > right) return null;
      int mid = left + (right - left) / 2;
      TreeNode root = new TreeNode(values.get(mid));
      root.left = build(values, left, mid - 1);
      root.right = build(values, mid + 1, right);
      return root;
    }
  }

  /*
   * Question 14: Trim a Binary Search Tree
   * 
   * Question: Given a BST root and range [low, high], trim the tree so every node value lies in the range while preserving relative ancestor-child structure among remaining nodes.
   * 
   * Constraints: 1 <= number of nodes <= 10000; 0 <= Node.val <= 10000; 0 <= low <= high <= 10000.
   * 
   * Optimized complexity: Time O(n); Space O(1). Iteratively find valid root and trim invalid boundary chains.
   * Recursive complexity: Time O(n); Space O(h). Recursive trimming skips invalid subtrees by range.
   * 
   * Example 1:
   * Input: root = [1,0,2], low = 1, high = 2
   * Output: [1,null,2]
   * Explanation: 0 is removed.
   * 
   * Example 2:
   * Input: root = [3,0,4,null,2,null,null,1], low = 1, high = 3
   * Output: [3,2,null,1]
   * Explanation: 0 and 4 are trimmed.
   * 
   * Example 3:
   * Input: root = [1], low = 1, high = 1
   * Output: [1]
   * Explanation: Root remains.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q14Optimized {
    public TreeNode trimBST(TreeNode root, int low, int high) {
      while (root != null && (root.val < low || root.val > high)) {
        root = root.val < low ? root.right : root.left;
      }
      TreeNode current = root;
      while (current != null) {
        while (current.left != null && current.left.val < low) current.left = current.left.right;
        current = current.left;
      }
      current = root;
      while (current != null) {
        while (current.right != null && current.right.val > high) current.right = current.right.left;
        current = current.right;
      }
      return root;
    }
  }

  public static class Q14Recursive {
    public TreeNode trimBST(TreeNode root, int low, int high) {
      if (root == null) return null;
      if (root.val < low) return trimBST(root.right, low, high);
      if (root.val > high) return trimBST(root.left, low, high);
      root.left = trimBST(root.left, low, high);
      root.right = trimBST(root.right, low, high);
      return root;
    }
  }

  /*
   * Question 15: Construct BST from Preorder Traversal
   * 
   * Question: Given preorder traversal of a BST, construct the tree and return its root.
   * 
   * Constraints: 1 <= preorder.length <= 100; 1 <= preorder[i] <= 1000; all values are unique.
   * 
   * Optimized complexity: Time O(n); Space O(n). Iterative stack links nodes by preorder transitions.
   * Recursive complexity: Time O(n); Space O(h). Recursive bounds consume each value once.
   * 
   * Example 1:
   * Input: preorder = [8,5,1,7,10,12]
   * Output: [8,5,10,1,7,null,12]
   * Explanation: Values are inserted respecting BST preorder structure.
   * 
   * Example 2:
   * Input: preorder = [1,3]
   * Output: [1,null,3]
   * Explanation: 3 is the right child.
   * 
   * Example 3:
   * Input: preorder = [2,1]
   * Output: [2,1]
   * Explanation: 1 is the left child.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q15Optimized {
    public TreeNode bstFromPreorder(int[] preorder) {
      TreeNode root = new TreeNode(preorder[0]);
      Deque<TreeNode> stack = new ArrayDeque<>();
      stack.push(root);
      for (int i = 1; i < preorder.length; i++) {
        TreeNode node = new TreeNode(preorder[i]);
        if (preorder[i] < stack.peek().val) {
          stack.peek().left = node;
        } else {
          TreeNode parent = null;
          while (!stack.isEmpty() && preorder[i] > stack.peek().val) parent = stack.pop();
          parent.right = node;
        }
        stack.push(node);
      }
      return root;
    }
  }

  public static class Q15Recursive {
    private int index = 0;
  
    public TreeNode bstFromPreorder(int[] preorder) {
      index = 0;
      return build(preorder, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
  
    private TreeNode build(int[] preorder, int low, int high) {
      if (index == preorder.length) return null;
      int value = preorder[index];
      if (value < low || value > high) return null;
      index++;
      TreeNode root = new TreeNode(value);
      root.left = build(preorder, low, value);
      root.right = build(preorder, value, high);
      return root;
    }
  }

  /*
   * Question 16: Unique Binary Search Trees
   * 
   * Question: Given n, return the number of structurally unique BSTs that store values 1 through n.
   * 
   * Constraints: 1 <= n <= 19.
   * 
   * Optimized complexity: Time O(n^2); Space O(n). Bottom-up Catalan DP computes each size once.
   * Recursive complexity: Time O(n^2); Space O(n). Memoized recursion caches counts by subtree size.
   * 
   * Example 1:
   * Input: n = 3
   * Output: 5
   * Explanation: There are five structurally unique BSTs.
   * 
   * Example 2:
   * Input: n = 1
   * Output: 1
   * Explanation: Only one tree can be formed.
   * 
   * Example 3:
   * Input: n = 2
   * Output: 2
   * Explanation: Either 1 or 2 can be root.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q16Optimized {
    public int numTrees(int n) {
      int[] dp = new int[n + 1];
      dp[0] = 1;
      dp[1] = 1;
      for (int nodes = 2; nodes <= n; nodes++) {
        for (int root = 1; root <= nodes; root++) {
          dp[nodes] += dp[root - 1] * dp[nodes - root];
        }
      }
      return dp[n];
    }
  }

  public static class Q16Recursive {
    public int numTrees(int n) {
      int[] memo = new int[n + 1];
      Arrays.fill(memo, -1);
      return count(n, memo);
    }
  
    private int count(int nodes, int[] memo) {
      if (nodes <= 1) return 1;
      if (memo[nodes] != -1) return memo[nodes];
      int total = 0;
      for (int root = 1; root <= nodes; root++) {
        total += count(root - 1, memo) * count(nodes - root, memo);
      }
      memo[nodes] = total;
      return total;
    }
  }

  /*
   * Question 17: Unique Binary Search Trees II
   * 
   * Question: Given n, generate all structurally unique BSTs that store values 1 through n.
   * 
   * Constraints: 1 <= n <= 8.
   * 
   * Optimized complexity: Time O(Cn * n); Space O(Cn * n). Iterative DP builds trees by interval length.
   * Recursive complexity: Time O(Cn * n); Space O(Cn * n). Recursive range generation creates Catalan many trees.
   * 
   * Example 1:
   * Input: n = 3
   * Output: five unique BST roots
   * Explanation: All five structures for values 1..3 are generated.
   * 
   * Example 2:
   * Input: n = 1
   * Output: [[1]]
   * Explanation: Only one tree exists.
   * 
   * Example 3:
   * Input: n = 2
   * Output: [[1,null,2],[2,1]]
   * Explanation: Either value can be the root.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q17Optimized {
    public List<TreeNode> generateTrees(int n) {
      List<TreeNode>[][] dp = new ArrayList[n + 2][n + 2];
      for (int i = 1; i <= n + 1; i++) {
        dp[i][i - 1] = new ArrayList<>();
        dp[i][i - 1].add(null);
      }
      for (int len = 1; len <= n; len++) {
        for (int left = 1; left + len - 1 <= n; left++) {
          int right = left + len - 1;
          dp[left][right] = new ArrayList<>();
          for (int root = left; root <= right; root++) {
            for (TreeNode l : dp[left][root - 1]) for (TreeNode r : dp[root + 1][right]) {
              dp[left][right].add(new TreeNode(root, l, r));
            }
          }
        }
      }
      return dp[1][n];
    }
  }

  public static class Q17Recursive {
    public List<TreeNode> generateTrees(int n) {
      return build(1, n);
    }
  
    private List<TreeNode> build(int left, int right) {
      List<TreeNode> answer = new ArrayList<>();
      if (left > right) {
        answer.add(null);
        return answer;
      }
      for (int root = left; root <= right; root++) {
        for (TreeNode l : build(left, root - 1)) {
          for (TreeNode r : build(root + 1, right)) {
            answer.add(new TreeNode(root, l, r));
          }
        }
      }
      return answer;
    }
  }

  /*
   * Question 18: Closest Binary Search Tree Value
   * 
   * Question: Given the root of a BST and a target double, return the value in the BST closest to target.
   * 
   * Constraints: 1 <= number of nodes <= 10000; -1000000000 <= Node.val <= 1000000000; target is a double.
   * 
   * Optimized complexity: Time O(h); Space O(1). Iterative BST search follows the target direction.
   * Recursive complexity: Time O(h); Space O(h). Recursive search tracks closest value.
   * 
   * Example 1:
   * Input: root = [4,2,5,1,3], target = 3.714286
   * Output: 4
   * Explanation: 4 is closest to the target.
   * 
   * Example 2:
   * Input: root = [1], target = 4.428571
   * Output: 1
   * Explanation: Only one value exists.
   * 
   * Example 3:
   * Input: root = [2,1,3], target = 3.0
   * Output: 3
   * Explanation: Exact match is closest.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q18Optimized {
    public int closestValue(TreeNode root, double target) {
      int closest = root.val;
      TreeNode current = root;
      while (current != null) {
        if (Math.abs(current.val - target) < Math.abs(closest - target)) closest = current.val;
        current = target < current.val ? current.left : current.right;
      }
      return closest;
    }
  }

  public static class Q18Recursive {
    public int closestValue(TreeNode root, double target) {
      return search(root, target, root.val);
    }
  
    private int search(TreeNode node, double target, int closest) {
      if (node == null) return closest;
      if (Math.abs(node.val - target) < Math.abs(closest - target)) closest = node.val;
      if (target < node.val) return search(node.left, target, closest);
      return search(node.right, target, closest);
    }
  }

  /*
   * Question 19: Closest Binary Search Tree Value II
   * 
   * Question: Given a non-empty BST, a target double, and integer k, return k values in the BST that are closest to target.
   * 
   * Constraints: 1 <= k <= number of nodes <= 10000; target is a double; all node values are unique in common variants.
   * 
   * Optimized complexity: Time O(h + k); Space O(h + k). Two stacks generate predecessor and successor values lazily.
   * Recursive complexity: Time O(n log k); Space O(h + k). DFS maintains a max-heap of k closest values.
   * 
   * Example 1:
   * Input: root = [4,2,5,1,3], target = 3.714286, k = 2
   * Output: [4,3]
   * Explanation: 4 and 3 are the two closest values.
   * 
   * Example 2:
   * Input: root = [1], target = 0.0, k = 1
   * Output: [1]
   * Explanation: Only one node exists.
   * 
   * Example 3:
   * Input: root = [2,1,3], target = 10.0, k = 2
   * Output: [3,2]
   * Explanation: Largest values are closest when target is above all nodes.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q19Optimized {
    public List<Integer> closestKValues(TreeNode root, double target, int k) {
      Deque<TreeNode> predecessors = new ArrayDeque<>();
      Deque<TreeNode> successors = new ArrayDeque<>();
      initPredecessors(root, target, predecessors);
      initSuccessors(root, target, successors);
      List<Integer> answer = new ArrayList<>();
      while (k-- > 0) {
        if (predecessors.isEmpty()) answer.add(nextSuccessor(successors));
        else if (successors.isEmpty()) answer.add(nextPredecessor(predecessors));
        else if (Math.abs(predecessors.peek().val - target) <= Math.abs(successors.peek().val - target)) answer.add(nextPredecessor(predecessors));
        else answer.add(nextSuccessor(successors));
      }
      return answer;
    }
  
    private void initPredecessors(TreeNode node, double target, Deque<TreeNode> stack) {
      while (node != null) {
        if (node.val <= target) { stack.push(node); node = node.right; }
        else node = node.left;
      }
    }
  
    private void initSuccessors(TreeNode node, double target, Deque<TreeNode> stack) {
      while (node != null) {
        if (node.val > target) { stack.push(node); node = node.left; }
        else node = node.right;
      }
    }
  
    private int nextPredecessor(Deque<TreeNode> stack) {
      TreeNode node = stack.pop();
      int value = node.val;
      node = node.left;
      while (node != null) { stack.push(node); node = node.right; }
      return value;
    }
  
    private int nextSuccessor(Deque<TreeNode> stack) {
      TreeNode node = stack.pop();
      int value = node.val;
      node = node.right;
      while (node != null) { stack.push(node); node = node.left; }
      return value;
    }
  }

  public static class Q19Recursive {
    public List<Integer> closestKValues(TreeNode root, double target, int k) {
      PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> Double.compare(Math.abs(b - target), Math.abs(a - target)));
      dfs(root, target, k, heap);
      return new ArrayList<>(heap);
    }
  
    private void dfs(TreeNode node, double target, int k, PriorityQueue<Integer> heap) {
      if (node == null) return;
      heap.offer(node.val);
      if (heap.size() > k) heap.poll();
      dfs(node.left, target, k, heap);
      dfs(node.right, target, k, heap);
    }
  }

  /*
   * Question 20: Binary Search Tree Iterator
   * 
   * Question: Design an iterator over a BST that supports next() returning the next smallest value and hasNext() indicating whether another value exists.
   * 
   * Constraints: 1 <= number of nodes <= 100000; at most 100000 calls; next is called only when hasNext is true.
   * 
   * Optimized complexity: Constructor Time O(h), next Amortized O(1), hasNext O(1), Space O(h). Lazy stack stores only the path.
   * Recursive complexity: Constructor Time O(n), next O(1), hasNext O(1), Space O(n). Recursive inorder precomputes values.
   * 
   * Example 1:
   * Input: BSTIterator([7,3,15,null,null,9,20]); next(); next(); hasNext();
   * Output: 3, 7, true
   * Explanation: Values are returned in ascending order.
   * 
   * Example 2:
   * Input: next(); next(); next()
   * Output: 9, 15, 20 later in sequence
   * Explanation: The iterator continues inorder.
   * 
   * Example 3:
   * Input: single-node tree
   * Output: next returns root then hasNext false
   * Explanation: Only one value exists.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q20Optimized {
    private final Deque<TreeNode> stack = new ArrayDeque<>();
  
    public Q20Optimized(TreeNode root) {
      pushLeft(root);
    }
  
    public int next() {
      TreeNode node = stack.pop();
      pushLeft(node.right);
      return node.val;
    }
  
    public boolean hasNext() {
      return !stack.isEmpty();
    }
  
    private void pushLeft(TreeNode node) {
      while (node != null) {
        stack.push(node);
        node = node.left;
      }
    }
  }

  public static class Q20Recursive {
    private final List<Integer> values = new ArrayList<>();
    private int index = 0;
  
    public Q20Recursive(TreeNode root) {
      inorder(root);
    }
  
    public int next() {
      return values.get(index++);
    }
  
    public boolean hasNext() {
      return index < values.size();
    }
  
    private void inorder(TreeNode node) {
      if (node == null) return;
      inorder(node.left);
      values.add(node.val);
      inorder(node.right);
    }
  }
}
