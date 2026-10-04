package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 28: Advanced Graphs. Basic questions 1-12. */
public class AdvancedGraphsBasic {

  /*
   * Question 1: Network Delay Time
   * 
   * Question: Given directed weighted edges times[i] = [u,v,w], n nodes labeled 1..n, and source k, return the time for all nodes to receive the signal, or -1 if any node is unreachable.
   * 
   * Constraints: 1 <= n <= 100; edge weights are positive; node labels are 1-based.
   * 
   * Optimized complexity: Time O((n + E) log n); Space O(n + E). Dijkstra uses a min-heap adjacency list.
   * Recursive complexity: Time O(number of explored walks with pruning); Space O(n + E + recursion depth). DFS prunes states not improving distance.
   * 
   * Example 1:
   * Input: times=[[2,1,1],[2,3,1],[3,4,1]], n=4, k=2
   * Output: 2
   * Explanation: Node 4 receives after path 2->3->4.
   * 
   * Example 2:
   * Input: times=[[1,2,1]], n=2, k=2
   * Output: -1
   * Explanation: Node 1 is unreachable from 2.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 2: Cheapest Flights Within K Stops
   * 
   * Question: Given flights [from,to,price], find the cheapest price from src to dst using at most k stops, or -1 if no route satisfies the stop limit.
   * 
   * Constraints: n <= 100; prices are positive; at most k + 1 edges may be used.
   * 
   * Optimized complexity: Time O((k + 1)E); Space O(n). Bellman-Ford layers prevent same-round reuse.
   * Recursive complexity: Time O((k + 1)(n + E)) with memoization; Space O((k + 1)n + recursion depth).
   * 
   * Example 1:
   * Input: n=4, flights=[[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]], src=0, dst=3, k=1
   * Output: 700
   * Explanation: 0->1->3 uses one stop.
   * 
   * Example 2:
   * Input: n=3, flights=[[0,1,100],[1,2,100],[0,2,500]], src=0, dst=2, k=1
   * Output: 200
   * Explanation: The one-stop path is cheaper than direct.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 3: Path With Minimum Effort
   * 
   * Question: Given a heights grid, move 4-directionally from top-left to bottom-right while minimizing the maximum absolute height difference along the path.
   * 
   * Constraints: Grid size up to 100x100; height differences fit in int.
   * 
   * Optimized complexity: Time O(mn log(mn)); Space O(mn). Dijkstra over grid cells with minimax relaxation.
   * Recursive complexity: Time O(mn log W); Space O(mn). Binary search effort and recursive DFS reachability check.
   * 
   * Example 1:
   * Input: heights=[[1,2,2],[3,8,2],[5,3,5]]
   * Output: 2
   * Explanation: A route exists where every step differs by at most 2.
   * 
   * Example 2:
   * Input: heights=[[1,2,3],[3,8,4],[5,3,5]]
   * Output: 1
   * Explanation: The right-side route keeps max effort 1.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 4: Swim in Rising Water
   * 
   * Question: Given an n x n grid where grid[r][c] is the time that cell becomes passable, return the earliest time when a path exists from top-left to bottom-right.
   * 
   * Constraints: n <= 50; values are unique from 0 to n^2 - 1 in the original LeetCode problem.
   * 
   * Optimized complexity: Time O(n^2 log n); Space O(n^2). Dijkstra minimizes the maximum grid value seen so far.
   * Recursive complexity: Time O(n^2 log W); Space O(n^2). Binary search the time and use recursive DFS for feasibility.
   * 
   * Example 1:
   * Input: grid=[[0,2],[1,3]]
   * Output: 3
   * Explanation: The destination opens at time 3.
   * 
   * Example 2:
   * Input: grid=[[0,1,2,3,4],[24,23,22,21,5],[12,13,14,15,16],[11,17,18,19,20],[10,9,8,7,6]]
   * Output: 16
   * Explanation: The snake path is feasible when time reaches 16.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 5: Find Critical and Pseudo-Critical Edges in MST
   * 
   * Question: Given an undirected weighted connected graph, return which edge indices are critical for every MST and which can appear in at least one MST.
   * 
   * Constraints: n <= 100 and edges <= 200 in the LeetCode problem; O(E^2 alpha(V)) classification is acceptable.
   * 
   * Optimized complexity: Time O(E^2 alpha(V) + E log E); Space O(E + V). Sorted Kruskal classification with DSU.
   * Recursive complexity: Time O(E^2 alpha(V)); Space O(E + V). Recursive DSU find supports the same classification checks.
   * 
   * Example 1:
   * Input: n=5, edges=[[0,1,1],[1,2,1],[2,3,2],[0,3,2],[0,4,3],[3,4,3],[1,4,6]]
   * Output: [[0,1],[2,3,4,5]]
   * Explanation: Edges 0 and 1 are required in every MST.
   * 
   * Example 2:
   * Input: n=4, edges=[[0,1,1],[1,2,1],[2,3,1],[0,3,1]]
   * Output: [[],[0,1,2,3]]
   * Explanation: Any three cycle edges can form an MST.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 6: Min Cost to Connect All Points
   * 
   * Question: Given points on a plane, connect all points with minimum total Manhattan distance cost.
   * 
   * Constraints: n <= 1000; complete graph has O(n^2) implicit edges.
   * 
   * Optimized complexity: Time O(n^2); Space O(n). Dense Prim keeps the best current connection for every outside point.
   * Recursive complexity: Time O(n^2); Space O(n + recursion depth). Recursive Prim adds one point per call.
   * 
   * Example 1:
   * Input: points=[[0,0],[2,2],[3,10],[5,2],[7,0]]
   * Output: 20
   * Explanation: The minimum spanning tree has total cost 20.
   * 
   * Example 2:
   * Input: points=[[3,12],[-2,5],[-4,1]]
   * Output: 18
   * Explanation: Connect all three points with two cheapest useful edges.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 7: Optimize Water Distribution in a Village
   * 
   * Question: Given well costs for each house and pipe costs between houses, return the minimum cost to supply water to every house.
   * 
   * Constraints: n can be large enough that MST modeling is required; all costs are positive.
   * 
   * Optimized complexity: Time O((n + p) log n); Space O(n + p). Prim starts from virtual source and chooses cheapest frontier edge.
   * Recursive complexity: Time O((n + p) log(n + p)); Space O(n + p). Recursive DSU find inside Kruskal.
   * 
   * Example 1:
   * Input: n=3, wells=[1,2,2], pipes=[[1,2,1],[2,3,1]]
   * Output: 3
   * Explanation: Build well at house 1 and connect 1-2 and 2-3.
   * 
   * Example 2:
   * Input: n=2, wells=[5,5], pipes=[[1,2,1]]
   * Output: 6
   * Explanation: One well plus one pipe is cheapest.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 8: Reconstruct Itinerary
   * 
   * Question: Given airline tickets [from,to], reconstruct the lexicographically smallest itinerary that starts at JFK and uses every ticket exactly once.
   * 
   * Constraints: All tickets belong to at least one valid itinerary in the LeetCode problem.
   * 
   * Optimized complexity: Time O(E log E); Space O(E). Iterative Hierholzer uses min-heaps per airport and a stack.
   * Recursive complexity: Time O(E log E); Space O(E). Recursive Hierholzer adds airports on postorder unwind.
   * 
   * Example 1:
   * Input: tickets=[[JFK,SFO],[JFK,ATL],[SFO,ATL],[ATL,JFK],[ATL,SFO]]
   * Output: [JFK, ATL, JFK, SFO, ATL, SFO]
   * Explanation: ATL is chosen before SFO lexicographically.
   * 
   * Example 2:
   * Input: tickets=[[JFK,KUL],[JFK,NRT],[NRT,JFK]]
   * Output: [JFK, NRT, JFK, KUL]
   * Explanation: Postorder handles the KUL dead end correctly.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 9: Alien Dictionary
   * 
   * Question: Given sorted words from an alien language, return one valid character order, or empty string if the order is invalid.
   * 
   * Constraints: Characters are lowercase English letters in the common LeetCode version.
   * 
   * Optimized complexity: Time O(total characters + V + E); Space O(V + E). Kahn queue processes zero-indegree characters.
   * Recursive complexity: Time O(total characters + V + E); Space O(V + E). DFS color states detect cycles and build reverse postorder.
   * 
   * Example 1:
   * Input: words=[wrt,wrf,er,ett,rftt]
   * Output: wertf
   * Explanation: Constraints imply w before e, e before r, r before t, t before f.
   * 
   * Example 2:
   * Input: words=[z,x,z]
   * Output: 
   * Explanation: The constraints create a cycle.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 10: Course Schedule II
   * 
   * Question: Given numCourses and prerequisites [course, prerequisite], return an order to finish all courses or an empty array if impossible.
   * 
   * Constraints: Courses are labeled 0..numCourses-1; any valid topological order is accepted.
   * 
   * Optimized complexity: Time O(V + E); Space O(V + E). Kahn queue emits zero-indegree courses.
   * Recursive complexity: Time O(V + E); Space O(V + E). DFS colors detect cycles and produce reverse postorder.
   * 
   * Example 1:
   * Input: numCourses=2, prerequisites=[[1,0]]
   * Output: [0,1]
   * Explanation: Course 0 must come before 1.
   * 
   * Example 2:
   * Input: numCourses=2, prerequisites=[[1,0],[0,1]]
   * Output: []
   * Explanation: The prerequisites form a cycle.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 11: Shortest Path Visiting All Nodes
   * 
   * Question: Given an undirected connected graph, return the shortest number of edges needed to visit every node at least once.
   * 
   * Constraints: n <= 12 in the LeetCode problem, making bitmask states feasible.
   * 
   * Optimized complexity: Time O(n * 2^n + E * 2^n); Space O(n * 2^n). BFS over (node, visitedMask).
   * Recursive complexity: Time O(n^3 + n^2 * 2^n); Space O(n * 2^n). Floyd distances plus recursive TSP memo.
   * 
   * Example 1:
   * Input: graph=[[1,2,3],[0],[0],[0]]
   * Output: 4
   * Explanation: Start at a leaf, visit center and the other leaves.
   * 
   * Example 2:
   * Input: graph=[[1],[0,2,4],[1,3,4],[2],[1,2]]
   * Output: 4
   * Explanation: A shortest walk can cover all nodes in four edges.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 12: Minimum Cost to Reach Destination in Time
   * 
   * Question: Given maxTime, undirected edges [u,v,time], and passingFees per city, return the minimum fee to reach city n-1 within maxTime or -1.
   * 
   * Constraints: maxTime <= 1000 in the LeetCode problem, so time-layer DP is feasible.
   * 
   * Optimized complexity: Time O(maxTime * E); Space O(maxTime * V). DP relaxes every edge between adjacent time layers.
   * Recursive complexity: Time O(V * maxTime + E * maxTime); Space O(V * maxTime). Memoized DFS chooses outgoing edges under remaining time.
   * 
   * Example 1:
   * Input: maxTime=30, edges=[[0,1,10],[1,2,10],[2,5,10],[0,3,1],[3,4,10],[4,5,15]], fees=[5,1,2,20,20,3]
   * Output: 11
   * Explanation: Path 0-1-2-5 reaches in 30 with fee 11.
   * 
   * Example 2:
   * Input: maxTime=29 with the same graph
   * Output: 48
   * Explanation: The cheaper path is too slow, so another route is needed.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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
}
