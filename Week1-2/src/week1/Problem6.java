package week1;

import java.util.concurrent.*;
import java.util.*;

class RateLimiter {

    static class TokenBucket {
        long capacity;
        double tokens;
        double refillRate;
        long lastRefillTime;

        TokenBucket(long capacity, double refillRate) {
            this.capacity = capacity;
            this.tokens = capacity;
            this.refillRate = refillRate;
            this.lastRefillTime = System.currentTimeMillis();
        }

        synchronized boolean allowRequest() {
            refill();
            if (tokens >= 1) {
                tokens -= 1;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            double tokensToAdd = (now - lastRefillTime) / 1000.0 * refillRate;
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = now;
        }

        synchronized long getRemainingTokens() {
            refill();
            return (long) tokens;
        }
    }

    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private final long capacity;
    private final double refillRate;

    public RateLimiter(long capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    public String checkRateLimit(String clientId) {
        TokenBucket bucket = buckets.computeIfAbsent(clientId,
                k -> new TokenBucket(capacity, refillRate));

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining)";
        }
    }

    public String getRateLimitStatus(String clientId) {
        TokenBucket bucket = buckets.get(clientId);
        if (bucket == null) return "No data";

        long remaining = bucket.getRemainingTokens();
        return "Remaining: " + remaining + ", Limit: " + capacity;
    }
}

public class Problem6 {
    public static void main(String[] args) throws InterruptedException {

        RateLimiter limiter = new RateLimiter(1000, 1000.0 / 3600.0);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1100; i++) {
            executor.submit(() -> {
                String result = limiter.checkRateLimit("abc123");
                System.out.println(result);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println(limiter.getRateLimitStatus("abc123"));
    }
}