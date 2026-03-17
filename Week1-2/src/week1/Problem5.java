package week1;

import java.util.*;
import java.util.concurrent.*;

class AnalyticsSystem {

    private final ConcurrentHashMap<String, Integer> pageViews = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<String>> uniqueVisitors = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> sourceCounts = new ConcurrentHashMap<>();

    public void processEvent(String url, String userId, String source) {
        pageViews.merge(url, 1, Integer::sum);

        uniqueVisitors.computeIfAbsent(url, k -> ConcurrentHashMap.newKeySet()).add(userId);

        sourceCounts.merge(source, 1, Integer::sum);
    }

    public List<Map.Entry<String, Integer>> getTopPages(int n) {
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(Map.Entry.comparingByValue());

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {
            pq.offer(entry);
            if (pq.size() > n) pq.poll();
        }

        List<Map.Entry<String, Integer>> result = new ArrayList<>();
        while (!pq.isEmpty()) result.add(pq.poll());
        Collections.reverse(result);
        return result;
    }

    public void getDashboard() {
        List<Map.Entry<String, Integer>> topPages = getTopPages(10);

        System.out.println("Top Pages:");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : topPages) {
            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.getOrDefault(url, Collections.emptySet()).size();

            System.out.println(rank + ". " + url + " - " + views + " views (" + unique + " unique)");
            rank++;
        }

        System.out.println("\nTraffic Sources:");
        for (Map.Entry<String, Integer> entry : sourceCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

public class Problem5 {
    public static void main(String[] args) throws InterruptedException {

        AnalyticsSystem system = new AnalyticsSystem();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        String[] urls = {"/article/news", "/sports/match", "/tech/update"};
        String[] sources = {"google", "facebook", "direct"};

        for (int i = 1; i <= 1000; i++) {
            final int userId = i;
            executor.submit(() -> {
                String url = urls[new Random().nextInt(urls.length)];
                String source = sources[new Random().nextInt(sources.length)];
                system.processEvent(url, "user_" + userId, source);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        system.getDashboard();
    }
}