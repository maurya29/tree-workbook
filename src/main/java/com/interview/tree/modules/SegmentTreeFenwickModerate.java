package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 27: Segment Tree / Fenwick. Moderate questions 13-20. */
public class SegmentTreeFenwickModerate {

  /*
   * Question 13: Count Integers in Intervals
   * 
   * Question: Design CountIntervals with add(left, right) and count(), where count returns how many distinct integers are covered by at least one added interval.
   * 
   * Constraints: 1 <= left <= right <= 1e9; many operations require logarithmic interval lookup instead of per-value marking.
   * 
   * Optimized complexity: Time O(k log m) per add where k intervals are merged and m intervals are stored; Space O(m).
   * Recursive complexity: Time O(m) per add in the recursive list merge; Space O(m) plus O(m) recursion depth.
   * 
   * Example 1:
   * Input: add(2, 3), add(7, 10), count()
   * Output: 6
   * Explanation: Covered values are 2,3,7,8,9,10.
   * 
   * Example 2:
   * Input: add(5, 8), count()
   * Output: 8
   * Explanation: The intervals become [2,3] and [5,10], covering 8 integers.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 14: Peaks in Array
   * 
   * Question: Given nums and queries, type 1 [1,l,r] asks for the number of peak indices inside nums[l..r], and type 2 [2,index,value] updates nums[index].
   * 
   * Constraints: nums length and queries can be large, so each query should avoid scanning the whole subarray.
   * 
   * Optimized complexity: Time O((n + q) log n); each update refreshes at most three Fenwick positions; Space O(n).
   * Recursive complexity: Time O((n + q) log n) with recursive segment tree point updates and range sums; Space O(n).
   * 
   * Example 1:
   * Input: nums=[3,1,4,2,5], query [1,0,4]
   * Output: [1]
   * Explanation: Index 2 is the only peak.
   * 
   * Example 2:
   * Input: update index 3 to 6, then query [1,0,4]
   * Output: [1]
   * Explanation: The peak moves from index 2 to index 3.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 15: Booking Concert Tickets in Groups
   * 
   * Question: Design BookMyShow with gather(k,maxRow), which seats k people contiguously in one row, and scatter(k,maxRow), which seats k people anywhere across rows 0..maxRow.
   * 
   * Constraints: n and operations are large; scanning every row for each request is too slow.
   * 
   * Optimized complexity: Time O(log n) for gather and O(t log n) for scatter where t rows are touched; Space O(n).
   * Recursive complexity: Time O(n) in the recursive direct simulation; Space O(n) plus recursion depth.
   * 
   * Example 1:
   * Input: BookMyShow(2,5); gather(4,0)
   * Output: [0,0]
   * Explanation: Row 0 has 5 free seats and gives seats starting at 0.
   * 
   * Example 2:
   * Input: scatter(6,1)
   * Output: true
   * Explanation: The remaining one seat in row 0 and five in row 1 are enough.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 16: Range Frequency Queries
   * 
   * Question: Design RangeFreqQuery so query(left, right, value) returns how many times value appears between left and right inclusive.
   * 
   * Constraints: Large query count makes repeated range scans too slow; preprocessing is allowed because values do not update.
   * 
   * Optimized complexity: Time O(n) preprocessing and O(log f) per query where f is frequency of value; Space O(n).
   * Recursive complexity: Time O(log f) per query using recursive binary searches; Space O(log f) recursion depth.
   * 
   * Example 1:
   * Input: arr=[12,33,4,56,22,2,34,33,22,12,34,56], query(1,2,4)
   * Output: 1
   * Explanation: Only index 2 contains 4.
   * 
   * Example 2:
   * Input: query(0,11,33)
   * Output: 2
   * Explanation: The value 33 appears at indices 1 and 7.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 17: Mutable Range Minimum Query
   * 
   * Question: Design a data structure with update(index, value) and rangeMin(left, right) over a mutable integer array.
   * 
   * Constraints: Use O(log n) updates and queries when the operation count is large.
   * 
   * Optimized complexity: Time O(log n) per update/query with an iterative segment tree; Space O(n).
   * Recursive complexity: Time O(log n) per update/query; Space O(n) tree plus O(log n) recursion depth.
   * 
   * Example 1:
   * Input: nums=[5,2,7,3], rangeMin(1,3)
   * Output: 2
   * Explanation: Minimum in [2,7,3] is 2.
   * 
   * Example 2:
   * Input: update(1,8), rangeMin(1,3)
   * Output: 3
   * Explanation: The range becomes [8,7,3].
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 18: Mutable Range Maximum Query
   * 
   * Question: Design a data structure with update(index, value) and rangeMax(left, right) over a mutable integer array.
   * 
   * Constraints: Large operation counts require O(log n) instead of O(n) range scans.
   * 
   * Optimized complexity: Time O(log n) per update/query with an iterative segment tree; Space O(n).
   * Recursive complexity: Time O(log n) per update/query; Space O(n) tree plus O(log n) recursion depth.
   * 
   * Example 1:
   * Input: nums=[5,2,7,3], rangeMax(1,3)
   * Output: 7
   * Explanation: Maximum in [2,7,3] is 7.
   * 
   * Example 2:
   * Input: update(2,1), rangeMax(1,3)
   * Output: 3
   * Explanation: The range becomes [2,1,3].
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 19: Lazy Propagation Range Add
   * 
   * Question: Design RangeAddSum with addRange(left,right,delta) and sumRange(left,right) over a mutable integer array.
   * 
   * Constraints: Use lazy propagation to keep both range add and range sum near O(log n).
   * 
   * Optimized complexity: Time O(log n) amortized per operation with iterative lazy propagation; Space O(n).
   * Recursive complexity: Time O(log n) per operation with recursive lazy propagation; Space O(n) plus recursion depth.
   * 
   * Example 1:
   * Input: nums=[1,2,3,4], addRange(1,3,2), sumRange(0,3)
   * Output: 16
   * Explanation: Array becomes [1,4,5,6].
   * 
   * Example 2:
   * Input: sumRange(1,2)
   * Output: 9
   * Explanation: The updated middle range is 4+5.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 20: Lazy Propagation Range Assign
   * 
   * Question: Design RangeAssignSum with assignRange(left,right,value) and sumRange(left,right) over a mutable integer array.
   * 
   * Constraints: Lazy assignment keeps repeated range overwrite and range sum operations near O(log n).
   * 
   * Optimized complexity: Time O(log n) amortized per operation with iterative lazy assignment; Space O(n).
   * Recursive complexity: Time O(log n) per operation with recursive lazy assignment; Space O(n) plus recursion depth.
   * 
   * Example 1:
   * Input: nums=[1,2,3,4], assignRange(1,2,5), sumRange(0,3)
   * Output: 15
   * Explanation: Array becomes [1,5,5,4].
   * 
   * Example 2:
   * Input: assignRange(0,3,2), sumRange(1,3)
   * Output: 6
   * Explanation: All positions become 2.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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
}
