package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 12: Trees. Moderate questions 13-20. */
public class TreesModerate {

  /*
   * Question 13: Sum Root to Leaf Numbers
   * 
   * Question: Given a binary tree containing digits 0 through 9, each root-to-leaf path represents a number. Return the total sum of all root-to-leaf numbers.
   * 
   * Constraints: 1 <= number of nodes <= 1000; 0 <= Node.val <= 9; depth <= 10 in original constraints.
   * 
   * Optimized complexity: Time O(n); Space O(h). Iterative DFS carries the numeric prefix.
   * Recursive complexity: Time O(n); Space O(h). Recursive DFS accumulates prefix values.
   * 
   * Example 1:
   * Input: root = [1,2,3]
   * Output: 25
   * Explanation: Paths 12 and 13 sum to 25.
   * 
   * Example 2:
   * Input: root = [4,9,0,5,1]
   * Output: 1026
   * Explanation: Paths 495, 491, and 40 sum to 1026.
   * 
   * Example 3:
   * Input: root = [0]
   * Output: 0
   * Explanation: The only path number is 0.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q13Optimized {
    public int sumNumbers(TreeNode root) {
      if (root == null) return 0;
      Deque<TreeNode> nodes = new ArrayDeque<>();
      Deque<Integer> values = new ArrayDeque<>();
      nodes.push(root);
      values.push(root.val);
      int sum = 0;
  
      while (!nodes.isEmpty()) {
        TreeNode node = nodes.pop();
        int value = values.pop();
        if (node.left == null && node.right == null) sum += value;
        if (node.right != null) { nodes.push(node.right); values.push(value * 10 + node.right.val); }
        if (node.left != null) { nodes.push(node.left); values.push(value * 10 + node.left.val); }
      }
      return sum;
    }
  }

  public static class Q13Recursive {
    public int sumNumbers(TreeNode root) {
      return dfs(root, 0);
    }
  
    private int dfs(TreeNode node, int current) {
      if (node == null) return 0;
      int next = current * 10 + node.val;
      if (node.left == null && node.right == null) return next;
      return dfs(node.left, next) + dfs(node.right, next);
    }
  }

  /*
   * Question 14: Count Good Nodes in Binary Tree
   * 
   * Question: Given a binary tree root, a node is good if no node on the path from root to that node has a value greater than it. Return the number of good nodes.
   * 
   * Constraints: 1 <= number of nodes <= 100000; -10000 <= Node.val <= 10000.
   * 
   * Optimized complexity: Time O(n); Space O(h). Iterative DFS carries path maximum per stack item.
   * Recursive complexity: Time O(n); Space O(h). Recursive DFS passes path maximum down.
   * 
   * Example 1:
   * Input: root = [3,1,4,3,null,1,5]
   * Output: 4
   * Explanation: Nodes 3, 3, 4, and 5 are good.
   * 
   * Example 2:
   * Input: root = [3,3,null,4,2]
   * Output: 3
   * Explanation: Equal values count as good when not less than path max.
   * 
   * Example 3:
   * Input: root = [1]
   * Output: 1
   * Explanation: The root is always good.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q14Optimized {
    public int goodNodes(TreeNode root) {
      int count = 0;
      Deque<State> stack = new ArrayDeque<>();
      stack.push(new State(root, Integer.MIN_VALUE));
  
      while (!stack.isEmpty()) {
        State state = stack.pop();
        if (state.node == null) continue;
        if (state.node.val >= state.maxSoFar) count++;
        int nextMax = Math.max(state.maxSoFar, state.node.val);
        stack.push(new State(state.node.right, nextMax));
        stack.push(new State(state.node.left, nextMax));
      }
      return count;
    }
  
    private static class State {
      TreeNode node; int maxSoFar;
      State(TreeNode node, int maxSoFar) { this.node = node; this.maxSoFar = maxSoFar; }
    }
  }

  public static class Q14Recursive {
    public int goodNodes(TreeNode root) {
      return dfs(root, Integer.MIN_VALUE);
    }
  
    private int dfs(TreeNode node, int maxSoFar) {
      if (node == null) return 0;
      int good = node.val >= maxSoFar ? 1 : 0;
      int nextMax = Math.max(maxSoFar, node.val);
      return good + dfs(node.left, nextMax) + dfs(node.right, nextMax);
    }
  }

  /*
   * Question 15: Validate Binary Search Tree
   * 
   * Question: Given the root of a binary tree, return true if it is a valid binary search tree with strict ordering.
   * 
   * Constraints: 1 <= number of nodes <= 10000; -2147483648 <= Node.val <= 2147483647.
   * 
   * Optimized complexity: Time O(n); Space O(h). Iterative inorder stops at first violation.
   * Recursive complexity: Time O(n); Space O(h). Recursive bounds validate each node once.
   * 
   * Example 1:
   * Input: root = [2,1,3]
   * Output: true
   * Explanation: The tree satisfies strict BST order.
   * 
   * Example 2:
   * Input: root = [5,1,4,null,null,3,6]
   * Output: false
   * Explanation: 3 is in the right subtree of 5 but less than 5.
   * 
   * Example 3:
   * Input: root = [2,2,2]
   * Output: false
   * Explanation: Duplicates are not allowed.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q15Optimized {
    public boolean isValidBST(TreeNode root) {
      Deque<TreeNode> stack = new ArrayDeque<>();
      TreeNode current = root;
      Long previous = null;
  
      while (current != null || !stack.isEmpty()) {
        while (current != null) { stack.push(current); current = current.left; }
        current = stack.pop();
        if (previous != null && current.val <= previous) return false;
        previous = (long) current.val;
        current = current.right;
      }
      return true;
    }
  }

  public static class Q15Recursive {
    public boolean isValidBST(TreeNode root) {
      return valid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
  
    private boolean valid(TreeNode node, long low, long high) {
      if (node == null) return true;
      if (node.val <= low || node.val >= high) return false;
      return valid(node.left, low, node.val) && valid(node.right, node.val, high);
    }
  }

  /*
   * Question 16: Kth Smallest Element in a BST
   * 
   * Question: Given the root of a BST and an integer k, return the kth smallest value among all node values.
   * 
   * Constraints: 1 <= k <= number of nodes <= 10000; 0 <= Node.val <= 10000.
   * 
   * Optimized complexity: Time O(h + k); Space O(h). Iterative inorder stops after k pops.
   * Recursive complexity: Time O(h + k); Space O(h). Recursive inorder stops once kth value is found.
   * 
   * Example 1:
   * Input: root = [3,1,4,null,2], k = 1
   * Output: 1
   * Explanation: The smallest value is 1.
   * 
   * Example 2:
   * Input: root = [5,3,6,2,4,null,null,1], k = 3
   * Output: 3
   * Explanation: The sorted order is [1,2,3,4,5,6].
   * 
   * Example 3:
   * Input: root = [1], k = 1
   * Output: 1
   * Explanation: The only node is kth smallest.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q16Optimized {
    public int kthSmallest(TreeNode root, int k) {
      Deque<TreeNode> stack = new ArrayDeque<>();
      TreeNode current = root;
  
      while (current != null || !stack.isEmpty()) {
        while (current != null) { stack.push(current); current = current.left; }
        current = stack.pop();
        if (--k == 0) return current.val;
        current = current.right;
      }
      return -1;
    }
  }

  public static class Q16Recursive {
    private int remaining;
    private int answer;
  
    public int kthSmallest(TreeNode root, int k) {
      remaining = k;
      inorder(root);
      return answer;
    }
  
    private void inorder(TreeNode node) {
      if (node == null || remaining == 0) return;
      inorder(node.left);
      if (--remaining == 0) {
        answer = node.val;
        return;
      }
      inorder(node.right);
    }
  }

  /*
   * Question 17: Construct Binary Tree from Preorder and Inorder Traversal
   * 
   * Question: Given preorder and inorder traversal arrays of a binary tree with unique values, construct and return the binary tree.
   * 
   * Constraints: 1 <= preorder.length <= 3000; inorder.length == preorder.length; values are unique and traversals are valid.
   * 
   * Optimized complexity: Time O(n); Space O(n). Iterative stack reconstructs links from traversal order.
   * Recursive complexity: Time O(n); Space O(n). Hash map enables O(1) root splits.
   * 
   * Example 1:
   * Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
   * Output: [3,9,20,null,null,15,7]
   * Explanation: 3 is root, then inorder splits left and right subtrees.
   * 
   * Example 2:
   * Input: preorder = [-1], inorder = [-1]
   * Output: [-1]
   * Explanation: One node is constructed.
   * 
   * Example 3:
   * Input: preorder = [1,2], inorder = [2,1]
   * Output: [1,2]
   * Explanation: 2 is the left child of 1.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q17Optimized {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
      TreeNode root = new TreeNode(preorder[0]);
      Deque<TreeNode> stack = new ArrayDeque<>();
      stack.push(root);
      int inorderIndex = 0;
  
      for (int i = 1; i < preorder.length; i++) {
        int value = preorder[i];
        TreeNode node = stack.peek();
        if (node.val != inorder[inorderIndex]) {
          node.left = new TreeNode(value);
          stack.push(node.left);
        } else {
          while (!stack.isEmpty() && stack.peek().val == inorder[inorderIndex]) {
            node = stack.pop();
            inorderIndex++;
          }
          node.right = new TreeNode(value);
          stack.push(node.right);
        }
      }
      return root;
    }
  }

  public static class Q17Recursive {
    private int preIndex = 0;
    private Map<Integer, Integer> inorderIndex = new HashMap<>();
  
    public TreeNode buildTree(int[] preorder, int[] inorder) {
      preIndex = 0; inorderIndex.clear();
      for (int i = 0; i < inorder.length; i++) inorderIndex.put(inorder[i], i);
      return build(preorder, 0, inorder.length - 1);
    }
  
    private TreeNode build(int[] preorder, int left, int right) {
      if (left > right) return null;
      int value = preorder[preIndex++];
      TreeNode root = new TreeNode(value);
      int split = inorderIndex.get(value);
      root.left = build(preorder, left, split - 1);
      root.right = build(preorder, split + 1, right);
      return root;
    }
  }

  /*
   * Question 18: Serialize and Deserialize Binary Tree
   * 
   * Question: Design an algorithm to serialize and deserialize a binary tree. Serialization converts a tree to a string; deserialization rebuilds the same structure.
   * 
   * Constraints: 0 <= number of nodes <= 10000; -1000 <= Node.val <= 1000.
   * 
   * Optimized complexity: Serialize Time O(n), Deserialize Time O(n); Space O(n). Iterative level order uses a queue.
   * Recursive complexity: Serialize Time O(n), Deserialize Time O(n); Space O(h) stack plus O(n) tokens.
   * 
   * Example 1:
   * Input: root = [1,2,3,null,null,4,5]
   * Output: deserialize(serialize(root)) returns same tree
   * Explanation: Null markers preserve child positions.
   * 
   * Example 2:
   * Input: root = []
   * Output: []
   * Explanation: The empty tree serializes to a null marker.
   * 
   * Example 3:
   * Input: root = [1]
   * Output: [1]
   * Explanation: A single value with two null children is reconstructed.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q18Optimized {
    public String serialize(TreeNode root) {
      if (root == null) return "#";
      StringBuilder sb = new StringBuilder();
      Queue<TreeNode> queue = new LinkedList<>();
      queue.offer(root);
      while (!queue.isEmpty()) {
        TreeNode node = queue.poll();
        if (node == null) { sb.append("#,"); continue; }
        sb.append(node.val).append(',');
        queue.offer(node.left);
        queue.offer(node.right);
      }
      return sb.toString();
    }
  
    public TreeNode deserialize(String data) {
      if (data.equals("#")) return null;
      String[] tokens = data.split(",");
      TreeNode root = new TreeNode(Integer.parseInt(tokens[0]));
      Queue<TreeNode> queue = new ArrayDeque<>();
      queue.offer(root);
      int index = 1;
      while (!queue.isEmpty()) {
        TreeNode node = queue.poll();
        if (!tokens[index].equals("#")) { node.left = new TreeNode(Integer.parseInt(tokens[index])); queue.offer(node.left); }
        index++;
        if (!tokens[index].equals("#")) { node.right = new TreeNode(Integer.parseInt(tokens[index])); queue.offer(node.right); }
        index++;
      }
      return root;
    }
  }

  public static class Q18Recursive {
    public String serialize(TreeNode root) {
      StringBuilder sb = new StringBuilder();
      write(root, sb);
      return sb.toString();
    }
  
    private void write(TreeNode node, StringBuilder sb) {
      if (node == null) { sb.append("#,"); return; }
      sb.append(node.val).append(',');
      write(node.left, sb);
      write(node.right, sb);
    }
  
    public TreeNode deserialize(String data) {
      Queue<String> tokens = new ArrayDeque<>(Arrays.asList(data.split(",")));
      return read(tokens);
    }
  
    private TreeNode read(Queue<String> tokens) {
      String token = tokens.poll();
      if (token.equals("#")) return null;
      TreeNode node = new TreeNode(Integer.parseInt(token));
      node.left = read(tokens);
      node.right = read(tokens);
      return node;
    }
  }

  /*
   * Question 19: Flatten Binary Tree to Linked List
   * 
   * Question: Given the root of a binary tree, flatten it into a linked list in-place using the same TreeNode class. The flattened order should follow preorder traversal using right pointers only.
   * 
   * Constraints: 0 <= number of nodes <= 2000; -100 <= Node.val <= 100.
   * 
   * Optimized complexity: Time O(n); Space O(1). Iterative Morris-style rewiring finds the rightmost predecessor of each left subtree.
   * Recursive complexity: Time O(n); Space O(h). Reverse preorder recursion links nodes with a previous pointer.
   * 
   * Example 1:
   * Input: root = [1,2,5,3,4,null,6]
   * Output: [1,null,2,null,3,null,4,null,5,null,6]
   * Explanation: The right chain follows preorder.
   * 
   * Example 2:
   * Input: root = []
   * Output: []
   * Explanation: Nothing changes.
   * 
   * Example 3:
   * Input: root = [0]
   * Output: [0]
   * Explanation: A single node is already flat.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q19Optimized {
    public void flatten(TreeNode root) {
      TreeNode current = root;
      while (current != null) {
        if (current.left != null) {
          TreeNode predecessor = current.left;
          while (predecessor.right != null) predecessor = predecessor.right;
          predecessor.right = current.right;
          current.right = current.left;
          current.left = null;
        }
        current = current.right;
      }
    }
  }

  public static class Q19Recursive {
    private TreeNode previous = null;
  
    public void flatten(TreeNode root) {
      previous = null;
      flattenDfs(root);
    }
  
    private void flattenDfs(TreeNode root) {
      if (root == null) return;
      flattenDfs(root.right);
      flattenDfs(root.left);
      root.right = previous;
      root.left = null;
      previous = root;
    }
  }

  /*
   * Question 20: Binary Tree Zigzag Level Order Traversal
   * 
   * Question: Given the root of a binary tree, return the zigzag level order traversal of its node values, alternating left-to-right and right-to-left each level.
   * 
   * Constraints: 0 <= number of nodes <= 2000; -100 <= Node.val <= 100.
   * 
   * Optimized complexity: Time O(n); Space O(w). BFS uses deque insertion to avoid post-reversal.
   * Recursive complexity: Time O(n); Space O(h). DFS appends values into depth lists with direction-aware insertion.
   * 
   * Example 1:
   * Input: root = [3,9,20,null,null,15,7]
   * Output: [[3],[20,9],[15,7]]
   * Explanation: The second level is reversed.
   * 
   * Example 2:
   * Input: root = [1]
   * Output: [[1]]
   * Explanation: One level has no direction change effect.
   * 
   * Example 3:
   * Input: root = []
   * Output: []
   * Explanation: No levels exist.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q20Optimized {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
      List<List<Integer>> answer = new ArrayList<>();
      if (root == null) return answer;
      Queue<TreeNode> queue = new ArrayDeque<>();
      queue.offer(root);
      boolean leftToRight = true;
  
      while (!queue.isEmpty()) {
        int size = queue.size();
        Deque<Integer> level = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
          TreeNode node = queue.poll();
          if (leftToRight) level.addLast(node.val); else level.addFirst(node.val);
          if (node.left != null) queue.offer(node.left);
          if (node.right != null) queue.offer(node.right);
        }
        answer.add(new ArrayList<>(level));
        leftToRight = !leftToRight;
      }
      return answer;
    }
  }

  public static class Q20Recursive {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
      List<LinkedList<Integer>> levels = new ArrayList<>();
      dfs(root, 0, levels);
      return new ArrayList<>(levels);
    }
  
    private void dfs(TreeNode node, int depth, List<LinkedList<Integer>> levels) {
      if (node == null) return;
      if (depth == levels.size()) levels.add(new LinkedList<>());
      if (depth % 2 == 0) levels.get(depth).addLast(node.val);
      else levels.get(depth).addFirst(node.val);
      dfs(node.left, depth + 1, levels);
      dfs(node.right, depth + 1, levels);
    }
  }
}
