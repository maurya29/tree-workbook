# Tree Workbook - Moderate Questions and Solutions

## Topic 12: Trees

### Moderate (13-20)

#### 13. Sum Root to Leaf Numbers - [LeetCode 129](https://leetcode.com/problems/sum-root-to-leaf-numbers/)

Given a binary tree containing digits 0 through 9, each root-to-leaf path represents a number. Return the total sum of all root-to-leaf numbers.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 14. Count Good Nodes in Binary Tree - [LeetCode 1448](https://leetcode.com/problems/count-good-nodes-in-binary-tree/)

Given a binary tree root, a node is good if no node on the path from root to that node has a value greater than it. Return the number of good nodes.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 15. Validate Binary Search Tree - [LeetCode 98](https://leetcode.com/problems/validate-binary-search-tree/)

Given the root of a binary tree, return true if it is a valid binary search tree with strict ordering.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 16. Kth Smallest Element in a BST - [LeetCode 230](https://leetcode.com/problems/kth-smallest-element-in-a-bst/)

Given the root of a BST and an integer k, return the kth smallest value among all node values.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 17. Construct Binary Tree from Preorder and Inorder Traversal - [LeetCode 105](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)

Given preorder and inorder traversal arrays of a binary tree with unique values, construct and return the binary tree.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 18. Serialize and Deserialize Binary Tree - [LeetCode 297](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/)

Design an algorithm to serialize and deserialize a binary tree. Serialization converts a tree to a string; deserialization rebuilds the same structure.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 19. Flatten Binary Tree to Linked List - [LeetCode 114](https://leetcode.com/problems/flatten-binary-tree-to-linked-list/)

Given the root of a binary tree, flatten it into a linked list in-place using the same TreeNode class. The flattened order should follow preorder traversal using right pointers only.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 20. Binary Tree Zigzag Level Order Traversal - [LeetCode 103](https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/)

Given the root of a binary tree, return the zigzag level order traversal of its node values, alternating left-to-right and right-to-left each level.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

## Topic 13: Binary Search Tree

### Moderate (13-20)

#### 13. Balance a Binary Search Tree - [LeetCode 1382](https://leetcode.com/problems/balance-a-binary-search-tree/)

Given the root of a BST, return a balanced BST containing the same node values.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 14. Trim a Binary Search Tree - [LeetCode 669](https://leetcode.com/problems/trim-a-binary-search-tree/)

Given a BST root and range [low, high], trim the tree so every node value lies in the range while preserving relative ancestor-child structure among remaining nodes.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 15. Construct BST from Preorder Traversal - [LeetCode 1008](https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/)

Given preorder traversal of a BST, construct the tree and return its root.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 16. Unique Binary Search Trees - [LeetCode 96](https://leetcode.com/problems/unique-binary-search-trees/)

Given n, return the number of structurally unique BSTs that store values 1 through n.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 17. Unique Binary Search Trees II - [LeetCode 95](https://leetcode.com/problems/unique-binary-search-trees-ii/)

Given n, generate all structurally unique BSTs that store values 1 through n.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 18. Closest Binary Search Tree Value - [LeetCode 270](https://leetcode.com/problems/closest-binary-search-tree-value/)

Given the root of a BST and a target double, return the value in the BST closest to target.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 19. Closest Binary Search Tree Value II - [LeetCode 272](https://leetcode.com/problems/closest-binary-search-tree-value-ii/)

Given a non-empty BST, a target double, and integer k, return k values in the BST that are closest to target.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

#### 20. Binary Search Tree Iterator - [LeetCode 173](https://leetcode.com/problems/binary-search-tree-iterator/)

Design an iterator over a BST that supports next() returning the next smallest value and hasNext() indicating whether another value exists.

##### Optimized / Iterative Solution

```java
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
```

##### Recursive Solution

```java
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
```

## Topic 16: Trie

### Moderate (13-20)

#### 13. Index Pairs of a String - [LeetCode 1065](https://leetcode.com/problems/index-pairs-of-a-string/)

Given a text and a list of words, return all index pairs [i, j] where text substring i..j appears in words.

##### Optimized / Iterative Solution

```java
public static class Q13Optimized {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  public int[][] indexPairs(String text, String[] words) {
    Node root = new Node();
    for (String word : words) insert(root, word);
    List<int[]> pairs = new ArrayList<>();

    for (int start = 0; start < text.length(); start++) {
      Node node = root;
      for (int end = start; end < text.length(); end++) {
        node = node.next[text.charAt(end) - 'a'];
        if (node == null) break;
        if (node.word) pairs.add(new int[] {start, end});
      }
    }
    return pairs.toArray(new int[pairs.size()][]);
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
public static class Q13Recursive {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  public int[][] indexPairs(String text, String[] words) {
    Node root = new Node();
    for (String word : words) insert(root, word, 0);
    List<int[]> pairs = new ArrayList<>();
    scanStarts(text, 0, root, pairs);
    return pairs.toArray(new int[pairs.size()][]);
  }

  private void scanStarts(String text, int start, Node root, List<int[]> pairs) {
    if (start == text.length()) return;
    collect(text, start, start, root, pairs);
    scanStarts(text, start + 1, root, pairs);
  }

  private void collect(String text, int start, int end, Node node, List<int[]> pairs) {
    if (end == text.length()) return;
    Node child = node.next[text.charAt(end) - 'a'];
    if (child == null) return;
    if (child.word) pairs.add(new int[] {start, end});
    collect(text, start, end + 1, child, pairs);
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

#### 14. Prefix and Suffix Search - [LeetCode 745](https://leetcode.com/problems/prefix-and-suffix-search/)

Design WordFilter so f(prefix, suffix) returns the largest index of a word that starts with prefix and ends with suffix.

##### Optimized / Iterative Solution

```java
public static class Q14Optimized {
  private static class Node {
    Map<Character, Node> next = new HashMap<>();
    int weight = -1;
  }

  private final Node root = new Node();

  public Q14Optimized(String[] words) {
    for (int weight = 0; weight < words.length; weight++) {
      String word = words[weight];
      for (int i = 0; i <= word.length(); i++) {
        insert(word.substring(i) + "{" + word, weight);
      }
    }
  }

  public int f(String pref, String suff) {
    Node node = root;
    String key = suff + "{" + pref;
    for (char ch : key.toCharArray()) {
      node = node.next.get(ch);
      if (node == null) return -1;
    }
    return node.weight;
  }

  private void insert(String key, int weight) {
    Node node = root;
    node.weight = weight;
    for (char ch : key.toCharArray()) {
      node = node.next.computeIfAbsent(ch, unused -> new Node());
      node.weight = weight;
    }
  }
}
```

##### Recursive Solution

```java
public static class Q14Recursive {
  private static class Node {
    Map<Character, Node> next = new HashMap<>();
    int weight = -1;
  }

  private final Node root = new Node();

  public Q14Recursive(String[] words) {
    for (int weight = 0; weight < words.length; weight++) addSuffixes(words[weight], 0, weight);
  }

  public int f(String pref, String suff) {
    Node node = find(root, suff + "{" + pref, 0);
    return node == null ? -1 : node.weight;
  }

  private void addSuffixes(String word, int start, int weight) {
    if (start > word.length()) return;
    insert(root, word.substring(start) + "{" + word, 0, weight);
    addSuffixes(word, start + 1, weight);
  }

  private void insert(Node node, String key, int index, int weight) {
    node.weight = weight;
    if (index == key.length()) return;
    char ch = key.charAt(index);
    node.next.putIfAbsent(ch, new Node());
    insert(node.next.get(ch), key, index + 1, weight);
  }

  private Node find(Node node, String key, int index) {
    if (node == null || index == key.length()) return node;
    return find(node.next.get(key.charAt(index)), key, index + 1);
  }
}
```

#### 15. Implement Magic Dictionary - [LeetCode 676](https://leetcode.com/problems/implement-magic-dictionary/)

Design a dictionary that supports building from words and searching whether a query can match a dictionary word after modifying exactly one character.

##### Optimized / Iterative Solution

```java
public static class Q15Optimized {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  private final Node root = new Node();

  public void buildDict(String[] dictionary) {
    for (String word : dictionary) {
      Node node = root;
      for (char ch : word.toCharArray()) {
        int child = ch - 'a';
        if (node.next[child] == null) node.next[child] = new Node();
        node = node.next[child];
      }
      node.word = true;
    }
  }

  public boolean search(String searchWord) {
    Deque<State> stack = new ArrayDeque<>();
    stack.push(new State(root, 0, 0));
    while (!stack.isEmpty()) {
      State state = stack.pop();
      if (state.index == searchWord.length()) {
        if (state.diff == 1 && state.node.word) return true;
        continue;
      }
      int current = searchWord.charAt(state.index) - 'a';
      for (int i = 0; i < 26; i++) {
        if (state.node.next[i] == null) continue;
        int nextDiff = state.diff + (i == current ? 0 : 1);
        if (nextDiff <= 1) stack.push(new State(state.node.next[i], state.index + 1, nextDiff));
      }
    }
    return false;
  }

  private static class State {
    Node node;
    int index;
    int diff;
    State(Node node, int index, int diff) { this.node = node; this.index = index; this.diff = diff; }
  }
}
```

##### Recursive Solution

```java
public static class Q15Recursive {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  private final Node root = new Node();

  public void buildDict(String[] dictionary) {
    for (String word : dictionary) insert(root, word, 0);
  }

  public boolean search(String searchWord) {
    return dfs(root, searchWord, 0, 0);
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

  private boolean dfs(Node node, String word, int index, int diff) {
    if (node == null || diff > 1) return false;
    if (index == word.length()) return diff == 1 && node.word;
    int current = word.charAt(index) - 'a';
    for (int i = 0; i < 26; i++) {
      if (dfs(node.next[i], word, index + 1, diff + (i == current ? 0 : 1))) return true;
    }
    return false;
  }
}
```

#### 16. Count Pairs With XOR in a Range - [LeetCode 1803](https://leetcode.com/problems/count-pairs-with-xor-in-a-range/)

Given nums, low, and high, count pairs i < j whose XOR value lies in the inclusive range [low, high].

##### Optimized / Iterative Solution

```java
public static class Q16Optimized {
  private static class Node {
    Node[] next = new Node[2];
    int count;
  }

  public int countPairs(int[] nums, int low, int high) {
    Node root = new Node();
    int answer = 0;
    for (int num : nums) {
      answer += countLess(root, num, high + 1) - countLess(root, num, low);
      insert(root, num);
    }
    return answer;
  }

  private void insert(Node root, int num) {
    Node node = root;
    for (int bit = 14; bit >= 0; bit--) {
      int current = (num >>> bit) & 1;
      if (node.next[current] == null) node.next[current] = new Node();
      node = node.next[current];
      node.count++;
    }
  }

  private int countLess(Node root, int num, int limit) {
    Node node = root;
    int count = 0;
    for (int bit = 14; bit >= 0 && node != null; bit--) {
      int current = (num >>> bit) & 1;
      int bound = (limit >>> bit) & 1;
      if (bound == 1) {
        if (node.next[current] != null) count += node.next[current].count;
        node = node.next[current ^ 1];
      } else {
        node = node.next[current];
      }
    }
    return count;
  }
}
```

##### Recursive Solution

```java
public static class Q16Recursive {
  private static class Node {
    Node[] next = new Node[2];
    int count;
  }

  public int countPairs(int[] nums, int low, int high) {
    Node root = new Node();
    int answer = 0;
    for (int num : nums) {
      answer += countLess(root, num, high + 1, 14) - countLess(root, num, low, 14);
      insert(root, num, 14);
    }
    return answer;
  }

  private void insert(Node node, int num, int bit) {
    if (bit < 0) return;
    int current = (num >>> bit) & 1;
    if (node.next[current] == null) node.next[current] = new Node();
    node.next[current].count++;
    insert(node.next[current], num, bit - 1);
  }

  private int countLess(Node node, int num, int limit, int bit) {
    if (node == null || bit < 0) return 0;
    int current = (num >>> bit) & 1;
    int bound = (limit >>> bit) & 1;
    if (bound == 0) return countLess(node.next[current], num, limit, bit - 1);
    int sameCount = node.next[current] == null ? 0 : node.next[current].count;
    return sameCount + countLess(node.next[current ^ 1], num, limit, bit - 1);
  }
}
```

#### 17. Sum of Prefix Scores of Strings - [LeetCode 2416](https://leetcode.com/problems/sum-of-prefix-scores-of-strings/)

For each word, return the sum of counts of all its non-empty prefixes across the entire words array.

##### Optimized / Iterative Solution

```java
public static class Q17Optimized {
  private static class Node {
    Node[] next = new Node[26];
    int count;
  }

  public int[] sumPrefixScores(String[] words) {
    Node root = new Node();
    for (String word : words) insert(root, word);

    int[] answer = new int[words.length];
    for (int i = 0; i < words.length; i++) {
      Node node = root;
      for (char ch : words[i].toCharArray()) {
        node = node.next[ch - 'a'];
        answer[i] += node.count;
      }
    }
    return answer;
  }

  private void insert(Node root, String word) {
    Node node = root;
    for (char ch : word.toCharArray()) {
      int child = ch - 'a';
      if (node.next[child] == null) node.next[child] = new Node();
      node = node.next[child];
      node.count++;
    }
  }
}
```

##### Recursive Solution

```java
public static class Q17Recursive {
  private static class Node {
    Node[] next = new Node[26];
    int count;
  }

  public int[] sumPrefixScores(String[] words) {
    Node root = new Node();
    for (String word : words) insert(root, word, 0);
    int[] answer = new int[words.length];
    fillScores(words, 0, root, answer);
    return answer;
  }

  private void fillScores(String[] words, int index, Node root, int[] answer) {
    if (index == words.length) return;
    answer[index] = score(root, words[index], 0);
    fillScores(words, index + 1, root, answer);
  }

  private void insert(Node node, String word, int index) {
    if (index == word.length()) return;
    int child = word.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    node.next[child].count++;
    insert(node.next[child], word, index + 1);
  }

  private int score(Node node, String word, int index) {
    if (index == word.length()) return 0;
    Node child = node.next[word.charAt(index) - 'a'];
    return child.count + score(child, word, index + 1);
  }
}
```

#### 18. Short Encoding of Words - [LeetCode 820](https://leetcode.com/problems/short-encoding-of-words/)

Given words, return the length of the shortest reference string that encodes every word as a suffix followed by #.

##### Optimized / Iterative Solution

```java
public static class Q18Optimized {
  private static class Node {
    Node[] next = new Node[26];
  }

  public int minimumLengthEncoding(String[] words) {
    Set<String> unique = new HashSet<>(Arrays.asList(words));
    Node root = new Node();
    Map<Node, Integer> leafDepth = new HashMap<>();

    for (String word : unique) {
      Node node = root;
      for (int i = word.length() - 1; i >= 0; i--) {
        int child = word.charAt(i) - 'a';
        if (node.next[child] == null) node.next[child] = new Node();
        node = node.next[child];
      }
      leafDepth.put(node, word.length() + 1);
    }

    int total = 0;
    for (Map.Entry<Node, Integer> entry : leafDepth.entrySet()) {
      if (isLeaf(entry.getKey())) total += entry.getValue();
    }
    return total;
  }

  private boolean isLeaf(Node node) {
    for (Node child : node.next) if (child != null) return false;
    return true;
  }
}
```

##### Recursive Solution

```java
public static class Q18Recursive {
  private static class Node {
    Node[] next = new Node[26];
  }

  public int minimumLengthEncoding(String[] words) {
    Node root = new Node();
    Set<String> unique = new HashSet<>(Arrays.asList(words));
    for (String word : unique) insert(root, word, word.length() - 1);
    return length(root, 0);
  }

  private void insert(Node node, String word, int index) {
    if (index < 0) return;
    int child = word.charAt(index) - 'a';
    if (node.next[child] == null) node.next[child] = new Node();
    insert(node.next[child], word, index - 1);
  }

  private int length(Node node, int depth) {
    boolean leaf = true;
    int total = 0;
    for (Node child : node.next) {
      if (child != null) {
        leaf = false;
        total += length(child, depth + 1);
      }
    }
    return leaf ? depth + 1 : total;
  }
}
```

#### 19. Word Break with Trie - [LeetCode 139](https://leetcode.com/problems/word-break/)

Given a string and a dictionary, return true if the string can be segmented into one or more dictionary words.

##### Optimized / Iterative Solution

```java
public static class Q19Optimized {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  public boolean wordBreak(String s, List<String> wordDict) {
    Node root = new Node();
    for (String word : wordDict) insert(root, word);
    boolean[] dp = new boolean[s.length() + 1];
    dp[0] = true;

    for (int start = 0; start < s.length(); start++) {
      if (!dp[start]) continue;
      Node node = root;
      for (int end = start; end < s.length(); end++) {
        node = node.next[s.charAt(end) - 'a'];
        if (node == null) break;
        if (node.word) dp[end + 1] = true;
      }
    }
    return dp[s.length()];
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
public static class Q19Recursive {
  private static class Node {
    Node[] next = new Node[26];
    boolean word;
  }

  public boolean wordBreak(String s, List<String> wordDict) {
    Node root = new Node();
    for (String word : wordDict) insert(root, word, 0);
    Boolean[] memo = new Boolean[s.length()];
    return canBreak(s, 0, root, memo);
  }

  private boolean canBreak(String s, int start, Node root, Boolean[] memo) {
    if (start == s.length()) return true;
    if (memo[start] != null) return memo[start];
    Node node = root;
    for (int end = start; end < s.length(); end++) {
      node = node.next[s.charAt(end) - 'a'];
      if (node == null) break;
      if (node.word && canBreak(s, end + 1, root, memo)) return memo[start] = true;
    }
    return memo[start] = false;
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

#### 20. Design Search Autocomplete System - [LeetCode 642](https://leetcode.com/problems/design-search-autocomplete-system/)

Design an autocomplete system that returns the top three historical sentences for the current typed prefix, ranked by frequency then lexicographic order. Character # commits the current sentence.

##### Optimized / Iterative Solution

```java
public static class Q20Optimized {
  private static class Node {
    Map<Character, Node> next = new HashMap<>();
    Map<String, Integer> hot = new HashMap<>();
  }

  private final Node root = new Node();
  private final StringBuilder current = new StringBuilder();
  private Node node = root;

  public Q20Optimized(String[] sentences, int[] times) {
    for (int i = 0; i < sentences.length; i++) add(sentences[i], times[i]);
  }

  public List<String> input(char c) {
    if (c == '#') {
      add(current.toString(), 1);
      current.setLength(0);
      node = root;
      return new ArrayList<>();
    }

    current.append(c);
    node = node == null ? null : node.next.get(c);
    if (node == null) return new ArrayList<>();

    PriorityQueue<String> pq = new PriorityQueue<>((a, b) -> node.hot.get(a).equals(node.hot.get(b))
        ? b.compareTo(a) : node.hot.get(a) - node.hot.get(b));
    for (String sentence : node.hot.keySet()) {
      pq.offer(sentence);
      if (pq.size() > 3) pq.poll();
    }
    List<String> answer = new ArrayList<>();
    while (!pq.isEmpty()) answer.add(pq.poll());
    Collections.reverse(answer);
    return answer;
  }

  private void add(String sentence, int delta) {
    Node walk = root;
    for (char ch : sentence.toCharArray()) {
      walk = walk.next.computeIfAbsent(ch, unused -> new Node());
      walk.hot.put(sentence, walk.hot.getOrDefault(sentence, 0) + delta);
    }
  }
}
```

##### Recursive Solution

```java
public static class Q20Recursive {
  private static class Node {
    Map<Character, Node> next = new HashMap<>();
    String sentence;
    int count;
  }

  private final Node root = new Node();
  private final StringBuilder current = new StringBuilder();

  public Q20Recursive(String[] sentences, int[] times) {
    for (int i = 0; i < sentences.length; i++) add(root, sentences[i], 0, times[i]);
  }

  public List<String> input(char c) {
    if (c == '#') {
      add(root, current.toString(), 0, 1);
      current.setLength(0);
      return new ArrayList<>();
    }
    current.append(c);
    Node node = find(root, current.toString(), 0);
    List<Node> matches = new ArrayList<>();
    collect(node, matches);
    matches.sort((a, b) -> a.count == b.count ? a.sentence.compareTo(b.sentence) : b.count - a.count);
    List<String> answer = new ArrayList<>();
    for (int i = 0; i < Math.min(3, matches.size()); i++) answer.add(matches.get(i).sentence);
    return answer;
  }

  private void add(Node node, String sentence, int index, int delta) {
    if (index == sentence.length()) {
      node.sentence = sentence;
      node.count += delta;
      return;
    }
    char ch = sentence.charAt(index);
    node.next.putIfAbsent(ch, new Node());
    add(node.next.get(ch), sentence, index + 1, delta);
  }

  private Node find(Node node, String prefix, int index) {
    if (node == null || index == prefix.length()) return node;
    return find(node.next.get(prefix.charAt(index)), prefix, index + 1);
  }

  private void collect(Node node, List<Node> matches) {
    if (node == null) return;
    if (node.sentence != null) matches.add(node);
    for (Node child : node.next.values()) collect(child, matches);
  }
}
```

## Topic 14: Graphs

### Moderate (13-20)

#### 13. Word Ladder - [LeetCode 127](https://leetcode.com/problems/word-ladder/)

Given beginWord, endWord, and a dictionary, return the length of the shortest transformation sequence where each step changes one character and every intermediate word is in the dictionary.

##### Optimized / Iterative Solution

```java
public static class Q13Optimized {
  public int ladderLength(String beginWord, String endWord, List<String> wordList) {
    Set<String> words = new HashSet<>(wordList);
    if (!words.contains(endWord)) return 0;

    Deque<String> queue = new ArrayDeque<>();
    queue.offer(beginWord);
    words.remove(beginWord);
    int length = 1;

    while (!queue.isEmpty()) {
      int size = queue.size();
      for (int s = 0; s < size; s++) {
        String word = queue.poll();
        if (word.equals(endWord)) return length;
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
          char original = chars[i];
          for (char ch = 'a'; ch <= 'z'; ch++) {
            chars[i] = ch;
            String next = new String(chars);
            if (words.remove(next)) queue.offer(next);
          }
          chars[i] = original;
        }
      }
      length++;
    }
    return 0;
  }
}
```

##### Recursive Solution

```java
public static class Q13Recursive {
  public int ladderLength(String beginWord, String endWord, List<String> wordList) {
    Set<String> words = new HashSet<>(wordList);
    if (!words.contains(endWord)) return 0;
    Set<String> frontier = new HashSet<>();
    frontier.add(beginWord);
    words.remove(beginWord);
    return bfs(frontier, words, endWord, 1);
  }

  private int bfs(Set<String> frontier, Set<String> words, String endWord, int depth) {
    if (frontier.isEmpty()) return 0;
    Set<String> nextFrontier = new HashSet<>();
    for (String word : frontier) {
      if (word.equals(endWord)) return depth;
      char[] chars = word.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        char original = chars[i];
        for (char ch = 'a'; ch <= 'z'; ch++) {
          chars[i] = ch;
          String next = new String(chars);
          if (words.remove(next)) nextFrontier.add(next);
        }
        chars[i] = original;
      }
    }
    return bfs(nextFrontier, words, endWord, depth + 1);
  }
}
```

#### 14. Open the Lock - [LeetCode 752](https://leetcode.com/problems/open-the-lock/)

Given deadends and a target lock combination, return the minimum wheel turns needed to reach target from 0000, or -1 if impossible.

##### Optimized / Iterative Solution

```java
public static class Q14Optimized {
  public int openLock(String[] deadends, String target) {
    Set<String> dead = new HashSet<>(Arrays.asList(deadends));
    if (dead.contains("0000")) return -1;

    Deque<String> queue = new ArrayDeque<>();
    Set<String> seen = new HashSet<>();
    queue.offer("0000");
    seen.add("0000");
    int turns = 0;

    while (!queue.isEmpty()) {
      int size = queue.size();
      for (int i = 0; i < size; i++) {
        String state = queue.poll();
        if (state.equals(target)) return turns;
        for (String next : neighbors(state)) {
          if (dead.contains(next) || !seen.add(next)) continue;
          queue.offer(next);
        }
      }
      turns++;
    }
    return -1;
  }

  private List<String> neighbors(String state) {
    List<String> list = new ArrayList<>();
    char[] chars = state.toCharArray();
    for (int i = 0; i < 4; i++) {
      char original = chars[i];
      chars[i] = original == '9' ? '0' : (char) (original + 1);
      list.add(new String(chars));
      chars[i] = original == '0' ? '9' : (char) (original - 1);
      list.add(new String(chars));
      chars[i] = original;
    }
    return list;
  }
}
```

##### Recursive Solution

```java
public static class Q14Recursive {
  public int openLock(String[] deadends, String target) {
    Set<String> dead = new HashSet<>(Arrays.asList(deadends));
    if (dead.contains("0000")) return -1;
    Set<String> frontier = new HashSet<>();
    Set<String> seen = new HashSet<>();
    frontier.add("0000");
    seen.add("0000");
    return bfs(frontier, seen, dead, target, 0);
  }

  private int bfs(Set<String> frontier, Set<String> seen, Set<String> dead, String target, int turns) {
    if (frontier.isEmpty()) return -1;
    Set<String> nextLayer = new HashSet<>();
    for (String state : frontier) {
      if (state.equals(target)) return turns;
      for (String next : neighbors(state)) {
        if (!dead.contains(next) && seen.add(next)) nextLayer.add(next);
      }
    }
    return bfs(nextLayer, seen, dead, target, turns + 1);
  }

  private List<String> neighbors(String state) {
    List<String> list = new ArrayList<>();
    char[] chars = state.toCharArray();
    for (int i = 0; i < 4; i++) {
      char original = chars[i];
      chars[i] = original == '9' ? '0' : (char) (original + 1);
      list.add(new String(chars));
      chars[i] = original == '0' ? '9' : (char) (original - 1);
      list.add(new String(chars));
      chars[i] = original;
    }
    return list;
  }
}
```

#### 15. Shortest Path in Binary Matrix - [LeetCode 1091](https://leetcode.com/problems/shortest-path-in-binary-matrix/)

Given an n x n binary matrix, return the length of the shortest clear path from top-left to bottom-right moving in 8 directions, or -1 if none exists.

##### Optimized / Iterative Solution

```java
public static class Q15Optimized {
  public int shortestPathBinaryMatrix(int[][] grid) {
    int n = grid.length;
    if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) return -1;

    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
    Deque<int[]> queue = new ArrayDeque<>();
    queue.offer(new int[] {0, 0, 1});
    grid[0][0] = 1;

    while (!queue.isEmpty()) {
      int[] cell = queue.poll();
      if (cell[0] == n - 1 && cell[1] == n - 1) return cell[2];
      for (int[] d : dirs) {
        int nr = cell[0] + d[0], nc = cell[1] + d[1];
        if (nr < 0 || nc < 0 || nr == n || nc == n || grid[nr][nc] == 1) continue;
        grid[nr][nc] = 1;
        queue.offer(new int[] {nr, nc, cell[2] + 1});
      }
    }
    return -1;
  }
}
```

##### Recursive Solution

```java
public static class Q15Recursive {
  private final int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};

  public int shortestPathBinaryMatrix(int[][] grid) {
    int n = grid.length;
    if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) return -1;
    List<int[]> frontier = new ArrayList<>();
    frontier.add(new int[] {0, 0});
    grid[0][0] = 1;
    return bfs(grid, frontier, 1);
  }

  private int bfs(int[][] grid, List<int[]> frontier, int distance) {
    if (frontier.isEmpty()) return -1;
    int n = grid.length;
    List<int[]> nextLayer = new ArrayList<>();
    for (int[] cell : frontier) {
      if (cell[0] == n - 1 && cell[1] == n - 1) return distance;
      for (int[] d : dirs) {
        int nr = cell[0] + d[0], nc = cell[1] + d[1];
        if (nr < 0 || nc < 0 || nr == n || nc == n || grid[nr][nc] == 1) continue;
        grid[nr][nc] = 1;
        nextLayer.add(new int[] {nr, nc});
      }
    }
    return bfs(grid, nextLayer, distance + 1);
  }
}
```

#### 16. 01 Matrix - [LeetCode 542](https://leetcode.com/problems/01-matrix/)

Given a binary matrix, return a matrix where each cell stores the distance to the nearest 0.

##### Optimized / Iterative Solution

```java
public static class Q16Optimized {
  public int[][] updateMatrix(int[][] mat) {
    int rows = mat.length, cols = mat[0].length;
    int[][] dist = new int[rows][cols];
    Deque<int[]> queue = new ArrayDeque<>();

    for (int r = 0; r < rows; r++) {
      Arrays.fill(dist[r], -1);
      for (int c = 0; c < cols; c++) {
        if (mat[r][c] == 0) {
          dist[r][c] = 0;
          queue.offer(new int[] {r, c});
        }
      }
    }

    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!queue.isEmpty()) {
      int[] cell = queue.poll();
      for (int[] d : dirs) {
        int nr = cell[0] + d[0], nc = cell[1] + d[1];
        if (nr < 0 || nc < 0 || nr == rows || nc == cols || dist[nr][nc] != -1) continue;
        dist[nr][nc] = dist[cell[0]][cell[1]] + 1;
        queue.offer(new int[] {nr, nc});
      }
    }
    return dist;
  }
}
```

##### Recursive Solution

```java
public static class Q16Recursive {
  private final int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

  public int[][] updateMatrix(int[][] mat) {
    int rows = mat.length, cols = mat[0].length;
    int[][] dist = new int[rows][cols];
    for (int r = 0; r < rows; r++) Arrays.fill(dist[r], Integer.MAX_VALUE);

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (mat[r][c] == 0) relax(dist, r, c, 0);
      }
    }
    return dist;
  }

  private void relax(int[][] dist, int r, int c, int value) {
    if (r < 0 || c < 0 || r == dist.length || c == dist[0].length) return;
    if (value >= dist[r][c]) return;
    dist[r][c] = value;
    for (int[] d : dirs) relax(dist, r + d[0], c + d[1], value + 1);
  }
}
```

#### 17. Is Graph Bipartite? - [LeetCode 785](https://leetcode.com/problems/is-graph-bipartite/)

Given an undirected graph as adjacency lists, determine whether its nodes can be split into two sets so every edge connects nodes in different sets.

##### Optimized / Iterative Solution

```java
public static class Q17Optimized {
  public boolean isBipartite(int[][] graph) {
    int[] color = new int[graph.length];
    for (int start = 0; start < graph.length; start++) {
      if (color[start] != 0) continue;
      Deque<Integer> queue = new ArrayDeque<>();
      queue.offer(start);
      color[start] = 1;

      while (!queue.isEmpty()) {
        int node = queue.poll();
        for (int next : graph[node]) {
          if (color[next] == color[node]) return false;
          if (color[next] != 0) continue;
          color[next] = -color[node];
          queue.offer(next);
        }
      }
    }
    return true;
  }
}
```

##### Recursive Solution

```java
public static class Q17Recursive {
  public boolean isBipartite(int[][] graph) {
    int[] color = new int[graph.length];
    for (int node = 0; node < graph.length; node++) {
      if (color[node] == 0 && !dfs(node, 1, graph, color)) return false;
    }
    return true;
  }

  private boolean dfs(int node, int paint, int[][] graph, int[] color) {
    if (color[node] != 0) return color[node] == paint;
    color[node] = paint;
    for (int next : graph[node]) {
      if (!dfs(next, -paint, graph, color)) return false;
    }
    return true;
  }
}
```

#### 18. Possible Bipartition - [LeetCode 886](https://leetcode.com/problems/possible-bipartition/)

Given n people and pairs of people who dislike each other, return true if everyone can be split into two groups so no pair in the same group dislikes each other.

##### Optimized / Iterative Solution

```java
public static class Q18Optimized {
  public boolean possibleBipartition(int n, int[][] dislikes) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i <= n; i++) graph.add(new ArrayList<>());
    for (int[] edge : dislikes) {
      graph.get(edge[0]).add(edge[1]);
      graph.get(edge[1]).add(edge[0]);
    }

    int[] color = new int[n + 1];
    for (int person = 1; person <= n; person++) {
      if (color[person] != 0) continue;
      Deque<Integer> queue = new ArrayDeque<>();
      queue.offer(person);
      color[person] = 1;
      while (!queue.isEmpty()) {
        int current = queue.poll();
        for (int next : graph.get(current)) {
          if (color[next] == color[current]) return false;
          if (color[next] != 0) continue;
          color[next] = -color[current];
          queue.offer(next);
        }
      }
    }
    return true;
  }
}
```

##### Recursive Solution

```java
public static class Q18Recursive {
  public boolean possibleBipartition(int n, int[][] dislikes) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i <= n; i++) graph.add(new ArrayList<>());
    for (int[] edge : dislikes) {
      graph.get(edge[0]).add(edge[1]);
      graph.get(edge[1]).add(edge[0]);
    }

    int[] color = new int[n + 1];
    for (int person = 1; person <= n; person++) {
      if (color[person] == 0 && !dfs(person, 1, graph, color)) return false;
    }
    return true;
  }

  private boolean dfs(int person, int paint, List<List<Integer>> graph, int[] color) {
    if (color[person] != 0) return color[person] == paint;
    color[person] = paint;
    for (int next : graph.get(person)) {
      if (!dfs(next, -paint, graph, color)) return false;
    }
    return true;
  }
}
```

#### 19. Find Eventual Safe States - [LeetCode 802](https://leetcode.com/problems/find-eventual-safe-states/)

Given a directed graph, return all nodes that eventually lead only to terminal nodes and never enter a cycle.

##### Optimized / Iterative Solution

```java
public static class Q19Optimized {
  public List<Integer> eventualSafeNodes(int[][] graph) {
    int n = graph.length;
    List<List<Integer>> reverse = new ArrayList<>();
    for (int i = 0; i < n; i++) reverse.add(new ArrayList<>());
    int[] outdegree = new int[n];

    for (int node = 0; node < n; node++) {
      outdegree[node] = graph[node].length;
      for (int next : graph[node]) reverse.get(next).add(node);
    }

    Deque<Integer> queue = new ArrayDeque<>();
    for (int node = 0; node < n; node++) {
      if (outdegree[node] == 0) queue.offer(node);
    }

    boolean[] safe = new boolean[n];
    while (!queue.isEmpty()) {
      int node = queue.poll();
      safe[node] = true;
      for (int prev : reverse.get(node)) {
        if (--outdegree[prev] == 0) queue.offer(prev);
      }
    }

    List<Integer> answer = new ArrayList<>();
    for (int node = 0; node < n; node++) if (safe[node]) answer.add(node);
    return answer;
  }
}
```

##### Recursive Solution

```java
public static class Q19Recursive {
  public List<Integer> eventualSafeNodes(int[][] graph) {
    int[] state = new int[graph.length];
    List<Integer> answer = new ArrayList<>();
    for (int node = 0; node < graph.length; node++) {
      if (safe(node, graph, state)) answer.add(node);
    }
    return answer;
  }

  private boolean safe(int node, int[][] graph, int[] state) {
    if (state[node] != 0) return state[node] == 2;
    state[node] = 1;
    for (int next : graph[node]) {
      if (!safe(next, graph, state)) return false;
    }
    state[node] = 2;
    return true;
  }
}
```

#### 20. Keys and Rooms - [LeetCode 841](https://leetcode.com/problems/keys-and-rooms/)

Given rooms where each room contains keys to other rooms, return true if every room can be visited starting from room 0.

##### Optimized / Iterative Solution

```java
public static class Q20Optimized {
  public boolean canVisitAllRooms(List<List<Integer>> rooms) {
    boolean[] visited = new boolean[rooms.size()];
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(0);
    visited[0] = true;
    int count = 0;

    while (!stack.isEmpty()) {
      int room = stack.pop();
      count++;
      for (int key : rooms.get(room)) {
        if (visited[key]) continue;
        visited[key] = true;
        stack.push(key);
      }
    }
    return count == rooms.size();
  }
}
```

##### Recursive Solution

```java
public static class Q20Recursive {
  public boolean canVisitAllRooms(List<List<Integer>> rooms) {
    boolean[] visited = new boolean[rooms.size()];
    dfs(0, rooms, visited);
    for (boolean room : visited) if (!room) return false;
    return true;
  }

  private void dfs(int room, List<List<Integer>> rooms, boolean[] visited) {
    if (visited[room]) return;
    visited[room] = true;
    for (int key : rooms.get(room)) {
      dfs(key, rooms, visited);
    }
  }
}
```

## Topic 27: Segment Tree / Fenwick

### Moderate (13-20)

#### 13. Count Integers in Intervals - [LeetCode 2276](https://leetcode.com/problems/count-integers-in-intervals/)

Design CountIntervals with add(left, right) and count(), where count returns how many distinct integers are covered by at least one added interval.

##### Optimized / Iterative Solution

```java
public static class Q13Optimized {
  private final TreeMap<Integer, Integer> intervals = new TreeMap<>();
  private long covered;

  public void add(int left, int right) {
    Map.Entry<Integer, Integer> entry = intervals.floorEntry(right);
    while (entry != null && entry.getValue() >= left - 1) {
      left = Math.min(left, entry.getKey());
      right = Math.max(right, entry.getValue());
      covered -= (long) entry.getValue() - entry.getKey() + 1;
      intervals.remove(entry.getKey());
      entry = intervals.floorEntry(right);
    }
    intervals.put(left, right);
    covered += (long) right - left + 1;
  }

  public int count() {
    return (int) covered;
  }
}
```

##### Recursive Solution

```java
public static class Q13Recursive {
  private final List<int[]> intervals = new ArrayList<>();
  private long covered;

  public void add(int left, int right) {
    merge(0, left, right);
  }

  public int count() {
    return (int) covered;
  }

  private void merge(int index, int left, int right) {
    if (index == intervals.size() || intervals.get(index)[0] > right + 1) {
      intervals.add(index, new int[] {left, right});
      covered += (long) right - left + 1;
      return;
    }
    int[] current = intervals.get(index);
    if (current[1] < left - 1) {
      merge(index + 1, left, right);
      return;
    }
    intervals.remove(index);
    covered -= (long) current[1] - current[0] + 1;
    merge(index, Math.min(left, current[0]), Math.max(right, current[1]));
  }
}
```

#### 14. Peaks in Array - [LeetCode 3187](https://leetcode.com/problems/peaks-in-array/)

Given nums and queries, type 1 [1,l,r] asks for the number of peak indices inside nums[l..r], and type 2 [2,index,value] updates nums[index].

##### Optimized / Iterative Solution

```java
public static class Q14Optimized {
  public List<Integer> countOfPeaks(int[] nums, int[][] queries) {
    Fenwick tree = new Fenwick(nums.length);
    for (int index = 1; index + 1 < nums.length; index++) {
      if (isPeak(nums, index)) tree.add(index, 1);
    }

    List<Integer> answer = new ArrayList<>();
    for (int[] query : queries) {
      if (query[0] == 1) {
        int left = query[1] + 1;
        int right = query[2] - 1;
        answer.add(left <= right ? tree.sumRange(left, right) : 0);
      } else {
        int index = query[1];
        nums[index] = query[2];
        refresh(nums, tree, index - 1);
        refresh(nums, tree, index);
        refresh(nums, tree, index + 1);
      }
    }
    return answer;
  }

  private void refresh(int[] nums, Fenwick tree, int index) {
    if (index <= 0 || index + 1 >= nums.length) return;
    int current = tree.sumRange(index, index);
    int next = isPeak(nums, index) ? 1 : 0;
    tree.add(index, next - current);
  }

  private boolean isPeak(int[] nums, int index) {
    return nums[index] > nums[index - 1] && nums[index] > nums[index + 1];
  }

  private static class Fenwick {
    private final int[] bit;

    Fenwick(int n) {
      bit = new int[n + 1];
    }

    void add(int index, int delta) {
      for (int i = index + 1; i < bit.length; i += i & -i) bit[i] += delta;
    }

    int prefixSum(int index) {
      int sum = 0;
      for (int i = index + 1; i > 0; i -= i & -i) sum += bit[i];
      return sum;
    }

    int sumRange(int left, int right) {
      return prefixSum(right) - (left == 0 ? 0 : prefixSum(left - 1));
    }
  }
}
```

##### Recursive Solution

```java
public static class Q14Recursive {
  public List<Integer> countOfPeaks(int[] nums, int[][] queries) {
    int[] peaks = new int[nums.length];
    for (int i = 1; i + 1 < nums.length; i++) peaks[i] = isPeak(nums, i) ? 1 : 0;
    SegmentTree tree = new SegmentTree(peaks);

    List<Integer> answer = new ArrayList<>();
    for (int[] query : queries) {
      if (query[0] == 1) {
        int left = query[1] + 1;
        int right = query[2] - 1;
        answer.add(left <= right ? tree.query(left, right) : 0);
      } else {
        int index = query[1];
        nums[index] = query[2];
        updatePeak(nums, tree, index - 1);
        updatePeak(nums, tree, index);
        updatePeak(nums, tree, index + 1);
      }
    }
    return answer;
  }

  private void updatePeak(int[] nums, SegmentTree tree, int index) {
    if (index <= 0 || index + 1 >= nums.length) return;
    tree.update(index, isPeak(nums, index) ? 1 : 0);
  }

  private boolean isPeak(int[] nums, int index) {
    return nums[index] > nums[index - 1] && nums[index] > nums[index + 1];
  }

  private static class SegmentTree {
    private final int n;
    private final int[] tree;

    SegmentTree(int[] values) {
      n = values.length;
      tree = new int[Math.max(1, 4 * n)];
      build(1, 0, n - 1, values);
    }

    void update(int index, int value) {
      update(1, 0, n - 1, index, value);
    }

    int query(int left, int right) {
      return query(1, 0, n - 1, left, right);
    }

    private void build(int node, int left, int right, int[] values) {
      if (left == right) {
        tree[node] = values[left];
        return;
      }
      int mid = left + (right - left) / 2;
      build(node * 2, left, mid, values);
      build(node * 2 + 1, mid + 1, right, values);
      tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }

    private void update(int node, int left, int right, int index, int value) {
      if (left == right) {
        tree[node] = value;
        return;
      }
      int mid = left + (right - left) / 2;
      if (index <= mid) update(node * 2, left, mid, index, value);
      else update(node * 2 + 1, mid + 1, right, index, value);
      tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }

    private int query(int node, int left, int right, int ql, int qr) {
      if (qr < left || right < ql) return 0;
      if (ql <= left && right <= qr) return tree[node];
      int mid = left + (right - left) / 2;
      return query(node * 2, left, mid, ql, qr) + query(node * 2 + 1, mid + 1, right, ql, qr);
    }
  }
}
```

#### 15. Booking Concert Tickets in Groups - [LeetCode 2286](https://leetcode.com/problems/booking-concert-tickets-in-groups/)

Design BookMyShow with gather(k,maxRow), which seats k people contiguously in one row, and scatter(k,maxRow), which seats k people anywhere across rows 0..maxRow.

##### Optimized / Iterative Solution

```java
public static class Q15Optimized {
  private final int n;
  private final int m;
  private final long[] sum;
  private final int[] max;

  public Q15Optimized(int n, int m) {
    this.n = n;
    this.m = m;
    sum = new long[4 * n];
    max = new int[4 * n];
    build(1, 0, n - 1);
  }

  public int[] gather(int k, int maxRow) {
    maxRow = Math.min(maxRow, n - 1);
    if (queryMax(1, 0, n - 1, 0, maxRow) < k) return new int[0];
    int row = findFirst(1, 0, n - 1, maxRow, k);
    int free = (int) querySum(1, 0, n - 1, row, row);
    int start = m - free;
    update(1, 0, n - 1, row, free - k);
    return new int[] {row, start};
  }

  public boolean scatter(int k, int maxRow) {
    maxRow = Math.min(maxRow, n - 1);
    if (querySum(1, 0, n - 1, 0, maxRow) < k) return false;
    while (k > 0) {
      int row = findFirst(1, 0, n - 1, maxRow, 1);
      int free = (int) querySum(1, 0, n - 1, row, row);
      int take = Math.min(k, free);
      update(1, 0, n - 1, row, free - take);
      k -= take;
    }
    return true;
  }

  private void build(int node, int left, int right) {
    if (left == right) {
      sum[node] = m;
      max[node] = m;
      return;
    }
    int mid = left + (right - left) / 2;
    build(node * 2, left, mid);
    build(node * 2 + 1, mid + 1, right);
    pull(node);
  }

  private void update(int node, int left, int right, int index, int free) {
    if (left == right) {
      sum[node] = free;
      max[node] = free;
      return;
    }
    int mid = left + (right - left) / 2;
    if (index <= mid) update(node * 2, left, mid, index, free);
    else update(node * 2 + 1, mid + 1, right, index, free);
    pull(node);
  }

  private int findFirst(int node, int left, int right, int maxRow, int need) {
    if (left > maxRow || max[node] < need) return -1;
    if (left == right) return left;
    int mid = left + (right - left) / 2;
    int fromLeft = findFirst(node * 2, left, mid, maxRow, need);
    return fromLeft != -1 ? fromLeft : findFirst(node * 2 + 1, mid + 1, right, maxRow, need);
  }

  private int queryMax(int node, int left, int right, int ql, int qr) {
    if (qr < left || right < ql) return 0;
    if (ql <= left && right <= qr) return max[node];
    int mid = left + (right - left) / 2;
    return Math.max(queryMax(node * 2, left, mid, ql, qr), queryMax(node * 2 + 1, mid + 1, right, ql, qr));
  }

  private long querySum(int node, int left, int right, int ql, int qr) {
    if (qr < left || right < ql) return 0;
    if (ql <= left && right <= qr) return sum[node];
    int mid = left + (right - left) / 2;
    return querySum(node * 2, left, mid, ql, qr) + querySum(node * 2 + 1, mid + 1, right, ql, qr);
  }

  private void pull(int node) {
    sum[node] = sum[node * 2] + sum[node * 2 + 1];
    max[node] = Math.max(max[node * 2], max[node * 2 + 1]);
  }
}
```

##### Recursive Solution

```java
public static class Q15Recursive {
  private final int rows;
  private final int seatsPerRow;
  private final int[] used;

  public Q15Recursive(int n, int m) {
    rows = n;
    seatsPerRow = m;
    used = new int[n];
  }

  public int[] gather(int k, int maxRow) {
    return gather(0, Math.min(maxRow, rows - 1), k);
  }

  public boolean scatter(int k, int maxRow) {
    maxRow = Math.min(maxRow, rows - 1);
    if (freeSeats(0, maxRow) < k) return false;
    scatter(0, maxRow, new int[] {k});
    return true;
  }

  private int[] gather(int row, int maxRow, int k) {
    if (row > maxRow) return new int[0];
    if (seatsPerRow - used[row] >= k) {
      int start = used[row];
      used[row] += k;
      return new int[] {row, start};
    }
    return gather(row + 1, maxRow, k);
  }

  private long freeSeats(int row, int maxRow) {
    if (row > maxRow) return 0;
    return seatsPerRow - used[row] + freeSeats(row + 1, maxRow);
  }

  private void scatter(int row, int maxRow, int[] remaining) {
    if (row > maxRow || remaining[0] == 0) return;
    int take = Math.min(remaining[0], seatsPerRow - used[row]);
    used[row] += take;
    remaining[0] -= take;
    scatter(row + 1, maxRow, remaining);
  }
}
```

#### 16. Range Frequency Queries - [LeetCode 2080](https://leetcode.com/problems/range-frequency-queries/)

Design RangeFreqQuery so query(left, right, value) returns how many times value appears between left and right inclusive.

##### Optimized / Iterative Solution

```java
public static class Q16Optimized {
  private final Map<Integer, List<Integer>> positions = new HashMap<>();

  public Q16Optimized(int[] arr) {
    for (int index = 0; index < arr.length; index++) {
      positions.computeIfAbsent(arr[index], key -> new ArrayList<>()).add(index);
    }
  }

  public int query(int left, int right, int value) {
    List<Integer> list = positions.get(value);
    if (list == null) return 0;
    return upperBound(list, right) - lowerBound(list, left);
  }

  private int lowerBound(List<Integer> list, int target) {
    int left = 0, right = list.size();
    while (left < right) {
      int mid = left + (right - left) / 2;
      if (list.get(mid) < target) left = mid + 1;
      else right = mid;
    }
    return left;
  }

  private int upperBound(List<Integer> list, int target) {
    int left = 0, right = list.size();
    while (left < right) {
      int mid = left + (right - left) / 2;
      if (list.get(mid) <= target) left = mid + 1;
      else right = mid;
    }
    return left;
  }
}
```

##### Recursive Solution

```java
public static class Q16Recursive {
  private final Map<Integer, List<Integer>> positions = new HashMap<>();

  public Q16Recursive(int[] arr) {
    for (int index = 0; index < arr.length; index++) {
      positions.computeIfAbsent(arr[index], key -> new ArrayList<>()).add(index);
    }
  }

  public int query(int left, int right, int value) {
    List<Integer> list = positions.get(value);
    if (list == null) return 0;
    return upperBound(list, right, 0, list.size()) - lowerBound(list, left, 0, list.size());
  }

  private int lowerBound(List<Integer> list, int target, int left, int right) {
    if (left >= right) return left;
    int mid = left + (right - left) / 2;
    if (list.get(mid) < target) return lowerBound(list, target, mid + 1, right);
    return lowerBound(list, target, left, mid);
  }

  private int upperBound(List<Integer> list, int target, int left, int right) {
    if (left >= right) return left;
    int mid = left + (right - left) / 2;
    if (list.get(mid) <= target) return upperBound(list, target, mid + 1, right);
    return upperBound(list, target, left, mid);
  }
}
```

#### 17. Mutable Range Minimum Query - [Segment Tree - CP Algorithms](https://cp-algorithms.com/data_structures/segment_tree.html)

Design a data structure with update(index, value) and rangeMin(left, right) over a mutable integer array.

##### Optimized / Iterative Solution

```java
public static class Q17Optimized {
  private final int size;
  private final int[] tree;

  public Q17Optimized(int[] nums) {
    int power = 1;
    while (power < nums.length) power <<= 1;
    size = power;
    tree = new int[2 * size];
    Arrays.fill(tree, Integer.MAX_VALUE);
    for (int i = 0; i < nums.length; i++) tree[size + i] = nums[i];
    for (int i = size - 1; i > 0; i--) tree[i] = Math.min(tree[i * 2], tree[i * 2 + 1]);
  }

  public void update(int index, int value) {
    int pos = size + index;
    tree[pos] = value;
    for (pos >>= 1; pos > 0; pos >>= 1) tree[pos] = Math.min(tree[pos * 2], tree[pos * 2 + 1]);
  }

  public int rangeMin(int left, int right) {
    int answer = Integer.MAX_VALUE;
    for (left += size, right += size; left <= right; left >>= 1, right >>= 1) {
      if ((left & 1) == 1) answer = Math.min(answer, tree[left++]);
      if ((right & 1) == 0) answer = Math.min(answer, tree[right--]);
    }
    return answer;
  }
}
```

##### Recursive Solution

```java
public static class Q17Recursive {
  private final int n;
  private final int[] tree;

  public Q17Recursive(int[] nums) {
    n = nums.length;
    tree = new int[4 * n];
    build(1, 0, n - 1, nums);
  }

  public void update(int index, int value) {
    update(1, 0, n - 1, index, value);
  }

  public int rangeMin(int left, int right) {
    return query(1, 0, n - 1, left, right);
  }

  private void build(int node, int left, int right, int[] nums) {
    if (left == right) {
      tree[node] = nums[left];
      return;
    }
    int mid = left + (right - left) / 2;
    build(node * 2, left, mid, nums);
    build(node * 2 + 1, mid + 1, right, nums);
    tree[node] = Math.min(tree[node * 2], tree[node * 2 + 1]);
  }

  private void update(int node, int left, int right, int index, int value) {
    if (left == right) {
      tree[node] = value;
      return;
    }
    int mid = left + (right - left) / 2;
    if (index <= mid) update(node * 2, left, mid, index, value);
    else update(node * 2 + 1, mid + 1, right, index, value);
    tree[node] = Math.min(tree[node * 2], tree[node * 2 + 1]);
  }

  private int query(int node, int left, int right, int ql, int qr) {
    if (qr < left || right < ql) return Integer.MAX_VALUE;
    if (ql <= left && right <= qr) return tree[node];
    int mid = left + (right - left) / 2;
    return Math.min(query(node * 2, left, mid, ql, qr), query(node * 2 + 1, mid + 1, right, ql, qr));
  }
}
```

#### 18. Mutable Range Maximum Query - [Segment Tree - CP Algorithms](https://cp-algorithms.com/data_structures/segment_tree.html)

Design a data structure with update(index, value) and rangeMax(left, right) over a mutable integer array.

##### Optimized / Iterative Solution

```java
public static class Q18Optimized {
  private final int size;
  private final int[] tree;

  public Q18Optimized(int[] nums) {
    int power = 1;
    while (power < nums.length) power <<= 1;
    size = power;
    tree = new int[2 * size];
    Arrays.fill(tree, Integer.MIN_VALUE);
    for (int i = 0; i < nums.length; i++) tree[size + i] = nums[i];
    for (int i = size - 1; i > 0; i--) tree[i] = Math.max(tree[i * 2], tree[i * 2 + 1]);
  }

  public void update(int index, int value) {
    int pos = size + index;
    tree[pos] = value;
    for (pos >>= 1; pos > 0; pos >>= 1) tree[pos] = Math.max(tree[pos * 2], tree[pos * 2 + 1]);
  }

  public int rangeMax(int left, int right) {
    int answer = Integer.MIN_VALUE;
    for (left += size, right += size; left <= right; left >>= 1, right >>= 1) {
      if ((left & 1) == 1) answer = Math.max(answer, tree[left++]);
      if ((right & 1) == 0) answer = Math.max(answer, tree[right--]);
    }
    return answer;
  }
}
```

##### Recursive Solution

```java
public static class Q18Recursive {
  private final int n;
  private final int[] tree;

  public Q18Recursive(int[] nums) {
    n = nums.length;
    tree = new int[4 * n];
    build(1, 0, n - 1, nums);
  }

  public void update(int index, int value) {
    update(1, 0, n - 1, index, value);
  }

  public int rangeMax(int left, int right) {
    return query(1, 0, n - 1, left, right);
  }

  private void build(int node, int left, int right, int[] nums) {
    if (left == right) {
      tree[node] = nums[left];
      return;
    }
    int mid = left + (right - left) / 2;
    build(node * 2, left, mid, nums);
    build(node * 2 + 1, mid + 1, right, nums);
    tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);
  }

  private void update(int node, int left, int right, int index, int value) {
    if (left == right) {
      tree[node] = value;
      return;
    }
    int mid = left + (right - left) / 2;
    if (index <= mid) update(node * 2, left, mid, index, value);
    else update(node * 2 + 1, mid + 1, right, index, value);
    tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);
  }

  private int query(int node, int left, int right, int ql, int qr) {
    if (qr < left || right < ql) return Integer.MIN_VALUE;
    if (ql <= left && right <= qr) return tree[node];
    int mid = left + (right - left) / 2;
    return Math.max(query(node * 2, left, mid, ql, qr), query(node * 2 + 1, mid + 1, right, ql, qr));
  }
}
```

#### 19. Lazy Propagation Range Add - [Lazy Segment Tree - CP Algorithms](https://cp-algorithms.com/data_structures/segment_tree.html#range-updates-lazy-propagation)

Design RangeAddSum with addRange(left,right,delta) and sumRange(left,right) over a mutable integer array.

##### Optimized / Iterative Solution

```java
public static class Q19Optimized {
  private final int size;
  private final int height;
  private final long[] tree;
  private final long[] lazy;
  private final int[] length;

  public Q19Optimized(int[] nums) {
    int power = 1;
    int levels = 0;
    while (power < nums.length) {
      power <<= 1;
      levels++;
    }
    size = power;
    height = levels;
    tree = new long[2 * size];
    lazy = new long[2 * size];
    length = new int[2 * size];
    for (int i = 0; i < size; i++) length[size + i] = i < nums.length ? 1 : 0;
    for (int i = size - 1; i > 0; i--) length[i] = length[i * 2] + length[i * 2 + 1];
    for (int i = 0; i < nums.length; i++) tree[size + i] = nums[i];
    for (int i = size - 1; i > 0; i--) pull(i);
  }

  public void addRange(int left, int right, long delta) {
    int l = left + size;
    int r = right + size;
    int originalLeft = l;
    int originalRight = r;
    pushPath(l);
    pushPath(r);
    while (l <= r) {
      if ((l & 1) == 1) apply(l++, delta);
      if ((r & 1) == 0) apply(r--, delta);
      l >>= 1;
      r >>= 1;
    }
    pullPath(originalLeft);
    pullPath(originalRight);
  }

  public long sumRange(int left, int right) {
    int l = left + size;
    int r = right + size;
    pushPath(l);
    pushPath(r);
    long sum = 0;
    while (l <= r) {
      if ((l & 1) == 1) sum += tree[l++];
      if ((r & 1) == 0) sum += tree[r--];
      l >>= 1;
      r >>= 1;
    }
    return sum;
  }

  private void apply(int node, long delta) {
    tree[node] += delta * length[node];
    if (node < size) lazy[node] += delta;
  }

  private void push(int node) {
    if (lazy[node] == 0) return;
    apply(node * 2, lazy[node]);
    apply(node * 2 + 1, lazy[node]);
    lazy[node] = 0;
  }

  private void pushPath(int node) {
    for (int level = height; level > 0; level--) push(node >> level);
  }

  private void pull(int node) {
    tree[node] = tree[node * 2] + tree[node * 2 + 1] + lazy[node] * length[node];
  }

  private void pullPath(int node) {
    for (node >>= 1; node > 0; node >>= 1) pull(node);
  }
}
```

##### Recursive Solution

```java
public static class Q19Recursive {
  private final int n;
  private final long[] tree;
  private final long[] lazy;

  public Q19Recursive(int[] nums) {
    n = nums.length;
    tree = new long[4 * n];
    lazy = new long[4 * n];
    build(1, 0, n - 1, nums);
  }

  public void addRange(int left, int right, long delta) {
    add(1, 0, n - 1, left, right, delta);
  }

  public long sumRange(int left, int right) {
    return query(1, 0, n - 1, left, right);
  }

  private void build(int node, int left, int right, int[] nums) {
    if (left == right) {
      tree[node] = nums[left];
      return;
    }
    int mid = left + (right - left) / 2;
    build(node * 2, left, mid, nums);
    build(node * 2 + 1, mid + 1, right, nums);
    tree[node] = tree[node * 2] + tree[node * 2 + 1];
  }

  private void add(int node, int left, int right, int ql, int qr, long delta) {
    if (qr < left || right < ql) return;
    if (ql <= left && right <= qr) {
      tree[node] += delta * (right - left + 1L);
      lazy[node] += delta;
      return;
    }
    push(node, left, right);
    int mid = left + (right - left) / 2;
    add(node * 2, left, mid, ql, qr, delta);
    add(node * 2 + 1, mid + 1, right, ql, qr, delta);
    tree[node] = tree[node * 2] + tree[node * 2 + 1];
  }

  private long query(int node, int left, int right, int ql, int qr) {
    if (qr < left || right < ql) return 0;
    if (ql <= left && right <= qr) return tree[node];
    push(node, left, right);
    int mid = left + (right - left) / 2;
    return query(node * 2, left, mid, ql, qr) + query(node * 2 + 1, mid + 1, right, ql, qr);
  }

  private void push(int node, int left, int right) {
    if (lazy[node] == 0 || left == right) return;
    int mid = left + (right - left) / 2;
    apply(node * 2, left, mid, lazy[node]);
    apply(node * 2 + 1, mid + 1, right, lazy[node]);
    lazy[node] = 0;
  }

  private void apply(int node, int left, int right, long delta) {
    tree[node] += delta * (right - left + 1L);
    lazy[node] += delta;
  }
}
```

#### 20. Lazy Propagation Range Assign - [Lazy Segment Tree - CP Algorithms](https://cp-algorithms.com/data_structures/segment_tree.html#assignment-on-segments)

Design RangeAssignSum with assignRange(left,right,value) and sumRange(left,right) over a mutable integer array.

##### Optimized / Iterative Solution

```java
public static class Q20Optimized {
  private final int size;
  private final int height;
  private final long[] tree;
  private final long[] lazyValue;
  private final boolean[] hasLazy;
  private final int[] length;

  public Q20Optimized(int[] nums) {
    int power = 1;
    int levels = 0;
    while (power < nums.length) {
      power <<= 1;
      levels++;
    }
    size = power;
    height = levels;
    tree = new long[2 * size];
    lazyValue = new long[2 * size];
    hasLazy = new boolean[2 * size];
    length = new int[2 * size];
    for (int i = 0; i < size; i++) length[size + i] = i < nums.length ? 1 : 0;
    for (int i = size - 1; i > 0; i--) length[i] = length[i * 2] + length[i * 2 + 1];
    for (int i = 0; i < nums.length; i++) tree[size + i] = nums[i];
    for (int i = size - 1; i > 0; i--) pull(i);
  }

  public void assignRange(int left, int right, long value) {
    int l = left + size;
    int r = right + size;
    int originalLeft = l;
    int originalRight = r;
    pushPath(l);
    pushPath(r);
    while (l <= r) {
      if ((l & 1) == 1) apply(l++, value);
      if ((r & 1) == 0) apply(r--, value);
      l >>= 1;
      r >>= 1;
    }
    pullPath(originalLeft);
    pullPath(originalRight);
  }

  public long sumRange(int left, int right) {
    int l = left + size;
    int r = right + size;
    pushPath(l);
    pushPath(r);
    long sum = 0;
    while (l <= r) {
      if ((l & 1) == 1) sum += tree[l++];
      if ((r & 1) == 0) sum += tree[r--];
      l >>= 1;
      r >>= 1;
    }
    return sum;
  }

  private void apply(int node, long value) {
    tree[node] = value * length[node];
    if (node < size) {
      lazyValue[node] = value;
      hasLazy[node] = true;
    }
  }

  private void push(int node) {
    if (!hasLazy[node]) return;
    apply(node * 2, lazyValue[node]);
    apply(node * 2 + 1, lazyValue[node]);
    hasLazy[node] = false;
  }

  private void pushPath(int node) {
    for (int level = height; level > 0; level--) push(node >> level);
  }

  private void pull(int node) {
    tree[node] = hasLazy[node] ? lazyValue[node] * length[node] : tree[node * 2] + tree[node * 2 + 1];
  }

  private void pullPath(int node) {
    for (node >>= 1; node > 0; node >>= 1) pull(node);
  }
}
```

##### Recursive Solution

```java
public static class Q20Recursive {
  private final int n;
  private final long[] tree;
  private final long[] lazyValue;
  private final boolean[] hasLazy;

  public Q20Recursive(int[] nums) {
    n = nums.length;
    tree = new long[4 * n];
    lazyValue = new long[4 * n];
    hasLazy = new boolean[4 * n];
    build(1, 0, n - 1, nums);
  }

  public void assignRange(int left, int right, long value) {
    assign(1, 0, n - 1, left, right, value);
  }

  public long sumRange(int left, int right) {
    return query(1, 0, n - 1, left, right);
  }

  private void build(int node, int left, int right, int[] nums) {
    if (left == right) {
      tree[node] = nums[left];
      return;
    }
    int mid = left + (right - left) / 2;
    build(node * 2, left, mid, nums);
    build(node * 2 + 1, mid + 1, right, nums);
    tree[node] = tree[node * 2] + tree[node * 2 + 1];
  }

  private void assign(int node, int left, int right, int ql, int qr, long value) {
    if (qr < left || right < ql) return;
    if (ql <= left && right <= qr) {
      apply(node, left, right, value);
      return;
    }
    push(node, left, right);
    int mid = left + (right - left) / 2;
    assign(node * 2, left, mid, ql, qr, value);
    assign(node * 2 + 1, mid + 1, right, ql, qr, value);
    tree[node] = tree[node * 2] + tree[node * 2 + 1];
  }

  private long query(int node, int left, int right, int ql, int qr) {
    if (qr < left || right < ql) return 0;
    if (ql <= left && right <= qr) return tree[node];
    push(node, left, right);
    int mid = left + (right - left) / 2;
    return query(node * 2, left, mid, ql, qr) + query(node * 2 + 1, mid + 1, right, ql, qr);
  }

  private void apply(int node, int left, int right, long value) {
    tree[node] = value * (right - left + 1L);
    lazyValue[node] = value;
    hasLazy[node] = true;
  }

  private void push(int node, int left, int right) {
    if (!hasLazy[node] || left == right) return;
    int mid = left + (right - left) / 2;
    apply(node * 2, left, mid, lazyValue[node]);
    apply(node * 2 + 1, mid + 1, right, lazyValue[node]);
    hasLazy[node] = false;
  }
}
```

## Topic 28: Advanced Graphs

### Moderate (13-20)

#### 13. Find the City With the Smallest Number of Neighbors - [LeetCode 1334](https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/)

Given weighted undirected edges and a distance threshold, return the city with the fewest reachable cities within the threshold, breaking ties by larger city index.

##### Optimized / Iterative Solution

```java
public static class Q13Optimized {
  public int findTheCity(int n, int[][] edges, int distanceThreshold) {
    int[][] dist = new int[n][n];
    for (int[] row : dist) Arrays.fill(row, 1_000_000_000);
    for (int i = 0; i < n; i++) dist[i][i] = 0;
    for (int[] edge : edges) {
      dist[edge[0]][edge[1]] = Math.min(dist[edge[0]][edge[1]], edge[2]);
      dist[edge[1]][edge[0]] = Math.min(dist[edge[1]][edge[0]], edge[2]);
    }
    for (int mid = 0; mid < n; mid++) for (int from = 0; from < n; from++) for (int to = 0; to < n; to++) {
      dist[from][to] = Math.min(dist[from][to], dist[from][mid] + dist[mid][to]);
    }
    int answer = -1, bestCount = Integer.MAX_VALUE;
    for (int city = 0; city < n; city++) {
      int count = 0;
      for (int other = 0; other < n; other++) if (other != city && dist[city][other] <= distanceThreshold) count++;
      if (count <= bestCount) { bestCount = count; answer = city; }
    }
    return answer;
  }
}
```

##### Recursive Solution

```java
public static class Q13Recursive {
  private int[][][] memo;
  private int n;

  public int findTheCity(int n, int[][] edges, int distanceThreshold) {
    this.n = n;
    memo = new int[n][n][n + 1];
    for (int[][] layer : memo) for (int[] row : layer) Arrays.fill(row, -1);
    int[][] direct = new int[n][n];
    for (int[] row : direct) Arrays.fill(row, 1_000_000_000);
    for (int i = 0; i < n; i++) direct[i][i] = 0;
    for (int[] edge : edges) { direct[edge[0]][edge[1]] = Math.min(direct[edge[0]][edge[1]], edge[2]); direct[edge[1]][edge[0]] = Math.min(direct[edge[1]][edge[0]], edge[2]); }
    this.direct = direct;
    int answer = -1, best = Integer.MAX_VALUE;
    for (int city = 0; city < n; city++) {
      int count = 0;
      for (int other = 0; other < n; other++) if (other != city && shortest(city, other, n) <= distanceThreshold) count++;
      if (count <= best) { best = count; answer = city; }
    }
    return answer;
  }

  private int[][] direct;

  private int shortest(int from, int to, int k) {
    if (k == 0) return direct[from][to];
    if (memo[from][to][k] != -1) return memo[from][to][k];
    int without = shortest(from, to, k - 1);
    int through = shortest(from, k - 1, k - 1) + shortest(k - 1, to, k - 1);
    return memo[from][to][k] = Math.min(without, through);
  }
}
```

#### 14. Critical Connections in a Network - [LeetCode 1192](https://leetcode.com/problems/critical-connections-in-a-network/)

Given an undirected connected network, return all critical connections whose removal disconnects the graph.

##### Optimized / Iterative Solution

```java
public static class Q14Optimized {
  private List<Integer>[] graph;
  private int[] disc;
  private int[] low;
  private int time;
  private List<List<Integer>> bridges;

  public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
    graph = new ArrayList[n];
    for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    for (List<Integer> edge : connections) { graph[edge.get(0)].add(edge.get(1)); graph[edge.get(1)].add(edge.get(0)); }
    disc = new int[n]; low = new int[n]; bridges = new ArrayList<>();
    dfs(0, -1);
    return bridges;
  }

  private void dfs(int node, int parent) {
    disc[node] = low[node] = ++time;
    for (int next : graph[node]) {
      if (next == parent) continue;
      if (disc[next] == 0) {
        dfs(next, node);
        low[node] = Math.min(low[node], low[next]);
        if (low[next] > disc[node]) bridges.add(Arrays.asList(node, next));
      } else {
        low[node] = Math.min(low[node], disc[next]);
      }
    }
  }
}
```

##### Recursive Solution

```java
public static class Q14Recursive {
  private List<Integer>[] graph;
  private int[] disc;
  private int[] low;
  private int time;
  private List<List<Integer>> answer;

  public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
    graph = new ArrayList[n];
    for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    for (List<Integer> edge : connections) { graph[edge.get(0)].add(edge.get(1)); graph[edge.get(1)].add(edge.get(0)); }
    disc = new int[n];
    low = new int[n];
    answer = new ArrayList<>();
    dfs(0, -1);
    return answer;
  }

  private void dfs(int node, int parent) {
    disc[node] = low[node] = ++time;
    for (int next : graph[node]) {
      if (next == parent) continue;
      if (disc[next] == 0) {
        dfs(next, node);
        low[node] = Math.min(low[node], low[next]);
        if (low[next] > disc[node]) answer.add(Arrays.asList(node, next));
      } else low[node] = Math.min(low[node], disc[next]);
    }
  }
}
```

#### 15. Number of Connected Components in an Undirected Graph - [LeetCode 323](https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/)

Given n nodes and undirected edges, return the number of connected components.

##### Optimized / Iterative Solution

```java
public static class Q15Optimized {
  public int countComponents(int n, int[][] edges) {
    DSU dsu = new DSU(n);
    int components = n;
    for (int[] edge : edges) if (dsu.union(edge[0], edge[1])) components--;
    return components;
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
public static class Q15Recursive {
  public int countComponents(int n, int[][] edges) {
    List<Integer>[] graph = new ArrayList[n];
    for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    for (int[] edge : edges) { graph[edge[0]].add(edge[1]); graph[edge[1]].add(edge[0]); }
    boolean[] seen = new boolean[n];
    int count = 0;
    for (int node = 0; node < n; node++) {
      if (!seen[node]) {
        count++;
        dfs(node, graph, seen);
      }
    }
    return count;
  }

  private void dfs(int node, List<Integer>[] graph, boolean[] seen) {
    seen[node] = true;
    for (int next : graph[node]) if (!seen[next]) dfs(next, graph, seen);
  }
}
```

#### 16. Kosaraju SCC Count - [Kosaraju Algorithm - GeeksforGeeks](https://www.geeksforgeeks.org/kosarajus-algorithm-in-java/)

Given a directed graph with n nodes and edges [u,v], return the number of strongly connected components.

##### Optimized / Iterative Solution

```java
public static class Q16Optimized {
  public int countSCC(int n, int[][] edges) {
    List<Integer>[] graph = build(n, edges, false);
    List<Integer>[] reverse = build(n, edges, true);
    boolean[] seen = new boolean[n];
    List<Integer> order = new ArrayList<>();
    for (int node = 0; node < n; node++) if (!seen[node]) dfsOrder(node, graph, seen, order);
    Arrays.fill(seen, false);
    int count = 0;
    for (int i = order.size() - 1; i >= 0; i--) {
      int node = order.get(i);
      if (!seen[node]) {
        count++;
        mark(node, reverse, seen);
      }
    }
    return count;
  }

  private void dfsOrder(int node, List<Integer>[] graph, boolean[] seen, List<Integer> order) {
    seen[node] = true;
    for (int next : graph[node]) if (!seen[next]) dfsOrder(next, graph, seen, order);
    order.add(node);
  }

  private void mark(int start, List<Integer>[] graph, boolean[] seen) {
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(start); seen[start] = true;
    while (!stack.isEmpty()) for (int next : graph[stack.pop()]) if (!seen[next]) { seen[next] = true; stack.push(next); }
  }

  private List<Integer>[] build(int n, int[][] edges, boolean rev) {
    List<Integer>[] graph = new ArrayList[n];
    for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    for (int[] edge : edges) graph[rev ? edge[1] : edge[0]].add(rev ? edge[0] : edge[1]);
    return graph;
  }
}
```

##### Recursive Solution

```java
public static class Q16Recursive {
  public int countSCC(int n, int[][] edges) {
    List<Integer>[] graph = build(n, edges, false);
    List<Integer>[] reverse = build(n, edges, true);
    boolean[] seen = new boolean[n];
    List<Integer> order = new ArrayList<>();
    for (int node = 0; node < n; node++) if (!seen[node]) finish(node, graph, seen, order);
    Arrays.fill(seen, false);
    return collect(order.size() - 1, order, reverse, seen, 0);
  }

  private int collect(int index, List<Integer> order, List<Integer>[] reverse, boolean[] seen, int count) {
    if (index < 0) return count;
    int node = order.get(index);
    if (!seen[node]) {
      mark(node, reverse, seen);
      return collect(index - 1, order, reverse, seen, count + 1);
    }
    return collect(index - 1, order, reverse, seen, count);
  }

  private void finish(int node, List<Integer>[] graph, boolean[] seen, List<Integer> order) {
    seen[node] = true;
    for (int next : graph[node]) if (!seen[next]) finish(next, graph, seen, order);
    order.add(node);
  }

  private void mark(int node, List<Integer>[] graph, boolean[] seen) {
    seen[node] = true;
    for (int next : graph[node]) if (!seen[next]) mark(next, graph, seen);
  }

  private List<Integer>[] build(int n, int[][] edges, boolean rev) {
    List<Integer>[] graph = new ArrayList[n];
    for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    for (int[] edge : edges) graph[rev ? edge[1] : edge[0]].add(rev ? edge[0] : edge[1]);
    return graph;
  }
}
```

#### 17. Tarjan SCC Count - [Tarjan SCC Algorithm - GeeksforGeeks](https://www.geeksforgeeks.org/tarjan-algorithm-find-strongly-connected-components/)

Given a directed graph, count strongly connected components using one DFS pass with low-link values.

##### Optimized / Iterative Solution

```java
public static class Q17Optimized {
  private List<Integer>[] graph;
  private int[] disc;
  private int[] low;
  private boolean[] inStack;
  private Deque<Integer> stack;
  private int time;
  private int count;

  public int countSCC(int n, int[][] edges) {
    graph = new ArrayList[n];
    for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    for (int[] edge : edges) graph[edge[0]].add(edge[1]);
    disc = new int[n]; low = new int[n]; inStack = new boolean[n]; stack = new ArrayDeque<>(); time = 0; count = 0;
    for (int node = 0; node < n; node++) if (disc[node] == 0) dfs(node);
    return count;
  }

  private void dfs(int node) {
    disc[node] = low[node] = ++time;
    stack.push(node);
    inStack[node] = true;
    for (int next : graph[node]) {
      if (disc[next] == 0) { dfs(next); low[node] = Math.min(low[node], low[next]); }
      else if (inStack[next]) low[node] = Math.min(low[node], disc[next]);
    }
    if (low[node] == disc[node]) {
      while (true) {
        int top = stack.pop();
        inStack[top] = false;
        if (top == node) break;
      }
      count++;
    }
  }
}
```

##### Recursive Solution

```java
public static class Q17Recursive {
  private List<Integer>[] graph;
  private int[] disc;
  private int[] low;
  private boolean[] active;
  private Deque<Integer> stack;
  private int time;
  private int count;

  public int countSCC(int n, int[][] edges) {
    graph = new ArrayList[n];
    for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    for (int[] edge : edges) graph[edge[0]].add(edge[1]);
    disc = new int[n]; low = new int[n]; active = new boolean[n]; stack = new ArrayDeque<>(); time = 0; count = 0;
    for (int node = 0; node < n; node++) if (disc[node] == 0) dfs(node);
    return count;
  }

  private void dfs(int node) {
    disc[node] = low[node] = ++time;
    stack.push(node);
    active[node] = true;
    for (int next : graph[node]) {
      if (disc[next] == 0) {
        dfs(next);
        low[node] = Math.min(low[node], low[next]);
      } else if (active[next]) low[node] = Math.min(low[node], disc[next]);
    }
    if (low[node] == disc[node]) popComponent(node);
  }

  private void popComponent(int root) {
    int top = stack.pop();
    active[top] = false;
    if (top != root) popComponent(root);
    else count++;
  }
}
```

#### 18. Minimum Spanning Tree Kruskal - [Kruskal MST - CP Algorithms](https://cp-algorithms.com/graph/mst_kruskal.html)

Given n nodes and undirected weighted edges [u,v,w], return the minimum spanning tree weight, or -1 if the graph is disconnected.

##### Optimized / Iterative Solution

```java
public static class Q18Optimized {
  public int minimumSpanningTree(int n, int[][] edges) {
    Arrays.sort(edges, Comparator.comparingInt(a -> a[2]));
    DSU dsu = new DSU(n);
    int cost = 0, used = 0;
    for (int[] edge : edges) {
      if (dsu.union(edge[0], edge[1])) {
        cost += edge[2];
        if (++used == n - 1) return cost;
      }
    }
    return n == 1 ? 0 : -1;
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
public static class Q18Recursive {
  public int minimumSpanningTree(int n, int[][] edges) {
    Arrays.sort(edges, Comparator.comparingInt(a -> a[2]));
    int cost = scan(0, 0, 0, n, edges, new DSU(n));
    return cost == Integer.MAX_VALUE ? -1 : cost;
  }

  private int scan(int index, int used, int cost, int n, int[][] edges, DSU dsu) {
    if (n == 1) return 0;
    if (used == n - 1) return cost;
    if (index == edges.length) return Integer.MAX_VALUE;
    int[] edge = edges[index];
    if (dsu.union(edge[0], edge[1])) return scan(index + 1, used + 1, cost + edge[2], n, edges, dsu);
    return scan(index + 1, used, cost, n, edges, dsu);
  }

  private static class DSU {
    int[] parent;
    DSU(int n) { parent = new int[n]; for (int i = 0; i < n; i++) parent[i] = i; }
    int find(int x) { if (parent[x] != x) parent[x] = find(parent[x]); return parent[x]; }
    boolean union(int a, int b) { int pa = find(a), pb = find(b); if (pa == pb) return false; parent[pa] = pb; return true; }
  }
}
```

#### 19. Minimum Spanning Tree Prim - [Prim MST - CP Algorithms](https://cp-algorithms.com/graph/mst_prim.html)

Given n nodes and undirected weighted edges, return the MST weight using Prim-style frontier expansion, or -1 if disconnected.

##### Optimized / Iterative Solution

```java
public static class Q19Optimized {
  public int minimumSpanningTree(int n, int[][] edges) {
    if (n == 1) return 0;
    List<int[]>[] graph = new ArrayList[n];
    for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    for (int[] edge : edges) { graph[edge[0]].add(new int[] {edge[1], edge[2]}); graph[edge[1]].add(new int[] {edge[0], edge[2]}); }
    boolean[] used = new boolean[n];
    PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
    heap.offer(new int[] {0, 0});
    int cost = 0, count = 0;
    while (!heap.isEmpty()) {
      int[] current = heap.poll();
      int node = current[0];
      if (used[node]) continue;
      used[node] = true;
      cost += current[1];
      count++;
      for (int[] edge : graph[node]) if (!used[edge[0]]) heap.offer(edge);
    }
    return count == n ? cost : -1;
  }
}
```

##### Recursive Solution

```java
public static class Q19Recursive {
  public int minimumSpanningTree(int n, int[][] edges) {
    if (n == 1) return 0;
    boolean[] used = new boolean[n];
    used[0] = true;
    return addNext(1, 0, used, edges, n);
  }

  private int addNext(int count, int cost, boolean[] used, int[][] edges, int n) {
    if (count == n) return cost;
    int best = -1;
    for (int i = 0; i < edges.length; i++) {
      if ((used[edges[i][0]] ^ used[edges[i][1]]) && (best == -1 || edges[i][2] < edges[best][2])) best = i;
    }
    if (best == -1) return -1;
    used[edges[best][0]] = true;
    used[edges[best][1]] = true;
    return addNext(count + 1, cost + edges[best][2], used, edges, n);
  }
}
```

#### 20. Minimum Obstacle Removal to Reach Corner - [LeetCode 2290](https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/)

Given a grid of 0 empty cells and 1 obstacles, return the minimum obstacles to remove to travel from top-left to bottom-right.

##### Optimized / Iterative Solution

```java
public static class Q20Optimized {
  private final int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

  public int minimumObstacles(int[][] grid) {
    int rows = grid.length, cols = grid[0].length;
    int[][] dist = new int[rows][cols];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
    Deque<int[]> deque = new ArrayDeque<>();
    dist[0][0] = 0;
    deque.offerFirst(new int[] {0, 0});
    while (!deque.isEmpty()) {
      int[] current = deque.pollFirst();
      int row = current[0], col = current[1];
      for (int[] dir : dirs) {
        int nr = row + dir[0], nc = col + dir[1];
        if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) continue;
        int next = dist[row][col] + grid[nr][nc];
        if (next < dist[nr][nc]) {
          dist[nr][nc] = next;
          if (grid[nr][nc] == 0) deque.offerFirst(new int[] {nr, nc});
          else deque.offerLast(new int[] {nr, nc});
        }
      }
    }
    return dist[rows - 1][cols - 1];
  }
}
```

##### Recursive Solution

```java
public static class Q20Recursive {
  private final int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
  private int[][] dist;

  public int minimumObstacles(int[][] grid) {
    dist = new int[grid.length][grid[0].length];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
    relax(0, 0, 0, grid);
    return dist[grid.length - 1][grid[0].length - 1];
  }

  private void relax(int row, int col, int cost, int[][] grid) {
    if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length || cost >= dist[row][col]) return;
    dist[row][col] = cost;
    for (int[] dir : dirs) {
      int nr = row + dir[0], nc = col + dir[1];
      if (nr >= 0 && nc >= 0 && nr < grid.length && nc < grid[0].length) relax(nr, nc, cost + grid[nr][nc], grid);
    }
  }
}
```
