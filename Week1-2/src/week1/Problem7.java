package week1;

import java.util.*;

class AutocompleteSystem {

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();
        boolean isEnd;
    }

    private final TrieNode root = new TrieNode();

    public void insert(String query, int count) {
        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
            node.counts.put(query, node.counts.getOrDefault(query, 0) + count);
        }
        node.isEnd = true;
    }

    public List<String> search(String prefix) {
        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) return new ArrayList<>();
            node = node.children.get(c);
        }

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> entry : node.counts.entrySet()) {
            pq.offer(entry);
            if (pq.size() > 10) pq.poll();
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) result.add(pq.poll().getKey());
        Collections.reverse(result);
        return result;
    }

    public void updateFrequency(String query) {
        insert(query, 1);
    }
}

public class Problem7 {
    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.insert("java tutorial", 100);
        system.insert("java stream", 80);
        system.insert("java string", 90);
        system.insert("javascript basics", 70);
        system.insert("java download", 60);

        List<String> results = system.search("ja");

        int rank = 1;
        for (String res : results) {
            System.out.println(rank + ". " + res);
            rank++;
        }

        system.updateFrequency("java tutorial");

        System.out.println("\nAfter update:");
        results = system.search("ja");

        rank = 1;
        for (String res : results) {
            System.out.println(rank + ". " + res);
            rank++;
        }
    }
}