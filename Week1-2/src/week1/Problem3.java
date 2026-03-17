package week1;

import java.util.*;

class DNSCache {

    static class Entry {
        String domain;
        String ip;
        long expiry;

        Entry(String domain, String ip, long ttl) {
            this.domain = domain;
            this.ip = ip;
            this.expiry = System.currentTimeMillis() + ttl;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiry;
        }
    }

    private final int capacity;
    private final LinkedHashMap<String, Entry> cache;
    private int hits = 0;
    private int misses = 0;

    public DNSCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, Entry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };
    }

    public synchronized String resolve(String domain) {
        Entry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT -> " + entry.ip;
        }

        if (entry != null && entry.isExpired()) {
            cache.remove(domain);
        }

        misses++;
        String ip = queryUpstream(domain);
        cache.put(domain, new Entry(domain, ip, 3000));
        return "Cache MISS -> " + ip;
    }

    private String queryUpstream(String domain) {
        return "172.217.14." + (new Random().nextInt(100) + 1);
    }

    public synchronized String getStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);
        return "Hit Rate: " + String.format("%.2f", hitRate) + "%";
    }
}

public class Problem3 {
    public static void main(String[] args) throws InterruptedException {

        DNSCache cache = new DNSCache(3);

        System.out.println(cache.resolve("google.com"));
        System.out.println(cache.resolve("google.com"));

        Thread.sleep(3100);

        System.out.println(cache.resolve("google.com"));

        System.out.println(cache.getStats());
    }
}