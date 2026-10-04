package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 16: Trie. Basic questions 1-12. */
public class TrieBasic {

  /*
   * Question 1: Implement Trie Prefix Tree
   * 
   * Question: Design a trie that supports inserting words, searching complete words, and checking whether any word starts with a given prefix.
   * 
   * Constraints: 1 <= word.length, prefix.length <= 2000; calls contain lowercase English letters; at most 30000 operations.
   * 
   * Optimized complexity: Time O(L) per operation; Space O(total characters). Each character moves one trie edge.
   * Recursive complexity: Time O(L) per operation; Space O(L) call stack. Recursive helpers process one character per call.
   * 
   * Example 1:
   * Input: insert("apple"), search("apple"), search("app"), startsWith("app")
   * Output: true, false, true
   * Explanation: app is only a prefix until it is inserted.
   * 
   * Example 2:
   * Input: insert("app"), search("app")
   * Output: true
   * Explanation: The terminal flag is set on the app node.
   * 
   * Example 3:
   * Input: startsWith("banana") on empty trie
   * Output: false
   * Explanation: The first character path is missing.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 2: Design Add and Search Words Data Structure
   * 
   * Question: Design a data structure that adds lowercase words and searches words where dot can match any one letter.
   * 
   * Constraints: 1 <= word.length <= 25; calls contain lowercase letters and dots in search; at most 10000 calls.
   * 
   * Optimized complexity: Time add O(L), search O(26^d * L) worst case where d is wildcard count; Space O(total characters).
   * Recursive complexity: Time add O(L), search O(26^d * L); Space O(L) call stack for one search path.
   * 
   * Example 1:
   * Input: addWord("bad"), addWord("dad"), search("pad")
   * Output: false
   * Explanation: No stored word matches pad.
   * 
   * Example 2:
   * Input: search(".ad")
   * Output: true
   * Explanation: The dot can match b or d.
   * 
   * Example 3:
   * Input: search("b..")
   * Output: true
   * Explanation: Both dots match the remaining letters of bad.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 3: Word Search II
   * 
   * Question: Given a board of letters and a list of words, return every word that can be formed by adjacent horizontal or vertical cells without reusing a cell.
   * 
   * Constraints: 1 <= board rows, cols <= 12; 1 <= words.length <= 30000; words contain lowercase English letters.
   * 
   * Optimized complexity: Time O(mn * 4^L) with prefix pruning; Space O(total word characters). Trie nodes cut dead paths early.
   * Recursive complexity: Time O(mn * 4^L) with trie pruning; Space O(total word characters + L) including recursion stack.
   * 
   * Example 1:
   * Input: board = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]], words = ["oath","pea","eat","rain"]
   * Output: ["oath","eat"]
   * Explanation: Only oath and eat can be formed on valid adjacent paths.
   * 
   * Example 2:
   * Input: board = [["a","b"],["c","d"]], words = ["abcb"]
   * Output: []
   * Explanation: The path would need to reuse b.
   * 
   * Example 3:
   * Input: board = [["a"]], words = ["a"]
   * Output: ["a"]
   * Explanation: The single cell forms the word.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 4: Replace Words
   * 
   * Question: Given dictionary roots and a sentence, replace each word with the shortest root that is its prefix when such a root exists.
   * 
   * Constraints: 1 <= dictionary.length <= 1000; 1 <= sentence.length <= 100000; words contain lowercase English letters.
   * 
   * Optimized complexity: Time O(total root chars + sentence chars); Space O(total root chars). Trie lookup stops at shortest terminal root.
   * Recursive complexity: Time O(total root chars + sentence chars); Space O(total root chars + L) including recursion stack.
   * 
   * Example 1:
   * Input: dictionary = ["cat","bat","rat"], sentence = "the cattle was rattled by the battery"
   * Output: "the cat was rat by the bat"
   * Explanation: cattle, rattled, and battery use their shortest roots.
   * 
   * Example 2:
   * Input: dictionary = ["a","aa","aaa"], sentence = "a aa aaaa"
   * Output: "a a a"
   * Explanation: The shortest root a wins each time.
   * 
   * Example 3:
   * Input: dictionary = ["x"], sentence = "hello world"
   * Output: "hello world"
   * Explanation: No word has x as a prefix.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 5: Map Sum Pairs
   * 
   * Question: Design a structure that inserts key-value pairs and returns the sum of values of all keys with a given prefix.
   * 
   * Constraints: 1 <= key.length, prefix.length <= 50; 1 <= val <= 1000; at most 50 operations in the original problem.
   * 
   * Optimized complexity: Time insert O(L), sum O(P); Space O(total key chars). Each trie node stores the aggregate prefix sum.
   * Recursive complexity: Time insert O(L), sum O(P); Space O(L) call stack for update and lookup.
   * 
   * Example 1:
   * Input: insert("apple", 3), sum("ap")
   * Output: 3
   * Explanation: apple contributes 3 to prefix ap.
   * 
   * Example 2:
   * Input: insert("app", 2), sum("ap")
   * Output: 5
   * Explanation: apple and app both match ap.
   * 
   * Example 3:
   * Input: insert("apple", 2), sum("apple")
   * Output: 2
   * Explanation: Overwriting apple changes its contribution from 3 to 2.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 6: Longest Word in Dictionary
   * 
   * Question: Given a list of words, return the longest word that can be built one character at a time by other words in the list. Return the lexicographically smallest answer on ties.
   * 
   * Constraints: 1 <= words.length <= 1000; 1 <= words[i].length <= 30; words contain lowercase English letters.
   * 
   * Optimized complexity: Time O(nL + nodes); Space O(nL). Trie DFS visits only terminal-prefix paths.
   * Recursive complexity: Time O(nL + nodes); Space O(nL + L). Recursive DFS builds candidates through terminal nodes.
   * 
   * Example 1:
   * Input: words = ["w","wo","wor","worl","world"]
   * Output: "world"
   * Explanation: Every prefix of world is present.
   * 
   * Example 2:
   * Input: words = ["a","banana","app","appl","ap","apply","apple"]
   * Output: "apple"
   * Explanation: apple and apply have length 5; apple is lexicographically smaller.
   * 
   * Example 3:
   * Input: words = ["abc","bc"]
   * Output: ""
   * Explanation: No word can be built from single-character prefixes.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 7: Stream of Characters
   * 
   * Question: Design a stream checker that receives one character at a time and returns true if any suffix of the stream equals a dictionary word.
   * 
   * Constraints: 1 <= words.length <= 2000; 1 <= words[i].length <= 200; at most 40000 query calls.
   * 
   * Optimized complexity: Time O(max word length) per query; Space O(total word chars + max word length). Reverse trie stops at first broken suffix.
   * Recursive complexity: Time O(max word length) per query; Space O(total word chars + max word length) including recursion.
   * 
   * Example 1:
   * Input: words = ["cd","f","kl"], queries = a,b,c,d
   * Output: false,false,false,true
   * Explanation: The suffix cd appears after reading d.
   * 
   * Example 2:
   * Input: queries continue with f
   * Output: true
   * Explanation: The single-character word f matches the latest suffix.
   * 
   * Example 3:
   * Input: queries end with k,l
   * Output: false,true
   * Explanation: The suffix kl matches after reading l.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 8: Search Suggestions System
   * 
   * Question: Given product names and a search word, return up to three lexicographically smallest product suggestions after each typed character.
   * 
   * Constraints: 1 <= products.length <= 1000; 1 <= products[i].length, searchWord.length <= 3000 total input size.
   * 
   * Optimized complexity: Time O(nL log n + S); Space O(nL). Trie nodes store at most three sorted suggestions while inserting sorted products.
   * Recursive complexity: Time O(nL + S * nodes under prefix); Space O(nL + L). DFS collects up to three words from each prefix node.
   * 
   * Example 1:
   * Input: products = ["mobile","mouse","moneypot","monitor","mousepad"], searchWord = "mouse"
   * Output: [["mobile","moneypot","monitor"],["mobile","moneypot","monitor"],["mouse","mousepad"],["mouse","mousepad"],["mouse","mousepad"]]
   * Explanation: Suggestions shrink as the prefix becomes more specific.
   * 
   * Example 2:
   * Input: products = ["havana"], searchWord = "havana"
   * Output: [["havana"],["havana"],["havana"],["havana"],["havana"],["havana"]]
   * Explanation: The only product matches every prefix.
   * 
   * Example 3:
   * Input: products = ["bags","baggage","banner","box","cloths"], searchWord = "bags"
   * Output: [["baggage","bags","banner"],["baggage","bags","banner"],["baggage","bags"],["bags"]]
   * Explanation: Each prefix returns up to three sorted matches.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 9: Maximum XOR of Two Numbers in an Array
   * 
   * Question: Given an integer array, return the maximum XOR value of any two numbers in the array.
   * 
   * Constraints: 1 <= nums.length <= 200000; 0 <= nums[i] <= 2^31 - 1.
   * 
   * Optimized complexity: Time O(31n); Space O(31n). Each number inserts and queries one path in a binary trie.
   * Recursive complexity: Time O(31n); Space O(31n + 31). Recursive insert/query processes one bit per call.
   * 
   * Example 1:
   * Input: nums = [3,10,5,25,2,8]
   * Output: 28
   * Explanation: 5 XOR 25 equals 28.
   * 
   * Example 2:
   * Input: nums = [0]
   * Output: 0
   * Explanation: Only one number exists.
   * 
   * Example 3:
   * Input: nums = [14,70,53,83,49,91,36,80,92,51,66,70]
   * Output: 127
   * Explanation: The best pair creates all seven lower bits as 1.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 10: Palindrome Pairs
   * 
   * Question: Given unique words, return all index pairs [i, j] such that words[i] + words[j] is a palindrome.
   * 
   * Constraints: 1 <= words.length <= 5000; 0 <= words[i].length <= 300; words are unique lowercase strings.
   * 
   * Optimized complexity: Time O(nL^2); Space O(nL). Reversed trie stores palindrome suffix candidates.
   * Recursive complexity: Time O(nL^2); Space O(nL + L). Recursive insertion and search process one character at a time.
   * 
   * Example 1:
   * Input: words = ["abcd","dcba","lls","s","sssll"]
   * Output: [[0,1],[1,0],[3,2],[2,4]]
   * Explanation: Each listed concatenation is a palindrome.
   * 
   * Example 2:
   * Input: words = ["bat","tab","cat"]
   * Output: [[0,1],[1,0]]
   * Explanation: bat+tab and tab+bat are palindromes.
   * 
   * Example 3:
   * Input: words = ["a",""]
   * Output: [[0,1],[1,0]]
   * Explanation: The empty string pairs with any palindrome word.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 11: Concatenated Words
   * 
   * Question: Given an array of words, return all words that are formed by concatenating at least two shorter words from the same array.
   * 
   * Constraints: 1 <= words.length <= 10000; 0 <= words[i].length <= 30; lowercase English letters.
   * 
   * Optimized complexity: Time O(nL^2); Space O(nL + L). Trie DP follows only dictionary prefixes.
   * Recursive complexity: Time O(nL^2); Space O(nL + L). Memoized recursive trie segmentation stores start positions.
   * 
   * Example 1:
   * Input: words = ["cat","cats","catsdogcats","dog","dogcatsdog","hippopotamuses","rat","ratcatdogcat"]
   * Output: ["catsdogcats","dogcatsdog","ratcatdogcat"]
   * Explanation: Each returned word is composed from shorter dictionary words.
   * 
   * Example 2:
   * Input: words = ["cat","dog","catdog"]
   * Output: ["catdog"]
   * Explanation: catdog is cat + dog.
   * 
   * Example 3:
   * Input: words = ["a","aa","aaa"]
   * Output: ["aa","aaa"]
   * Explanation: aa is a+a and aaa can be a+aa.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 12: Word Squares
   * 
   * Question: Given same-length words, return all word squares where the kth row and kth column read the same string.
   * 
   * Constraints: 1 <= words.length <= 1000; 1 <= words[i].length <= 5 in the original problem; words contain lowercase letters.
   * 
   * Optimized complexity: Time O(number of partial squares * candidate lookup); Space O(nk). Trie prefix lookup prunes invalid branches.
   * Recursive complexity: Time O(number of partial squares * candidate lookup); Space O(nk + k). Recursive backtracking stores current square.
   * 
   * Example 1:
   * Input: words = ["area","lead","wall","lady","ball"]
   * Output: [["wall","area","lead","lady"],["ball","area","lead","lady"]]
   * Explanation: Rows and columns match for both squares.
   * 
   * Example 2:
   * Input: words = ["abat","baba","atan","atal"]
   * Output: [["baba","abat","baba","atan"],["baba","abat","baba","atal"]]
   * Explanation: The prefix trie finds valid row candidates.
   * 
   * Example 3:
   * Input: words = ["a"]
   * Output: [["a"]]
   * Explanation: A one-letter word forms a one-row square.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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
}
