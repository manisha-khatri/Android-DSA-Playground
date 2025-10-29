package com.example.core.backendlld.parkinglot;

import java.util.*;

public class ParkingLotSystem {

    // Enum for vehicle sizes
    public enum VehicleSize {
        LARGE, MEDIUM, SMALL
    }

    // Represents a single parking spot
    public static class ParkingSpot {
        private final int id;

        public ParkingSpot(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    // Holds metadata and queues for spots of a particular size
    public static class SpotInfo {
        private final int total;
        private int available;
        private final Queue<ParkingSpot> availableSpots;
        private final Set<Integer> occupiedSpotIds;

        public SpotInfo(int total, int available) {
            this.total = total;
            this.available = available;
            this.availableSpots = new LinkedList<>();
            this.occupiedSpotIds = new HashSet<>();
        }

        public int getTotal() {
            return total;
        }

        public int getAvailable() {
            return available;
        }

        public boolean hasAvailableSpot() {
            return available > 0;
        }

        public ParkingSpot allocateSpot() {
            if (availableSpots.isEmpty()) return null;
            ParkingSpot spot = availableSpots.poll();
            if (spot != null) {
                occupiedSpotIds.add(spot.getId());
                available--;
            }
            return spot;
        }

        public void releaseSpot(int spotId) {
            if (occupiedSpotIds.remove(spotId)) {
                availableSpots.offer(new ParkingSpot(spotId));
                available++;
            }
        }

        public void addAvailableSpot(ParkingSpot spot) {
            availableSpots.offer(spot);
        }
    }

    // Core parking lot class
    public static class ParkingLot {
        private static final int DEFAULT_TOTAL = 500;
        private static final int DEFAULT_AVAILABLE = 300;

        private final Map<VehicleSize, SpotInfo> parkingMap = new EnumMap<>(VehicleSize.class);

        public ParkingLot() {
            for (VehicleSize size : VehicleSize.values()) {
                SpotInfo info = new SpotInfo(DEFAULT_TOTAL, DEFAULT_AVAILABLE);
                initializeSpots(info);
                parkingMap.put(size, info);
            }
        }

        private void initializeSpots(SpotInfo info) {
            int spotIdStart = (int) (Math.random() * 10000); // Make spot IDs unique
            for (int i = 0; i < info.getAvailable(); i++) {
                info.addAvailableSpot(new ParkingSpot(spotIdStart + i));
            }
        }

        public boolean isSpotAvailable(VehicleSize size) {
            SpotInfo info = parkingMap.get(size);
            return info != null && info.hasAvailableSpot();
        }

        public Integer allocateSpot(VehicleSize size) {
            SpotInfo info = parkingMap.get(size);
            if (info == null) return null;

            ParkingSpot spot = info.allocateSpot();
            return (spot != null) ? spot.getId() : null;
        }

        public void releaseSpot(VehicleSize size, int spotId) {
            SpotInfo info = parkingMap.get(size);
            if (info != null) {
                info.releaseSpot(spotId);
            }
        }

        public void printSummary() {
            System.out.println("\n--- Parking Lot Status ---");
            for (Map.Entry<VehicleSize, SpotInfo> entry : parkingMap.entrySet()) {
                System.out.printf("%s -> Available: %d / Total: %d%n",
                        entry.getKey(), entry.getValue().getAvailable(), entry.getValue().getTotal());
            }
        }
    }

    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot();

        System.out.println("Is Large spot available? " + lot.isSpotAvailable(VehicleSize.LARGE));

        Integer largeSpot = lot.allocateSpot(VehicleSize.LARGE);
        System.out.println("Allocated LARGE spot ID: " + largeSpot);

        Integer mediumSpot = lot.allocateSpot(VehicleSize.MEDIUM);
        System.out.println("Allocated MEDIUM spot ID: " + mediumSpot);

        Integer smallSpot = lot.allocateSpot(VehicleSize.SMALL);
        System.out.println("Allocated SMALL spot ID: " + smallSpot);

        lot.printSummary();

        // Release a spot
        lot.releaseSpot(VehicleSize.LARGE, largeSpot);
        System.out.println("\nReleased LARGE spot: " + largeSpot);
        lot.printSummary();
    }
}
