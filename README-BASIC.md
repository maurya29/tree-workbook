# Tree Workbook - Basic Questions and Solutions

## Topic 12: Trees

### Basic (1-12)

#### 1. Maximum Depth of Binary Tree - [LeetCode 104](https://leetcode.com/problems/maximum-depth-of-binary-tree/)

Given the root of a binary tree, return its maximum depth. Maximum depth is the number of nodes along the longest path from the root node down to a leaf.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
public static class Q01Recursive {
  public int maxDepth(TreeNode root) {
    if (root == null) return 0;
    int leftDepth = maxDepth(root.left);
    int rightDepth = maxDepth(root.right);
    return 1 + Math.max(leftDepth, rightDepth);
  }
}
```

#### 2. Same Tree - [LeetCode 100](https://leetcode.com/problems/same-tree/)

Given the roots of two binary trees p and q, return true if they are structurally identical and every corresponding node has the same value.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
public static class Q02Recursive {
  public boolean isSameTree(TreeNode p, TreeNode q) {
    if (p == null || q == null) return p == q;
    if (p.val != q.val) return false;
    return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
  }
}
```

#### 3. Invert Binary Tree - [LeetCode 226](https://leetcode.com/problems/invert-binary-tree/)

Given the root of a binary tree, invert the tree and return its root. Inversion swaps every node’s left and right children.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 4. Binary Tree Level Order Traversal - [LeetCode 102](https://leetcode.com/problems/binary-tree-level-order-traversal/)

Given the root of a binary tree, return the level order traversal of its nodes values from left to right, level by level.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 5. Binary Tree Right Side View - [LeetCode 199](https://leetcode.com/problems/binary-tree-right-side-view/)

Given the root of a binary tree, return the values visible when looking at the tree from the right side, ordered from top to bottom.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 6. Diameter of Binary Tree - [LeetCode 543](https://leetcode.com/problems/diameter-of-binary-tree/)

Given the root of a binary tree, return the length in edges of the longest path between any two nodes in the tree.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 7. Balanced Binary Tree - [LeetCode 110](https://leetcode.com/problems/balanced-binary-tree/)

Given a binary tree, return true if it is height-balanced: for every node, the left and right subtree heights differ by no more than one.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 8. Subtree of Another Tree - [LeetCode 572](https://leetcode.com/problems/subtree-of-another-tree/)

Given roots root and subRoot, return true if subRoot is a subtree of root with the same structure and node values.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 9. Lowest Common Ancestor of a Binary Tree - [LeetCode 236](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/)

Given a binary tree and two nodes p and q, return their lowest common ancestor.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
public static class Q09Recursive {
  public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) return root;
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    if (left != null && right != null) return root;
    return left != null ? left : right;
  }
}
```

#### 10. Binary Tree Maximum Path Sum - [LeetCode 124](https://leetcode.com/problems/binary-tree-maximum-path-sum/)

Given the root of a non-empty binary tree, return the maximum path sum of any non-empty path. A path can start and end at any nodes and must follow parent-child links.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 11. Path Sum - [LeetCode 112](https://leetcode.com/problems/path-sum/)

Given a binary tree root and an integer targetSum, return true if the tree has a root-to-leaf path whose node values sum to targetSum.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
public static class Q11Recursive {
  public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) return false;
    if (root.left == null && root.right == null) return root.val == targetSum;
    return hasPathSum(root.left, targetSum - root.val)
        || hasPathSum(root.right, targetSum - root.val);
  }
}
```

#### 12. Path Sum II - [LeetCode 113](https://leetcode.com/problems/path-sum-ii/)

Given a binary tree root and targetSum, return all root-to-leaf paths where the sum of node values equals targetSum.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

## Topic 13: Binary Search Tree

### Basic (1-12)

#### 1. Search in a Binary Search Tree - [LeetCode 700](https://leetcode.com/problems/search-in-a-binary-search-tree/)

Given the root of a binary search tree and an integer val, return the subtree rooted at the node whose value equals val, or null if the value does not exist.

##### Optimized / Iterative Solution

```java
public static class Q01Optimized {
  public TreeNode searchBST(TreeNode root, int val) {
    TreeNode current = root;
    while (current != null && current.val != val) {
      current = val < current.val ? current.left : current.right;
    }
    return current;
  }
}
```

##### Recursive Solution

```java
public static class Q01Recursive {
  public TreeNode searchBST(TreeNode root, int val) {
    if (root == null || root.val == val) return root;
    if (val < root.val) return searchBST(root.left, val);
    return searchBST(root.right, val);
  }
}
```

#### 2. Insert into a Binary Search Tree - [LeetCode 701](https://leetcode.com/problems/insert-into-a-binary-search-tree/)

Given the root of a BST and a value not currently in the tree, insert the value into the BST and return the root.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
public static class Q02Recursive {
  public TreeNode insertIntoBST(TreeNode root, int val) {
    if (root == null) return new TreeNode(val);
    if (val < root.val) root.left = insertIntoBST(root.left, val);
    else root.right = insertIntoBST(root.right, val);
    return root;
  }
}
```

#### 3. Delete Node in a BST - [LeetCode 450](https://leetcode.com/problems/delete-node-in-a-bst/)

Given the root of a BST and a key, delete the node with value key if it exists and return the updated root.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 4. Validate Binary Search Tree - [LeetCode 98](https://leetcode.com/problems/validate-binary-search-tree/)

Given the root of a binary tree, determine if it is a valid BST. Every node must be strictly greater than all values in its left subtree and strictly less than all values in its right subtree.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 5. Kth Smallest Element in a BST - [LeetCode 230](https://leetcode.com/problems/kth-smallest-element-in-a-bst/)

Given the root of a BST and an integer k, return the kth smallest value in the tree.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 6. Lowest Common Ancestor of a Binary Search Tree - [LeetCode 235](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/)

Given a BST and two nodes p and q, return their lowest common ancestor.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
public static class Q06Recursive {
  public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (p.val < root.val && q.val < root.val) return lowestCommonAncestor(root.left, p, q);
    if (p.val > root.val && q.val > root.val) return lowestCommonAncestor(root.right, p, q);
    return root;
  }
}
```

#### 7. Convert Sorted Array to Binary Search Tree - [LeetCode 108](https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/)

Given a sorted integer array, convert it to a height-balanced BST.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 8. Two Sum IV - Input is a BST - [LeetCode 653](https://leetcode.com/problems/two-sum-iv-input-is-a-bst/)

Given the root of a BST and an integer k, return true if there exist two different nodes whose values sum to k.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 9. Minimum Absolute Difference in BST - [LeetCode 530](https://leetcode.com/problems/minimum-absolute-difference-in-bst/)

Given the root of a BST, return the minimum absolute difference between values of any two different nodes.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 10. Range Sum of BST - [LeetCode 938](https://leetcode.com/problems/range-sum-of-bst/)

Given a BST root and integers low and high, return the sum of values of all nodes with low <= value <= high.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
public static class Q10Recursive {
  public int rangeSumBST(TreeNode root, int low, int high) {
    if (root == null) return 0;
    if (root.val < low) return rangeSumBST(root.right, low, high);
    if (root.val > high) return rangeSumBST(root.left, low, high);
    return root.val + rangeSumBST(root.left, low, high) + rangeSumBST(root.right, low, high);
  }
}
```

#### 11. Increasing Order Search Tree - [LeetCode 897](https://leetcode.com/problems/increasing-order-search-tree/)

Given the root of a BST, rearrange the tree in increasing order so the leftmost node becomes the new root and every node has no left child and only one right child.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 12. Recover Binary Search Tree - [LeetCode 99](https://leetcode.com/problems/recover-binary-search-tree/)

Two nodes of a BST were swapped by mistake. Recover the tree without changing its structure.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

## Topic 16: Trie

### Basic (1-12)

#### 1. Implement Trie Prefix Tree - [LeetCode 208](https://leetcode.com/problems/implement-trie-prefix-tree/)

Design a trie that supports inserting words, searching complete words, and checking whether any word starts with a given prefix.

##### Optimized / Iterative Solution

```java
public static class Q01Optimized {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  private final Node root = new Node();

  public void insert(String word) {
    Node node = root;
    for (char ch : word.toCharArray()) {
      int index = ch - 'a';
      if (node.next[index] == null) node.next[index] = new Node();
      node = node.next[index];
    }
    node.word = true;
  }

  public boolean search(String word) {
    Node node = find(word);
    return node != null && node.word;
  }

  public boolean startsWith(String prefix) {
    return find(prefix) != null;
  }

  private Node find(String text) {
    Node node = root;
    for (char ch : text.toCharArray()) {
      node = node.next[ch - 'a'];
      if (node == null) return null;
    }
    return node;
  }
}
```

##### Recursive Solution

```java
public static class Q01Recursive {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  private final Node root = new Node();

  public void insert(String word) {
    insert(root, word, 0);
  }

  public boolean search(String word) {
    Node node = find(root, word, 0);
    return node != null && node.word;
  }

  public boolean startsWith(String prefix) {
    return find(root, prefix, 0) != null;
  }

  private void insert(Node node, String word, int index) {
    if (index == word.length()) {
      node.word = true;
      return;
    }
    int child = word.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    insert(node.next[child], word, index + 1);
  }

  private Node find(Node node, String text, int index) {
    if (node == null || index == text.length()) return node;
    return find(node.next[text.charAt(index) - 'a'], text, index + 1);
  }
}
```

#### 2. Design Add and Search Words Data Structure - [LeetCode 211](https://leetcode.com/problems/design-add-and-search-words-data-structure/)

Design a data structure that adds lowercase words and searches words where dot can match any one letter.

##### Optimized / Iterative Solution

```java
public static class Q02Optimized {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  private final Node root = new Node();

  public void addWord(String word) {
    Node node = root;
    for (char ch : word.toCharArray()) {
      int index = ch - 'a';
      if (node.next[index] == null) node.next[index] = new Node();
      node = node.next[index];
    }
    node.word = true;
  }

  public boolean search(String pattern) {
    Deque<State> stack = new ArrayDeque<>();
    stack.push(new State(root, 0));
    while (!stack.isEmpty()) {
      State state = stack.pop();
      if (state.index == pattern.length()) {
        if (state.node.word) return true;
        continue;
      }
      char ch = pattern.charAt(state.index);
      if (ch == '.') {
        for (Node child : state.node.next) {
          if (child != null) stack.push(new State(child, state.index + 1));
        }
      } else {
        Node child = state.node.next[ch - 'a'];
        if (child != null) stack.push(new State(child, state.index + 1));
      }
    }
    return false;
  }

  private static class State {
    Node node;
    int index;
    State(Node node, int index) { this.node = node; this.index = index; }
  }
}
```

##### Recursive Solution

```java
public static class Q02Recursive {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  private final Node root = new Node();

  public void addWord(String word) {
    Node node = root;
    for (char ch : word.toCharArray()) {
      int index = ch - 'a';
      if (node.next[index] == null) node.next[index] = new Node();
      node = node.next[index];
    }
    node.word = true;
  }

  public boolean search(String word) {
    return dfs(root, word, 0);
  }

  private boolean dfs(Node node, String word, int index) {
    if (node == null) return false;
    if (index == word.length()) return node.word;

    char ch = word.charAt(index);
    if (ch != '.') return dfs(node.next[ch - 'a'], word, index + 1);
    for (Node child : node.next) {
      if (child != null && dfs(child, word, index + 1)) return true;
    }
    return false;
  }
}
```

#### 3. Word Search II - [LeetCode 212](https://leetcode.com/problems/word-search-ii/)

Given a board of letters and a list of words, return every word that can be formed by adjacent horizontal or vertical cells without reusing a cell.

##### Optimized / Iterative Solution

```java
public static class Q03Optimized {
  private static class Node {
    Node[] next = new Node[26];
    String word;
  }

  public List<String> findWords(char[][] board, String[] words) {
    Node root = build(words);
    Set<String> found = new LinkedHashSet<>();
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board[0].length; c++) {
        collect(board, r, c, root, found);
      }
    }
    return new ArrayList<>(found);
  }

  private Node build(String[] words) {
    Node root = new Node();
    for (String word : words) {
      Node node = root;
      for (char ch : word.toCharArray()) {
        int index = ch - 'a';
        if (node.next[index] == null) node.next[index] = new Node();
        node = node.next[index];
      }
      node.word = word;
    }
    return root;
  }

  private void collect(char[][] board, int r, int c, Node node, Set<String> found) {
    if (r < 0 || c < 0 || r == board.length || c == board[0].length) return;
    char ch = board[r][c];
    if (ch == '#' || node.next[ch - 'a'] == null) return;

    Node next = node.next[ch - 'a'];
    if (next.word != null) found.add(next.word);
    board[r][c] = '#';
    collect(board, r + 1, c, next, found);
    collect(board, r - 1, c, next, found);
    collect(board, r, c + 1, next, found);
    collect(board, r, c - 1, next, found);
    board[r][c] = ch;
  }
}
```

##### Recursive Solution

```java
public static class Q03Recursive {
  private static class Node {
    Node[] next = new Node[26];
    String word;
  }

  public List<String> findWords(char[][] board, String[] words) {
    Node root = new Node();
    insertAll(root, words, 0);
    List<String> answer = new ArrayList<>();
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board[0].length; c++) {
        dfs(board, r, c, root, answer);
      }
    }
    return answer;
  }

  private void insertAll(Node root, String[] words, int index) {
    if (index == words.length) return;
    insert(root, words[index], 0, words[index]);
    insertAll(root, words, index + 1);
  }

  private void insert(Node node, String word, int index, String full) {
    if (index == word.length()) {
      node.word = full;
      return;
    }
    int child = word.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    insert(node.next[child], word, index + 1, full);
  }

  private void dfs(char[][] board, int r, int c, Node node, List<String> answer) {
    if (r < 0 || c < 0 || r == board.length || c == board[0].length) return;
    char ch = board[r][c];
    if (ch == '#' || node.next[ch - 'a'] == null) return;
    Node next = node.next[ch - 'a'];
    if (next.word != null) {
      answer.add(next.word);
      next.word = null;
    }
    board[r][c] = '#';
    dfs(board, r + 1, c, next, answer);
    dfs(board, r - 1, c, next, answer);
    dfs(board, r, c + 1, next, answer);
    dfs(board, r, c - 1, next, answer);
    board[r][c] = ch;
  }
}
```

#### 4. Replace Words - [LeetCode 648](https://leetcode.com/problems/replace-words/)

Given dictionary roots and a sentence, replace each word with the shortest root that is its prefix when such a root exists.

##### Optimized / Iterative Solution

```java
public static class Q04Optimized {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  public String replaceWords(List<String> dictionary, String sentence) {
    Node root = new Node();
    for (String word : dictionary) insert(root, word);

    String[] words = sentence.split(" ");
    for (int i = 0; i < words.length; i++) {
      words[i] = shortestRoot(root, words[i]);
    }
    return String.join(" ", words);
  }

  private void insert(Node root, String word) {
    Node node = root;
    for (char ch : word.toCharArray()) {
      int index = ch - 'a';
      if (node.next[index] == null) node.next[index] = new Node();
      node = node.next[index];
    }
    node.word = true;
  }

  private String shortestRoot(Node root, String word) {
    Node node = root;
    StringBuilder prefix = new StringBuilder();
    for (char ch : word.toCharArray()) {
      int index = ch - 'a';
      if (node.next[index] == null) return word;
      prefix.append(ch);
      node = node.next[index];
      if (node.word) return prefix.toString();
    }
    return word;
  }
}
```

##### Recursive Solution

```java
public static class Q04Recursive {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  public String replaceWords(List<String> dictionary, String sentence) {
    Node root = new Node();
    for (String word : dictionary) insert(root, word, 0);
    String[] words = sentence.split(" ");
    replace(words, 0, root);
    return String.join(" ", words);
  }

  private void replace(String[] words, int index, Node root) {
    if (index == words.length) return;
    String match = find(root, words[index], 0);
    if (match != null) words[index] = match;
    replace(words, index + 1, root);
  }

  private void insert(Node node, String word, int index) {
    if (index == word.length()) {
      node.word = true;
      return;
    }
    int child = word.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    insert(node.next[child], word, index + 1);
  }

  private String find(Node node, String word, int index) {
    if (node.word) return word.substring(0, index);
    if (index == word.length()) return null;
    Node child = node.next[word.charAt(index) - 'a'];
    if (child == null) return null;
    return find(child, word, index + 1);
  }
}
```

#### 5. Map Sum Pairs - [LeetCode 677](https://leetcode.com/problems/map-sum-pairs/)

Design a structure that inserts key-value pairs and returns the sum of values of all keys with a given prefix.

##### Optimized / Iterative Solution

```java
public static class Q05Optimized {
  private static class Node {
    Node[] next = new Node[26];
    int sum;
  }

  private final Node root = new Node();
  private final Map<String, Integer> values = new HashMap<>();

  public void insert(String key, int val) {
    int delta = val - values.getOrDefault(key, 0);
    values.put(key, val);
    Node node = root;
    node.sum += delta;
    for (char ch : key.toCharArray()) {
      int index = ch - 'a';
      if (node.next[index] == null) node.next[index] = new Node();
      node = node.next[index];
      node.sum += delta;
    }
  }

  public int sum(String prefix) {
    Node node = root;
    for (char ch : prefix.toCharArray()) {
      node = node.next[ch - 'a'];
      if (node == null) return 0;
    }
    return node.sum;
  }
}
```

##### Recursive Solution

```java
public static class Q05Recursive {
  private static class Node {
    Node[] next = new Node[26];
    int sum;
  }

  private final Node root = new Node();
  private final Map<String, Integer> values = new HashMap<>();

  public void insert(String key, int val) {
    int delta = val - values.getOrDefault(key, 0);
    values.put(key, val);
    add(root, key, 0, delta);
  }

  public int sum(String prefix) {
    Node node = find(root, prefix, 0);
    return node == null ? 0 : node.sum;
  }

  private void add(Node node, String key, int index, int delta) {
    node.sum += delta;
    if (index == key.length()) return;
    int child = key.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    add(node.next[child], key, index + 1, delta);
  }

  private Node find(Node node, String prefix, int index) {
    if (node == null || index == prefix.length()) return node;
    return find(node.next[prefix.charAt(index) - 'a'], prefix, index + 1);
  }
}
```

#### 6. Longest Word in Dictionary - [LeetCode 720](https://leetcode.com/problems/longest-word-in-dictionary/)

Given a list of words, return the longest word that can be built one character at a time by other words in the list. Return the lexicographically smallest answer on ties.

##### Optimized / Iterative Solution

```java
public static class Q06Optimized {
  private static class Node {
    Node[] next = new Node[26];
    String word;
  }

  public String longestWord(String[] words) {
    Node root = new Node();
    for (String word : words) insert(root, word);

    String best = "";
    java.util.Deque<Node> stack = new java.util.ArrayDeque<>();
    stack.push(root);
    while (!stack.isEmpty()) {
      Node node = stack.pop();
      if (node.word != null && better(node.word, best)) best = node.word;
      for (int i = 25; i >= 0; i--) {
        Node child = node.next[i];
        if (child != null && child.word != null) stack.push(child);
      }
    }
    return best;
  }

  private void insert(Node root, String word) {
    Node node = root;
    for (char ch : word.toCharArray()) {
      int index = ch - 'a';
      if (node.next[index] == null) node.next[index] = new Node();
      node = node.next[index];
    }
    node.word = word;
  }

  private boolean better(String word, String best) {
    return word.length() > best.length()
        || (word.length() == best.length() && word.compareTo(best) < 0);
  }
}
```

##### Recursive Solution

```java
public static class Q06Recursive {
  private static class Node {
    Node[] next = new Node[26];
    String word;
  }

  public String longestWord(String[] words) {
    Node root = new Node();
    for (String word : words) insert(root, word, 0, word);
    return dfs(root, "");
  }

  private void insert(Node node, String word, int index, String full) {
    if (index == word.length()) {
      node.word = full;
      return;
    }
    int child = word.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    insert(node.next[child], word, index + 1, full);
  }

  private String dfs(Node node, String best) {
    if (node.word != null && isBetter(node.word, best)) best = node.word;
    for (int i = 0; i < 26; i++) {
      Node child = node.next[i];
      if (child != null && child.word != null) best = dfs(child, best);
    }
    return best;
  }

  private boolean isBetter(String word, String best) {
    return word.length() > best.length()
        || (word.length() == best.length() && word.compareTo(best) < 0);
  }
}
```

#### 7. Stream of Characters - [LeetCode 1032](https://leetcode.com/problems/stream-of-characters/)

Design a stream checker that receives one character at a time and returns true if any suffix of the stream equals a dictionary word.

##### Optimized / Iterative Solution

```java
public static class Q07Optimized {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  private final Node root = new Node();
  private final Deque<Character> stream = new ArrayDeque<>();
  private int maxLength = 0;

  public Q07Optimized(String[] words) {
    for (String word : words) {
      maxLength = Math.max(maxLength, word.length());
      Node node = root;
      for (int i = word.length() - 1; i >= 0; i--) {
        int index = word.charAt(i) - 'a';
        if (node.next[index] == null) node.next[index] = new Node();
        node = node.next[index];
      }
      node.word = true;
    }
  }

  public boolean query(char letter) {
    stream.addFirst(letter);
    if (stream.size() > maxLength) stream.removeLast();
    Node node = root;
    for (char ch : stream) {
      node = node.next[ch - 'a'];
      if (node == null) return false;
      if (node.word) return true;
    }
    return false;
  }
}
```

##### Recursive Solution

```java
public static class Q07Recursive {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  private final Node root = new Node();
  private final StringBuilder stream = new StringBuilder();
  private int maxLength = 0;

  public Q07Recursive(String[] words) {
    for (String word : words) {
      maxLength = Math.max(maxLength, word.length());
      insert(root, word, word.length() - 1);
    }
  }

  public boolean query(char letter) {
    stream.append(letter);
    if (stream.length() > maxLength) stream.deleteCharAt(0);
    return search(root, stream.length() - 1);
  }

  private void insert(Node node, String word, int index) {
    if (index < 0) {
      node.word = true;
      return;
    }
    int child = word.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    insert(node.next[child], word, index - 1);
  }

  private boolean search(Node node, int index) {
    if (node.word) return true;
    if (index < 0) return false;
    Node child = node.next[stream.charAt(index) - 'a'];
    return child != null && search(child, index - 1);
  }
}
```

#### 8. Search Suggestions System - [LeetCode 1268](https://leetcode.com/problems/search-suggestions-system/)

Given product names and a search word, return up to three lexicographically smallest product suggestions after each typed character.

##### Optimized / Iterative Solution

```java
public static class Q08Optimized {
  private static class Node {
    Node[] next = new Node[26];
    List<String> suggestions = new ArrayList<>();
  }

  public List<List<String>> suggestedProducts(String[] products, String searchWord) {
    Arrays.sort(products);
    Node root = new Node();
    for (String product : products) insert(root, product);

    List<List<String>> answer = new ArrayList<>();
    Node node = root;
    for (char ch : searchWord.toCharArray()) {
      if (node != null) node = node.next[ch - 'a'];
      answer.add(node == null ? new ArrayList<>() : new ArrayList<>(node.suggestions));
    }
    return answer;
  }

  private void insert(Node root, String product) {
    Node node = root;
    for (char ch : product.toCharArray()) {
      int index = ch - 'a';
      if (node.next[index] == null) node.next[index] = new Node();
      node = node.next[index];
      if (node.suggestions.size() < 3) node.suggestions.add(product);
    }
  }
}
```

##### Recursive Solution

```java
public static class Q08Recursive {
  private static class Node {
    Node[] next = new Node[26];
    String word;
  }

  public List<List<String>> suggestedProducts(String[] products, String searchWord) {
    Node root = new Node();
    for (String product : products) insert(root, product, 0);
    List<List<String>> answer = new ArrayList<>();
    Node node = root;
    for (char ch : searchWord.toCharArray()) {
      node = node == null ? null : node.next[ch - 'a'];
      List<String> list = new ArrayList<>();
      collect(node, list);
      answer.add(list);
    }
    return answer;
  }

  private void insert(Node node, String word, int index) {
    if (index == word.length()) {
      node.word = word;
      return;
    }
    int child = word.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    insert(node.next[child], word, index + 1);
  }

  private void collect(Node node, List<String> list) {
    if (node == null || list.size() == 3) return;
    if (node.word != null) list.add(node.word);
    for (int i = 0; i < 26 && list.size() < 3; i++) collect(node.next[i], list);
  }
}
```

#### 9. Maximum XOR of Two Numbers in an Array - [LeetCode 421](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/)

Given an integer array, return the maximum XOR value of any two numbers in the array.

##### Optimized / Iterative Solution

```java
public static class Q09Optimized {
  private static class Node {
    Node[] next = new Node[2];
  }

  public int findMaximumXOR(int[] nums) {
    Node root = new Node();
    for (int num : nums) insert(root, num);

    int best = 0;
    for (int num : nums) {
      Node node = root;
      int value = 0;
      for (int bit = 30; bit >= 0; bit--) {
        int current = (num >>> bit) & 1;
        int want = current ^ 1;
        if (node.next[want] != null) {
          value |= 1 << bit;
          node = node.next[want];
        } else {
          node = node.next[current];
        }
      }
      best = Math.max(best, value);
    }
    return best;
  }

  private void insert(Node root, int num) {
    Node node = root;
    for (int bit = 30; bit >= 0; bit--) {
      int current = (num >>> bit) & 1;
      if (node.next[current] == null) node.next[current] = new Node();
      node = node.next[current];
    }
  }
}
```

##### Recursive Solution

```java
public static class Q09Recursive {
  private static class Node {
    Node[] next = new Node[2];
  }

  public int findMaximumXOR(int[] nums) {
    Node root = new Node();
    for (int num : nums) insert(root, num, 30);
    int best = 0;
    for (int num : nums) best = Math.max(best, query(root, num, 30));
    return best;
  }

  private void insert(Node node, int num, int bit) {
    if (bit < 0) return;
    int current = (num >>> bit) & 1;
    if (node.next[current] == null) node.next[current] = new Node();
    insert(node.next[current], num, bit - 1);
  }

  private int query(Node node, int num, int bit) {
    if (bit < 0) return 0;
    int current = (num >>> bit) & 1;
    int want = current ^ 1;
    if (node.next[want] != null) {
      return (1 << bit) | query(node.next[want], num, bit - 1);
    }
    return query(node.next[current], num, bit - 1);
  }
}
```

#### 10. Palindrome Pairs - [LeetCode 336](https://leetcode.com/problems/palindrome-pairs/)

Given unique words, return all index pairs [i, j] such that words[i] + words[j] is a palindrome.

##### Optimized / Iterative Solution

```java
public static class Q10Optimized {
  private static class Node {
    Node[] next = new Node[26];
    int wordIndex = -1;
    List<Integer> palindromeBelow = new ArrayList<>();
  }

  public List<List<Integer>> palindromePairs(String[] words) {
    Node root = new Node();
    for (int i = 0; i < words.length; i++) insert(root, words[i], i);

    List<List<Integer>> answer = new ArrayList<>();
    for (int i = 0; i < words.length; i++) search(root, words[i], i, answer);
    return answer;
  }

  private void insert(Node root, String word, int index) {
    Node node = root;
    for (int i = word.length() - 1; i >= 0; i--) {
      if (isPalindrome(word, 0, i)) node.palindromeBelow.add(index);
      int child = word.charAt(i) - 'a';
      if (node.next[child] == null) node.next[child] = new Node();
      node = node.next[child];
    }
    node.wordIndex = index;
    node.palindromeBelow.add(index);
  }

  private void search(Node root, String word, int index, List<List<Integer>> answer) {
    Node node = root;
    for (int i = 0; i < word.length(); i++) {
      if (node.wordIndex != -1 && node.wordIndex != index && isPalindrome(word, i, word.length() - 1)) {
        answer.add(Arrays.asList(index, node.wordIndex));
      }
      node = node.next[word.charAt(i) - 'a'];
      if (node == null) return;
    }
    for (int other : node.palindromeBelow) {
      if (other != index) answer.add(Arrays.asList(index, other));
    }
  }

  private boolean isPalindrome(String word, int left, int right) {
    while (left < right) if (word.charAt(left++) != word.charAt(right--)) return false;
    return true;
  }
}
```

##### Recursive Solution

```java
public static class Q10Recursive {
  private static class Node {
    Node[] next = new Node[26];
    int wordIndex = -1;
    List<Integer> palindromeBelow = new ArrayList<>();
  }

  public List<List<Integer>> palindromePairs(String[] words) {
    Node root = new Node();
    for (int i = 0; i < words.length; i++) insert(root, words[i], words[i].length() - 1, i);
    List<List<Integer>> answer = new ArrayList<>();
    for (int i = 0; i < words.length; i++) search(root, words[i], 0, i, answer);
    return answer;
  }

  private void insert(Node node, String word, int index, int wordIndex) {
    if (index < 0) {
      node.wordIndex = wordIndex;
      node.palindromeBelow.add(wordIndex);
      return;
    }
    if (isPalindrome(word, 0, index)) node.palindromeBelow.add(wordIndex);
    int child = word.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    insert(node.next[child], word, index - 1, wordIndex);
  }

  private void search(Node node, String word, int index, int wordIndex, List<List<Integer>> answer) {
    if (node == null) return;
    if (index == word.length()) {
      for (int other : node.palindromeBelow) if (other != wordIndex) answer.add(Arrays.asList(wordIndex, other));
      return;
    }
    if (node.wordIndex != -1 && node.wordIndex != wordIndex && isPalindrome(word, index, word.length() - 1)) {
      answer.add(Arrays.asList(wordIndex, node.wordIndex));
    }
    search(node.next[word.charAt(index) - 'a'], word, index + 1, wordIndex, answer);
  }

  private boolean isPalindrome(String word, int left, int right) {
    while (left < right) if (word.charAt(left++) != word.charAt(right--)) return false;
    return true;
  }
}
```

#### 11. Concatenated Words - [LeetCode 472](https://leetcode.com/problems/concatenated-words/)

Given an array of words, return all words that are formed by concatenating at least two shorter words from the same array.

##### Optimized / Iterative Solution

```java
public static class Q11Optimized {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  public List<String> findAllConcatenatedWordsInADict(String[] words) {
    Arrays.sort(words, Comparator.comparingInt(String::length));
    Node root = new Node();
    List<String> answer = new ArrayList<>();
    for (String word : words) {
      if (word.length() == 0) continue;
      if (canBuild(root, word)) answer.add(word);
      insert(root, word);
    }
    return answer;
  }

  private boolean canBuild(Node root, String word) {
    boolean[] dp = new boolean[word.length() + 1];
    dp[0] = true;
    for (int start = 0; start < word.length(); start++) {
      if (!dp[start]) continue;
      Node node = root;
      for (int end = start; end < word.length(); end++) {
        node = node.next[word.charAt(end) - 'a'];
        if (node == null) break;
        if (node.word) dp[end + 1] = true;
      }
    }
    return dp[word.length()];
  }

  private void insert(Node root, String word) {
    Node node = root;
    for (char ch : word.toCharArray()) {
      int child = ch - 'a';
      if (node.next[child] == null) node.next[child] = new Node();
      node = node.next[child];
    }
    node.word = true;
  }
}
```

##### Recursive Solution

```java
public static class Q11Recursive {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  public List<String> findAllConcatenatedWordsInADict(String[] words) {
    Node root = new Node();
    for (String word : words) if (!word.isEmpty()) insert(root, word, 0);
    List<String> answer = new ArrayList<>();
    for (String word : words) {
      if (!word.isEmpty() && dfs(root, root, word, 0, 0, new Boolean[word.length() + 1][word.length() + 1])) {
        answer.add(word);
      }
    }
    return answer;
  }

  private boolean dfs(Node root, Node node, String word, int index, int count, Boolean[][] memo) {
    if (index == word.length()) return node.word && count >= 1;
    if (memo[index][count] != null) return memo[index][count];
    Node child = node.next[word.charAt(index) - 'a'];
    if (child == null) return memo[index][count] = false;
    boolean ok = dfs(root, child, word, index + 1, count, memo);
    if (child.word) ok |= dfs(root, root, word, index + 1, count + 1, memo);
    return memo[index][count] = ok;
  }

  private void insert(Node node, String word, int index) {
    if (index == word.length()) {
      node.word = true;
      return;
    }
    int child = word.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    insert(node.next[child], word, index + 1);
  }
}
```

#### 12. Word Squares - [LeetCode 425](https://leetcode.com/problems/word-squares/)

Given same-length words, return all word squares where the kth row and kth column read the same string.

##### Optimized / Iterative Solution

```java
public static class Q12Optimized {
  private static class Node {
    Node[] next = new Node[26];
    List<String> words = new ArrayList<>();
  }

  public List<List<String>> wordSquares(String[] words) {
    Node root = new Node();
    for (String word : words) insert(root, word);
    List<List<String>> answer = new ArrayList<>();
    for (String word : words) {
      List<String> square = new ArrayList<>();
      square.add(word);
      build(words[0].length(), root, square, answer);
    }
    return answer;
  }

  private void insert(Node root, String word) {
    Node node = root;
    node.words.add(word);
    for (char ch : word.toCharArray()) {
      int child = ch - 'a';
      if (node.next[child] == null) node.next[child] = new Node();
      node = node.next[child];
      node.words.add(word);
    }
  }

  private void build(int size, Node root, List<String> square, List<List<String>> answer) {
    if (square.size() == size) {
      answer.add(new ArrayList<>(square));
      return;
    }
    int row = square.size();
    StringBuilder prefix = new StringBuilder();
    for (String word : square) prefix.append(word.charAt(row));
    for (String candidate : candidates(root, prefix.toString())) {
      square.add(candidate);
      build(size, root, square, answer);
      square.remove(square.size() - 1);
    }
  }

  private List<String> candidates(Node root, String prefix) {
    Node node = root;
    for (char ch : prefix.toCharArray()) {
      node = node.next[ch - 'a'];
      if (node == null) return Collections.emptyList();
    }
    return node.words;
  }
}
```

##### Recursive Solution

```java
public static class Q12Recursive {
  private static class Node {
    Node[] next = new Node[26];
    List<String> words = new ArrayList<>();
  }

  public List<List<String>> wordSquares(String[] words) {
    Node root = new Node();
    insertAll(root, words, 0);
    List<List<String>> answer = new ArrayList<>();
    start(words, 0, root, answer);
    return answer;
  }

  private void start(String[] words, int index, Node root, List<List<String>> answer) {
    if (index == words.length) return;
    List<String> square = new ArrayList<>();
    square.add(words[index]);
    dfs(words[index].length(), root, square, answer);
    start(words, index + 1, root, answer);
  }

  private void dfs(int size, Node root, List<String> square, List<List<String>> answer) {
    if (square.size() == size) {
      answer.add(new ArrayList<>(square));
      return;
    }
    String prefix = prefix(square, square.size(), 0, new StringBuilder()).toString();
    for (String next : find(root, prefix, 0).words) {
      square.add(next);
      dfs(size, root, square, answer);
      square.remove(square.size() - 1);
    }
  }

  private StringBuilder prefix(List<String> square, int row, int index, StringBuilder out) {
    if (index == square.size()) return out;
    out.append(square.get(index).charAt(row));
    return prefix(square, row, index + 1, out);
  }

  private Node find(Node node, String prefix, int index) {
    if (node == null) return new Node();
    if (index == prefix.length()) return node;
    return find(node.next[prefix.charAt(index) - 'a'], prefix, index + 1);
  }

  private void insertAll(Node root, String[] words, int index) {
    if (index == words.length) return;
    insert(root, words[index], 0, words[index]);
    insertAll(root, words, index + 1);
  }

  private void insert(Node node, String word, int index, String full) {
    node.words.add(full);
    if (index == word.length()) return;
    int child = word.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    insert(node.next[child], word, index + 1, full);
  }
}
```

## Topic 14: Graphs

### Basic (1-12)

#### 1. Number of Islands - [LeetCode 200](https://leetcode.com/problems/number-of-islands/)

Given an m x n grid of characters 1 and 0, count how many disconnected groups of land cells exist. Land cells connect vertically and horizontally.

##### Optimized / Iterative Solution

```java
public static class Q01Optimized {
  public int numIslands(char[][] grid) {
    int rows = grid.length;
    int cols = grid[0].length;
    boolean[][] seen = new boolean[rows][cols];
    int islands = 0;
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (grid[r][c] == '0' || seen[r][c]) continue;
        islands++;
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] {r, c});
        seen[r][c] = true;

        while (!queue.isEmpty()) {
          int[] cell = queue.poll();
          for (int[] d : dirs) {
            int nr = cell[0] + d[0], nc = cell[1] + d[1];
            if (nr < 0 || nc < 0 || nr == rows || nc == cols) continue;
            if (seen[nr][nc] || grid[nr][nc] == '0') continue;
            seen[nr][nc] = true;
            queue.offer(new int[] {nr, nc});
          }
        }
      }
    }

    return islands;
  }
}
```

##### Recursive Solution

```java
public static class Q01Recursive {
  public int numIslands(char[][] grid) {
    int islands = 0;
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[0].length; c++) {
        if (grid[r][c] == '1') {
          islands++;
          sink(grid, r, c);
        }
      }
    }
    return islands;
  }

  private void sink(char[][] grid, int r, int c) {
    if (r < 0 || c < 0 || r == grid.length || c == grid[0].length) return;
    if (grid[r][c] != '1') return;

    grid[r][c] = '0';
    sink(grid, r + 1, c);
    sink(grid, r - 1, c);
    sink(grid, r, c + 1);
    sink(grid, r, c - 1);
  }
}
```

#### 2. Clone Graph - [LeetCode 133](https://leetcode.com/problems/clone-graph/)

Given a reference to a node in a connected undirected graph, return a deep copy of the graph.

##### Optimized / Iterative Solution

```java
public static class Q02Optimized {
  public Node cloneGraph(Node node) {
    if (node == null) return null;
    Map<Node, Node> clone = new HashMap<>();
    Deque<Node> queue = new ArrayDeque<>();
    clone.put(node, new Node(node.val));
    queue.offer(node);

    while (!queue.isEmpty()) {
      Node current = queue.poll();
      for (Node next : current.neighbors) {
        if (!clone.containsKey(next)) {
          clone.put(next, new Node(next.val));
          queue.offer(next);
        }
        clone.get(current).neighbors.add(clone.get(next));
      }
    }
    return clone.get(node);
  }
}
```

##### Recursive Solution

```java
public static class Q02Recursive {
  public Node cloneGraph(Node node) {
    return clone(node, new HashMap<>());
  }

