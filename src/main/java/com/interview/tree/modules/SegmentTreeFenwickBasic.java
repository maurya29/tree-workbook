package com.interview.tree.modules;

import java.util.*;
import com.interview.tree.model.*;

/** Topic 27: Segment Tree / Fenwick. Basic questions 1-12. */
public class SegmentTreeFenwickBasic {

  /*
   * Question 1: Range Sum Query Mutable
   * 
   * Question: Design NumArray with update(index,val) and sumRange(left,right), where sumRange returns the current sum of nums[left..right].
   * 
   * Constraints: 1 <= nums.length <= 30000; up to 30000 calls; values fit in signed int.
   * 
   * Optimized complexity: update Time O(log n), sumRange Time O(log n); Space O(n). Fenwick tree stores prefix deltas.
   * Recursive complexity: update Time O(log n), sumRange Time O(log n); Space O(n). Recursive segment tree splits ranges.
   * 
   * Example 1:
   * Input: NumArray([1,3,5]); sumRange(0,2)
   * Output: 9
   * Explanation: The initial full range sums to 9.
   * 
   * Example 2:
   * Input: update(1,2); sumRange(0,2)
   * Output: 8
   * Explanation: The array becomes [1,2,5].
   * 
   * Example 3:
   * Input: NumArray([7]); update(0,4); sumRange(0,0)
   * Output: 4
   * Explanation: Single index updates and queries are valid.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 2: Range Sum Query 2D Mutable
   * 
   * Question: Design NumMatrix with update(row,col,val) and sumRegion(row1,col1,row2,col2), returning the current rectangle sum.
   * 
   * Constraints: 1 <= rows, cols <= 200; up to 10000 calls; values fit in signed int.
   * 
   * Optimized complexity: update Time O(log m log n), sumRegion Time O(log m log n); Space O(mn). 2D Fenwick tree.
   * Recursive complexity: Time O(rows * cols) per query and O(1) per update; Space O(rows + cols) recursion depth for the direct recursive scan.
   * 
   * Example 1:
   * Input: NumMatrix sample matrix; sumRegion(2,1,4,3)
   * Output: 8
   * Explanation: The selected rectangle sums to 8.
   * 
   * Example 2:
   * Input: update(3,2,2); sumRegion(2,1,4,3)
   * Output: 10
   * Explanation: Updating one cell changes later region sums.
   * 
   * Example 3:
   * Input: matrix = [[5]]; update(0,0,3); sumRegion(0,0,0,0)
   * Output: 3
   * Explanation: The one-cell matrix is handled directly.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 3: Count of Smaller Numbers After Self
   * 
   * Question: Given nums, return counts where counts[i] is the number of smaller elements to the right of nums[i].
   * 
   * Constraints: 1 <= nums.length <= 100000; values fit in signed int.
   * 
   * Optimized complexity: Time O(n log n); Space O(n). Fenwick tree over compressed ranks.
   * Recursive complexity: Time O(n log n); Space O(n). Merge sort counts right-half values that cross each left index.
   * 
   * Example 1:
   * Input: nums = [5,2,6,1]
   * Output: [2,1,1,0]
   * Explanation: 5 has smaller values 2 and 1 after it.
   * 
   * Example 2:
   * Input: nums = [-1]
   * Output: [0]
   * Explanation: No elements appear after the only value.
   * 
   * Example 3:
   * Input: nums = [-1,-1]
   * Output: [0,0]
   * Explanation: Equal values are not smaller.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 4: Reverse Pairs
   * 
   * Question: Given nums, count pairs i < j where nums[i] > 2 * nums[j].
   * 
   * Constraints: 1 <= nums.length <= 50000; values fit in signed int.
   * 
   * Optimized complexity: Time O(n log n); Space O(n). Fenwick tree over compressed nums and doubled thresholds.
   * Recursive complexity: Time O(n log n); Space O(n). Merge sort counts cross pairs before merging sorted halves.
   * 
   * Example 1:
   * Input: nums = [1,3,2,3,1]
   * Output: 2
   * Explanation: Two pairs satisfy the strict double relation.
   * 
   * Example 2:
   * Input: nums = [2,4,3,5,1]
   * Output: 3
   * Explanation: Three left values are more than double a later value.
   * 
   * Example 3:
   * Input: nums = [-5,-5]
   * Output: 1
   * Explanation: -5 > -10 is true for the ordered pair.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 5: Create Sorted Array through Instructions
   * 
   * Question: Given instructions, insert each value into a sorted array and add min(count strictly less, count strictly greater) to the cost.
   * 
   * Constraints: 1 <= instructions.length <= 100000; 1 <= instructions[i] <= 100000; answer modulo 1e9+7.
   * 
   * Optimized complexity: Time O(n log V); Space O(V). Fenwick tree counts inserted values by rank.
   * Recursive complexity: Time O(n log V); Space O(V). Recursive segment tree queries less/greater counts and updates frequencies.
   * 
   * Example 1:
   * Input: instructions = [1,5,6,2]
   * Output: 1
   * Explanation: Only inserting 2 contributes min(1,2)=1.
   * 
   * Example 2:
   * Input: instructions = [1,2,3,6,5,4]
   * Output: 3
   * Explanation: Later smaller values create insertion costs.
   * 
   * Example 3:
   * Input: instructions = [1,3,3,3,2,4,2,1,2]
   * Output: 4
   * Explanation: Duplicates are excluded from strictly less/greater counts.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 6: Count of Range Sum
   * 
   * Question: Given nums and bounds lower and upper, count range sums nums[i..j] whose sum lies in [lower, upper].
   * 
   * Constraints: 1 <= nums.length <= 100000; values and bounds fit int; count can exceed int during computation.
   * 
   * Optimized complexity: Time O(n log n); Space O(n). Fenwick tree counts previous compressed prefix sums in the needed interval.
   * Recursive complexity: Time O(n log n); Space O(n). Merge sort counts valid cross-prefix ranges while sorting prefix sums.
   * 
   * Example 1:
   * Input: nums = [-2,5,-1], lower = -2, upper = 2
   * Output: 3
   * Explanation: Three subarrays have sums inside the target range.
   * 
   * Example 2:
   * Input: nums = [0], lower = 0, upper = 0
   * Output: 1
   * Explanation: The single zero sum is valid.
   * 
   * Example 3:
   * Input: nums = [1,-1], lower = 0, upper = 0
   * Output: 1
   * Explanation: The full subarray sums to zero.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 7: Range Module
   * 
   * Question: Design RangeModule with addRange, queryRange, and removeRange over half-open intervals [left,right).
   * 
   * Constraints: 1 <= left < right <= 1e9; up to 10000 operations.
   * 
   * Optimized complexity: add/remove/query Time O(k log n); Space O(n). TreeMap stores disjoint intervals by start.
   * Recursive complexity: add/remove/query Time O(n); Space O(n + stack). Recursive list processing merges, splits, and scans intervals.
   * 
   * Example 1:
   * Input: addRange(10,20); removeRange(14,16); queryRange(10,14)
   * Output: true
   * Explanation: [10,14) remains covered.
   * 
   * Example 2:
   * Input: queryRange(13,15)
   * Output: false
   * Explanation: The removed gap breaks full coverage.
   * 
   * Example 3:
   * Input: queryRange(16,17)
   * Output: true
   * Explanation: [16,20) remains covered.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 8: My Calendar III
   * 
   * Question: Design MyCalendarThree where book(start,end) adds a half-open event [start,end) and returns the maximum number of overlapping events so far.
   * 
   * Constraints: 0 <= start < end <= 1e9; up to 400 calls.
   * 
   * Optimized complexity: book Time O(n) for sweep-map implementation here; Space O(n). Ordered deltas compute max active count.
   * Recursive complexity: book Time O(log C); Space O(number of touched nodes). Dynamic lazy segment tree over the coordinate domain.
   * 
   * Example 1:
   * Input: book(10,20); book(50,60); book(10,40)
   * Output: 1,1,2
   * Explanation: The third booking overlaps [10,20).
   * 
   * Example 2:
   * Input: book(5,15); book(5,10); book(25,55)
   * Output: 3,3,3 after the sample continuation
   * Explanation: The maximum overlap reaches three.
   * 
   * Example 3:
   * Input: book(1,2); book(2,3)
   * Output: 1,1
   * Explanation: Touching half-open intervals do not overlap.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 9: Falling Squares
   * 
   * Question: Given falling squares as [left, sideLength], return the maximum stack height after each square lands.
   * 
   * Constraints: 1 <= positions.length <= 1000; coordinates and side lengths are positive integers.
   * 
   * Optimized complexity: Time O(n log n); Space O(n). Coordinate-compressed segment tree does range max query and range assign.
   * Recursive complexity: Time O(n^2); Space O(n + stack). Recursive scan finds the base height among previous intervals.
   * 
   * Example 1:
   * Input: positions = [[1,2],[2,3],[6,1]]
   * Output: [2,5,5]
   * Explanation: The second square overlaps the first and stacks to height 5.
   * 
   * Example 2:
   * Input: positions = [[100,100],[200,100]]
   * Output: [100,100]
   * Explanation: Touching intervals do not overlap.
   * 
   * Example 3:
   * Input: positions = [[1,3],[1,2]]
   * Output: [3,5]
   * Explanation: The second square lands on the previous square.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 10: Longest Increasing Subsequence II
   * 
   * Question: Given nums and k, return the length of the longest increasing subsequence where adjacent chosen values differ by at most k.
   * 
   * Constraints: 1 <= nums.length <= 100000; 1 <= nums[i], k <= 100000.
   * 
   * Optimized complexity: Time O(n log V); Space O(V). Segment tree range maximum query over value coordinates.
   * Recursive complexity: Time O(n log V); Space O(V). Recursive segment tree query/update stores best length per value.
   * 
   * Example 1:
   * Input: nums = [4,2,1,4,3,4,5,8,15], k = 3
   * Output: 5
   * Explanation: One valid subsequence is [1,3,4,5,8].
   * 
   * Example 2:
   * Input: nums = [7,4,5,1,8,12,4,7], k = 5
   * Output: 4
   * Explanation: The value-difference limit restricts transitions.
   * 
   * Example 3:
   * Input: nums = [1,5], k = 1
   * Output: 1
   * Explanation: 5 cannot follow 1 because the difference is greater than k.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 11: Number of Longest Increasing Subsequence
   * 
   * Question: Given nums, return the number of longest strictly increasing subsequences.
   * 
   * Constraints: 1 <= nums.length <= 2000; answer fits in 32-bit signed int.
   * 
   * Optimized complexity: Time O(n log n); Space O(n). Segment tree over compressed values stores best length/count pairs.
   * Recursive complexity: Time O(n log n); Space O(n). Recursive segment tree merges length/count pairs.
   * 
   * Example 1:
   * Input: nums = [1,3,5,4,7]
   * Output: 2
   * Explanation: The longest length is 4 with two choices ending at 7.
   * 
   * Example 2:
   * Input: nums = [2,2,2,2,2]
   * Output: 5
   * Explanation: Every single value is a longest subsequence.
   * 
   * Example 3:
   * Input: nums = [1]
   * Output: 1
   * Explanation: The only subsequence has length 1.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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

  /*
   * Question 12: Handling Sum Queries After Update
   * 
   * Question: Given nums1, nums2, and queries, support flipping nums1 bits in a range, adding p times the number of ones in nums1 to the sum of nums2, and reporting the sum.
   * 
   * Constraints: 1 <= nums1.length <= 100000; nums1 is binary; queries length up to 100000.
   * 
   * Optimized complexity: Time O((n+q) log n); Space O(n + answers). Lazy segment tree supports range flip and total ones.
   * Recursive complexity: Time O(nq); Space O(n + answers + recursion). Recursive brute force flips and counts ranges.
   * 
   * Example 1:
   * Input: nums1=[1,0,1], nums2=[0,0,0], queries=[[1,1,1],[2,1,0],[3,0,0]]
   * Output: [3]
   * Explanation: After flipping index 1, nums1 has three ones, so add 3.
   * 
   * Example 2:
   * Input: flip the same range twice
   * Output: original ones count restored
   * Explanation: Lazy flip toggles cancel when applied twice.
   * 
   * Example 3:
   * Input: query type 3 before type 2
   * Output: initial sum(nums2)
   * Explanation: The sum is tracked independently as long.
   * 
   * Source: https://maurya29.github.io/DSA-Pattern-Workbook/pages/segment-tree.html
   */


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
}
