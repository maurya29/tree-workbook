package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 13: Binary Search Tree. Basic questions 1-12. */
public class BinarySearchTreeBasic {

  /*
   * Question 1: Search in a Binary Search Tree
   * 
   * Question: Given the root of a binary search tree and an integer val, return the subtree rooted at the node whose value equals val, or null if the value does not exist.
   * 
   * Constraints: 1 <= number of nodes <= 5000; 1 <= Node.val <= 10000000; all values are unique; 1 <= val <= 10000000.
   * 
   * Optimized complexity: Time O(h); Space O(1). Iterative BST search follows one path.
   * Recursive complexity: Time O(h); Space O(h). Recursive BST search follows one path.
   * 
   * Example 1:
   * Input: root = [4,2,7,1,3], val = 2
   * Output: [2,1,3]
   * Explanation: The node with value 2 is found and its subtree is returned.
   * 
   * Example 2:
   * Input: root = [4,2,7,1,3], val = 5
   * Output: []
   * Explanation: 5 does not exist in the BST.
   * 
   * Example 3:
   * Input: root = [1], val = 1
   * Output: [1]
   * Explanation: The root itself matches.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q01Optimized {
    public TreeNode searchBST(TreeNode root, int val) {
      TreeNode current = root;
      while (current != null && current.val != val) {
        current = val < current.val ? current.left : current.right;
      }
      return current;
    }
  }

  public static class Q01Recursive {
    public TreeNode searchBST(TreeNode root, int val) {
      if (root == null || root.val == val) return root;
      if (val < root.val) return searchBST(root.left, val);
      return searchBST(root.right, val);
    }
  }

  /*
   * Question 2: Insert into a Binary Search Tree
   * 
   * Question: Given the root of a BST and a value not currently in the tree, insert the value into the BST and return the root.
   * 
   * Constraints: 0 <= number of nodes <= 10000; -100000000 <= Node.val,val <= 100000000; val does not exist in the BST.
   * 
   * Optimized complexity: Time O(h); Space O(1). Iterative insertion follows one search path.
   * Recursive complexity: Time O(h); Space O(h). Recursive insertion returns the updated subtree root.
   * 
   * Example 1:
   * Input: root = [4,2,7,1,3], val = 5
   * Output: [4,2,7,1,3,5]
   * Explanation: 5 is inserted as the left child of 7.
   * 
   * Example 2:
   * Input: root = [40,20,60,10,30,50,70], val = 25
   * Output: valid BST containing 25
   * Explanation: 25 belongs between 20 and 30.
   * 
   * Example 3:
   * Input: root = [], val = 5
   * Output: [5]
   * Explanation: A new root is created.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q02Optimized {
    public TreeNode insertIntoBST(TreeNode root, int val) {
      if (root == null) return new TreeNode(val);
      TreeNode current = root;
      while (true) {
        if (val < current.val) {
          if (current.left == null) { current.left = new TreeNode(val); break; }
          current = current.left;
        } else {
          if (current.right == null) { current.right = new TreeNode(val); break; }
          current = current.right;
        }
      }
      return root;
    }
  }

  public static class Q02Recursive {
    public TreeNode insertIntoBST(TreeNode root, int val) {
      if (root == null) return new TreeNode(val);
      if (val < root.val) root.left = insertIntoBST(root.left, val);
      else root.right = insertIntoBST(root.right, val);
      return root;
    }
  }

  /*
   * Question 3: Delete Node in a BST
   * 
   * Question: Given the root of a BST and a key, delete the node with value key if it exists and return the updated root.
   * 
   * Constraints: 0 <= number of nodes <= 10000; -100000 <= Node.val,key <= 100000; all node values are unique.
   * 
   * Optimized complexity: Time O(h); Space O(1). Iteratively search and rewire the deleted node.
   * Recursive complexity: Time O(h); Space O(h). Recursive deletion updates subtree links while unwinding.
   * 
   * Example 1:
   * Input: root = [5,3,6,2,4,null,7], key = 3
   * Output: [5,4,6,2,null,null,7]
   * Explanation: 3 is replaced by its inorder successor 4.
   * 
   * Example 2:
   * Input: root = [5,3,6,2,4,null,7], key = 0
   * Output: same tree
   * Explanation: 0 is absent.
   * 
   * Example 3:
   * Input: root = [], key = 0
   * Output: []
   * Explanation: Empty tree remains empty.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q03Optimized {
    public TreeNode deleteNode(TreeNode root, int key) {
      TreeNode dummy = new TreeNode(0, null, root);
      TreeNode parent = dummy;
      TreeNode current = root;
      while (current != null && current.val != key) {
        parent = current;
        current = key < current.val ? current.left : current.right;
      }
      if (current == null) return dummy.right;
  
      TreeNode replacement = removeRoot(current);
      if (parent.left == current) parent.left = replacement;
      else parent.right = replacement;
      return dummy.right;
    }
  
    private TreeNode removeRoot(TreeNode node) {
      if (node.left == null) return node.right;
      if (node.right == null) return node.left;
      TreeNode successorParent = node;
      TreeNode successor = node.right;
      while (successor.left != null) {
        successorParent = successor;
        successor = successor.left;
      }
      if (successorParent != node) {
        successorParent.left = successor.right;
        successor.right = node.right;
      }
      successor.left = node.left;
      return successor;
    }
  }

  public static class Q03Recursive {
    public TreeNode deleteNode(TreeNode root, int key) {
      if (root == null) return null;
      if (key < root.val) root.left = deleteNode(root.left, key);
      else if (key > root.val) root.right = deleteNode(root.right, key);
      else {
        if (root.left == null) return root.right;
        if (root.right == null) return root.left;
        TreeNode successor = min(root.right);
        root.val = successor.val;
        root.right = deleteNode(root.right, successor.val);
      }
      return root;
    }
  
    private TreeNode min(TreeNode node) {
      while (node.left != null) node = node.left;
      return node;
    }
  }

  /*
   * Question 4: Validate Binary Search Tree
   * 
   * Question: Given the root of a binary tree, determine if it is a valid BST. Every node must be strictly greater than all values in its left subtree and strictly less than all values in its right subtree.
   * 
   * Constraints: 1 <= number of nodes <= 10000; -2147483648 <= Node.val <= 2147483647.
   * 
   * Optimized complexity: Time O(n); Space O(h). Iterative inorder stops at first violation.
   * Recursive complexity: Time O(n); Space O(h). Recursive bounds validate every node once.
   * 
   * Example 1:
   * Input: root = [2,1,3]
   * Output: true
   * Explanation: All nodes satisfy BST ordering.
   * 
   * Example 2:
   * Input: root = [5,1,4,null,null,3,6]
   * Output: false
   * Explanation: 3 is in the right subtree of 5 but less than 5.
   * 
   * Example 3:
   * Input: root = [2,2,2]
   * Output: false
   * Explanation: BST ordering is strict.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q04Optimized {
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

  public static class Q04Recursive {
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
   * Question 5: Kth Smallest Element in a BST
   * 
   * Question: Given the root of a BST and an integer k, return the kth smallest value in the tree.
   * 
   * Constraints: 1 <= k <= number of nodes <= 10000; 0 <= Node.val <= 10000.
   * 
   * Optimized complexity: Time O(h + k); Space O(h). Iterative inorder stops after k nodes.
   * Recursive complexity: Time O(h + k); Space O(h). Recursive inorder stops when kth is found.
   * 
   * Example 1:
   * Input: root = [3,1,4,null,2], k = 1
   * Output: 1
   * Explanation: The first inorder value is 1.
   * 
   * Example 2:
   * Input: root = [5,3,6,2,4,null,null,1], k = 3
   * Output: 3
   * Explanation: The sorted values are [1,2,3,4,5,6].
   * 
   * Example 3:
   * Input: root = [1], k = 1
   * Output: 1
   * Explanation: The only value is kth.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q05Optimized {
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

  public static class Q05Recursive {
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
      if (--remaining == 0) { answer = node.val; return; }
      inorder(node.right);
    }
  }

  /*
   * Question 6: Lowest Common Ancestor of a Binary Search Tree
   * 
   * Question: Given a BST and two nodes p and q, return their lowest common ancestor.
   * 
   * Constraints: 2 <= number of nodes <= 100000; all Node.val values are unique; p and q exist in the BST.
   * 
   * Optimized complexity: Time O(h); Space O(1). Iterative BST split search follows one path.
   * Recursive complexity: Time O(h); Space O(h). Recursive BST split search follows one path.
   * 
   * Example 1:
   * Input: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 8
   * Output: 6
   * Explanation: 2 and 8 split at root 6.
   * 
   * Example 2:
   * Input: same root, p = 2, q = 4
   * Output: 2
   * Explanation: 2 is an ancestor of 4.
   * 
   * Example 3:
   * Input: root = [2,1], p = 2, q = 1
   * Output: 2
   * Explanation: Root is the LCA.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q06Optimized {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
      TreeNode current = root;
      while (current != null) {
        if (p.val < current.val && q.val < current.val) current = current.left;
        else if (p.val > current.val && q.val > current.val) current = current.right;
        else return current;
      }
      return null;
    }
  }

  public static class Q06Recursive {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
      if (p.val < root.val && q.val < root.val) return lowestCommonAncestor(root.left, p, q);
      if (p.val > root.val && q.val > root.val) return lowestCommonAncestor(root.right, p, q);
      return root;
    }
  }

  /*
   * Question 7: Convert Sorted Array to Binary Search Tree
   * 
   * Question: Given a sorted integer array, convert it to a height-balanced BST.
   * 
   * Constraints: 1 <= nums.length <= 10000; -10000 <= nums[i] <= 10000; nums is strictly increasing.
   * 
   * Optimized complexity: Time O(n); Space O(n). Iterative stack builds ranges.
   * Recursive complexity: Time O(n); Space O(log n). Recursive midpoint construction creates each node once.
   * 
   * Example 1:
   * Input: nums = [-10,-3,0,5,9]
   * Output: [0,-3,9,-10,null,5]
   * Explanation: One valid balanced BST uses 0 as root.
   * 
   * Example 2:
   * Input: nums = [1,3]
   * Output: [1,null,3] or [3,1]
   * Explanation: Both are height-balanced.
   * 
   * Example 3:
   * Input: nums = [1]
   * Output: [1]
   * Explanation: The only value becomes root.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q07Optimized {
    public TreeNode sortedArrayToBST(int[] nums) {
      if (nums.length == 0) return null;
      TreeNode root = new TreeNode(0);
      Deque<State> stack = new ArrayDeque<>();
      stack.push(new State(root, 0, nums.length - 1));
      while (!stack.isEmpty()) {
        State state = stack.pop();
        int mid = state.left + (state.right - state.left) / 2;
        state.node.val = nums[mid];
        if (state.left <= mid - 1) { state.node.left = new TreeNode(0); stack.push(new State(state.node.left, state.left, mid - 1)); }
        if (mid + 1 <= state.right) { state.node.right = new TreeNode(0); stack.push(new State(state.node.right, mid + 1, state.right)); }
      }
      return root;
    }
  
    private static class State { TreeNode node; int left, right; State(TreeNode node, int left, int right){this.node=node;this.left=left;this.right=right;} }
  }

  public static class Q07Recursive {
    public TreeNode sortedArrayToBST(int[] nums) {
      return build(nums, 0, nums.length - 1);
    }
  
    private TreeNode build(int[] nums, int left, int right) {
      if (left > right) return null;
      int mid = left + (right - left) / 2;
      TreeNode root = new TreeNode(nums[mid]);
      root.left = build(nums, left, mid - 1);
      root.right = build(nums, mid + 1, right);
      return root;
    }
  }

  /*
   * Question 8: Two Sum IV - Input is a BST
   * 
   * Question: Given the root of a BST and an integer k, return true if there exist two different nodes whose values sum to k.
   * 
   * Constraints: 1 <= number of nodes <= 10000; -10000 <= Node.val <= 10000; -100000 <= k <= 100000.
   * 
   * Optimized complexity: Time O(n); Space O(n). Inorder list plus two pointers scans once.
   * Recursive complexity: Time O(n); Space O(n). DFS checks complements in a hash set.
   * 
   * Example 1:
   * Input: root = [5,3,6,2,4,null,7], k = 9
   * Output: true
   * Explanation: 2 + 7 = 9.
   * 
   * Example 2:
   * Input: root = [5,3,6,2,4,null,7], k = 28
   * Output: false
   * Explanation: No pair sums to 28.
   * 
   * Example 3:
   * Input: root = [1], k = 2
   * Output: false
   * Explanation: One node cannot form a pair with itself.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q08Optimized {
    public boolean findTarget(TreeNode root, int k) {
      List<Integer> values = new ArrayList<>();
      Deque<TreeNode> stack = new ArrayDeque<>();
      TreeNode current = root;
      while (current != null || !stack.isEmpty()) {
        while (current != null) { stack.push(current); current = current.left; }
        current = stack.pop();
        values.add(current.val);
        current = current.right;
      }
      int left = 0, right = values.size() - 1;
      while (left < right) {
        int sum = values.get(left) + values.get(right);
        if (sum == k) return true;
        if (sum < k) left++;
        else right--;
      }
      return false;
    }
  }

  public static class Q08Recursive {
    public boolean findTarget(TreeNode root, int k) {
      return dfs(root, k, new HashSet<>());
    }
  
    private boolean dfs(TreeNode node, int k, Set<Integer> seen) {
      if (node == null) return false;
      if (seen.contains(k - node.val)) return true;
      seen.add(node.val);
      return dfs(node.left, k, seen) || dfs(node.right, k, seen);
    }
  }

  /*
   * Question 9: Minimum Absolute Difference in BST
   * 
   * Question: Given the root of a BST, return the minimum absolute difference between values of any two different nodes.
   * 
   * Constraints: 2 <= number of nodes <= 10000; 0 <= Node.val <= 100000.
   * 
   * Optimized complexity: Time O(n); Space O(h). Iterative inorder compares adjacent sorted values.
   * Recursive complexity: Time O(n); Space O(h). Recursive inorder tracks previous value and best answer.
   * 
   * Example 1:
   * Input: root = [4,2,6,1,3]
   * Output: 1
   * Explanation: Adjacent sorted pairs differ by 1.
   * 
   * Example 2:
   * Input: root = [1,0,48,null,null,12,49]
   * Output: 1
   * Explanation: 48 and 49 are closest.
   * 
   * Example 3:
   * Input: root = [2,1]
   * Output: 1
   * Explanation: The only pair differs by 1.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q09Optimized {
    public int getMinimumDifference(TreeNode root) {
      Deque<TreeNode> stack = new ArrayDeque<>();
      TreeNode current = root;
      Integer previous = null;
      int best = Integer.MAX_VALUE;
      while (current != null || !stack.isEmpty()) {
        while (current != null) { stack.push(current); current = current.left; }
        current = stack.pop();
        if (previous != null) best = Math.min(best, current.val - previous);
        previous = current.val;
        current = current.right;
      }
      return best;
    }
  }

  public static class Q09Recursive {
    private Integer previous = null;
    private int best = Integer.MAX_VALUE;
  
    public int getMinimumDifference(TreeNode root) {
      previous = null; best = Integer.MAX_VALUE;
      inorder(root);
      return best;
    }
  
    private void inorder(TreeNode node) {
      if (node == null) return;
      inorder(node.left);
      if (previous != null) best = Math.min(best, node.val - previous);
      previous = node.val;
      inorder(node.right);
    }
  }

  /*
   * Question 10: Range Sum of BST
   * 
   * Question: Given a BST root and integers low and high, return the sum of values of all nodes with low <= value <= high.
   * 
   * Constraints: 1 <= number of nodes <= 20000; 1 <= Node.val <= 100000; 1 <= low <= high <= 100000.
   * 
   * Optimized complexity: Time O(m + h); Space O(h). Iterative DFS skips irrelevant subtrees, where m is nodes visited in/pruned near range.
   * Recursive complexity: Time O(m + h); Space O(h). Recursive pruning skips impossible subtrees.
   * 
   * Example 1:
   * Input: root = [10,5,15,3,7,null,18], low = 7, high = 15
   * Output: 32
   * Explanation: 7 + 10 + 15 = 32.
   * 
   * Example 2:
   * Input: root = [10,5,15,3,7,13,18,1,null,6], low = 6, high = 10
   * Output: 23
   * Explanation: 6 + 7 + 10 = 23.
   * 
   * Example 3:
   * Input: root = [1], low = 2, high = 3
   * Output: 0
   * Explanation: The only value is outside range.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q10Optimized {
    public int rangeSumBST(TreeNode root, int low, int high) {
      int sum = 0;
      Deque<TreeNode> stack = new ArrayDeque<>();
      if (root != null) stack.push(root);
      while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        if (node == null) continue;
        if (node.val >= low && node.val <= high) sum += node.val;
        if (node.val > low && node.left != null) stack.push(node.left);
        if (node.val < high && node.right != null) stack.push(node.right);
      }
      return sum;
    }
  }

  public static class Q10Recursive {
    public int rangeSumBST(TreeNode root, int low, int high) {
      if (root == null) return 0;
      if (root.val < low) return rangeSumBST(root.right, low, high);
      if (root.val > high) return rangeSumBST(root.left, low, high);
      return root.val + rangeSumBST(root.left, low, high) + rangeSumBST(root.right, low, high);
    }
  }

  /*
   * Question 11: Increasing Order Search Tree
   * 
   * Question: Given the root of a BST, rearrange the tree in increasing order so the leftmost node becomes the new root and every node has no left child and only one right child.
   * 
   * Constraints: 1 <= number of nodes <= 100; 0 <= Node.val <= 1000.
   * 
   * Optimized complexity: Time O(n); Space O(h). Iterative inorder rewires existing nodes.
   * Recursive complexity: Time O(n); Space O(h). Recursive inorder appends nodes to a tail pointer.
   * 
   * Example 1:
   * Input: root = [5,3,6,2,4,null,8,1,null,null,null,7,9]
   * Output: [1,null,2,null,3,null,4,null,5,null,6,null,7,null,8,null,9]
   * Explanation: The right chain follows inorder order.
   * 
   * Example 2:
   * Input: root = [5,1,7]
   * Output: [1,null,5,null,7]
   * Explanation: Values are rearranged ascending.
   * 
   * Example 3:
   * Input: root = [1]
   * Output: [1]
   * Explanation: A single node already satisfies the shape.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q11Optimized {
    public TreeNode increasingBST(TreeNode root) {
      TreeNode dummy = new TreeNode(0);
      TreeNode tail = dummy;
      Deque<TreeNode> stack = new ArrayDeque<>();
      TreeNode current = root;
      while (current != null || !stack.isEmpty()) {
        while (current != null) { stack.push(current); current = current.left; }
        current = stack.pop();
        current.left = null;
        tail.right = current;
        tail = current;
        current = current.right;
      }
      return dummy.right;
    }
  }

  public static class Q11Recursive {
    private TreeNode tail;
  
    public TreeNode increasingBST(TreeNode root) {
      TreeNode dummy = new TreeNode(0);
      tail = dummy;
      inorder(root);
      return dummy.right;
    }
  
    private void inorder(TreeNode node) {
      if (node == null) return;
      inorder(node.left);
      node.left = null;
      tail.right = node;
      tail = node;
      inorder(node.right);
    }
  }

  /*
   * Question 12: Recover Binary Search Tree
   * 
   * Question: Two nodes of a BST were swapped by mistake. Recover the tree without changing its structure.
   * 
   * Constraints: 2 <= number of nodes <= 1000; -2147483648 <= Node.val <= 2147483647.
   * 
   * Optimized complexity: Time O(n); Space O(h). Iterative inorder detects the two swapped nodes.
   * Recursive complexity: Time O(n); Space O(h). Recursive inorder detects inversions.
   * 
   * Example 1:
   * Input: root = [1,3,null,null,2]
   * Output: [3,1,null,null,2]
   * Explanation: Values 1 and 3 are swapped back.
   * 
   * Example 2:
   * Input: root = [3,1,4,null,null,2]
   * Output: [2,1,4,null,null,3]
   * Explanation: Values 2 and 3 are swapped back.
   * 
   * Example 3:
   * Input: root = [2,1]
   * Output: [2,1]
   * Explanation: A valid tree remains valid in no-op variants.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/bst.html
   */


  public static class Q12Optimized {
    public void recoverTree(TreeNode root) {
      TreeNode first = null, second = null, previous = null;
      Deque<TreeNode> stack = new ArrayDeque<>();
      TreeNode current = root;
      while (current != null || !stack.isEmpty()) {
        while (current != null) { stack.push(current); current = current.left; }
        current = stack.pop();
        if (previous != null && previous.val > current.val) {
          if (first == null) first = previous;
          second = current;
        }
        previous = current;
        current = current.right;
      }
      int temp = first.val;
      first.val = second.val;
      second.val = temp;
    }
  }

  public static class Q12Recursive {
    private TreeNode first;
    private TreeNode second;
    private TreeNode previous;
  
    public void recoverTree(TreeNode root) {
      first = second = previous = null;
      inorder(root);
      int temp = first.val;
      first.val = second.val;
      second.val = temp;
    }
  
    private void inorder(TreeNode node) {
      if (node == null) return;
      inorder(node.left);
      if (previous != null && previous.val > node.val) {
        if (first == null) first = previous;
        second = node;
      }
      previous = node;
      inorder(node.right);
    }
  }
}