  private Node clone(Node node, Map<Node, Node> copies) {
    if (node == null) return null;
    if (copies.containsKey(node)) return copies.get(node);

    Node copy = new Node(node.val);
    copies.put(node, copy);
    for (Node next : node.neighbors) {
      copy.neighbors.add(clone(next, copies));
    }
    return copy;
  }
}
```

#### 3. Max Area of Island - [LeetCode 695](https://leetcode.com/problems/max-area-of-island/)

Given a binary grid, return the maximum number of land cells in any connected island.

##### Optimized / Iterative Solution

```java
public static class Q03Optimized {
  public int maxAreaOfIsland(int[][] grid) {
    boolean[][] seen = new boolean[grid.length][grid[0].length];
    int best = 0;
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[0].length; c++) {
        if (grid[r][c] == 0 || seen[r][c]) continue;
        int area = 0;
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {r, c});
        seen[r][c] = true;

        while (!stack.isEmpty()) {
          int[] cell = stack.pop();
          area++;
          for (int[] d : dirs) {
            int nr = cell[0] + d[0], nc = cell[1] + d[1];
            if (nr < 0 || nc < 0 || nr == grid.length || nc == grid[0].length) continue;
            if (seen[nr][nc] || grid[nr][nc] == 0) continue;
            seen[nr][nc] = true;
            stack.push(new int[] {nr, nc});
          }
        }
        best = Math.max(best, area);
      }
    }
    return best;
  }
}
```

##### Recursive Solution

```java
public static class Q03Recursive {
  public int maxAreaOfIsland(int[][] grid) {
    int best = 0;
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[0].length; c++) {
        best = Math.max(best, dfs(grid, r, c));
      }
    }
    return best;
  }

  private int dfs(int[][] grid, int r, int c) {
    if (r < 0 || c < 0 || r == grid.length || c == grid[0].length) return 0;
    if (grid[r][c] == 0) return 0;

    grid[r][c] = 0;
    return 1 + dfs(grid, r + 1, c) + dfs(grid, r - 1, c)
             + dfs(grid, r, c + 1) + dfs(grid, r, c - 1);
  }
}
```

#### 4. Pacific Atlantic Water Flow - [LeetCode 417](https://leetcode.com/problems/pacific-atlantic-water-flow/)

Given a matrix of heights, return all cells from which water can flow to both the Pacific and Atlantic oceans.

##### Optimized / Iterative Solution

```java
public static class Q04Optimized {
  public List<List<Integer>> pacificAtlantic(int[][] heights) {
    int rows = heights.length, cols = heights[0].length;
    boolean[][] pacific = new boolean[rows][cols];
    boolean[][] atlantic = new boolean[rows][cols];
    Deque<int[]> pQueue = new ArrayDeque<>();
    Deque<int[]> aQueue = new ArrayDeque<>();

    for (int r = 0; r < rows; r++) {
      add(pacific, pQueue, r, 0);
      add(atlantic, aQueue, r, cols - 1);
    }
    for (int c = 0; c < cols; c++) {
      add(pacific, pQueue, 0, c);
      add(atlantic, aQueue, rows - 1, c);
    }

    bfs(heights, pQueue, pacific);
    bfs(heights, aQueue, atlantic);

    List<List<Integer>> answer = new ArrayList<>();
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (pacific[r][c] && atlantic[r][c]) answer.add(Arrays.asList(r, c));
      }
    }
    return answer;
  }

  private void add(boolean[][] seen, Deque<int[]> queue, int r, int c) {
    if (!seen[r][c]) {
      seen[r][c] = true;
      queue.offer(new int[] {r, c});
    }
  }

  private void bfs(int[][] heights, Deque<int[]> queue, boolean[][] seen) {
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!queue.isEmpty()) {
      int[] cell = queue.poll();
      for (int[] d : dirs) {
        int nr = cell[0] + d[0], nc = cell[1] + d[1];
        if (nr < 0 || nc < 0 || nr == heights.length || nc == heights[0].length) continue;
        if (seen[nr][nc] || heights[nr][nc] < heights[cell[0]][cell[1]]) continue;
        seen[nr][nc] = true;
        queue.offer(new int[] {nr, nc});
      }
    }
  }
}
```

##### Recursive Solution

```java
public static class Q04Recursive {
  public List<List<Integer>> pacificAtlantic(int[][] heights) {
    int rows = heights.length, cols = heights[0].length;
    boolean[][] pacific = new boolean[rows][cols];
    boolean[][] atlantic = new boolean[rows][cols];

    for (int r = 0; r < rows; r++) {
      dfs(heights, r, 0, Integer.MIN_VALUE, pacific);
      dfs(heights, r, cols - 1, Integer.MIN_VALUE, atlantic);
    }
    for (int c = 0; c < cols; c++) {
      dfs(heights, 0, c, Integer.MIN_VALUE, pacific);
      dfs(heights, rows - 1, c, Integer.MIN_VALUE, atlantic);
    }

    List<List<Integer>> answer = new ArrayList<>();
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (pacific[r][c] && atlantic[r][c]) answer.add(Arrays.asList(r, c));
      }
    }
    return answer;
  }

  private void dfs(int[][] heights, int r, int c, int previous, boolean[][] seen) {
    if (r < 0 || c < 0 || r == heights.length || c == heights[0].length) return;
    if (seen[r][c] || heights[r][c] < previous) return;

    seen[r][c] = true;
    dfs(heights, r + 1, c, heights[r][c], seen);
    dfs(heights, r - 1, c, heights[r][c], seen);
    dfs(heights, r, c + 1, heights[r][c], seen);
    dfs(heights, r, c - 1, heights[r][c], seen);
  }
}
```

#### 5. Surrounded Regions - [LeetCode 130](https://leetcode.com/problems/surrounded-regions/)

Given an m x n board of X and O, capture every O region fully surrounded by X by changing it to X.

##### Optimized / Iterative Solution

```java
public static class Q05Optimized {
  public void solve(char[][] board) {
    int rows = board.length, cols = board[0].length;
    Deque<int[]> queue = new ArrayDeque<>();

    for (int r = 0; r < rows; r++) {
      offer(board, queue, r, 0);
      offer(board, queue, r, cols - 1);
    }
    for (int c = 0; c < cols; c++) {
      offer(board, queue, 0, c);
      offer(board, queue, rows - 1, c);
    }

    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!queue.isEmpty()) {
      int[] cell = queue.poll();
      for (int[] d : dirs) {
        offer(board, queue, cell[0] + d[0], cell[1] + d[1]);
      }
    }

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (board[r][c] == 'O') board[r][c] = 'X';
        if (board[r][c] == '#') board[r][c] = 'O';
      }
    }
  }

  private void offer(char[][] board, Deque<int[]> queue, int r, int c) {
    if (r < 0 || c < 0 || r == board.length || c == board[0].length) return;
    if (board[r][c] != 'O') return;
    board[r][c] = '#';
    queue.offer(new int[] {r, c});
  }
}
```

##### Recursive Solution

```java
public static class Q05Recursive {
  public void solve(char[][] board) {
    int rows = board.length, cols = board[0].length;
    for (int r = 0; r < rows; r++) {
      mark(board, r, 0);
      mark(board, r, cols - 1);
    }
    for (int c = 0; c < cols; c++) {
      mark(board, 0, c);
      mark(board, rows - 1, c);
    }

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (board[r][c] == 'O') board[r][c] = 'X';
        if (board[r][c] == '#') board[r][c] = 'O';
      }
    }
  }

  private void mark(char[][] board, int r, int c) {
    if (r < 0 || c < 0 || r == board.length || c == board[0].length) return;
    if (board[r][c] != 'O') return;
    board[r][c] = '#';
    mark(board, r + 1, c);
    mark(board, r - 1, c);
    mark(board, r, c + 1);
    mark(board, r, c - 1);
  }
}
```

#### 6. Rotting Oranges - [LeetCode 994](https://leetcode.com/problems/rotting-oranges/)

Given a grid containing empty cells, fresh oranges, and rotten oranges, return the minimum minutes until no fresh orange remains, or -1 if impossible.

##### Optimized / Iterative Solution

```java
public static class Q06Optimized {
  public int orangesRotting(int[][] grid) {
    Deque<int[]> queue = new ArrayDeque<>();
    int fresh = 0;
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[0].length; c++) {
        if (grid[r][c] == 2) queue.offer(new int[] {r, c});
        if (grid[r][c] == 1) fresh++;
      }
    }

    int minutes = 0;
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (fresh > 0 && !queue.isEmpty()) {
      int size = queue.size();
      for (int i = 0; i < size; i++) {
        int[] cell = queue.poll();
        for (int[] d : dirs) {
          int nr = cell[0] + d[0], nc = cell[1] + d[1];
          if (nr < 0 || nc < 0 || nr == grid.length || nc == grid[0].length) continue;
          if (grid[nr][nc] != 1) continue;
          grid[nr][nc] = 2;
          fresh--;
          queue.offer(new int[] {nr, nc});
        }
      }
      minutes++;
    }
    return fresh == 0 ? minutes : -1;
  }
}
```

##### Recursive Solution

```java
public static class Q06Recursive {
  private final int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

