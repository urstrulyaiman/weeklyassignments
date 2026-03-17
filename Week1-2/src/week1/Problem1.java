package week1;

import java.util.*;

class UsernameChecker {
    private Map<String, Integer> usernameToUserId = new HashMap<>();
    private Map<String, Integer> usernameAttempts = new HashMap<>();
    private int userIdCounter = 1;

    public boolean checkAvailability(String username) {
        usernameAttempts.put(username, usernameAttempts.getOrDefault(username, 0) + 1);
        return !usernameToUserId.containsKey(username);
    }

    public void registerUser(String username) {
        if (checkAvailability(username)) {
            usernameToUserId.put(username, userIdCounter++);
        } else {
            System.out.println("Username already taken");
        }
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            suggestions.add(username + i);
        }
        suggestions.add(username.replace("_", "."));
        return suggestions;
    }

    public String getMostAttempted() {
        String maxUser = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : usernameAttempts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxUser = entry.getKey();
            }
        }
        return maxUser;
    }

    public static void main(String[] args) {
        UsernameChecker checker = new UsernameChecker();

        checker.registerUser("john_doe");

        System.out.println(checker.checkAvailability("john_doe"));
        System.out.println(checker.checkAvailability("jane_smith"));

        System.out.println(checker.suggestAlternatives("john_doe"));
        System.out.println(checker.getMostAttempted());
    }
}