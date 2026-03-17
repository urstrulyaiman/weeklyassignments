package week1;

import java.util.*;

class ParkingLot {

    static class Slot {
        String plate;
        long entryTime;
        boolean occupied;

        Slot() {
            this.plate = null;
            this.entryTime = 0;
            this.occupied = false;
        }
    }

    private final Slot[] table;
    private final int capacity;
    private int size = 0;
    private int totalProbes = 0;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.table = new Slot[capacity];
        for (int i = 0; i < capacity; i++) table[i] = new Slot();
    }

    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % capacity;
    }

    public String parkVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;

        while (probes < capacity) {
            int i = (index + probes) % capacity;

            if (!table[i].occupied) {
                table[i].plate = plate;
                table[i].entryTime = System.currentTimeMillis();
                table[i].occupied = true;
                size++;
                totalProbes += probes;
                return "Assigned spot #" + i + " (" + probes + " probes)";
            }
            probes++;
        }
        return "Parking Full";
    }

    public String exitVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;

        while (probes < capacity) {
            int i = (index + probes) % capacity;

            if (table[i].occupied && table[i].plate.equals(plate)) {
                long duration = (System.currentTimeMillis() - table[i].entryTime) / 1000;
                double fee = duration * 0.01;

                table[i].occupied = false;
                table[i].plate = null;
                size--;

                return "Spot #" + i + " freed, Duration: " + duration + "s, Fee: $" + String.format("%.2f", fee);
            }
            probes++;
        }
        return "Vehicle not found";
    }

    public String getStatistics() {
        double occupancy = (size * 100.0) / capacity;
        double avgProbes = size == 0 ? 0 : (totalProbes * 1.0 / size);
        return "Occupancy: " + String.format("%.2f", occupancy) + "%, Avg Probes: " + String.format("%.2f", avgProbes);
    }
}

public class Problem8 {
    public static void main(String[] args) throws InterruptedException {

        ParkingLot lot = new ParkingLot(500);

        System.out.println(lot.parkVehicle("ABC-1234"));
        System.out.println(lot.parkVehicle("ABC-1235"));
        System.out.println(lot.parkVehicle("XYZ-9999"));

        Thread.sleep(2000);

        System.out.println(lot.exitVehicle("ABC-1234"));

        System.out.println(lot.getStatistics());
    }
}