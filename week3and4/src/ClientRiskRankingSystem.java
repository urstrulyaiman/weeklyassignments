import java.util.*;

class Client {
    String name;
    int riskScore;
    double accountBalance;

    Client(String name, int riskScore, double accountBalance) {
        this.name = name;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }
}

public class ClientRiskRankingSystem {

    public static void bubbleSort(Client[] arr) {
        int n = arr.length;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swaps++;
                }
            }
        }

        System.out.print("Bubble (asc): ");
        for (Client c : arr) {
            System.out.print("[" + c.name + ":" + c.riskScore + "] ");
        }
        System.out.println("// Swaps: " + swaps);
    }

    public static void insertionSort(Client[] arr) {
        for (int i = 1; i < arr.length; i++) {
            Client key = arr[i];
            int j = i - 1;

            while (j >= 0 && (arr[j].riskScore < key.riskScore ||
                    (arr[j].riskScore == key.riskScore &&
                            arr[j].accountBalance < key.accountBalance))) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }

        System.out.print("Insertion (desc): ");
        for (Client c : arr) {
            System.out.print("[" + c.name + ":" + c.riskScore + "] ");
        }
        System.out.println();
    }

    public static void topRisks(Client[] arr, int k) {
        System.out.print("Top " + k + " risks: ");
        for (int i = 0; i < k && i < arr.length; i++) {
            System.out.print(arr[i].name + "(" + arr[i].riskScore + ") ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Client[] clients = {
                new Client("clientC", 80, 5000),
                new Client("clientA", 20, 2000),
                new Client("clientB", 50, 3000)
        };

        Client[] bubbleArr = clients.clone();
        bubbleSort(bubbleArr);

        Client[] insertionArr = clients.clone();
        insertionSort(insertionArr);

        topRisks(insertionArr, 3);
    }
}