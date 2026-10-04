package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 16: Trie. Moderate questions 13-20. */
public class TrieModerate {

  /*
   * Question 13: Index Pairs of a String
   * 
   * Question: Given a text and a list of words, return all index pairs [i, j] where text substring i..j appears in words.
   * 
   * Constraints: 1 <= text.length <= 100; 1 <= words.length <= 20; words and text contain lowercase English letters.
   * 
   * Optimized complexity: Time O(n * maxWordLength); Space O(total word chars + P). Trie walk stops when the prefix fails.
   * Recursive complexity: Time O(n * maxWordLength); Space O(total word chars + maxWordLength + P). Recursive scan follows one path per start.
   * 
   * Example 1:
   * Input: text = "thestoryofleetcodeandme", words = ["story","fleet","leetcode"]
   * Output: [[3,7],[9,13],[10,17]]
   * Explanation: All matching substrings are returned in sorted order.
   * 
   * Example 2:
   * Input: text = "ababa", words = ["aba","ab"]
   * Output: [[0,1],[0,2],[2,3],[2,4]]
   * Explanation: Matches can overlap.
   * 
   * Example 3:
   * Input: text = "abc", words = ["d"]
   * Output: []
   * Explanation: No dictionary word appears.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 14: Prefix and Suffix Search
   * 
   * Question: Design WordFilter so f(prefix, suffix) returns the largest index of a word that starts with prefix and ends with suffix.
   * 
   * Constraints: 1 <= words.length <= 15000; 1 <= words[i].length <= 10; at most 15000 queries.
   * 
   * Optimized complexity: Time constructor O(nL^2), query O(P + S); Space O(nL^2). Combined trie stores every suffix separator word path.
   * Recursive complexity: Time constructor O(nL^2), query O(P + S); Space O(nL^2 + L). Recursive trie insert/find follows combined keys.
   * 
   * Example 1:
   * Input: WordFilter(["apple"]), f("a", "e")
   * Output: 0
   * Explanation: apple starts with a and ends with e.
   * 
   * Example 2:
   * Input: WordFilter(["apple","apply"]), f("app", "ly")
   * Output: 1
   * Explanation: apply is the latest matching word.
   * 
   * Example 3:
   * Input: f("b", "e")
   * Output: -1
   * Explanation: No word has both constraints.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 15: Implement Magic Dictionary
   * 
   * Question: Design a dictionary that supports building from words and searching whether a query can match a dictionary word after modifying exactly one character.
   * 
   * Constraints: 1 <= dictionary.length <= 100; 1 <= word.length <= 100; searchWord contains lowercase English letters.
   * 
   * Optimized complexity: Time build O(nL), search O(26L) worst case; Space O(nL). Trie state tracks whether the modification was used.
   * Recursive complexity: Time build O(nL), search O(26L) worst case; Space O(nL + L). Recursive DFS tracks mismatch count.
   * 
   * Example 1:
   * Input: buildDict(["hello","leetcode"]), search("hello")
   * Output: false
   * Explanation: Exactly one character must change.
   * 
   * Example 2:
   * Input: search("hhllo")
   * Output: true
   * Explanation: Changing e to h gives hello.
   * 
   * Example 3:
   * Input: search("hell")
   * Output: false
   * Explanation: Length must stay the same.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 16: Count Pairs With XOR in a Range
   * 
   * Question: Given nums, low, and high, count pairs i < j whose XOR value lies in the inclusive range [low, high].
   * 
   * Constraints: 1 <= nums.length <= 20000; 1 <= nums[i] <= 20000; 1 <= low <= high <= 20000.
   * 
   * Optimized complexity: Time O(15n); Space O(15n). Binary trie counts prior values with XOR below each bound.
   * Recursive complexity: Time O(15n); Space O(15n + 15). Recursive query processes one bit per level.
   * 
   * Example 1:
   * Input: nums = [1,4,2,7], low = 2, high = 6
   * Output: 6
   * Explanation: Six index pairs have XOR in the target range.
   * 
   * Example 2:
   * Input: nums = [9,8,4,2,1], low = 5, high = 14
   * Output: 8
   * Explanation: Eight pairs satisfy the inclusive range.
   * 
   * Example 3:
   * Input: nums = [1], low = 1, high = 1
   * Output: 0
   * Explanation: No pair exists.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 17: Sum of Prefix Scores of Strings
   * 
   * Question: For each word, return the sum of counts of all its non-empty prefixes across the entire words array.
   * 
   * Constraints: 1 <= words.length <= 1000; 1 <= words[i].length <= 1000; total characters <= 1000000.
   * 
   * Optimized complexity: Time O(total characters); Space O(total characters). Trie nodes store prefix frequencies.
   * Recursive complexity: Time O(total characters); Space O(total characters + L). Recursive insert and score walk one character per level.
   * 
   * Example 1:
   * Input: words = ["abc","ab","bc","b"]
   * Output: [5,4,3,2]
   * Explanation: abc has prefix counts a=2, ab=2, abc=1 for total 5.
   * 
   * Example 2:
   * Input: words = ["abcd"]
   * Output: [4]
   * Explanation: Each of its four prefixes appears once.
   * 
   * Example 3:
   * Input: words = ["a","a"]
   * Output: [2,2]
   * Explanation: The prefix a appears twice.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 18: Short Encoding of Words
   * 
   * Question: Given words, return the length of the shortest reference string that encodes every word as a suffix followed by #.
   * 
   * Constraints: 1 <= words.length <= 2000; 1 <= words[i].length <= 7; words contain lowercase English letters.
   * 
   * Optimized complexity: Time O(nL); Space O(nL). Reverse trie counts only terminal leaves.
   * Recursive complexity: Time O(nL); Space O(nL + L). Recursive trie traversal sums leaf depths.
   * 
   * Example 1:
   * Input: words = ["time","me","bell"]
   * Output: 10
   * Explanation: time#bell# encodes me because me is a suffix of time.
   * 
   * Example 2:
   * Input: words = ["t"]
   * Output: 2
   * Explanation: The encoding is t#.
   * 
   * Example 3:
   * Input: words = ["me","time","ime"]
   * Output: 5
   * Explanation: time# also covers me and ime.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 19: Word Break with Trie
   * 
   * Question: Given a string and a dictionary, return true if the string can be segmented into one or more dictionary words.
   * 
   * Constraints: 1 <= s.length <= 300; 1 <= wordDict.length <= 1000; words contain lowercase English letters.
   * 
   * Optimized complexity: Time O(nL); Space O(total word chars + n). Trie walk from reachable starts avoids unrelated dictionary words.
   * Recursive complexity: Time O(nL); Space O(total word chars + n). Memoized recursion stores failed start positions.
   * 
   * Example 1:
   * Input: s = "leetcode", wordDict = ["leet","code"]
   * Output: true
   * Explanation: leetcode splits into leet + code.
   * 
   * Example 2:
   * Input: s = "applepenapple", wordDict = ["apple","pen"]
   * Output: true
   * Explanation: Dictionary words may be reused.
   * 
   * Example 3:
   * Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
   * Output: false
   * Explanation: No complete segmentation covers the string.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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

  /*
   * Question 20: Design Search Autocomplete System
   * 
   * Question: Design an autocomplete system that returns the top three historical sentences for the current typed prefix, ranked by frequency then lexicographic order. Character # commits the current sentence.
   * 
   * Constraints: Sentences contain lowercase letters and spaces; times are positive; input is one lowercase letter, space, or #.
   * 
   * Optimized complexity: Time O(P + K log K) per query where K is candidates at the prefix; Space O(total sentence characters * prefix maps).
   * Recursive complexity: Time O(P + K log K) per query; Space O(total sentence characters + recursion depth). DFS collects candidates below the prefix node.
   * 
   * Example 1:
   * Input: sentences = ["i love you","island","iroman","i love leetcode"], times = [5,3,2,2], input("i")
   * Output: ["i love you","island","i love leetcode"]
   * Explanation: Top matches are ranked by count then lexicographic order.
   * 
   * Example 2:
   * Input: input(" ")
   * Output: ["i love you","i love leetcode"]
   * Explanation: The prefix becomes "i ".
   * 
   * Example 3:
   * Input: input("#")
   * Output: []
   * Explanation: The current sentence is committed and the buffer resets.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/tries.html
   */


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
}