  public int orangesRotting(int[][] grid) {
    Deque<int[]> queue = new ArrayDeque<>();
    int fresh = 0;
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[0].length; c++) {
        if (grid[r][c] == 2) queue.offer(new int[] {r, c});
        if (grid[r][c] == 1) fresh++;
      }
    }
    return rotLayer(grid, queue, fresh, 0);
  }

  private int rotLayer(int[][] grid, Deque<int[]> queue, int fresh, int minutes) {
    if (fresh == 0) return minutes;
    if (queue.isEmpty()) return -1;

    int size = queue.size();
    for (int i = 0; i < size; i++) {
      int[] cell = queue.poll();
      for (int[] d : dirs) {
        int nr = cell[0] + d[0], nc = cell[1] + d[1];
        if (nr < 0 || nc < 0 || nr == grid.length || nc == grid[0].length) continue;
        if (grid[nr][nc] != 1) continue;
        grid[nr][nc] = 2;
        fresh--;
        queue.offer(new int[] {nr, nc});
      }
    }
    return rotLayer(grid, queue, fresh, minutes + 1);
  }
}
```

#### 7. Walls and Gates - [LeetCode 286](https://leetcode.com/problems/walls-and-gates/)

Given rooms marked as walls, gates, and empty rooms, fill each empty room with its distance to the nearest gate.

##### Optimized / Iterative Solution

```java
public static class Q07Optimized {
  private static final int INF = 2147483647;

