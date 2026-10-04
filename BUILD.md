# Build and use

This Maven project targets Java 8 bytecode. Build with JDK 9 or newer and Maven:

```sh
mvn clean package
java -jar target/tree-workbook-1.0.0.jar
```

The first build downloads Maven plugins and test dependencies. Production solutions use the Java standard library.

| Workbook topic | Basic class (1–12) | Moderate class (13–20) |
| --- | --- | --- |
| 12 Trees | TreesBasic | TreesModerate |
| 13 Binary Search Tree | BinarySearchTreeBasic | BinarySearchTreeModerate |
| 16 Trie | TrieBasic | TrieModerate |
| 14 Graphs | GraphsBasic | GraphsModerate |
| 27 Segment Tree / Fenwick Tree | SegmentTreeFenwickBasic | SegmentTreeFenwickModerate |
| 28 Advanced Graphs | AdvancedGraphsBasic | AdvancedGraphsModerate |

Classes are in `com.interview.tree.modules`. Each question has nested `Q01Optimized` and `Q01Recursive` classes (with the corresponding question number) to isolate helper methods and stateful data structures. For example:

```java
import com.interview.tree.model.TreeNode;
import com.interview.tree.modules.TreesBasic;

TreeNode root = new TreeNode(2, new TreeNode(1), new TreeNode(3));
int depth = new TreesBasic.Q01Optimized().maxDepth(root);
int recursiveDepth = new TreesBasic.Q01Recursive().maxDepth(root);
```

`README-BASIC.md` contains questions 1–12 from each topic; `README-MODERATE.md` contains questions 13–20. Each question includes both optimized and recursive solutions. Both contain topic headings, questions, solutions, and source/LeetCode links. Constraints, examples, and complexity are in Java comments. Some optimized algorithms naturally retain recursive helpers. Recursive alternatives can have higher complexity or exhaust the Java stack on deep inputs; consult each question's complexity comments and constraints.

Questions and starting implementations are adapted from [DSA Pattern Workbook](https://maurya29.github.io/DSA-Pattern-Workbook/dsa.html), using its first 20 questions in each selected module, retrieved October 4, 2026. Source links and metadata are retained in `workbook-data`. Adaptations include shared node models, isolated implementation classes, resetting mutable recursive state between calls, and null-safe iterative BST range traversal.

Run `mvn test` for 240 source-example solution checks and regression checks covering repeated calls, missing tree children, and randomized lazy range updates against a reference array. These checks do not prove correctness for every possible input.

To regenerate sources and README files from the saved data, run `python tools/build_workbook.py`, then `python tools/build_tests.py`. Changes to generated implementations should be made in the generator's adaptation function or the saved data so regeneration preserves them.
