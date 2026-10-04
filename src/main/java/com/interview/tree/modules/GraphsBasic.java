package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 14: Graphs. Basic questions 1-12. */
public class GraphsBasic {

  /*
   * Question 1: Number of Islands
   * 
   * Question: Given an m x n grid of characters 1 and 0, count how many disconnected groups of land cells exist. Land cells connect vertically and horizontally.
   * 
   * Constraints: m == grid.length; n == grid[i].length; 1 <= m,n <= 300; grid[i][j] is 0 or 1.
   * 
   * Optimized complexity: Time O(mn); Space O(mn). Each cell is pushed into the queue at most once.
   * Recursive complexity: Time O(mn); Space O(mn). DFS visits each cell once and recursion depth can reach mn.
   * 
   * Example 1:
   * Input: grid = [["1","1","1","1","0"],["1","1","0","1","0"],["1","1","0","0","0"],["0","0","0","0","0"]]
   * Output: 1
   * Explanation: All land cells are connected into one island.
   * 
   * Example 2:
   * Input: grid = [["1","1","0","0","0"],["1","1","0","0","0"],["0","0","1","0","0"],["0","0","0","1","1"]]
   * Output: 3
   * Explanation: There are three disconnected land components.
   * 
   * Example 3:
   * Input: grid = [["0"]]
   * Output: 0
   * Explanation: There is no land to count.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 2: Clone Graph
   * 
   * Question: Given a reference to a node in a connected undirected graph, return a deep copy of the graph.
   * 
   * Constraints: 0 <= nodes <= 100; 1 <= Node.val <= 100; Node.val is unique; graph is connected from the input node.
   * 
   * Optimized complexity: Time O(V + E); Space O(V). BFS creates one clone per node and processes each edge once.
   * Recursive complexity: Time O(V + E); Space O(V). DFS recursion plus the map stores cloned nodes.
   * 
   * Example 1:
   * Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
   * Output: [[2,4],[1,3],[2,4],[1,3]]
   * Explanation: The four-node cycle is copied with new node objects.
   * 
   * Example 2:
   * Input: adjList = [[]]
   * Output: [[]]
   * Explanation: A single isolated node is cloned.
   * 
   * Example 3:
   * Input: adjList = []
   * Output: []
   * Explanation: A null input returns null.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 3: Max Area of Island
   * 
   * Question: Given a binary grid, return the maximum number of land cells in any connected island.
   * 
   * Constraints: 1 <= rows, cols <= 50; grid[i][j] is 0 or 1.
   * 
   * Optimized complexity: Time O(mn); Space O(mn). Shared visited state avoids recounting a component.
   * Recursive complexity: Time O(mn); Space O(mn). Recursive DFS depth can include the whole grid.
   * 
   * Example 1:
   * Input: grid = [[0,0,1,0],[1,1,1,0],[0,0,0,1]]
   * Output: 4
   * Explanation: The largest four-direction component has four cells.
   * 
   * Example 2:
   * Input: grid = [[0,0,0]]
   * Output: 0
   * Explanation: There is no island.
   * 
   * Example 3:
   * Input: grid = [[1,1],[1,1]]
   * Output: 4
   * Explanation: Every cell belongs to the same island.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 4: Pacific Atlantic Water Flow
   * 
   * Question: Given a matrix of heights, return all cells from which water can flow to both the Pacific and Atlantic oceans.
   * 
   * Constraints: 1 <= m,n <= 200; 0 <= heights[i][j] <= 100000.
   * 
   * Optimized complexity: Time O(mn); Space O(mn). Two border BFS traversals mark all cells once per ocean.
   * Recursive complexity: Time O(mn); Space O(mn). Two DFS traversals mark all reachable cells per ocean.
   * 
   * Example 1:
   * Input: heights = [[1,2,2,3,5],[3,2,3,4,4],[2,4,5,3,1],[6,7,1,4,5],[5,1,1,2,4]]
   * Output: [[0,4],[1,3],[1,4],[2,2],[3,0],[3,1],[4,0]]
   * Explanation: These cells can flow to both ocean borders.
   * 
   * Example 2:
   * Input: heights = [[1]]
   * Output: [[0,0]]
   * Explanation: The only cell touches both oceans.
   * 
   * Example 3:
   * Input: heights = [[1,2,3]]
   * Output: [[0,2]]
   * Explanation: Only the rightmost cell can flow to both ends in this row.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 5: Surrounded Regions
   * 
   * Question: Given an m x n board of X and O, capture every O region fully surrounded by X by changing it to X.
   * 
   * Constraints: 1 <= m,n <= 200; board[i][j] is X or O.
   * 
   * Optimized complexity: Time O(mn); Space O(mn). Border BFS marks each safe cell once.
   * Recursive complexity: Time O(mn); Space O(mn). Border DFS marks each safe cell once.
   * 
   * Example 1:
   * Input: board = [["X","X","X","X"],["X","O","O","X"],["X","X","O","X"],["X","O","X","X"]]
   * Output: [["X","X","X","X"],["X","X","X","X"],["X","X","X","X"],["X","O","X","X"]]
   * Explanation: Only the border-connected O survives.
   * 
   * Example 2:
   * Input: board = [["X"]]
   * Output: [["X"]]
   * Explanation: A single X is unchanged.
   * 
   * Example 3:
   * Input: board = [["O","O"],["O","O"]]
   * Output: [["O","O"],["O","O"]]
   * Explanation: Every O touches the border.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 6: Rotting Oranges
   * 
   * Question: Given a grid containing empty cells, fresh oranges, and rotten oranges, return the minimum minutes until no fresh orange remains, or -1 if impossible.
   * 
   * Constraints: 1 <= rows, cols <= 10; grid[i][j] is 0, 1, or 2.
   * 
   * Optimized complexity: Time O(mn); Space O(mn). Multi-source BFS processes each orange once.
   * Recursive complexity: Time O(mn); Space O(mn). Recursive layer processing consumes one BFS frontier per call.
   * 
   * Example 1:
   * Input: grid = [[2,1,1],[1,1,0],[0,1,1]]
   * Output: 4
   * Explanation: The infection reaches the farthest fresh orange after four minutes.
   * 
   * Example 2:
   * Input: grid = [[2,1,1],[0,1,1],[1,0,1]]
   * Output: -1
   * Explanation: The bottom-left fresh orange is isolated.
   * 
   * Example 3:
   * Input: grid = [[0,2]]
   * Output: 0
   * Explanation: There are no fresh oranges.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 7: Walls and Gates
   * 
   * Question: Given rooms marked as walls, gates, and empty rooms, fill each empty room with its distance to the nearest gate.
   * 
   * Constraints: m == rooms.length; n == rooms[i].length; rooms use -1 for walls, 0 for gates, and 2147483647 for empty rooms.
   * 
   * Optimized complexity: Time O(mn); Space O(mn). All gates expand together and each room is finalized once.
   * Recursive complexity: Time O(mn); Space O(mn). DFS from gates relaxes only when it improves a room distance.
   * 
   * Example 1:
   * Input: rooms = [[2147483647,-1,0,2147483647],[2147483647,2147483647,2147483647,-1],[2147483647,-1,2147483647,-1],[0,-1,2147483647,2147483647]]
   * Output: [[3,-1,0,1],[2,2,1,-1],[1,-1,2,-1],[0,-1,3,4]]
   * Explanation: Each empty room receives the shortest gate distance.
   * 
   * Example 2:
   * Input: rooms = [[-1]]
   * Output: [[-1]]
   * Explanation: A wall stays unchanged.
   * 
   * Example 3:
   * Input: rooms = [[0,2147483647]]
   * Output: [[0,1]]
   * Explanation: The empty room is one step from the gate.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 8: Course Schedule
   * 
   * Question: Given numCourses and prerequisite pairs, determine whether it is possible to finish every course.
   * 
   * Constraints: 1 <= numCourses <= 2000; 0 <= prerequisites.length <= 5000; prerequisites[i] = [course, prerequisite].
   * 
   * Optimized complexity: Time O(V + E); Space O(V + E). Kahn BFS removes zero-indegree nodes once.
   * Recursive complexity: Time O(V + E); Space O(V + E). DFS coloring detects back edges.
   * 
   * Example 1:
   * Input: numCourses = 2, prerequisites = [[1,0]]
   * Output: true
   * Explanation: Take course 0 before course 1.
   * 
   * Example 2:
   * Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
   * Output: false
   * Explanation: The two courses depend on each other.
   * 
   * Example 3:
   * Input: numCourses = 3, prerequisites = []
   * Output: true
   * Explanation: There are no dependency cycles.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 9: Course Schedule II
   * 
   * Question: Given numCourses and prerequisites, return one valid order to finish all courses, or an empty array if impossible.
   * 
   * Constraints: 1 <= numCourses <= 2000; 0 <= prerequisites.length <= numCourses * (numCourses - 1).
   * 
   * Optimized complexity: Time O(V + E); Space O(V + E). Kahn BFS processes every course and edge once.
   * Recursive complexity: Time O(V + E); Space O(V + E). DFS postorder builds the topological order when no cycle exists.
   * 
   * Example 1:
   * Input: numCourses = 2, prerequisites = [[1,0]]
   * Output: [0,1]
   * Explanation: Course 0 must appear before course 1.
   * 
   * Example 2:
   * Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
   * Output: [0,1,2,3]
   * Explanation: Several valid topological orders exist.
   * 
   * Example 3:
   * Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
   * Output: []
   * Explanation: A directed cycle prevents a valid order.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 10: Graph Valid Tree
   * 
   * Question: Given n nodes labeled 0 to n - 1 and undirected edges, return true if the edges form exactly one valid tree.
   * 
   * Constraints: 1 <= n <= 2000; 0 <= edges.length <= 5000; edges[i] connects two distinct nodes.
   * 
   * Optimized complexity: Time O(V + E); Space O(V + E). BFS checks reachability after the edge count filter.
   * Recursive complexity: Time O(V + E); Space O(V + E). DFS detects parent-aware cycles and connectivity.
   * 
   * Example 1:
   * Input: n = 5, edges = [[0,1],[0,2],[0,3],[1,4]]
   * Output: true
   * Explanation: All nodes are connected and there is no cycle.
   * 
   * Example 2:
   * Input: n = 5, edges = [[0,1],[1,2],[2,3],[1,3],[1,4]]
   * Output: false
   * Explanation: The edge [1,3] creates a cycle.
   * 
   * Example 3:
   * Input: n = 4, edges = [[0,1],[2,3]]
   * Output: false
   * Explanation: The graph has two components.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 11: Number of Connected Components in an Undirected Graph
   * 
   * Question: Given n nodes and undirected edges, return the number of connected components in the graph.
   * 
   * Constraints: 1 <= n <= 2000; 0 <= edges.length <= 5000; nodes are labeled from 0 to n - 1.
   * 
   * Optimized complexity: Time O(V + E); Space O(V + E). Adjacency lists let each edge be processed once.
   * Recursive complexity: Time O(V + E); Space O(V + E). Recursive DFS visits every node and edge once.
   * 
   * Example 1:
   * Input: n = 5, edges = [[0,1],[1,2],[3,4]]
   * Output: 2
   * Explanation: Nodes 0,1,2 form one component and 3,4 form another.
   * 
   * Example 2:
   * Input: n = 5, edges = [[0,1],[1,2],[2,3],[3,4]]
   * Output: 1
   * Explanation: All nodes are connected.
   * 
   * Example 3:
   * Input: n = 3, edges = []
   * Output: 3
   * Explanation: Each isolated node is its own component.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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

  /*
   * Question 12: Redundant Connection
   * 
   * Question: Given edges of an undirected graph that started as a tree with one extra edge, return the edge that can be removed to make the graph a tree again.
   * 
   * Constraints: 3 <= edges.length <= 1000; edges[i] = [ai, bi]; nodes are labeled from 1 to edges.length.
   * 
   * Optimized complexity: Time O(E alpha(V)); Space O(V). Union find detects the first same-component edge.
   * Recursive complexity: Time O(E(V + E)); Space O(V + E). Recursive DFS checks reachability before adding each edge.
   * 
   * Example 1:
   * Input: edges = [[1,2],[1,3],[2,3]]
   * Output: [2,3]
   * Explanation: Nodes 2 and 3 are already connected through 1.
   * 
   * Example 2:
   * Input: edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
   * Output: [1,4]
   * Explanation: Adding [1,4] closes the cycle.
   * 
   * Example 3:
   * Input: edges = [[1,2],[2,3],[3,1]]
   * Output: [3,1]
   * Explanation: The last edge completes the triangle.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/graphs.html
   */


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
}