  public void wallsAndGates(int[][] rooms) {
    Deque<int[]> queue = new ArrayDeque<>();
    for (int r = 0; r < rooms.length; r++) {
      for (int c = 0; c < rooms[0].length; c++) {
        if (rooms[r][c] == 0) queue.offer(new int[] {r, c});
      }
    }

    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!queue.isEmpty()) {
      int[] cell = queue.poll();
      for (int[] d : dirs) {
        int nr = cell[0] + d[0], nc = cell[1] + d[1];
        if (nr < 0 || nc < 0 || nr == rooms.length || nc == rooms[0].length) continue;
        if (rooms[nr][nc] != INF) continue;
        rooms[nr][nc] = rooms[cell[0]][cell[1]] + 1;
        queue.offer(new int[] {nr, nc});
      }
    }
  }
}
```

##### Recursive Solution

```java
public static class Q07Recursive {
  public void wallsAndGates(int[][] rooms) {
    for (int r = 0; r < rooms.length; r++) {
      for (int c = 0; c < rooms[0].length; c++) {
        if (rooms[r][c] == 0) fill(rooms, r, c, 0);
      }
    }
  }

  private void fill(int[][] rooms, int r, int c, int distance) {
    if (r < 0 || c < 0 || r == rooms.length || c == rooms[0].length) return;
    if (rooms[r][c] < distance) return;

    rooms[r][c] = distance;
    fill(rooms, r + 1, c, distance + 1);
    fill(rooms, r - 1, c, distance + 1);
    fill(rooms, r, c + 1, distance + 1);
    fill(rooms, r, c - 1, distance + 1);
  }
}
```

#### 8. Course Schedule - [LeetCode 207](https://leetcode.com/problems/course-schedule/)

Given numCourses and prerequisite pairs, determine whether it is possible to finish every course.

##### Optimized / Iterative Solution

```java
public static class Q08Optimized {
  public boolean canFinish(int numCourses, int[][] prerequisites) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
    int[] indegree = new int[numCourses];

    for (int[] edge : prerequisites) {
      graph.get(edge[1]).add(edge[0]);
      indegree[edge[0]]++;
    }

    Deque<Integer> queue = new ArrayDeque<>();
    for (int i = 0; i < numCourses; i++) {
      if (indegree[i] == 0) queue.offer(i);
    }

    int taken = 0;
    while (!queue.isEmpty()) {
      int course = queue.poll();
      taken++;
      for (int next : graph.get(course)) {
        if (--indegree[next] == 0) queue.offer(next);
      }
    }
    return taken == numCourses;
  }
}
```

##### Recursive Solution

```java
public static class Q08Recursive {
  public boolean canFinish(int numCourses, int[][] prerequisites) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
    for (int[] edge : prerequisites) graph.get(edge[1]).add(edge[0]);

    int[] state = new int[numCourses];
    for (int course = 0; course < numCourses; course++) {
      if (hasCycle(course, graph, state)) return false;
    }
    return true;
  }

  private boolean hasCycle(int course, List<List<Integer>> graph, int[] state) {
    if (state[course] == 1) return true;
    if (state[course] == 2) return false;

    state[course] = 1;
    for (int next : graph.get(course)) {
      if (hasCycle(next, graph, state)) return true;
    }
    state[course] = 2;
    return false;
  }
}
```

#### 9. Course Schedule II - [LeetCode 210](https://leetcode.com/problems/course-schedule-ii/)

Given numCourses and prerequisites, return one valid order to finish all courses, or an empty array if impossible.

##### Optimized / Iterative Solution

```java
public static class Q09Optimized {
  public int[] findOrder(int numCourses, int[][] prerequisites) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
    int[] indegree = new int[numCourses];

    for (int[] edge : prerequisites) {
      graph.get(edge[1]).add(edge[0]);
      indegree[edge[0]]++;
    }

    Deque<Integer> queue = new ArrayDeque<>();
    for (int i = 0; i < numCourses; i++) {
      if (indegree[i] == 0) queue.offer(i);
    }

    int[] order = new int[numCourses];
    int index = 0;
    while (!queue.isEmpty()) {
      int course = queue.poll();
      order[index++] = course;
      for (int next : graph.get(course)) {
        if (--indegree[next] == 0) queue.offer(next);
      }
    }
    return index == numCourses ? order : new int[0];
  }
}
```

##### Recursive Solution

```java
public static class Q09Recursive {
  public int[] findOrder(int numCourses, int[][] prerequisites) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
    for (int[] edge : prerequisites) graph.get(edge[1]).add(edge[0]);

    int[] state = new int[numCourses];
    List<Integer> postorder = new ArrayList<>();
    for (int course = 0; course < numCourses; course++) {
      if (!dfs(course, graph, state, postorder)) return new int[0];
    }

    Collections.reverse(postorder);
    int[] order = new int[numCourses];
    for (int i = 0; i < numCourses; i++) order[i] = postorder.get(i);
    return order;
  }

  private boolean dfs(int course, List<List<Integer>> graph, int[] state, List<Integer> postorder) {
    if (state[course] == 1) return false;
    if (state[course] == 2) return true;

    state[course] = 1;
    for (int next : graph.get(course)) {
      if (!dfs(next, graph, state, postorder)) return false;
    }
    state[course] = 2;
    postorder.add(course);
    return true;
  }
}
```

#### 10. Graph Valid Tree - [LeetCode 261](https://leetcode.com/problems/graph-valid-tree/)

Given n nodes labeled 0 to n - 1 and undirected edges, return true if the edges form exactly one valid tree.

##### Optimized / Iterative Solution

```java
public static class Q10Optimized {
  public boolean validTree(int n, int[][] edges) {
    if (edges.length != n - 1) return false;
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
    for (int[] edge : edges) {
      graph.get(edge[0]).add(edge[1]);
      graph.get(edge[1]).add(edge[0]);
    }

    boolean[] seen = new boolean[n];
    Deque<Integer> queue = new ArrayDeque<>();
    queue.offer(0);
    seen[0] = true;
    int visited = 0;

    while (!queue.isEmpty()) {
      int node = queue.poll();
      visited++;
      for (int next : graph.get(node)) {
        if (seen[next]) continue;
        seen[next] = true;
        queue.offer(next);
      }
    }
    return visited == n;
  }
}
```

##### Recursive Solution

```java
public static class Q10Recursive {
  public boolean validTree(int n, int[][] edges) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
    for (int[] edge : edges) {
      graph.get(edge[0]).add(edge[1]);
      graph.get(edge[1]).add(edge[0]);
    }

    boolean[] seen = new boolean[n];
    if (hasCycle(0, -1, graph, seen)) return false;
    for (boolean value : seen) {
      if (!value) return false;
    }
    return true;
  }

  private boolean hasCycle(int node, int parent, List<List<Integer>> graph, boolean[] seen) {
    if (seen[node]) return true;
    seen[node] = true;
    for (int next : graph.get(node)) {
      if (next == parent) continue;
      if (hasCycle(next, node, graph, seen)) return true;
    }
    return false;
  }
}
```

#### 11. Number of Connected Components in an Undirected Graph - [LeetCode 323](https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/)

Given n nodes and undirected edges, return the number of connected components in the graph.

##### Optimized / Iterative Solution

```java
public static class Q11Optimized {
  public int countComponents(int n, int[][] edges) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
    for (int[] edge : edges) {
      graph.get(edge[0]).add(edge[1]);
      graph.get(edge[1]).add(edge[0]);
    }

    boolean[] seen = new boolean[n];
    int components = 0;
    for (int node = 0; node < n; node++) {
      if (seen[node]) continue;
      components++;
      Deque<Integer> stack = new ArrayDeque<>();
      stack.push(node);
      seen[node] = true;
      while (!stack.isEmpty()) {
        int current = stack.pop();
        for (int next : graph.get(current)) {
          if (seen[next]) continue;
          seen[next] = true;
          stack.push(next);
        }
      }
    }
    return components;
  }
}
```

##### Recursive Solution

```java
public static class Q11Recursive {
  public int countComponents(int n, int[][] edges) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
    for (int[] edge : edges) {
      graph.get(edge[0]).add(edge[1]);
      graph.get(edge[1]).add(edge[0]);
    }

    boolean[] seen = new boolean[n];
    int components = 0;
    for (int node = 0; node < n; node++) {
      if (!seen[node]) {
        components++;
        dfs(node, graph, seen);
      }
    }
    return components;
  }

  private void dfs(int node, List<List<Integer>> graph, boolean[] seen) {
    seen[node] = true;
    for (int next : graph.get(node)) {
      if (!seen[next]) dfs(next, graph, seen);
    }
  }
}
```

#### 12. Redundant Connection - [LeetCode 684](https://leetcode.com/problems/redundant-connection/)

Given edges of an undirected graph that started as a tree with one extra edge, return the edge that can be removed to make the graph a tree again.

##### Optimized / Iterative Solution

```java
public static class Q12Optimized {
  public int[] findRedundantConnection(int[][] edges) {
    int n = edges.length;
    int[] parent = new int[n + 1];
    int[] rank = new int[n + 1];
    for (int i = 1; i <= n; i++) parent[i] = i;

    for (int[] edge : edges) {
      if (!union(edge[0], edge[1], parent, rank)) return edge;
    }
    return new int[0];
  }

  private boolean union(int a, int b, int[] parent, int[] rank) {
    int rootA = find(a, parent);
    int rootB = find(b, parent);
    if (rootA == rootB) return false;
    if (rank[rootA] < rank[rootB]) parent[rootA] = rootB;
    else if (rank[rootA] > rank[rootB]) parent[rootB] = rootA;
    else {
      parent[rootB] = rootA;
      rank[rootA]++;
    }
    return true;
  }

  private int find(int node, int[] parent) {
    while (node != parent[node]) {
      parent[node] = parent[parent[node]];
      node = parent[node];
    }
    return node;
  }
}
```

##### Recursive Solution

```java
public static class Q12Recursive {
  public int[] findRedundantConnection(int[][] edges) {
    Map<Integer, List<Integer>> graph = new HashMap<>();
    for (int[] edge : edges) {
      if (hasPath(edge[0], edge[1], graph, new HashSet<>())) return edge;
      graph.computeIfAbsent(edge[0], unused -> new ArrayList<>()).add(edge[1]);
      graph.computeIfAbsent(edge[1], unused -> new ArrayList<>()).add(edge[0]);
    }
    return new int[0];
  }

  private boolean hasPath(int node, int target, Map<Integer, List<Integer>> graph, Set<Integer> seen) {
    if (node == target) return true;
    if (!seen.add(node)) return false;
    for (int next : graph.getOrDefault(node, Collections.emptyList())) {
      if (hasPath(next, target, graph, seen)) return true;
    }
    return false;
  }
}
```

## Topic 27: Segment Tree / Fenwick

### Basic (1-12)

#### 1. Range Sum Query Mutable - [LeetCode 307](https://leetcode.com/problems/range-sum-query-mutable/)

Design NumArray with update(index,val) and sumRange(left,right), where sumRange returns the current sum of nums[left..right].

##### Optimized / Iterative Solution

```java
public static class Q01Optimized {
  private final int[] nums;
  private final int[] tree;

  public Q01Optimized(int[] nums) {
    this.nums = nums.clone();
    this.tree = new int[nums.length + 1];
    for (int i = 0; i < nums.length; i++) add(i + 1, nums[i]);
  }

  public void update(int index, int val) {
    int delta = val - nums[index];
    nums[index] = val;
    add(index + 1, delta);
  }

  public int sumRange(int left, int right) {
    return prefix(right + 1) - prefix(left);
  }

  private void add(int index, int delta) {
    while (index < tree.length) {
      tree[index] += delta;
      index += index & -index;
    }
  }

  private int prefix(int index) {
    int sum = 0;
    while (index > 0) {
      sum += tree[index];
      index -= index & -index;
    }
    return sum;
  }
}
```

##### Recursive Solution

```java
public static class Q01Recursive {
  private final int n;
  private final int[] tree;

  public Q01Recursive(int[] nums) {
    n = nums.length;
    tree = new int[4 * n];
    build(nums, 1, 0, n - 1);
  }

  public void update(int index, int val) {
    update(1, 0, n - 1, index, val);
  }

  public int sumRange(int left, int right) {
    return query(1, 0, n - 1, left, right);
  }

  private void build(int[] nums, int node, int left, int right) {
    if (left == right) {
      tree[node] = nums[left];
      return;
    }
    int mid = left + (right - left) / 2;
    build(nums, node * 2, left, mid);
    build(nums, node * 2 + 1, mid + 1, right);
    tree[node] = tree[node * 2] + tree[node * 2 + 1];
  }

  private void update(int node, int left, int right, int index, int val) {
    if (left == right) {
      tree[node] = val;
      return;
    }
    int mid = left + (right - left) / 2;
    if (index <= mid) update(node * 2, left, mid, index, val);
    else update(node * 2 + 1, mid + 1, right, index, val);
    tree[node] = tree[node * 2] + tree[node * 2 + 1];
  }

