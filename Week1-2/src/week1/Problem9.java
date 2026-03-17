package week1;

import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    long time;

    Transaction(int id, int amount, String merchant, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.time = time;
    }
}

class TransactionAnalyzer {

    public List<int[]> findTwoSum(List<Transaction> list, int target) {
        Map<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : list) {
            int complement = target - t.amount;
            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, t.id});
            }
            map.put(t.amount, t);
        }
        return result;
    }

    public List<int[]> findTwoSumWithWindow(List<Transaction> list, int target, long windowMillis) {
        List<int[]> result = new ArrayList<>();
        Map<Integer, List<Transaction>> map = new HashMap<>();

        for (Transaction t : list) {
            int complement = target - t.amount;
            if (map.containsKey(complement)) {
                for (Transaction prev : map.get(complement)) {
                    if (Math.abs(t.time - prev.time) <= windowMillis) {
                        result.add(new int[]{prev.id, t.id});
                    }
                }
            }
            map.computeIfAbsent(t.amount, k -> new ArrayList<>()).add(t);
        }
        return result;
    }

    public List<List<Integer>> findKSum(List<Transaction> list, int k, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(list, k, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(List<Transaction> list, int k, int target, int start, List<Integer> current, List<List<Integer>> result) {
        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        if (k == 0 || target < 0) return;

        for (int i = start; i < list.size(); i++) {
            current.add(list.get(i).id);
            backtrack(list, k - 1, target - list.get(i).amount, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    public Map<String, List<List<Integer>>> detectDuplicates(List<Transaction> list) {
        Map<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : list) {
            String key = t.amount + "_" + t.merchant;
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
        }

        Map<String, List<List<Integer>>> result = new HashMap<>();

        for (String key : map.keySet()) {
            List<Transaction> group = map.get(key);
            if (group.size() > 1) {
                List<List<Integer>> ids = new ArrayList<>();
                for (Transaction t : group) {
                    ids.add(Arrays.asList(t.id));
                }
                result.put(key, ids);
            }
        }
        return result;
    }
}

public class Problem9 {
    public static void main(String[] args) {

        List<Transaction> transactions = Arrays.asList(
                new Transaction(1, 500, "Store A", 1000),
                new Transaction(2, 300, "Store B", 1100),
                new Transaction(3, 200, "Store C", 1200),
                new Transaction(4, 500, "Store A", 1300)
        );

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        List<int[]> twoSum = analyzer.findTwoSum(transactions, 500);
        for (int[] pair : twoSum) {
            System.out.println("TwoSum: " + pair[0] + ", " + pair[1]);
        }

        List<int[]> window = analyzer.findTwoSumWithWindow(transactions, 500, 300);
        for (int[] pair : window) {
            System.out.println("Window Pair: " + pair[0] + ", " + pair[1]);
        }

        List<List<Integer>> ksum = analyzer.findKSum(transactions, 3, 1000);
        for (List<Integer> list : ksum) {
            System.out.println("KSum: " + list);
        }

        Map<String, List<List<Integer>>> dup = analyzer.detectDuplicates(transactions);
        for (String key : dup.keySet()) {
            System.out.println("Duplicate: " + key + " -> " + dup.get(key));
        }
    }
}