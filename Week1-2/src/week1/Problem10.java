package week1;

import java.util.*;

class MultiLevelCache {

    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        LRUCache(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    private final LRUCache<String, String> L1;
    private final LRUCache<String, String> L2;
    private final Map<String, String> L3;

    private int l1Hits = 0, l2Hits = 0, l3Hits = 0;
    private int l1Req = 0, l2Req = 0, l3Req = 0;

    public MultiLevelCache(int l1Cap, int l2Cap) {
        L1 = new LRUCache<>(l1Cap);
        L2 = new LRUCache<>(l2Cap);
        L3 = new HashMap<>();
    }

    public void putInDB(String key, String value) {
        L3.put(key, value);
    }

    public String getVideo(String key) {

        l1Req++;
        if (L1.containsKey(key)) {
            l1Hits++;
            return "L1 HIT -> " + L1.get(key);
        }

        l2Req++;
        if (L2.containsKey(key)) {
            l2Hits++;
            String val = L2.get(key);
            L1.put(key, val);
            return "L2 HIT -> Promoted to L1 -> " + val;
        }

        l3Req++;
        if (L3.containsKey(key)) {
            l3Hits++;
            String val = L3.get(key);
            L2.put(key, val);
            return "L3 HIT -> Added to L2 -> " + val;
        }

        return "MISS";
    }

    public String getStats() {
        double l1Rate = l1Req == 0 ? 0 : (l1Hits * 100.0 / l1Req);
        double l2Rate = l2Req == 0 ? 0 : (l2Hits * 100.0 / l2Req);
        double l3Rate = l3Req == 0 ? 0 : (l3Hits * 100.0 / l3Req);

        return "L1 Hit Rate: " + String.format("%.2f", l1Rate) + "%\n" +
                "L2 Hit Rate: " + String.format("%.2f", l2Rate) + "%\n" +
                "L3 Hit Rate: " + String.format("%.2f", l3Rate) + "%";
    }
}

public class Problem10 {
    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache(3, 5);

        cache.putInDB("video_123", "VideoData123");
        cache.putInDB("video_999", "VideoData999");

        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_999"));
        System.out.println(cache.getVideo("video_999"));

        System.out.println("\nStats:");
        System.out.println(cache.getStats());
    }
}