  private int query(int node, int left, int right, int ql, int qr) {
    if (qr < left || right < ql) return 0;
    if (ql <= left && right <= qr) return tree[node];
    int mid = left + (right - left) / 2;
    return query(node * 2, left, mid, ql, qr) + query(node * 2 + 1, mid + 1, right, ql, qr);
  }
}
```

#### 2. Range Sum Query 2D Mutable - [LeetCode 308](https://leetcode.com/problems/range-sum-query-2d-mutable/)

Design NumMatrix with update(row,col,val) and sumRegion(row1,col1,row2,col2), returning the current rectangle sum.

##### Optimized / Iterative Solution

```java
public static class Q02Optimized {
  private final int rows;
  private final int cols;
  private final int[][] nums;
  private final int[][] tree;

  public Q02Optimized(int[][] matrix) {
    rows = matrix.length;
    cols = matrix[0].length;
    nums = new int[rows][cols];
    tree = new int[rows + 1][cols + 1];
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) update(r, c, matrix[r][c]);
    }
  }

  public void update(int row, int col, int val) {
    int delta = val - nums[row][col];
    nums[row][col] = val;
    for (int r = row + 1; r <= rows; r += r & -r) {
      for (int c = col + 1; c <= cols; c += c & -c) tree[r][c] += delta;
    }
  }

  public int sumRegion(int row1, int col1, int row2, int col2) {
    return prefix(row2 + 1, col2 + 1) - prefix(row1, col2 + 1) - prefix(row2 + 1, col1) + prefix(row1, col1);
  }

  private int prefix(int row, int col) {
    int sum = 0;
    for (int r = row; r > 0; r -= r & -r) {
      for (int c = col; c > 0; c -= c & -c) sum += tree[r][c];
    }
    return sum;
  }
}
```

##### Recursive Solution

```java
public static class Q02Recursive {
  private final int[][] matrix;

  public Q02Recursive(int[][] matrix) {
    this.matrix = new int[matrix.length][matrix.length == 0 ? 0 : matrix[0].length];
    for (int row = 0; row < matrix.length; row++) {
      this.matrix[row] = matrix[row].clone();
    }
  }

  public void update(int row, int col, int val) {
    matrix[row][col] = val;
  }

  public int sumRegion(int row1, int col1, int row2, int col2) {
    return sumRows(row1, row2, col1, col2);
  }

  private int sumRows(int row, int row2, int col1, int col2) {
    if (row > row2) return 0;
    return sumCols(row, col1, col2) + sumRows(row + 1, row2, col1, col2);
  }

  private int sumCols(int row, int col, int col2) {
    if (col > col2) return 0;
    return matrix[row][col] + sumCols(row, col + 1, col2);
  }
}
```

#### 3. Count of Smaller Numbers After Self - [LeetCode 315](https://leetcode.com/problems/count-of-smaller-numbers-after-self/)

Given nums, return counts where counts[i] is the number of smaller elements to the right of nums[i].

##### Optimized / Iterative Solution

```java
public static class Q03Optimized {
  public List<Integer> countSmaller(int[] nums) {
    int[] sorted = nums.clone();
    Arrays.sort(sorted);
    Map<Integer, Integer> rank = new HashMap<>();
    int next = 1;
    for (int value : sorted) if (!rank.containsKey(value)) rank.put(value, next++);
    int[] tree = new int[next + 1];
    Integer[] answer = new Integer[nums.length];
    for (int i = nums.length - 1; i >= 0; i--) {
      int r = rank.get(nums[i]);
      answer[i] = query(tree, r - 1);
      add(tree, r, 1);
    }
    return Arrays.asList(answer);
  }

  private void add(int[] tree, int index, int delta) {
    while (index < tree.length) {
      tree[index] += delta;
      index += index & -index;
    }
  }

  private int query(int[] tree, int index) {
    int sum = 0;
    while (index > 0) {
      sum += tree[index];
      index -= index & -index;
    }
    return sum;
  }
}
```

##### Recursive Solution

```java
public static class Q03Recursive {
  public List<Integer> countSmaller(int[] nums) {
    int n = nums.length;
    int[] indexes = new int[n];
    int[] temp = new int[n];
    int[] counts = new int[n];
    for (int i = 0; i < n; i++) indexes[i] = i;
    sort(nums, indexes, temp, counts, 0, n - 1);
    List<Integer> answer = new ArrayList<>();
    for (int count : counts) answer.add(count);
    return answer;
  }

  private void sort(int[] nums, int[] indexes, int[] temp, int[] counts, int left, int right) {
    if (left >= right) return;
    int mid = left + (right - left) / 2;
    sort(nums, indexes, temp, counts, left, mid);
    sort(nums, indexes, temp, counts, mid + 1, right);
    merge(nums, indexes, temp, counts, left, mid, right);
  }

  private void merge(int[] nums, int[] indexes, int[] temp, int[] counts, int left, int mid, int right) {
    int i = left, j = mid + 1, k = left, smaller = 0;
    while (i <= mid && j <= right) {
      if (nums[indexes[j]] < nums[indexes[i]]) {
        temp[k++] = indexes[j++];
        smaller++;
      } else {
        counts[indexes[i]] += smaller;
        temp[k++] = indexes[i++];
      }
    }
    while (i <= mid) {
      counts[indexes[i]] += smaller;
      temp[k++] = indexes[i++];
    }
    while (j <= right) temp[k++] = indexes[j++];
    for (int p = left; p <= right; p++) indexes[p] = temp[p];
  }
}
```

#### 4. Reverse Pairs - [LeetCode 493](https://leetcode.com/problems/reverse-pairs/)

Given nums, count pairs i < j where nums[i] > 2 * nums[j].

##### Optimized / Iterative Solution

```java
public static class Q04Optimized {
  public int reversePairs(int[] nums) {
    TreeSet<Long> values = new TreeSet<>();
    for (int value : nums) {
      values.add((long) value);
      values.add(2L * value);
    }
    Map<Long, Integer> rank = new HashMap<>();
    int id = 1;
    for (long value : values) rank.put(value, id++);
    int[] tree = new int[id + 1];
    int pairs = 0;
    for (int i = 0; i < nums.length; i++) {
      int notGreater = query(tree, rank.get(2L * nums[i]));
      pairs += i - notGreater;
      add(tree, rank.get((long) nums[i]), 1);
    }
    return pairs;
  }

  private void add(int[] tree, int index, int delta) {
    while (index < tree.length) {
      tree[index] += delta;
      index += index & -index;
    }
  }

  private int query(int[] tree, int index) {
    int sum = 0;
    while (index > 0) {
      sum += tree[index];
      index -= index & -index;
    }
    return sum;
  }
}
```

##### Recursive Solution

```java
public static class Q04Recursive {
  public int reversePairs(int[] nums) {
    return sort(nums, new int[nums.length], 0, nums.length - 1);
  }

  private int sort(int[] nums, int[] temp, int left, int right) {
    if (left >= right) return 0;
    int mid = left + (right - left) / 2;
    int count = sort(nums, temp, left, mid) + sort(nums, temp, mid + 1, right);
    int j = mid + 1;
    for (int i = left; i <= mid; i++) {
      while (j <= right && (long) nums[i] > 2L * nums[j]) j++;
      count += j - (mid + 1);
    }
    merge(nums, temp, left, mid, right);
    return count;
  }

  private void merge(int[] nums, int[] temp, int left, int mid, int right) {
    int i = left, j = mid + 1, k = left;
    while (i <= mid && j <= right) temp[k++] = nums[i] <= nums[j] ? nums[i++] : nums[j++];
    while (i <= mid) temp[k++] = nums[i++];
    while (j <= right) temp[k++] = nums[j++];
    for (int p = left; p <= right; p++) nums[p] = temp[p];
  }
}
```

#### 5. Create Sorted Array through Instructions - [LeetCode 1649](https://leetcode.com/problems/create-sorted-array-through-instructions/)

Given instructions, insert each value into a sorted array and add min(count strictly less, count strictly greater) to the cost.

##### Optimized / Iterative Solution

```java
public static class Q05Optimized {
  private static final int MOD = 1_000_000_007;

  public int createSortedArray(int[] instructions) {
    int max = 0;
    for (int value : instructions) max = Math.max(max, value);
    int[] tree = new int[max + 2];
    long cost = 0;
    for (int i = 0; i < instructions.length; i++) {
      int value = instructions[i];
      int less = query(tree, value - 1);
      int greater = i - query(tree, value);
      cost = (cost + Math.min(less, greater)) % MOD;
      add(tree, value, 1);
    }
    return (int) cost;
  }

  private void add(int[] tree, int index, int delta) {
    while (index < tree.length) {
      tree[index] += delta;
      index += index & -index;
    }
  }

  private int query(int[] tree, int index) {
    int sum = 0;
    while (index > 0) {
      sum += tree[index];
      index -= index & -index;
    }
    return sum;
  }
}
```

##### Recursive Solution

```java
public static class Q05Recursive {
  private static final int MOD = 1_000_000_007;
  private int[] tree;

  public int createSortedArray(int[] instructions) {
    int max = 0;
    for (int value : instructions) max = Math.max(max, value);
    tree = new int[4 * (max + 1)];
    long cost = 0;
    for (int i = 0; i < instructions.length; i++) {
      int value = instructions[i];
      int less = query(1, 1, max, 1, value - 1);
      int greater = i - query(1, 1, max, 1, value);
      cost = (cost + Math.min(less, greater)) % MOD;
      update(1, 1, max, value);
    }
    return (int) cost;
  }

  private void update(int node, int left, int right, int index) {
    if (left == right) {
      tree[node]++;
      return;
    }
    int mid = left + (right - left) / 2;
    if (index <= mid) update(node * 2, left, mid, index);
    else update(node * 2 + 1, mid + 1, right, index);
    tree[node] = tree[node * 2] + tree[node * 2 + 1];
  }

  private int query(int node, int left, int right, int ql, int qr) {
    if (ql > qr || qr < left || right < ql) return 0;
    if (ql <= left && right <= qr) return tree[node];
    int mid = left + (right - left) / 2;
    return query(node * 2, left, mid, ql, qr) + query(node * 2 + 1, mid + 1, right, ql, qr);
  }
}
```

#### 6. Count of Range Sum - [LeetCode 327](https://leetcode.com/problems/count-of-range-sum/)

Given nums and bounds lower and upper, count range sums nums[i..j] whose sum lies in [lower, upper].

##### Optimized / Iterative Solution

```java
public static class Q06Optimized {
  public int countRangeSum(int[] nums, int lower, int upper) {
    long[] prefix = new long[nums.length + 1];
    for (int i = 0; i < nums.length; i++) prefix[i + 1] = prefix[i] + nums[i];
    long[] sorted = prefix.clone();
    Arrays.sort(sorted);
    sorted = unique(sorted);
    int[] tree = new int[sorted.length + 1];
    int count = 0;
    add(tree, rank(sorted, 0), 1);
    for (int i = 1; i < prefix.length; i++) {
      long leftValue = prefix[i] - upper;
      long rightValue = prefix[i] - lower;
      int left = lowerBound(sorted, leftValue) + 1;
      int right = upperBound(sorted, rightValue);
      count += query(tree, right) - query(tree, left - 1);
      add(tree, rank(sorted, prefix[i]), 1);
    }
    return count;
  }

  private long[] unique(long[] values) {
    int size = 0;
    for (long value : values) if (size == 0 || values[size - 1] != value) values[size++] = value;
    return Arrays.copyOf(values, size);
  }

  private int rank(long[] values, long target) {
    return lowerBound(values, target) + 1;
  }

  private int lowerBound(long[] values, long target) {
    int left = 0, right = values.length;
    while (left < right) {
      int mid = left + (right - left) / 2;
      if (values[mid] < target) left = mid + 1;
      else right = mid;
    }
    return left;
  }

  private int upperBound(long[] values, long target) {
    int left = 0, right = values.length;
    while (left < right) {
      int mid = left + (right - left) / 2;
      if (values[mid] <= target) left = mid + 1;
      else right = mid;
    }
    return left;
  }

  private void add(int[] tree, int index, int delta) {
    while (index < tree.length) {
      tree[index] += delta;
      index += index & -index;
    }
  }

  private int query(int[] tree, int index) {
    int sum = 0;
    while (index > 0) {
      sum += tree[index];
      index -= index & -index;
    }
    return sum;
  }
}
```

##### Recursive Solution

```java
public static class Q06Recursive {
  public int countRangeSum(int[] nums, int lower, int upper) {
    long[] prefix = new long[nums.length + 1];
    for (int i = 0; i < nums.length; i++) prefix[i + 1] = prefix[i] + nums[i];
    return sort(prefix, new long[prefix.length], 0, prefix.length - 1, lower, upper);
  }

  private int sort(long[] sums, long[] temp, int left, int right, int lower, int upper) {
    if (left >= right) return 0;
    int mid = left + (right - left) / 2;
    int count = sort(sums, temp, left, mid, lower, upper) + sort(sums, temp, mid + 1, right, lower, upper);
    int start = mid + 1, end = mid + 1;
    for (int i = left; i <= mid; i++) {
      while (start <= right && sums[start] - sums[i] < lower) start++;
      while (end <= right && sums[end] - sums[i] <= upper) end++;
      count += end - start;
    }
    merge(sums, temp, left, mid, right);
    return count;
  }

  private void merge(long[] sums, long[] temp, int left, int mid, int right) {
    int i = left, j = mid + 1, k = left;
    while (i <= mid && j <= right) temp[k++] = sums[i] <= sums[j] ? sums[i++] : sums[j++];
    while (i <= mid) temp[k++] = sums[i++];
    while (j <= right) temp[k++] = sums[j++];
    for (int p = left; p <= right; p++) sums[p] = temp[p];
  }
}
```

#### 7. Range Module - [LeetCode 715](https://leetcode.com/problems/range-module/)

Design RangeModule with addRange, queryRange, and removeRange over half-open intervals [left,right).

##### Optimized / Iterative Solution

```java
public static class Q07Optimized {
  private final TreeMap<Integer, Integer> ranges = new TreeMap<>();

  public void addRange(int left, int right) {
    Integer start = ranges.floorKey(left);
    if (start != null && ranges.get(start) >= left) {
      left = start;
      right = Math.max(right, ranges.remove(start));
    }
    Integer next = ranges.ceilingKey(left);
    while (next != null && next <= right) {
      right = Math.max(right, ranges.remove(next));
      next = ranges.ceilingKey(left);
    }
    ranges.put(left, right);
  }

  public boolean queryRange(int left, int right) {
    Integer start = ranges.floorKey(left);
    return start != null && ranges.get(start) >= right;
  }

  public void removeRange(int left, int right) {
    Integer start = ranges.floorKey(left);
    if (start != null && ranges.get(start) > left) {
      int end = ranges.get(start);
      ranges.put(start, left);
      if (end > right) ranges.put(right, end);
    }
    Integer next = ranges.ceilingKey(left);
    while (next != null && next < right) {
      int end = ranges.remove(next);
      if (end > right) {
        ranges.put(right, end);
        break;
      }
      next = ranges.ceilingKey(left);
    }
  }
}
```

##### Recursive Solution

```java
public static class Q07Recursive {
  private final List<int[]> ranges = new ArrayList<>();

  public void addRange(int left, int right) {
    ranges.add(new int[]{left, right});
    ranges.sort((a, b) -> Integer.compare(a[0], b[0]));
    List<int[]> merged = new ArrayList<>();
    mergeInto(0, merged);
    ranges.clear();
    ranges.addAll(merged);
  }

