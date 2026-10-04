package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 12: Trees. Basic questions 1-12. */
public class TreesBasic {

  /*
   * Question 1: Maximum Depth of Binary Tree
   * 
   * Question: Given the root of a binary tree, return its maximum depth. Maximum depth is the number of nodes along the longest path from the root node down to a leaf.
   * 
   * Constraints: 0 <= number of nodes <= 10000; -100 <= Node.val <= 100.
   * 
   * Optimized complexity: Time O(n); Space O(w). BFS level traversal counts tree levels.
   * Recursive complexity: Time O(n); Space O(h). DFS recursion returns height from each subtree.
   * 
   * Example 1:
   * Input: root = [3,9,20,null,null,15,7]
   * Output: 3
   * Explanation: The longest root-to-leaf path has 3 nodes.
   * 
   * Example 2:
   * Input: root = [1,null,2]
   * Output: 2
   * Explanation: The right-skewed path has two nodes.
   * 
   * Example 3:
   * Input: root = []
   * Output: 0
   * Explanation: An empty tree has depth 0.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q01Optimized {
    public int maxDepth(TreeNode root) {
      if (root == null) return 0;
      Queue<TreeNode> queue = new ArrayDeque<>();
      queue.offer(root);
      int depth = 0;
  
      while (!queue.isEmpty()) {
        int size = queue.size();
        depth++;
        for (int i = 0; i < size; i++) {
          TreeNode node = queue.poll();
          if (node.left != null) queue.offer(node.left);
          if (node.right != null) queue.offer(node.right);
        }
      }
  
      return depth;
    }
  }

  public static class Q01Recursive {
    public int maxDepth(TreeNode root) {
      if (root == null) return 0;
      int leftDepth = maxDepth(root.left);
      int rightDepth = maxDepth(root.right);
      return 1 + Math.max(leftDepth, rightDepth);
    }
  }

  /*
   * Question 2: Same Tree
   * 
   * Question: Given the roots of two binary trees p and q, return true if they are structurally identical and every corresponding node has the same value.
   * 
   * Constraints: 0 <= number of nodes <= 100; -10000 <= Node.val <= 10000.
   * 
   * Optimized complexity: Time O(n); Space O(h) to O(n). Iterative stack compares node pairs.
   * Recursive complexity: Time O(n); Space O(h). Recursive DFS compares corresponding nodes.
   * 
   * Example 1:
   * Input: p = [1,2,3], q = [1,2,3]
   * Output: true
   * Explanation: Both structure and values match.
   * 
   * Example 2:
   * Input: p = [1,2], q = [1,null,2]
   * Output: false
   * Explanation: The node positions differ.
   * 
   * Example 3:
   * Input: p = [1,2,1], q = [1,1,2]
   * Output: false
   * Explanation: The same values are in different positions.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q02Optimized {
    public boolean isSameTree(TreeNode p, TreeNode q) {
      Deque<TreeNode[]> stack = new ArrayDeque<>();
      stack.push(new TreeNode[] {p, q});
  
      while (!stack.isEmpty()) {
        TreeNode[] pair = stack.pop();
        TreeNode a = pair[0];
        TreeNode b = pair[1];
        if (a == null && b == null) continue;
        if (a == null || b == null || a.val != b.val) return false;
        stack.push(new TreeNode[] {a.left, b.left});
        stack.push(new TreeNode[] {a.right, b.right});
      }
  
      return true;
    }
  }

  public static class Q02Recursive {
    public boolean isSameTree(TreeNode p, TreeNode q) {
      if (p == null || q == null) return p == q;
      if (p.val != q.val) return false;
      return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
  }

  /*
   * Question 3: Invert Binary Tree
   * 
   * Question: Given the root of a binary tree, invert the tree and return its root. Inversion swaps every node’s left and right children.
   * 
   * Constraints: 0 <= number of nodes <= 100; -100 <= Node.val <= 100.
   * 
   * Optimized complexity: Time O(n); Space O(w). Iterative BFS mutates the tree in-place.
   * Recursive complexity: Time O(n); Space O(h). Recursive DFS mutates each node once.
   * 
   * Example 1:
   * Input: root = [4,2,7,1,3,6,9]
   * Output: [4,7,2,9,6,3,1]
   * Explanation: All left and right child links are swapped.
   * 
   * Example 2:
   * Input: root = [2,1,3]
   * Output: [2,3,1]
   * Explanation: The root children swap.
   * 
   * Example 3:
   * Input: root = []
   * Output: []
   * Explanation: The empty tree stays empty.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q03Optimized {
    public TreeNode invertTree(TreeNode root) {
      if (root == null) return null;
      Queue<TreeNode> queue = new ArrayDeque<>();
      queue.offer(root);
  
      while (!queue.isEmpty()) {
        TreeNode node = queue.poll();
        TreeNode temp = node.left;
        node.left = node.right;
        node.right = temp;
        if (node.left != null) queue.offer(node.left);
        if (node.right != null) queue.offer(node.right);
      }
  
      return root;
    }
  }

  public static class Q03Recursive {
    public TreeNode invertTree(TreeNode root) {
      if (root == null) return null;
      TreeNode left = invertTree(root.left);
      TreeNode right = invertTree(root.right);
      root.left = right;
      root.right = left;
      return root;
    }
  }

  /*
   * Question 4: Binary Tree Level Order Traversal
   * 
   * Question: Given the root of a binary tree, return the level order traversal of its nodes values from left to right, level by level.
   * 
   * Constraints: 0 <= number of nodes <= 2000; -1000 <= Node.val <= 1000.
   * 
   * Optimized complexity: Time O(n); Space O(w). BFS visits each node once and stores the widest level.
   * Recursive complexity: Time O(n); Space O(h). DFS creates a list for each depth and appends node values.
   * 
   * Example 1:
   * Input: root = [3,9,20,null,null,15,7]
   * Output: [[3],[9,20],[15,7]]
   * Explanation: Values are grouped by depth.
   * 
   * Example 2:
   * Input: root = [1]
   * Output: [[1]]
   * Explanation: One level contains the root.
   * 
   * Example 3:
   * Input: root = []
   * Output: []
   * Explanation: There are no levels.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q04Optimized {
    public List<List<Integer>> levelOrder(TreeNode root) {
      List<List<Integer>> answer = new ArrayList<>();
      if (root == null) return answer;
      Queue<TreeNode> queue = new ArrayDeque<>();
      queue.offer(root);
  
      while (!queue.isEmpty()) {
        int size = queue.size();
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
          TreeNode node = queue.poll();
          level.add(node.val);
          if (node.left != null) queue.offer(node.left);
          if (node.right != null) queue.offer(node.right);
        }
        answer.add(level);
      }
  
      return answer;
    }
  }

  public static class Q04Recursive {
    public List<List<Integer>> levelOrder(TreeNode root) {
      List<List<Integer>> answer = new ArrayList<>();
      dfs(root, 0, answer);
      return answer;
    }
  
    private void dfs(TreeNode node, int depth, List<List<Integer>> answer) {
      if (node == null) return;
      if (depth == answer.size()) answer.add(new ArrayList<>());
      answer.get(depth).add(node.val);
      dfs(node.left, depth + 1, answer);
      dfs(node.right, depth + 1, answer);
    }
  }

  /*
   * Question 5: Binary Tree Right Side View
   * 
   * Question: Given the root of a binary tree, return the values visible when looking at the tree from the right side, ordered from top to bottom.
   * 
   * Constraints: 0 <= number of nodes <= 100; -100 <= Node.val <= 100.
   * 
   * Optimized complexity: Time O(n); Space O(w). BFS records the last value at each level.
   * Recursive complexity: Time O(n); Space O(h). Right-first DFS records first node per depth.
   * 
   * Example 1:
   * Input: root = [1,2,3,null,5,null,4]
   * Output: [1,3,4]
   * Explanation: Nodes 1, 3, and 4 are rightmost by level.
   * 
   * Example 2:
   * Input: root = [1,null,3]
   * Output: [1,3]
   * Explanation: The visible path follows the right child.
   * 
   * Example 3:
   * Input: root = []
   * Output: []
   * Explanation: No nodes are visible.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q05Optimized {
    public List<Integer> rightSideView(TreeNode root) {
      List<Integer> answer = new ArrayList<>();
      if (root == null) return answer;
      Queue<TreeNode> queue = new ArrayDeque<>();
      queue.offer(root);
  
      while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
          TreeNode node = queue.poll();
          if (i == size - 1) answer.add(node.val);
          if (node.left != null) queue.offer(node.left);
          if (node.right != null) queue.offer(node.right);
        }
      }
  
      return answer;
    }
  }

  public static class Q05Recursive {
    public List<Integer> rightSideView(TreeNode root) {
      List<Integer> answer = new ArrayList<>();
      dfs(root, 0, answer);
      return answer;
    }
  
    private void dfs(TreeNode node, int depth, List<Integer> answer) {
      if (node == null) return;
      if (depth == answer.size()) answer.add(node.val);
      dfs(node.right, depth + 1, answer);
      dfs(node.left, depth + 1, answer);
    }
  }

  /*
   * Question 6: Diameter of Binary Tree
   * 
   * Question: Given the root of a binary tree, return the length in edges of the longest path between any two nodes in the tree.
   * 
   * Constraints: 1 <= number of nodes <= 10000; -100 <= Node.val <= 100.
   * 
   * Optimized complexity: Time O(n); Space O(n). Iterative postorder computes heights once per node.
   * Recursive complexity: Time O(n); Space O(h). Postorder DFS returns heights and updates the best diameter.
   * 
   * Example 1:
   * Input: root = [1,2,3,4,5]
   * Output: 3
   * Explanation: The longest path is 4-2-1-3 or 5-2-1-3.
   * 
   * Example 2:
   * Input: root = [1,2]
   * Output: 1
   * Explanation: The only edge is the diameter.
   * 
   * Example 3:
   * Input: root = [1]
   * Output: 0
   * Explanation: A single node has no edge path.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q06Optimized {
    public int diameterOfBinaryTree(TreeNode root) {
      if (root == null) return 0;
      Map<TreeNode, Integer> height = new HashMap<>();
      Deque<TreeNode> stack = new ArrayDeque<>();
      TreeNode current = root;
      TreeNode last = null;
      int best = 0;
  
      while (current != null || !stack.isEmpty()) {
        while (current != null) { stack.push(current); current = current.left; }
        TreeNode peek = stack.peek();
        if (peek.right != null && last != peek.right) { current = peek.right; continue; }
        int left = height.getOrDefault(peek.left, 0);
        int right = height.getOrDefault(peek.right, 0);
        best = Math.max(best, left + right);
        height.put(peek, 1 + Math.max(left, right));
        last = stack.pop();
      }
      return best;
    }
  }

  public static class Q06Recursive {
    private int best = 0;
  
    public int diameterOfBinaryTree(TreeNode root) {
      best = 0;
      height(root);
      return best;
    }
  
    private int height(TreeNode node) {
      if (node == null) return 0;
      int left = height(node.left);
      int right = height(node.right);
      best = Math.max(best, left + right);
      return 1 + Math.max(left, right);
    }
  }

  /*
   * Question 7: Balanced Binary Tree
   * 
   * Question: Given a binary tree, return true if it is height-balanced: for every node, the left and right subtree heights differ by no more than one.
   * 
   * Constraints: 0 <= number of nodes <= 5000; -10000 <= Node.val <= 10000.
   * 
   * Optimized complexity: Time O(n); Space O(n). Iterative postorder computes each height once.
   * Recursive complexity: Time O(n); Space O(h). DFS returns -1 immediately for unbalanced subtrees.
   * 
   * Example 1:
   * Input: root = [3,9,20,null,null,15,7]
   * Output: true
   * Explanation: Every subtree height difference is at most one.
   * 
   * Example 2:
   * Input: root = [1,2,2,3,3,null,null,4,4]
   * Output: false
   * Explanation: The left subtree is too deep.
   * 
   * Example 3:
   * Input: root = []
   * Output: true
   * Explanation: An empty tree is balanced.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q07Optimized {
    public boolean isBalanced(TreeNode root) {
      Map<TreeNode, Integer> height = new HashMap<>();
      Deque<TreeNode> stack = new ArrayDeque<>();
      TreeNode current = root;
      TreeNode last = null;
  
      while (current != null || !stack.isEmpty()) {
        while (current != null) { stack.push(current); current = current.left; }
        TreeNode node = stack.peek();
        if (node.right != null && last != node.right) { current = node.right; continue; }
        int left = height.getOrDefault(node.left, 0);
        int right = height.getOrDefault(node.right, 0);
        if (Math.abs(left - right) > 1) return false;
        height.put(node, 1 + Math.max(left, right));
        last = stack.pop();
      }
      return true;
    }
  }

  public static class Q07Recursive {
    public boolean isBalanced(TreeNode root) {
      return height(root) != -1;
    }
  
    private int height(TreeNode node) {
      if (node == null) return 0;
      int left = height(node.left);
      if (left == -1) return -1;
      int right = height(node.right);
      if (right == -1) return -1;
      if (Math.abs(left - right) > 1) return -1;
      return 1 + Math.max(left, right);
    }
  }

  /*
   * Question 8: Subtree of Another Tree
   * 
   * Question: Given roots root and subRoot, return true if subRoot is a subtree of root with the same structure and node values.
   * 
   * Constraints: 1 <= number of root nodes <= 2000; 1 <= number of subRoot nodes <= 1000; -10000 <= Node.val <= 10000.
   * 
   * Optimized complexity: Time O(n + m); Space O(n + m). Serialize both trees with null markers and search the serialized string.
   * Recursive complexity: Time O(n*m); Space O(h). Recursive traversal checks same-tree at candidate nodes.
   * 
   * Example 1:
   * Input: root = [3,4,5,1,2], subRoot = [4,1,2]
   * Output: true
   * Explanation: The subtree rooted at 4 matches subRoot.
   * 
   * Example 2:
   * Input: root = [3,4,5,1,2,null,null,null,null,0], subRoot = [4,1,2]
   * Output: false
   * Explanation: The extra 0 changes the structure.
   * 
   * Example 3:
   * Input: root = [1], subRoot = [1]
   * Output: true
   * Explanation: The whole tree can be the subtree.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q08Optimized {
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
      String tree = serialize(root);
      String sub = serialize(subRoot);
      return tree.contains(sub);
    }
  
    private String serialize(TreeNode node) {
      if (node == null) return "#,";
      return "^" + node.val + "," + serialize(node.left) + serialize(node.right);
    }
  }

  public static class Q08Recursive {
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
      if (root == null) return false;
      if (sameTree(root, subRoot)) return true;
      return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }
  
    private boolean sameTree(TreeNode a, TreeNode b) {
      if (a == null || b == null) return a == b;
      if (a.val != b.val) return false;
      return sameTree(a.left, b.left) && sameTree(a.right, b.right);
    }
  }

  /*
   * Question 9: Lowest Common Ancestor of a Binary Tree
   * 
   * Question: Given a binary tree and two nodes p and q, return their lowest common ancestor.
   * 
   * Constraints: 2 <= number of nodes <= 100000; all values are unique; p and q exist in the tree.
   * 
   * Optimized complexity: Time O(n); Space O(n). Iterative parent map climbs ancestors.
   * Recursive complexity: Time O(n); Space O(h). Recursive DFS bubbles candidate nodes upward.
   * 
   * Example 1:
   * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
   * Output: 3
   * Explanation: The nodes are in different subtrees of 3.
   * 
   * Example 2:
   * Input: same root, p = 5, q = 4
   * Output: 5
   * Explanation: 5 is an ancestor of 4.
   * 
   * Example 3:
   * Input: root = [1,2], p = 1, q = 2
   * Output: 1
   * Explanation: The root is an ancestor.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q09Optimized {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
      Map<TreeNode, TreeNode> parent = new HashMap<>();
      Deque<TreeNode> stack = new ArrayDeque<>();
      parent.put(root, null);
      stack.push(root);
  
      while (!parent.containsKey(p) || !parent.containsKey(q)) {
        TreeNode node = stack.pop();
        if (node.left != null) { parent.put(node.left, node); stack.push(node.left); }
        if (node.right != null) { parent.put(node.right, node); stack.push(node.right); }
      }
  
      Set<TreeNode> ancestors = new HashSet<>();
      while (p != null) { ancestors.add(p); p = parent.get(p); }
      while (!ancestors.contains(q)) q = parent.get(q);
      return q;
    }
  }

  public static class Q09Recursive {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
      if (root == null || root == p || root == q) return root;
      TreeNode left = lowestCommonAncestor(root.left, p, q);
      TreeNode right = lowestCommonAncestor(root.right, p, q);
      if (left != null && right != null) return root;
      return left != null ? left : right;
    }
  }

  /*
   * Question 10: Binary Tree Maximum Path Sum
   * 
   * Question: Given the root of a non-empty binary tree, return the maximum path sum of any non-empty path. A path can start and end at any nodes and must follow parent-child links.
   * 
   * Constraints: 1 <= number of nodes <= 30000; -1000 <= Node.val <= 1000.
   * 
   * Optimized complexity: Time O(n); Space O(n). Iterative postorder computes each downward gain once.
   * Recursive complexity: Time O(n); Space O(h). Postorder DFS returns one-side gain and updates global answer.
   * 
   * Example 1:
   * Input: root = [1,2,3]
   * Output: 6
   * Explanation: The best path is 2 -> 1 -> 3.
   * 
   * Example 2:
   * Input: root = [-10,9,20,null,null,15,7]
   * Output: 42
   * Explanation: The best path is 15 -> 20 -> 7.
   * 
   * Example 3:
   * Input: root = [-3]
   * Output: -3
   * Explanation: A path must contain at least one node.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q10Optimized {
    public int maxPathSum(TreeNode root) {
      Map<TreeNode, Integer> gain = new HashMap<>();
      Deque<TreeNode> stack = new ArrayDeque<>();
      TreeNode current = root;
      TreeNode last = null;
      int best = Integer.MIN_VALUE;
  
      while (current != null || !stack.isEmpty()) {
        while (current != null) { stack.push(current); current = current.left; }
        TreeNode node = stack.peek();
        if (node.right != null && last != node.right) { current = node.right; continue; }
        int left = Math.max(0, gain.getOrDefault(node.left, 0));
        int right = Math.max(0, gain.getOrDefault(node.right, 0));
        best = Math.max(best, node.val + left + right);
        gain.put(node, node.val + Math.max(left, right));
        last = stack.pop();
      }
      return best;
    }
  }

  public static class Q10Recursive {
    private int best = Integer.MIN_VALUE;
  
    public int maxPathSum(TreeNode root) {
      best = Integer.MIN_VALUE;
      gain(root);
      return best;
    }
  
    private int gain(TreeNode node) {
      if (node == null) return 0;
      int left = Math.max(0, gain(node.left));
      int right = Math.max(0, gain(node.right));
      best = Math.max(best, node.val + left + right);
      return node.val + Math.max(left, right);
    }
  }

  /*
   * Question 11: Path Sum
   * 
   * Question: Given a binary tree root and an integer targetSum, return true if the tree has a root-to-leaf path whose node values sum to targetSum.
   * 
   * Constraints: 0 <= number of nodes <= 5000; -1000 <= Node.val <= 1000; -1000 <= targetSum <= 1000.
   * 
   * Optimized complexity: Time O(n); Space O(h). Iterative DFS stores nodes with running sums.
   * Recursive complexity: Time O(n); Space O(h). Recursive DFS subtracts current values from target.
   * 
   * Example 1:
   * Input: root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
   * Output: true
   * Explanation: The path 5->4->11->2 sums to 22.
   * 
   * Example 2:
   * Input: root = [1,2,3], targetSum = 5
   * Output: false
   * Explanation: No root-to-leaf path sums to 5.
   * 
   * Example 3:
   * Input: root = [], targetSum = 0
   * Output: false
   * Explanation: An empty tree has no path.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q11Optimized {
    public boolean hasPathSum(TreeNode root, int targetSum) {
      if (root == null) return false;
      Deque<TreeNode> nodes = new ArrayDeque<>();
      Deque<Integer> sums = new ArrayDeque<>();
      nodes.push(root);
      sums.push(root.val);
  
      while (!nodes.isEmpty()) {
        TreeNode node = nodes.pop();
        int sum = sums.pop();
        if (node.left == null && node.right == null && sum == targetSum) return true;
        if (node.right != null) { nodes.push(node.right); sums.push(sum + node.right.val); }
        if (node.left != null) { nodes.push(node.left); sums.push(sum + node.left.val); }
      }
  
      return false;
    }
  }

  public static class Q11Recursive {
    public boolean hasPathSum(TreeNode root, int targetSum) {
      if (root == null) return false;
      if (root.left == null && root.right == null) return root.val == targetSum;
      return hasPathSum(root.left, targetSum - root.val)
          || hasPathSum(root.right, targetSum - root.val);
    }
  }

  /*
   * Question 12: Path Sum II
   * 
   * Question: Given a binary tree root and targetSum, return all root-to-leaf paths where the sum of node values equals targetSum.
   * 
   * Constraints: 0 <= number of nodes <= 5000; -1000 <= Node.val <= 1000; -1000 <= targetSum <= 1000.
   * 
   * Optimized complexity: Time O(n*h); Space O(n*h). Iterative DFS stores copied paths on the stack.
   * Recursive complexity: Time O(n*h); Space O(h). Backtracking reuses one path list excluding output.
   * 
   * Example 1:
   * Input: root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
   * Output: [[5,4,11,2],[5,8,4,5]]
   * Explanation: Two root-to-leaf paths sum to 22.
   * 
   * Example 2:
   * Input: root = [1,2,3], targetSum = 5
   * Output: []
   * Explanation: No leaf path has sum 5.
   * 
   * Example 3:
   * Input: root = [1,2], targetSum = 3
   * Output: [[1,2]]
   * Explanation: The only leaf path sums to 3.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/trees.html
   */


