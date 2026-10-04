package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 28: Advanced Graphs. Moderate questions 13-20. */
public class AdvancedGraphsModerate {

  /*
   * Question 13: Find the City With the Smallest Number of Neighbors
   * 
   * Question: Given weighted undirected edges and a distance threshold, return the city with the fewest reachable cities within the threshold, breaking ties by larger city index.
   * 
   * Constraints: n <= 100 in the LeetCode problem; O(n^3) Floyd-Warshall is acceptable.
   * 
   * Optimized complexity: Time O(n^3); Space O(n^2). Floyd-Warshall computes all-pairs distances.
   * Recursive complexity: Time O(n^3); Space O(n^3). Recursive shortest(i,j,k) memoizes allowed intermediate nodes.
   * 
   * Example 1:
   * Input: n=4, edges=[[0,1,3],[1,2,1],[1,3,4],[2,3,1]], threshold=4
   * Output: 3
   * Explanation: City 3 has the smallest reachable count after tie-breaking.
   * 
   * Example 2:
   * Input: n=5, edges=[[0,1,2],[0,4,8],[1,2,3],[1,4,2],[2,3,1],[3,4,1]], threshold=2
   * Output: 0
   * Explanation: City 0 reaches only city 1 within threshold.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 14: Critical Connections in a Network
   * 
   * Question: Given an undirected connected network, return all critical connections whose removal disconnects the graph.
   * 
   * Constraints: n up to 1e5; O(V + E) low-link traversal is required.
   * 
   * Optimized complexity: Time O(V + E); Space O(V + E). Tarjan low-link finds all bridges in one DFS.
   * Recursive complexity: Time O(V + E); Space O(V + E). Recursive DFS maintains discovery and low times.
   * 
   * Example 1:
   * Input: n=4, connections=[[0,1],[1,2],[2,0],[1,3]]
   * Output: [[1,3]]
   * Explanation: Removing 1-3 isolates node 3.
   * 
   * Example 2:
   * Input: n=2, connections=[[0,1]]
   * Output: [[0,1]]
   * Explanation: The only edge is a bridge.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 15: Number of Connected Components in an Undirected Graph
   * 
   * Question: Given n nodes and undirected edges, return the number of connected components.
   * 
   * Constraints: Nodes are labeled 0..n-1; DSU gives near-constant edge processing.
   * 
   * Optimized complexity: Time O(E alpha(V)); Space O(V). Union-Find maintains component count.
   * Recursive complexity: Time O(V + E); Space O(V + E). Recursive DFS counts components.
   * 
   * Example 1:
   * Input: n=5, edges=[[0,1],[1,2],[3,4]]
   * Output: 2
   * Explanation: Components are {0,1,2} and {3,4}.
   * 
   * Example 2:
   * Input: n=5, edges=[[0,1],[1,2],[2,3],[3,4]]
   * Output: 1
   * Explanation: All nodes are connected.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 16: Kosaraju SCC Count
   * 
   * Question: Given a directed graph with n nodes and edges [u,v], return the number of strongly connected components.
   * 
   * Constraints: Use O(V + E) graph traversals; nodes are labeled 0..n-1.
   * 
   * Optimized complexity: Time O(V + E); Space O(V + E). Kosaraju uses finish order and reverse graph.
   * Recursive complexity: Time O(V + E); Space O(V + E). Recursive DFS performs both passes.
   * 
   * Example 1:
   * Input: n=5, edges=[[1,0],[0,2],[2,1],[0,3],[3,4]]
   * Output: 3
   * Explanation: {0,1,2}, {3}, and {4} are SCCs.
   * 
   * Example 2:
   * Input: n=3, edges=[[0,1],[1,2],[2,0]]
   * Output: 1
   * Explanation: Every node reaches every other node.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 17: Tarjan SCC Count
   * 
   * Question: Given a directed graph, count strongly connected components using one DFS pass with low-link values.
   * 
   * Constraints: O(V + E) time and space with nodes labeled 0..n-1.
   * 
   * Optimized complexity: Time O(V + E); Space O(V + E). Tarjan low-link processes SCCs in one DFS.
   * Recursive complexity: Time O(V + E); Space O(V + E). Recursive low-link stack extraction.
   * 
   * Example 1:
   * Input: n=5, edges=[[1,0],[0,2],[2,1],[0,3],[3,4]]
   * Output: 3
   * Explanation: {0,1,2}, {3}, and {4} are SCCs.
   * 
   * Example 2:
   * Input: n=4, edges=[[0,1],[1,2],[2,3]]
   * Output: 4
   * Explanation: A DAG has each node as its own SCC.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 18: Minimum Spanning Tree Kruskal
   * 
   * Question: Given n nodes and undirected weighted edges [u,v,w], return the minimum spanning tree weight, or -1 if the graph is disconnected.
   * 
   * Constraints: Kruskal is O(E log E) and works when edges are given explicitly.
   * 
   * Optimized complexity: Time O(E log E); Space O(V). Kruskal sorts once and uses DSU.
   * Recursive complexity: Time O(E log E); Space O(V + E). Recursive scan accepts sorted edges with DSU.
   * 
   * Example 1:
   * Input: n=4, edges=[[0,1,1],[1,2,2],[2,3,3],[0,3,10]]
   * Output: 6
   * Explanation: Choose the first three edges.
   * 
   * Example 2:
   * Input: n=3, edges=[[0,1,5]]
   * Output: -1
   * Explanation: Node 2 is disconnected.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 19: Minimum Spanning Tree Prim
   * 
   * Question: Given n nodes and undirected weighted edges, return the MST weight using Prim-style frontier expansion, or -1 if disconnected.
   * 
   * Constraints: Prim with adjacency heap runs in O(E log V).
   * 
   * Optimized complexity: Time O(E log V); Space O(V + E). Heap-based Prim tracks frontier edges.
   * Recursive complexity: Time O(VE); Space O(V + recursion depth). Recursive dense Prim adds one node per call.
   * 
   * Example 1:
   * Input: n=4, edges=[[0,1,1],[1,2,2],[2,3,3],[0,3,10]]
   * Output: 6
   * Explanation: The frontier picks weights 1, 2, and 3.
   * 
   * Example 2:
   * Input: n=3, edges=[[0,1,5]]
   * Output: -1
   * Explanation: Node 2 never enters the tree.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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

  /*
   * Question 20: Minimum Obstacle Removal to Reach Corner
   * 
   * Question: Given a grid of 0 empty cells and 1 obstacles, return the minimum obstacles to remove to travel from top-left to bottom-right.
   * 
   * Constraints: Grid can be large; O(mn) 0-1 BFS is better than heap Dijkstra.
   * 
   * Optimized complexity: Time O(mn); Space O(mn). 0-1 BFS processes each directed grid edge a constant number of times.
   * Recursive complexity: Time O(mn * improvements); Space O(mn). Recursive relaxation prunes non-improving costs.
   * 
   * Example 1:
   * Input: grid=[[0,1,1],[1,1,0],[1,1,0]]
   * Output: 2
   * Explanation: Remove two obstacles along the best route.
   * 
   * Example 2:
   * Input: grid=[[0,1,0,0,0],[0,1,0,1,0],[0,0,0,1,0]]
   * Output: 0
   * Explanation: A zero-obstacle path exists.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/advanced-graphs.html
   */


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
}