  public boolean queryRange(int left, int right) {
    return query(left, right, 0);
  }

  public void removeRange(int left, int right) {
    List<int[]> next = new ArrayList<>();
    removeInto(left, right, 0, next);
    ranges.clear();
    ranges.addAll(next);
  }

  private void mergeInto(int index, List<int[]> merged) {
    if (index == ranges.size()) return;
    int[] range = ranges.get(index);
    if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < range[0]) merged.add(new int[]{range[0], range[1]});
    else merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], range[1]);
    mergeInto(index + 1, merged);
  }

  private boolean query(int left, int right, int index) {
    if (index == ranges.size()) return false;
    int[] range = ranges.get(index);
    return (range[0] <= left && right <= range[1]) || query(left, right, index + 1);
  }

  private void removeInto(int left, int right, int index, List<int[]> next) {
    if (index == ranges.size()) return;
    int[] range = ranges.get(index);
    if (range[1] <= left || right <= range[0]) next.add(range);
    else {
      if (range[0] < left) next.add(new int[]{range[0], left});
      if (right < range[1]) next.add(new int[]{right, range[1]});
    }
    removeInto(left, right, index + 1, next);
  }
}
```

#### 8. My Calendar III - [LeetCode 732](https://leetcode.com/problems/my-calendar-iii/)

Design MyCalendarThree where book(start,end) adds a half-open event [start,end) and returns the maximum number of overlapping events so far.

##### Optimized / Iterative Solution

```java
public static class Q08Optimized {
  private final TreeMap<Integer, Integer> delta = new TreeMap<>();

  public int book(int start, int end) {
    delta.put(start, delta.getOrDefault(start, 0) + 1);
    delta.put(end, delta.getOrDefault(end, 0) - 1);
    int active = 0;
    int best = 0;
    for (int change : delta.values()) {
      active += change;
      best = Math.max(best, active);
    }
    return best;
  }
}
```

##### Recursive Solution

```java
public static class Q08Recursive {
  private static class Node {
    Node left;
    Node right;
    int add;
    int max;
  }

  private final Node root = new Node();
  private static final int LIMIT = 1_000_000_000;

  public int book(int start, int end) {
    update(root, 0, LIMIT, start, end - 1);
    return root.max;
  }

  private void update(Node node, int left, int right, int ql, int qr) {
    if (ql <= left && right <= qr) {
      node.add++;
      node.max++;
      return;
    }
    int mid = left + (right - left) / 2;
    if (ql <= mid) {
      if (node.left == null) node.left = new Node();
      update(node.left, left, mid, ql, qr);
    }
    if (qr > mid) {
      if (node.right == null) node.right = new Node();
      update(node.right, mid + 1, right, ql, qr);
    }
    int leftMax = node.left == null ? 0 : node.left.max;
    int rightMax = node.right == null ? 0 : node.right.max;
    node.max = node.add + Math.max(leftMax, rightMax);
  }
}
```

#### 9. Falling Squares - [LeetCode 699](https://leetcode.com/problems/falling-squares/)

Given falling squares as [left, sideLength], return the maximum stack height after each square lands.

##### Optimized / Iterative Solution

```java
public static class Q09Optimized {
  private int[] max;
  private int[] lazy;

  public List<Integer> fallingSquares(int[][] positions) {
    TreeSet<Integer> coordinates = new TreeSet<>();
    for (int[] p : positions) {
      coordinates.add(p[0]);
      coordinates.add(p[0] + p[1] - 1);
    }
    Map<Integer, Integer> rank = new HashMap<>();
    int index = 0;
    for (int coordinate : coordinates) rank.put(coordinate, index++);
    max = new int[4 * index];
    lazy = new int[4 * index];
    List<Integer> answer = new ArrayList<>();
    int best = 0;
    for (int[] p : positions) {
      int left = rank.get(p[0]);
      int right = rank.get(p[0] + p[1] - 1);
      int height = query(1, 0, index - 1, left, right) + p[1];
      update(1, 0, index - 1, left, right, height);
      best = Math.max(best, height);
      answer.add(best);
    }
    return answer;
  }

  private int query(int node, int left, int right, int ql, int qr) {
    if (qr < left || right < ql) return 0;
    if (ql <= left && right <= qr) return max[node];
    push(node);
    int mid = left + (right - left) / 2;
    return Math.max(query(node * 2, left, mid, ql, qr), query(node * 2 + 1, mid + 1, right, ql, qr));
  }

  private void update(int node, int left, int right, int ql, int qr, int value) {
    if (qr < left || right < ql) return;
    if (ql <= left && right <= qr) {
      max[node] = value;
      lazy[node] = value;
      return;
    }
    push(node);
    int mid = left + (right - left) / 2;
    update(node * 2, left, mid, ql, qr, value);
    update(node * 2 + 1, mid + 1, right, ql, qr, value);
    max[node] = Math.max(max[node * 2], max[node * 2 + 1]);
  }

  private void push(int node) {
    if (lazy[node] == 0) return;
    for (int child : new int[]{node * 2, node * 2 + 1}) {
      max[child] = lazy[node];
      lazy[child] = lazy[node];
    }
    lazy[node] = 0;
  }
}
```

##### Recursive Solution

```java
public static class Q09Recursive {
  public List<Integer> fallingSquares(int[][] positions) {
    List<int[]> intervals = new ArrayList<>();
    List<Integer> answer = new ArrayList<>();
    place(positions, 0, intervals, answer, 0);
    return answer;
  }

  private void place(int[][] positions, int index, List<int[]> intervals, List<Integer> answer, int best) {
    if (index == positions.length) return;
    int left = positions[index][0];
    int right = left + positions[index][1];
    int height = base(intervals, 0, left, right, 0) + positions[index][1];
    intervals.add(new int[]{left, right, height});
    int nextBest = Math.max(best, height);
    answer.add(nextBest);
    place(positions, index + 1, intervals, answer, nextBest);
  }

  private int base(List<int[]> intervals, int index, int left, int right, int best) {
    if (index == intervals.size()) return best;
    int[] interval = intervals.get(index);
    if (left < interval[1] && interval[0] < right) best = Math.max(best, interval[2]);
    return base(intervals, index + 1, left, right, best);
  }
}
```

#### 10. Longest Increasing Subsequence II - [LeetCode 2407](https://leetcode.com/problems/longest-increasing-subsequence-ii/)

Given nums and k, return the length of the longest increasing subsequence where adjacent chosen values differ by at most k.

##### Optimized / Iterative Solution

```java
public static class Q10Optimized {
  private int[] tree;

  public int lengthOfLIS(int[] nums, int k) {
    int max = 0;
    for (int value : nums) max = Math.max(max, value);
    tree = new int[4 * (max + 1)];
    int answer = 0;
    for (int value : nums) {
      int left = Math.max(1, value - k);
      int best = query(1, 1, max, left, value - 1) + 1;
      update(1, 1, max, value, best);
      answer = Math.max(answer, best);
    }
    return answer;
  }

  private int query(int node, int left, int right, int ql, int qr) {
    if (ql > qr || qr < left || right < ql) return 0;
    if (ql <= left && right <= qr) return tree[node];
    int mid = left + (right - left) / 2;
    return Math.max(query(node * 2, left, mid, ql, qr), query(node * 2 + 1, mid + 1, right, ql, qr));
  }

  private void update(int node, int left, int right, int index, int value) {
    if (left == right) {
      tree[node] = Math.max(tree[node], value);
      return;
    }
    int mid = left + (right - left) / 2;
    if (index <= mid) update(node * 2, left, mid, index, value);
    else update(node * 2 + 1, mid + 1, right, index, value);
    tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);
  }
}
```

##### Recursive Solution

```java
public static class Q10Recursive {
  private int[] tree;
  private int maxValue;

  public int lengthOfLIS(int[] nums, int k) {
    for (int value : nums) maxValue = Math.max(maxValue, value);
    tree = new int[4 * (maxValue + 1)];
    return scan(nums, k, 0, 0);
  }

  private int scan(int[] nums, int k, int index, int bestAnswer) {
    if (index == nums.length) return bestAnswer;
    int value = nums[index];
    int best = query(1, 1, maxValue, Math.max(1, value - k), value - 1) + 1;
    update(1, 1, maxValue, value, best);
    return scan(nums, k, index + 1, Math.max(bestAnswer, best));
  }

  private int query(int node, int left, int right, int ql, int qr) {
    if (ql > qr || qr < left || right < ql) return 0;
    if (ql <= left && right <= qr) return tree[node];
    int mid = left + (right - left) / 2;
    return Math.max(query(node * 2, left, mid, ql, qr), query(node * 2 + 1, mid + 1, right, ql, qr));
  }

  private void update(int node, int left, int right, int index, int value) {
    if (left == right) {
      tree[node] = Math.max(tree[node], value);
      return;
    }
    int mid = left + (right - left) / 2;
    if (index <= mid) update(node * 2, left, mid, index, value);
    else update(node * 2 + 1, mid + 1, right, index, value);
    tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);
  }
}
```

#### 11. Number of Longest Increasing Subsequence - [LeetCode 673](https://leetcode.com/problems/number-of-longest-increasing-subsequence/)

Given nums, return the number of longest strictly increasing subsequences.

##### Optimized / Iterative Solution

```java
public static class Q11Optimized {
  private int[] bestLength;
  private int[] ways;

  public int findNumberOfLIS(int[] nums) {
    int[] sorted = nums.clone();
    Arrays.sort(sorted);
    int size = 0;
    for (int value : sorted) if (size == 0 || sorted[size - 1] != value) sorted[size++] = value;
    bestLength = new int[4 * size + 4];
    ways = new int[4 * size + 4];
    for (int value : nums) {
      int rank = lowerBound(sorted, size, value) + 1;
      int[] best = query(1, 1, size, 1, rank - 1);
      int length = best[0] + 1;
      int count = best[1] == 0 ? 1 : best[1];
      update(1, 1, size, rank, length, count);
    }
    return ways[1];
  }

  private int lowerBound(int[] values, int size, int target) {
    int left = 0, right = size;
    while (left < right) {
      int mid = left + (right - left) / 2;
      if (values[mid] < target) left = mid + 1;
      else right = mid;
    }
    return left;
  }

  private int[] query(int node, int left, int right, int ql, int qr) {
    if (ql > qr || qr < left || right < ql) return new int[]{0, 0};
    if (ql <= left && right <= qr) return new int[]{bestLength[node], ways[node]};
    int mid = left + (right - left) / 2;
    return merge(query(node * 2, left, mid, ql, qr), query(node * 2 + 1, mid + 1, right, ql, qr));
  }

  private void update(int node, int left, int right, int index, int length, int count) {
    if (left == right) {
      int[] merged = merge(new int[]{bestLength[node], ways[node]}, new int[]{length, count});
      bestLength[node] = merged[0];
      ways[node] = merged[1];
      return;
    }
    int mid = left + (right - left) / 2;
    if (index <= mid) update(node * 2, left, mid, index, length, count);
    else update(node * 2 + 1, mid + 1, right, index, length, count);
    int[] merged = merge(new int[]{bestLength[node * 2], ways[node * 2]}, new int[]{bestLength[node * 2 + 1], ways[node * 2 + 1]});
    bestLength[node] = merged[0];
    ways[node] = merged[1];
  }

  private int[] merge(int[] a, int[] b) {
    if (a[0] == b[0]) return new int[]{a[0], a[1] + b[1]};
    return a[0] > b[0] ? a : b;
  }
}
```

##### Recursive Solution

```java
public static class Q11Recursive {
  private int[] bestLength;
  private int[] ways;
  private int[] values;
  private int size;

  public int findNumberOfLIS(int[] nums) {
    values = nums.clone();
    Arrays.sort(values);
    for (int value : values) if (size == 0 || values[size - 1] != value) values[size++] = value;
    bestLength = new int[4 * size + 4];
    ways = new int[4 * size + 4];
    scan(nums, 0);
    return ways[1];
  }

  private void scan(int[] nums, int index) {
    if (index == nums.length) return;
    int rank = lowerBound(nums[index], 0, size) + 1;
    int[] best = query(1, 1, size, 1, rank - 1);
    update(1, 1, size, rank, best[0] + 1, best[1] == 0 ? 1 : best[1]);
    scan(nums, index + 1);
  }

  private int lowerBound(int target, int left, int right) {
    if (left >= right) return left;
    int mid = left + (right - left) / 2;
    if (values[mid] < target) return lowerBound(target, mid + 1, right);
    return lowerBound(target, left, mid);
  }

  private int[] query(int node, int left, int right, int ql, int qr) {
    if (ql > qr || qr < left || right < ql) return new int[]{0, 0};
    if (ql <= left && right <= qr) return new int[]{bestLength[node], ways[node]};
    int mid = left + (right - left) / 2;
    return merge(query(node * 2, left, mid, ql, qr), query(node * 2 + 1, mid + 1, right, ql, qr));
  }

  private void update(int node, int left, int right, int index, int length, int count) {
    if (left == right) {
      int[] merged = merge(new int[]{bestLength[node], ways[node]}, new int[]{length, count});
      bestLength[node] = merged[0];
      ways[node] = merged[1];
      return;
    }
    int mid = left + (right - left) / 2;
    if (index <= mid) update(node * 2, left, mid, index, length, count);
    else update(node * 2 + 1, mid + 1, right, index, length, count);
    int[] merged = merge(new int[]{bestLength[node * 2], ways[node * 2]}, new int[]{bestLength[node * 2 + 1], ways[node * 2 + 1]});
    bestLength[node] = merged[0];
    ways[node] = merged[1];
  }

  private int[] merge(int[] a, int[] b) {
    if (a[0] == b[0]) return new int[]{a[0], a[1] + b[1]};
    return a[0] > b[0] ? a : b;
  }
}
```

#### 12. Handling Sum Queries After Update - [LeetCode 2569](https://leetcode.com/problems/handling-sum-queries-after-update/)

Given nums1, nums2, and queries, support flipping nums1 bits in a range, adding p times the number of ones in nums1 to the sum of nums2, and reporting the sum.

##### Optimized / Iterative Solution

```java
public static class Q12Optimized {
  private int[] ones;
  private boolean[] lazy;
  private int n;

  public long[] handleQuery(int[] nums1, int[] nums2, int[][] queries) {
    n = nums1.length;
    ones = new int[4 * n];
    lazy = new boolean[4 * n];
    build(nums1, 1, 0, n - 1);
    long sum = 0;
    for (int value : nums2) sum += value;
    List<Long> answer = new ArrayList<>();
    for (int[] query : queries) {
      if (query[0] == 1) flip(1, 0, n - 1, query[1], query[2]);
      else if (query[0] == 2) sum += (long) query[1] * ones[1];
      else answer.add(sum);
    }
    long[] result = new long[answer.size()];
    for (int i = 0; i < answer.size(); i++) result[i] = answer.get(i);
    return result;
  }

  private void build(int[] nums, int node, int left, int right) {
    if (left == right) {
      ones[node] = nums[left];
      return;
    }
    int mid = left + (right - left) / 2;
    build(nums, node * 2, left, mid);
    build(nums, node * 2 + 1, mid + 1, right);
    ones[node] = ones[node * 2] + ones[node * 2 + 1];
  }

  private void flip(int node, int left, int right, int ql, int qr) {
    if (qr < left || right < ql) return;
    if (ql <= left && right <= qr) {
      apply(node, left, right);
      return;
    }
    push(node, left, right);
    int mid = left + (right - left) / 2;
    flip(node * 2, left, mid, ql, qr);
    flip(node * 2 + 1, mid + 1, right, ql, qr);
    ones[node] = ones[node * 2] + ones[node * 2 + 1];
  }