  public static class Q12Optimized {
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
      List<List<Integer>> answer = new ArrayList<>();
      if (root == null) return answer;
      Deque<State> stack = new ArrayDeque<>();
      stack.push(new State(root, root.val, new ArrayList<>(Arrays.asList(root.val))));
  
      while (!stack.isEmpty()) {
        State state = stack.pop();
        TreeNode node = state.node;
        if (node.left == null && node.right == null && state.sum == targetSum) answer.add(state.path);
        if (node.right != null) stack.push(next(state, node.right));
        if (node.left != null) stack.push(next(state, node.left));
      }
      return answer;
    }
  
    private State next(State state, TreeNode child) {
      List<Integer> path = new ArrayList<>(state.path);
      path.add(child.val);
      return new State(child, state.sum + child.val, path);
    }
  
    private static class State {
      TreeNode node; int sum; List<Integer> path;
      State(TreeNode node, int sum, List<Integer> path) { this.node = node; this.sum = sum; this.path = path; }
    }
  }

  public static class Q12Recursive {
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
      List<List<Integer>> answer = new ArrayList<>();
      dfs(root, targetSum, new ArrayList<>(), answer);
      return answer;
    }
  
    private void dfs(TreeNode node, int remaining, List<Integer> path, List<List<Integer>> answer) {
      if (node == null) return;
      path.add(node.val);
      if (node.left == null && node.right == null && node.val == remaining) {
        answer.add(new ArrayList<>(path));
      } else {
        dfs(node.left, remaining - node.val, path, answer);
        dfs(node.right, remaining - node.val, path, answer);
      }
      path.remove(path.size() - 1);
    }
  }
}
