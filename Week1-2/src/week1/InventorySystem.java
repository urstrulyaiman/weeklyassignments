package week1;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class InventorySystem {

    private final ConcurrentHashMap<String, AtomicInteger> stock = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Integer>> waitingList = new ConcurrentHashMap<>();

    public void addProduct(String productId, int quantity) {
        stock.put(productId, new AtomicInteger(quantity));
        waitingList.put(productId, new ConcurrentLinkedQueue<>());
    }

    public int checkStock(String productId) {
        AtomicInteger count = stock.get(productId);
        return count == null ? 0 : count.get();
    }

    public String purchaseItem(String productId, int userId) {
        AtomicInteger count = stock.get(productId);
        if (count == null) return "Product not found";

        while (true) {
            int current = count.get();
            if (current <= 0) {
                ConcurrentLinkedQueue<Integer> queue = waitingList.get(productId);
                queue.add(userId);
                return "Added to waiting list, position #" + queue.size();
            }
            if (count.compareAndSet(current, current - 1)) {
                return "Success, " + (current - 1) + " units remaining";
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        InventorySystem system = new InventorySystem();
        system.addProduct("IPHONE15_256GB", 100);

        ExecutorService executor = Executors.newFixedThreadPool(50);

        for (int i = 1; i <= 105; i++) {
            final int userId = i;
            executor.submit(() -> {
                String result = system.purchaseItem("IPHONE15_256GB", userId);
                System.out.println("User " + userId + ": " + result);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Final Stock: " + system.checkStock("IPHONE15_256GB"));
    }
}