  private void apply(int node, int left, int right) {
    ones[node] = right - left + 1 - ones[node];
    lazy[node] = !lazy[node];
  }

  private void push(int node, int left, int right) {
    if (!lazy[node] || left == right) return;
    int mid = left + (right - left) / 2;
    apply(node * 2, left, mid);
    apply(node * 2 + 1, mid + 1, right);
    lazy[node] = false;
  }
}
```

##### Recursive Solution

```java
public static class Q12Recursive {
  public long[] handleQuery(int[] nums1, int[] nums2, int[][] queries) {
    long sum = 0;
    for (int value : nums2) sum += value;
    List<Long> answer = new ArrayList<>();
    process(nums1, queries, 0, sum, answer);
    long[] result = new long[answer.size()];
    for (int i = 0; i < answer.size(); i++) result[i] = answer.get(i);
    return result;
  }

  private void process(int[] nums1, int[][] queries, int index, long sum, List<Long> answer) {
    if (index == queries.length) return;
    int[] query = queries[index];
    if (query[0] == 1) {
      flip(nums1, query[1], query[2]);
    } else if (query[0] == 2) {
      sum += (long) query[1] * countOnes(nums1, 0);
    } else {
      answer.add(sum);
    }
    process(nums1, queries, index + 1, sum, answer);
  }

  private void flip(int[] nums, int left, int right) {
    if (left > right) return;
    nums[left] ^= 1;
    flip(nums, left + 1, right);
  }

  private int countOnes(int[] nums, int index) {
    if (index == nums.length) return 0;
    return nums[index] + countOnes(nums, index + 1);
  }
}
```

## Topic 28: Advanced Graphs

### Basic (1-12)

#### 1. Network Delay Time - [LeetCode 743](https://leetcode.com/problems/network-delay-time/)

Given directed weighted edges times[i] = [u,v,w], n nodes labeled 1..n, and source k, return the time for all nodes to receive the signal, or -1 if any node is unreachable.

##### Optimized / Iterative Solution

```java
public static class Q01Optimized {
  public int networkDelayTime(int[][] times, int n, int k) {
    List<int[]>[] graph = new ArrayList[n + 1];
    for (int node = 1; node <= n; node++) graph[node] = new ArrayList<>();
    for (int[] edge : times) graph[edge[0]].add(new int[] {edge[1], edge[2]});

    int[] dist = new int[n + 1];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[k] = 0;
    PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
    heap.offer(new int[] {k, 0});

    while (!heap.isEmpty()) {
      int[] current = heap.poll();
      int node = current[0], distance = current[1];
      if (distance != dist[node]) continue;
      for (int[] edge : graph[node]) {
        int next = edge[0], nextDistance = distance + edge[1];
        if (nextDistance < dist[next]) {
          dist[next] = nextDistance;
          heap.offer(new int[] {next, nextDistance});
        }
      }
    }

    int answer = 0;
    for (int node = 1; node <= n; node++) {
      if (dist[node] == Integer.MAX_VALUE) return -1;
      answer = Math.max(answer, dist[node]);
    }
    return answer;
  }
}
```

##### Recursive Solution

```java
public static class Q01Recursive {
  public int networkDelayTime(int[][] times, int n, int k) {
    List<int[]>[] graph = new ArrayList[n + 1];
    for (int node = 1; node <= n; node++) graph[node] = new ArrayList<>();
    for (int[] edge : times) graph[edge[0]].add(new int[] {edge[1], edge[2]});
    for (List<int[]> edges : graph) {
      if (edges != null) edges.sort(Comparator.comparingInt(a -> a[1]));
    }

    int[] dist = new int[n + 1];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dfs(k, 0, graph, dist);

    int answer = 0;
    for (int node = 1; node <= n; node++) {
      if (dist[node] == Integer.MAX_VALUE) return -1;
      answer = Math.max(answer, dist[node]);
    }
    return answer;
  }

  private void dfs(int node, int time, List<int[]>[] graph, int[] dist) {
    if (time >= dist[node]) return;
    dist[node] = time;
    for (int[] edge : graph[node]) dfs(edge[0], time + edge[1], graph, dist);
  }
}
```

#### 2. Cheapest Flights Within K Stops - [LeetCode 787](https://leetcode.com/problems/cheapest-flights-within-k-stops/)

Given flights [from,to,price], find the cheapest price from src to dst using at most k stops, or -1 if no route satisfies the stop limit.

##### Optimized / Iterative Solution

```java
public static class Q02Optimized {
  public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
    final int INF = 1_000_000_000;
    int[] cost = new int[n];
    Arrays.fill(cost, INF);
    cost[src] = 0;

    for (int edges = 0; edges <= k; edges++) {
      int[] next = cost.clone();
      for (int[] flight : flights) {
        if (cost[flight[0]] == INF) continue;
        next[flight[1]] = Math.min(next[flight[1]], cost[flight[0]] + flight[2]);
      }
      cost = next;
    }
    return cost[dst] == INF ? -1 : cost[dst];
  }
}
```

##### Recursive Solution

```java
public static class Q02Recursive {
  private static final int INF = 1_000_000_000;
  private List<int[]>[] graph;
  private int[][] memo;
  private int dst;

  public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
    this.dst = dst;
    graph = new ArrayList[n];
    for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    for (int[] flight : flights) graph[flight[0]].add(new int[] {flight[1], flight[2]});
    memo = new int[n][k + 2];
    for (int[] row : memo) Arrays.fill(row, -2);
    int answer = solve(src, k + 1);
    return answer >= INF ? -1 : answer;
  }

  private int solve(int node, int edgesLeft) {
    if (node == dst) return 0;
    if (edgesLeft == 0) return INF;
    if (memo[node][edgesLeft] != -2) return memo[node][edgesLeft];
    int best = INF;
    for (int[] edge : graph[node]) {
      int suffix = solve(edge[0], edgesLeft - 1);
      if (suffix != INF) best = Math.min(best, edge[1] + suffix);
    }
    return memo[node][edgesLeft] = best;
  }
}
```

#### 3. Path With Minimum Effort - [LeetCode 1631](https://leetcode.com/problems/path-with-minimum-effort/)

Given a heights grid, move 4-directionally from top-left to bottom-right while minimizing the maximum absolute height difference along the path.

##### Optimized / Iterative Solution

```java
public static class Q03Optimized {
  private final int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

  public int minimumEffortPath(int[][] heights) {
    int rows = heights.length, cols = heights[0].length;
    int[][] effort = new int[rows][cols];
    for (int[] row : effort) Arrays.fill(row, Integer.MAX_VALUE);
    effort[0][0] = 0;
    PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
    heap.offer(new int[] {0, 0, 0});

    while (!heap.isEmpty()) {
      int[] current = heap.poll();
      int row = current[0], col = current[1], cost = current[2];
      if (row == rows - 1 && col == cols - 1) return cost;
      if (cost != effort[row][col]) continue;
      for (int[] dir : dirs) {
        int nr = row + dir[0], nc = col + dir[1];
        if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) continue;
        int next = Math.max(cost, Math.abs(heights[row][col] - heights[nr][nc]));
        if (next < effort[nr][nc]) {
          effort[nr][nc] = next;
          heap.offer(new int[] {nr, nc, next});
        }
      }
    }
    return 0;
  }
}
```

##### Recursive Solution

```java
public static class Q03Recursive {
  private final int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

  public int minimumEffortPath(int[][] heights) {
    int left = 0, right = 1_000_000;
    while (left < right) {
      int mid = left + (right - left) / 2;
      boolean[][] seen = new boolean[heights.length][heights[0].length];
      if (canReach(0, 0, mid, heights, seen)) right = mid;
      else left = mid + 1;
    }
    return left;
  }

  private boolean canReach(int row, int col, int limit, int[][] heights, boolean[][] seen) {
    int rows = heights.length, cols = heights[0].length;
    if (row == rows - 1 && col == cols - 1) return true;
    seen[row][col] = true;
    for (int[] dir : dirs) {
      int nr = row + dir[0], nc = col + dir[1];
      if (nr < 0 || nc < 0 || nr >= rows || nc >= cols || seen[nr][nc]) continue;
      if (Math.abs(heights[row][col] - heights[nr][nc]) <= limit && canReach(nr, nc, limit, heights, seen)) return true;
    }
    return false;
  }
}
```

#### 4. Swim in Rising Water - [LeetCode 778](https://leetcode.com/problems/swim-in-rising-water/)

Given an n x n grid where grid[r][c] is the time that cell becomes passable, return the earliest time when a path exists from top-left to bottom-right.

##### Optimized / Iterative Solution

```java
public static class Q04Optimized {
  private final int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

  public int swimInWater(int[][] grid) {
    int n = grid.length;
    boolean[][] seen = new boolean[n][n];
    PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
    heap.offer(new int[] {0, 0, grid[0][0]});
    seen[0][0] = true;

    while (!heap.isEmpty()) {
      int[] current = heap.poll();
      int row = current[0], col = current[1], time = current[2];
      if (row == n - 1 && col == n - 1) return time;
      for (int[] dir : dirs) {
        int nr = row + dir[0], nc = col + dir[1];
        if (nr < 0 || nc < 0 || nr >= n || nc >= n || seen[nr][nc]) continue;
        seen[nr][nc] = true;
        heap.offer(new int[] {nr, nc, Math.max(time, grid[nr][nc])});
      }
    }
    return -1;
  }
}
```

##### Recursive Solution

```java
public static class Q04Recursive {
  private final int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

  public int swimInWater(int[][] grid) {
    int n = grid.length;
    int left = Math.max(grid[0][0], grid[n - 1][n - 1]);
    int right = n * n - 1;
    while (left < right) {
      int mid = left + (right - left) / 2;
      if (canReach(0, 0, mid, grid, new boolean[n][n])) right = mid;
      else left = mid + 1;
    }
    return left;
  }

  private boolean canReach(int row, int col, int time, int[][] grid, boolean[][] seen) {
    int n = grid.length;
    if (row < 0 || col < 0 || row >= n || col >= n || seen[row][col] || grid[row][col] > time) return false;
    if (row == n - 1 && col == n - 1) return true;
    seen[row][col] = true;
    for (int[] dir : dirs) if (canReach(row + dir[0], col + dir[1], time, grid, seen)) return true;
    return false;
  }
}
```

#### 5. Find Critical and Pseudo-Critical Edges in MST - [LeetCode 1489](https://leetcode.com/problems/find-critical-and-pseudo-critical-edges-in-minimum-spanning-tree/)

Given an undirected weighted connected graph, return which edge indices are critical for every MST and which can appear in at least one MST.

##### Optimized / Iterative Solution

```java
public static class Q05Optimized {
  public List<List<Integer>> findCriticalAndPseudoCriticalEdges(int n, int[][] edges) {
    int m = edges.length;
    int[][] indexed = new int[m][4];
    for (int i = 0; i < m; i++) indexed[i] = new int[] {edges[i][0], edges[i][1], edges[i][2], i};
    Arrays.sort(indexed, Comparator.comparingInt(a -> a[2]));
    int base = mst(n, indexed, -1, -1);
    List<Integer> critical = new ArrayList<>();
    List<Integer> pseudo = new ArrayList<>();

    for (int i = 0; i < m; i++) {
      if (mst(n, indexed, i, -1) > base) critical.add(indexed[i][3]);
      else if (mst(n, indexed, -1, i) == base) pseudo.add(indexed[i][3]);
    }
    return Arrays.asList(critical, pseudo);
  }

  private int mst(int n, int[][] edges, int skip, int force) {
    DSU dsu = new DSU(n);
    int cost = 0, used = 0;
    if (force != -1) {
      dsu.union(edges[force][0], edges[force][1]);
      cost += edges[force][2];
      used++;
    }
    for (int i = 0; i < edges.length; i++) {
      if (i == skip) continue;
      if (dsu.union(edges[i][0], edges[i][1])) {
        cost += edges[i][2];
        used++;
        if (used == n - 1) break;
      }
    }
    return used == n - 1 ? cost : Integer.MAX_VALUE;
  }

  private static class DSU {
    int[] parent;
    int[] rank;
    DSU(int n) { parent = new int[n]; rank = new int[n]; for (int i = 0; i < n; i++) parent[i] = i; }
    int find(int x) { while (x != parent[x]) { parent[x] = parent[parent[x]]; x = parent[x]; } return x; }
    boolean union(int a, int b) {
      int pa = find(a), pb = find(b);
      if (pa == pb) return false;
      if (rank[pa] < rank[pb]) parent[pa] = pb;
      else if (rank[pa] > rank[pb]) parent[pb] = pa;
      else { parent[pb] = pa; rank[pa]++; }
      return true;
    }
  }
}
```

##### Recursive Solution

```java
public static class Q05Recursive {
  public List<List<Integer>> findCriticalAndPseudoCriticalEdges(int n, int[][] edges) {
    int m = edges.length;
    int[][] indexed = new int[m][4];
    for (int i = 0; i < m; i++) indexed[i] = new int[] {edges[i][0], edges[i][1], edges[i][2], i};
    Arrays.sort(indexed, Comparator.comparingInt(a -> a[2]));
    int base = mst(n, indexed, -1, -1);
    List<Integer> critical = new ArrayList<>();
    List<Integer> pseudo = new ArrayList<>();
    classify(0, n, indexed, base, critical, pseudo);
    return Arrays.asList(critical, pseudo);
  }

  private void classify(int index, int n, int[][] edges, int base, List<Integer> critical, List<Integer> pseudo) {
    if (index == edges.length) return;
    if (mst(n, edges, index, -1) > base) critical.add(edges[index][3]);
    else if (mst(n, edges, -1, index) == base) pseudo.add(edges[index][3]);
    classify(index + 1, n, edges, base, critical, pseudo);
  }

  private int mst(int n, int[][] edges, int skip, int force) {
    DSU dsu = new DSU(n);
    int cost = 0, used = 0;
    if (force != -1 && dsu.union(edges[force][0], edges[force][1])) { cost += edges[force][2]; used++; }
    for (int i = 0; i < edges.length; i++) {
      if (i != skip && dsu.union(edges[i][0], edges[i][1])) { cost += edges[i][2]; used++; }
    }
    return used == n - 1 ? cost : Integer.MAX_VALUE;
  }

  private static class DSU {
    int[] parent;
    DSU(int n) { parent = new int[n]; for (int i = 0; i < n; i++) parent[i] = i; }
    int find(int x) { if (parent[x] != x) parent[x] = find(parent[x]); return parent[x]; }
    boolean union(int a, int b) { int pa = find(a), pb = find(b); if (pa == pb) return false; parent[pa] = pb; return true; }
  }
}
```

#### 6. Min Cost to Connect All Points - [LeetCode 1584](https://leetcode.com/problems/min-cost-to-connect-all-points/)

Given points on a plane, connect all points with minimum total Manhattan distance cost.

##### Optimized / Iterative Solution

```java
public static class Q06Optimized {
  public int minCostConnectPoints(int[][] points) {
    int n = points.length;
    boolean[] used = new boolean[n];
    int[] min = new int[n];
    Arrays.fill(min, Integer.MAX_VALUE);
    min[0] = 0;
    int answer = 0;

    for (int step = 0; step < n; step++) {
      int node = -1;
      for (int i = 0; i < n; i++) if (!used[i] && (node == -1 || min[i] < min[node])) node = i;
      used[node] = true;
      answer += min[node];
      for (int next = 0; next < n; next++) if (!used[next]) min[next] = Math.min(min[next], distance(points, node, next));
    }
    return answer;
  }

