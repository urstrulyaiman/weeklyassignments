package week1;

import java.util.*;

class PlagiarismDetector {

    private final int n;
    private final Map<String, Set<String>> index = new HashMap<>();
    private final Map<String, List<String>> documents = new HashMap<>();

    public PlagiarismDetector(int n) {
        this.n = n;
    }

    public void addDocument(String docId, String content) {
        List<String> ngrams = generateNGrams(content);
        documents.put(docId, ngrams);

        for (String gram : ngrams) {
            index.computeIfAbsent(gram, k -> new HashSet<>()).add(docId);
        }
    }

    public void analyzeDocument(String docId) {
        List<String> target = documents.get(docId);
        if (target == null) return;

        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : target) {
            Set<String> docs = index.get(gram);
            if (docs != null) {
                for (String d : docs) {
                    if (!d.equals(docId)) {
                        matchCount.put(d, matchCount.getOrDefault(d, 0) + 1);
                    }
                }
            }
        }

        for (String other : matchCount.keySet()) {
            int matches = matchCount.get(other);
            double similarity = (matches * 100.0) / target.size();

            System.out.println("Compared with " + other);
            System.out.println("Matching n-grams: " + matches);
            System.out.println("Similarity: " + String.format("%.2f", similarity) + "%");
        }
    }

    private List<String> generateNGrams(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();

        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(words[i + j]).append(" ");
            }
            grams.add(sb.toString().trim());
        }
        return grams;
    }
}

public class Problem4 {
    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector(5);

        detector.addDocument("essay_089.txt", "data structures and algorithms are very important in computer science");
        detector.addDocument("essay_092.txt", "data structures and algorithms are essential topics in computer science field");
        detector.addDocument("essay_123.txt", "data structures and algorithms are very important in computer science");

        detector.analyzeDocument("essay_123.txt");
    }
}