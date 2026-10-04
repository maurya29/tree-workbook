package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 14: Graphs. Moderate questions 13-20. */
public class GraphsModerate {

  /*
   * Question 13: Word Ladder
   * 
   * Question: Given beginWord, endWord, and a dictionary, return the length of the shortest transformation sequence where each step changes one character and every intermediate word is in the dictionary.
   * 
   * Constraints: 1 <= wordList.length <= 5000; all words have the same length; words contain lowercase English letters.
   * 
   * Optimized complexity: Time O(N * L * 26); Space O(N). BFS removes visited words once.
   * Recursive complexity: Time O(N * L * 26); Space O(N). Recursive BFS processes one frontier per depth.
   * 
   * Example 1:
   * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
   * Output: 5
   * Explanation: hit -> hot -> dot -> dog -> cog.
   * 
   * Example 2:
   * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log"]
   * Output: 0
   * Explanation: The end word is not available.
   * 
   * Example 3:
   * Input: beginWord = "a", endWord = "c", wordList = ["a","b","c"]
   * Output: 2
   * Explanation: a can change directly to c.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 14: Open the Lock
   * 
   * Question: Given deadends and a target lock combination, return the minimum wheel turns needed to reach target from 0000, or -1 if impossible.
   * 
   * Constraints: 1 <= deadends.length <= 500; target has length 4; combinations contain digits 0 to 9.
   * 
   * Optimized complexity: Time O(10^4 * 8); Space O(10^4). BFS visits each combination at most once.
   * Recursive complexity: Time O(10^4 * 8); Space O(10^4). Recursive BFS advances one frontier layer per call.
   * 
   * Example 1:
   * Input: deadends = ["0201","0101","0102","1212","2002"], target = "0202"
   * Output: 6
   * Explanation: The shortest safe route takes six turns.
   * 
   * Example 2:
   * Input: deadends = ["8888"], target = "0009"
   * Output: 1
   * Explanation: Turn the last wheel backward once.
   * 
   * Example 3:
   * Input: deadends = ["0000"], target = "8888"
   * Output: -1
   * Explanation: The start state is blocked.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 15: Shortest Path in Binary Matrix
   * 
   * Question: Given an n x n binary matrix, return the length of the shortest clear path from top-left to bottom-right moving in 8 directions, or -1 if none exists.
   * 
   * Constraints: 1 <= n <= 100; grid[i][j] is 0 or 1.
   * 
   * Optimized complexity: Time O(n^2); Space O(n^2). BFS visits each clear cell at most once.
   * Recursive complexity: Time O(n^2); Space O(n^2). Recursive frontier expansion follows BFS layers.
   * 
   * Example 1:
   * Input: grid = [[0,1],[1,0]]
   * Output: 2
   * Explanation: Move diagonally from start to target.
   * 
   * Example 2:
   * Input: grid = [[0,0,0],[1,1,0],[1,1,0]]
   * Output: 4
   * Explanation: The shortest path follows the right side.
   * 
   * Example 3:
   * Input: grid = [[1,0,0],[1,1,0],[1,1,0]]
   * Output: -1
   * Explanation: The start cell is blocked.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 16: 01 Matrix
   * 
   * Question: Given a binary matrix, return a matrix where each cell stores the distance to the nearest 0.
   * 
   * Constraints: 1 <= rows, cols <= 10000 total cells; mat[i][j] is 0 or 1; at least one zero exists.
   * 
   * Optimized complexity: Time O(mn); Space O(mn). All zero cells expand together once.
   * Recursive complexity: Time O(mn); Space O(mn). Recursive relaxation updates a cell only when a shorter distance is found.
   * 
   * Example 1:
   * Input: mat = [[0,0,0],[0,1,0],[0,0,0]]
   * Output: [[0,0,0],[0,1,0],[0,0,0]]
   * Explanation: The center one is adjacent to a zero.
   * 
   * Example 2:
   * Input: mat = [[0,0,0],[0,1,0],[1,1,1]]
   * Output: [[0,0,0],[0,1,0],[1,2,1]]
   * Explanation: Distances grow away from the nearest zero.
   * 
   * Example 3:
   * Input: mat = [[1,0,1]]
   * Output: [[1,0,1]]
   * Explanation: Both one cells are one step from the zero.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 17: Is Graph Bipartite?
   * 
   * Question: Given an undirected graph as adjacency lists, determine whether its nodes can be split into two sets so every edge connects nodes in different sets.
   * 
   * Constraints: 1 <= graph.length <= 100; 0 <= graph[u].length < graph.length; graph is undirected.
   * 
   * Optimized complexity: Time O(V + E); Space O(V). BFS coloring checks each edge once.
   * Recursive complexity: Time O(V + E); Space O(V). DFS coloring recurses through each component.
   * 
   * Example 1:
   * Input: graph = [[1,2,3],[0,2],[0,1,3],[0,2]]
   * Output: false
   * Explanation: Nodes 0,1,2 form an odd cycle conflict.
   * 
   * Example 2:
   * Input: graph = [[1,3],[0,2],[1,3],[0,2]]
   * Output: true
   * Explanation: The graph can be split into {0,2} and {1,3}.
   * 
   * Example 3:
   * Input: graph = [[],[2],[1]]
   * Output: true
   * Explanation: The isolated node does not affect coloring.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 18: Possible Bipartition
   * 
   * Question: Given n people and pairs of people who dislike each other, return true if everyone can be split into two groups so no pair in the same group dislikes each other.
   * 
   * Constraints: 1 <= n <= 2000; 0 <= dislikes.length <= 10000; dislikes[i] = [a, b] with 1 <= a,b <= n.
   * 
   * Optimized complexity: Time O(n + E); Space O(n + E). BFS coloring handles all components.
   * Recursive complexity: Time O(n + E); Space O(n + E). DFS coloring recurses through dislike components.
   * 
   * Example 1:
   * Input: n = 4, dislikes = [[1,2],[1,3],[2,4]]
   * Output: true
   * Explanation: One valid split is {1,4} and {2,3}.
   * 
   * Example 2:
   * Input: n = 3, dislikes = [[1,2],[1,3],[2,3]]
   * Output: false
   * Explanation: Three mutual conflicts form an odd cycle.
   * 
   * Example 3:
   * Input: n = 5, dislikes = [[1,2],[2,3],[3,4],[4,5],[1,5]]
   * Output: false
   * Explanation: The cycle of length five cannot be two-colored.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 19: Find Eventual Safe States
   * 
   * Question: Given a directed graph, return all nodes that eventually lead only to terminal nodes and never enter a cycle.
   * 
   * Constraints: 1 <= graph.length <= 10000; 0 <= graph[i].length <= graph.length; graph[i] lists outgoing edges.
   * 
   * Optimized complexity: Time O(V + E); Space O(V + E). Reverse graph BFS removes each outgoing edge once.
   * Recursive complexity: Time O(V + E); Space O(V). DFS coloring memoizes safe and unsafe states.
   * 
   * Example 1:
   * Input: graph = [[1,2],[2,3],[5],[0],[5],[],[]]
   * Output: [2,4,5,6]
   * Explanation: These nodes cannot reach the cycle containing 0,1,3.
   * 
   * Example 2:
   * Input: graph = [[1,2,3,4],[1,2],[3,4],[0,4],[]]
   * Output: [4]
   * Explanation: Only node 4 is terminal-safe.
   * 
   * Example 3:
   * Input: graph = [[],[0],[0,1]]
   * Output: [0,1,2]
   * Explanation: Every path reaches terminal node 0.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 20: Keys and Rooms
   * 
   * Question: Given rooms where each room contains keys to other rooms, return true if every room can be visited starting from room 0.
   * 
   * Constraints: 2 <= rooms.length <= 1000; 0 <= rooms[i].length <= 1000; keys are room indexes.
   * 
   * Optimized complexity: Time O(V + E); Space O(V). Stack traversal processes each key edge once.
   * Recursive complexity: Time O(V + E); Space O(V). DFS visits each reachable room once.
   * 
   * Example 1:
   * Input: rooms = [[1],[2],[3],[]]
   * Output: true
   * Explanation: The keys unlock every room in order.
   * 
   * Example 2:
   * Input: rooms = [[1,3],[3,0,1],[2],[0]]
   * Output: false
   * Explanation: Room 2 is never reached.
   * 
   * Example 3:
   * Input: rooms = [[]]
   * Output: true
   * Explanation: The only room is already visited.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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
}