  private int distance(int[][] points, int a, int b) {
    return Math.abs(points[a][0] - points[b][0]) + Math.abs(points[a][1] - points[b][1]);
  }
}
```

##### Recursive Solution

```java
public static class Q06Recursive {
  public int minCostConnectPoints(int[][] points) {
    boolean[] used = new boolean[points.length];
    int[] min = new int[points.length];
    Arrays.fill(min, Integer.MAX_VALUE);
    min[0] = 0;
    return prim(points, used, min, 0, 0);
  }

  private int prim(int[][] points, boolean[] used, int[] min, int chosen, int total) {
    if (chosen == points.length) return total;
    int node = -1;
    for (int i = 0; i < points.length; i++) if (!used[i] && (node == -1 || min[i] < min[node])) node = i;
    used[node] = true;
    for (int next = 0; next < points.length; next++) if (!used[next]) min[next] = Math.min(min[next], distance(points, node, next));
    return prim(points, used, min, chosen + 1, total + min[node]);
  }

  private int distance(int[][] points, int a, int b) {
    return Math.abs(points[a][0] - points[b][0]) + Math.abs(points[a][1] - points[b][1]);
  }
}
```

#### 7. Optimize Water Distribution in a Village - [LeetCode 1168](https://leetcode.com/problems/optimize-water-distribution-in-a-village/)

Given well costs for each house and pipe costs between houses, return the minimum cost to supply water to every house.

##### Optimized / Iterative Solution

```java
public static class Q07Optimized {
  public int minCostToSupplyWater(int n, int[] wells, int[][] pipes) {
    List<int[]>[] graph = new ArrayList[n + 1];
    for (int i = 0; i <= n; i++) graph[i] = new ArrayList<>();
    for (int house = 1; house <= n; house++) {
      graph[0].add(new int[] {house, wells[house - 1]});
      graph[house].add(new int[] {0, wells[house - 1]});
    }
    for (int[] pipe : pipes) {
      graph[pipe[0]].add(new int[] {pipe[1], pipe[2]});
      graph[pipe[1]].add(new int[] {pipe[0], pipe[2]});
    }
    boolean[] used = new boolean[n + 1];
    PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
    heap.offer(new int[] {0, 0});
    int cost = 0, count = 0;
    while (!heap.isEmpty() && count <= n) {
      int[] current = heap.poll();
      int node = current[0];
      if (used[node]) continue;
      used[node] = true;
      cost += current[1];
      count++;
      for (int[] edge : graph[node]) if (!used[edge[0]]) heap.offer(edge);
    }
    return cost;
  }
}
```

##### Recursive Solution

```java
public static class Q07Recursive {
  public int minCostToSupplyWater(int n, int[] wells, int[][] pipes) {
    List<int[]> edges = new ArrayList<>();
    for (int house = 1; house <= n; house++) edges.add(new int[] {0, house, wells[house - 1]});
    for (int[] pipe : pipes) edges.add(pipe);
    edges.sort(Comparator.comparingInt(a -> a[2]));
    return kruskal(0, n, edges, new DSU(n + 1), 0, 0);
  }

  private int kruskal(int index, int n, List<int[]> edges, DSU dsu, int used, int cost) {
    if (used == n) return cost;
    int[] edge = edges.get(index);
    if (dsu.union(edge[0], edge[1])) return kruskal(index + 1, n, edges, dsu, used + 1, cost + edge[2]);
    return kruskal(index + 1, n, edges, dsu, used, cost);
  }

  private static class DSU {
    int[] parent;
    DSU(int n) { parent = new int[n]; for (int i = 0; i < n; i++) parent[i] = i; }
    int find(int x) { if (parent[x] != x) parent[x] = find(parent[x]); return parent[x]; }
    boolean union(int a, int b) { int pa = find(a), pb = find(b); if (pa == pb) return false; parent[pa] = pb; return true; }
  }
}
```

#### 8. Reconstruct Itinerary - [LeetCode 332](https://leetcode.com/problems/reconstruct-itinerary/)

Given airline tickets [from,to], reconstruct the lexicographically smallest itinerary that starts at JFK and uses every ticket exactly once.

##### Optimized / Iterative Solution

```java
public static class Q08Optimized {
  public List<String> findItinerary(List<List<String>> tickets) {
    Map<String, PriorityQueue<String>> graph = new HashMap<>();
    for (List<String> ticket : tickets) graph.computeIfAbsent(ticket.get(0), key -> new PriorityQueue<>()).offer(ticket.get(1));
    Deque<String> stack = new ArrayDeque<>();
    LinkedList<String> route = new LinkedList<>();
    stack.push("JFK");
    while (!stack.isEmpty()) {
      String airport = stack.peek();
      PriorityQueue<String> next = graph.get(airport);
      if (next != null && !next.isEmpty()) stack.push(next.poll());
      else route.addFirst(stack.pop());
    }
    return route;
  }
}
```

##### Recursive Solution

```java
public static class Q08Recursive {
  private final Map<String, PriorityQueue<String>> graph = new HashMap<>();
  private final LinkedList<String> route = new LinkedList<>();

  public List<String> findItinerary(List<List<String>> tickets) {
    for (List<String> ticket : tickets) graph.computeIfAbsent(ticket.get(0), key -> new PriorityQueue<>()).offer(ticket.get(1));
    dfs("JFK");
    return route;
  }

  private void dfs(String airport) {
    PriorityQueue<String> next = graph.get(airport);
    while (next != null && !next.isEmpty()) dfs(next.poll());
    route.addFirst(airport);
  }
}
```

#### 9. Alien Dictionary - [LeetCode 269](https://leetcode.com/problems/alien-dictionary/)

Given sorted words from an alien language, return one valid character order, or empty string if the order is invalid.

##### Optimized / Iterative Solution

```java
public static class Q09Optimized {
  public String alienOrder(String[] words) {
    List<Integer>[] graph = new ArrayList[26];
    for (int i = 0; i < 26; i++) graph[i] = new ArrayList<>();
    int[] indegree = new int[26];
    boolean[] exists = new boolean[26];
    boolean[][] edge = new boolean[26][26];
    int count = 0;
    for (String word : words) for (char ch : word.toCharArray()) if (!exists[ch - 'a']) { exists[ch - 'a'] = true; count++; }
    for (int i = 0; i + 1 < words.length; i++) {
      String a = words[i], b = words[i + 1];
      if (a.length() > b.length() && a.startsWith(b)) return "";
      int j = 0;
      while (j < a.length() && j < b.length() && a.charAt(j) == b.charAt(j)) j++;
      if (j < a.length() && j < b.length()) {
        int from = a.charAt(j) - 'a', to = b.charAt(j) - 'a';
        if (!edge[from][to]) { edge[from][to] = true; graph[from].add(to); indegree[to]++; }
      }
    }
    Queue<Integer> queue = new ArrayDeque<>();
    for (int c = 0; c < 26; c++) if (exists[c] && indegree[c] == 0) queue.offer(c);
    StringBuilder order = new StringBuilder();
    while (!queue.isEmpty()) {
      int current = queue.poll();
      order.append((char) ('a' + current));
      for (int next : graph[current]) if (--indegree[next] == 0) queue.offer(next);
    }
    return order.length() == count ? order.toString() : "";
  }
}
```

##### Recursive Solution

```java
public static class Q09Recursive {
  private List<Integer>[] graph;
  private boolean[] exists;
  private int[] color;
  private StringBuilder order;

  public String alienOrder(String[] words) {
    graph = new ArrayList[26];
    for (int i = 0; i < 26; i++) graph[i] = new ArrayList<>();
    exists = new boolean[26];
    boolean[][] edge = new boolean[26][26];
    for (String word : words) for (char ch : word.toCharArray()) exists[ch - 'a'] = true;
    for (int i = 0; i + 1 < words.length; i++) {
      String a = words[i], b = words[i + 1];
      if (a.length() > b.length() && a.startsWith(b)) return "";
      int j = 0;
      while (j < a.length() && j < b.length() && a.charAt(j) == b.charAt(j)) j++;
      if (j < a.length() && j < b.length()) {
        int from = a.charAt(j) - 'a', to = b.charAt(j) - 'a';
        if (!edge[from][to]) { edge[from][to] = true; graph[from].add(to); }
      }
    }
    color = new int[26];
    order = new StringBuilder();
    for (int c = 0; c < 26; c++) if (exists[c] && color[c] == 0 && !dfs(c)) return "";
    return order.reverse().toString();
  }

  private boolean dfs(int node) {
    color[node] = 1;
    for (int next : graph[node]) {
      if (color[next] == 1) return false;
      if (color[next] == 0 && !dfs(next)) return false;
    }
    color[node] = 2;
    order.append((char) ('a' + node));
    return true;
  }
}
```

#### 10. Course Schedule II - [LeetCode 210](https://leetcode.com/problems/course-schedule-ii/)

Given numCourses and prerequisites [course, prerequisite], return an order to finish all courses or an empty array if impossible.

##### Optimized / Iterative Solution

```java
public static class Q10Optimized {
  public int[] findOrder(int numCourses, int[][] prerequisites) {
    List<Integer>[] graph = new ArrayList[numCourses];
    for (int i = 0; i < numCourses; i++) graph[i] = new ArrayList<>();
    int[] indegree = new int[numCourses];
    for (int[] edge : prerequisites) { graph[edge[1]].add(edge[0]); indegree[edge[0]]++; }
    Queue<Integer> queue = new ArrayDeque<>();
    for (int course = 0; course < numCourses; course++) if (indegree[course] == 0) queue.offer(course);
    int[] order = new int[numCourses];
    int size = 0;
    while (!queue.isEmpty()) {
      int course = queue.poll();
      order[size++] = course;
      for (int next : graph[course]) if (--indegree[next] == 0) queue.offer(next);
    }
    return size == numCourses ? order : new int[0];
  }
}
```

##### Recursive Solution

```java
public static class Q10Recursive {
  private List<Integer>[] graph;
  private int[] color;
  private int[] order;
  private int index;

  public int[] findOrder(int numCourses, int[][] prerequisites) {
    graph = new ArrayList[numCourses];
    for (int i = 0; i < numCourses; i++) graph[i] = new ArrayList<>();
    for (int[] edge : prerequisites) graph[edge[1]].add(edge[0]);
    color = new int[numCourses];
    order = new int[numCourses];
    index = numCourses - 1;
    for (int course = 0; course < numCourses; course++) if (color[course] == 0 && !dfs(course)) return new int[0];
    return order;
  }

  private boolean dfs(int course) {
    color[course] = 1;
    for (int next : graph[course]) {
      if (color[next] == 1) return false;
      if (color[next] == 0 && !dfs(next)) return false;
    }
    color[course] = 2;
    order[index--] = course;
    return true;
  }
}
```

#### 11. Shortest Path Visiting All Nodes - [LeetCode 847](https://leetcode.com/problems/shortest-path-visiting-all-nodes/)

Given an undirected connected graph, return the shortest number of edges needed to visit every node at least once.

##### Optimized / Iterative Solution

```java
public static class Q11Optimized {
  public int shortestPathLength(int[][] graph) {
    int n = graph.length;
    int target = (1 << n) - 1;
    Queue<int[]> queue = new ArrayDeque<>();
    boolean[][] seen = new boolean[n][1 << n];
    for (int node = 0; node < n; node++) {
      int mask = 1 << node;
      queue.offer(new int[] {node, mask, 0});
      seen[node][mask] = true;
    }
    while (!queue.isEmpty()) {
      int[] state = queue.poll();
      if (state[1] == target) return state[2];
      for (int next : graph[state[0]]) {
        int nextMask = state[1] | (1 << next);
        if (!seen[next][nextMask]) {
          seen[next][nextMask] = true;
          queue.offer(new int[] {next, nextMask, state[2] + 1});
        }
      }
    }
    return 0;
  }
}
```

##### Recursive Solution

```java
public static class Q11Recursive {
  private int[][] dist;
  private int[][] memo;
  private int target;

  public int shortestPathLength(int[][] graph) {
    int n = graph.length;
    target = (1 << n) - 1;
    dist = new int[n][n];
    for (int i = 0; i < n; i++) {
      Arrays.fill(dist[i], 1_000_000);
      dist[i][i] = 0;
      for (int next : graph[i]) dist[i][next] = 1;
    }
    for (int k = 0; k < n; k++) for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
    memo = new int[n][1 << n];
    for (int[] row : memo) Arrays.fill(row, -1);
    int best = Integer.MAX_VALUE;
    for (int start = 0; start < n; start++) best = Math.min(best, solve(start, 1 << start));
    return best;
  }

  private int solve(int node, int mask) {
    if (mask == target) return 0;
    if (memo[node][mask] != -1) return memo[node][mask];
    int best = 1_000_000;
    for (int next = 0; next < dist.length; next++) {
      if ((mask & (1 << next)) == 0) best = Math.min(best, dist[node][next] + solve(next, mask | (1 << next)));
    }
    return memo[node][mask] = best;
  }
}
```

#### 12. Minimum Cost to Reach Destination in Time - [LeetCode 1928](https://leetcode.com/problems/minimum-cost-to-reach-destination-in-time/)

Given maxTime, undirected edges [u,v,time], and passingFees per city, return the minimum fee to reach city n-1 within maxTime or -1.

##### Optimized / Iterative Solution

```java
public static class Q12Optimized {
  public int minCost(int maxTime, int[][] edges, int[] passingFees) {
    int n = passingFees.length;
    int[][] dp = new int[maxTime + 1][n];
    for (int[] row : dp) Arrays.fill(row, 1_000_000_000);
    dp[0][0] = passingFees[0];
    for (int time = 1; time <= maxTime; time++) {
      for (int[] edge : edges) {
        int a = edge[0], b = edge[1], t = edge[2];
        if (time >= t) {
          dp[time][a] = Math.min(dp[time][a], dp[time - t][b] + passingFees[a]);
          dp[time][b] = Math.min(dp[time][b], dp[time - t][a] + passingFees[b]);
        }
      }
    }
    int answer = 1_000_000_000;
    for (int time = 0; time <= maxTime; time++) answer = Math.min(answer, dp[time][n - 1]);
    return answer == 1_000_000_000 ? -1 : answer;
  }
}
```

##### Recursive Solution

```java
public static class Q12Recursive {
  private List<int[]>[] graph;
  private int[] fees;
  private int[][] memo;
  private static final int INF = 1_000_000_000;

  public int minCost(int maxTime, int[][] edges, int[] passingFees) {
    fees = passingFees;
    graph = new ArrayList[fees.length];
    for (int i = 0; i < fees.length; i++) graph[i] = new ArrayList<>();
    for (int[] edge : edges) { graph[edge[0]].add(new int[] {edge[1], edge[2]}); graph[edge[1]].add(new int[] {edge[0], edge[2]}); }
    memo = new int[fees.length][maxTime + 1];
    for (int[] row : memo) Arrays.fill(row, -1);
    int suffix = dfs(0, maxTime);
    return suffix >= INF ? -1 : suffix;
  }

  private int dfs(int node, int timeLeft) {
    if (timeLeft < 0) return INF;
    if (node == fees.length - 1) return fees[node];
    if (memo[node][timeLeft] != -1) return memo[node][timeLeft];
    int best = INF;
    for (int[] edge : graph[node]) {
      int next = dfs(edge[0], timeLeft - edge[1]);
      if (next < INF) best = Math.min(best, fees[node] + next);
    }
    return memo[node][timeLeft] = best;
  }
}
```
