package util;

import model.Reservation;
import model.ReservationQueue;

import java.io.*;
import java.util.*;

public class ReservationManager {
    private static final int MAX_TABLES = 1;
    private final ReservationQueue activeReservations = new ReservationQueue();
    private final LinkedList<Reservation> waitingList = new LinkedList<>();

    public ReservationManager() {
    }

    public List<Reservation> getAllReservations(String filePath) {
        Reservation[] reservations = loadReservationsFromFile(filePath);
        reservations = mergeSortReservations(reservations);

        return new ArrayList<>(Arrays.asList(reservations));
    }

    private Reservation[] loadReservationsFromFile(String filePath) {
        List<Reservation> tempList = new ArrayList<>();
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Reservation reservation = new Reservation(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4]), parts[5]);
                    tempList.add(reservation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Restore queue/waiting list state
        activeReservations.clear();
        waitingList.clear();

        for (Reservation res : tempList) {
            if ("CONFIRMED".equals(res.getStatus())) {
                if (!activeReservations.isFull()) {
                    activeReservations.enqueue(res);
                } else {
                    res.setStatus("WAITING");
                    waitingList.add(res);
                }
            } else if ("WAITING".equals(res.getStatus())) {
                waitingList.add(res);
            }
        }

        return tempList.toArray(new Reservation[0]);
    }

    public boolean addReservation(Reservation reservation, String filePath) {
        // Load current state
        List<Reservation> current = getAllReservations(filePath);

        for (Reservation r : current) {
            if (r.getReservationId().equals(reservation.getReservationId())) {
                return false; // Duplicate ID
            }
        }

        // Determine status and place in appropriate structure
        if (activeReservations.isFull()) {
            reservation.setStatus("WAITING");
            waitingList.add(reservation);
        } else {
            reservation.setStatus("CONFIRMED");
            activeReservations.enqueue(reservation);
        }

        current.add(reservation);
        saveReservations(current, filePath);
        return true;
    }

    public void promoteFromWaitingList() {
        Reservation next = waitingList.poll(); // FIFO order
        if (next != null) {
            next.setStatus("CONFIRMED");
            activeReservations.enqueue(next);

        }
    }

    public void saveReservations(Collection<Reservation> reservations, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Reservation reservation : reservations) {
                writer.write(String.join(",", reservation.getReservationId(), reservation.getUserId(),
                        reservation.getDate(), reservation.getTime(),
                        String.valueOf(reservation.getNumberOfGuests()), reservation.getStatus()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getReservationsByUser(String userId, String filePath) {
        Reservation[] allReservations = loadReservationsFromFile(filePath);
        List<Reservation> userReservations = new ArrayList<>();
        for (Reservation reservation : allReservations) {
            if (reservation.getUserId().equals(userId)) {
                userReservations.add(reservation);
            }
        }
        return userReservations;
    }

    public Reservation getReservationById(String reservationId, String filePath) {
        Reservation[] allReservations = loadReservationsFromFile(filePath);
        for (Reservation r : allReservations) {
            if (r.getReservationId().equals(reservationId)) {
                return r;
            }
        }
        return null;
    }

    public boolean updateReservation(Reservation updatedRes, String filePath) {
        List<Reservation> reservations = getAllReservations(filePath);
        boolean found = false;

        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId().equals(updatedRes.getReservationId())) {
                reservations.set(i, updatedRes);
                found = true;
                break;
            }
        }

        if (found) {
            saveReservations(reservations, filePath);
        }

        return found;
    }

    public boolean removeReservationsByUser(String userId, String filePath) {
        List<Reservation> reservations = getAllReservations(filePath);
        int originalSize = reservations.size();
        reservations.removeIf(r -> r.getUserId().equals(userId));

        saveReservations(reservations, filePath);
        return reservations.size() != originalSize;
    }

    public void cancelReservation(String reservationId, String filePath) {
        List<Reservation> all = getAllReservations(filePath);
        Reservation cancelled = activeReservations.remove(reservationId);

        if (cancelled == null) {
            cancelled = removeFromWaitingList(reservationId);
        }

        if (cancelled != null) {
            cancelled.setStatus("CANCELLED");

            // Promote from waiting list
            if (!waitingList.isEmpty()) {
                Reservation next = waitingList.poll();
                next.setStatus("CONFIRMED");
                activeReservations.enqueue(next);
            }
        }

        // Update and save
        saveReservations(all, filePath);
    }

    private Reservation removeFromWaitingList(String reservationId) {
        Iterator<Reservation> iterator = waitingList.iterator();
        while (iterator.hasNext()) {
            Reservation res = iterator.next();
            if (res.getReservationId().equals(reservationId)) {
                iterator.remove();
                return res;
            }
        }
        return null;
    }

    public int getWaitingListPosition(Reservation reservation) {
        for (int i = 0; i < waitingList.size(); i++) {
            if (waitingList.get(i).getReservationId().equals(reservation.getReservationId())) {
                return i + 1;
            }
        }
        return -1;
    }

    public Reservation[] mergeSortReservations(Reservation[] reservations) {
        if (reservations == null || reservations.length <= 1) return reservations;

        int mid = reservations.length / 2;
        Reservation[] left = Arrays.copyOfRange(reservations, 0, mid);
        Reservation[] right = Arrays.copyOfRange(reservations, mid, reservations.length);

        return merge(mergeSortReservations(left), mergeSortReservations(right));
    }

    private Reservation[] merge(Reservation[] left, Reservation[] right) {
        Reservation[] merged = new Reservation[left.length + right.length];
        int i = 0, j = 0, k = 0;
        ReservationTimeComparator comparator = new ReservationTimeComparator();

        while (i < left.length && j < right.length) {
            if (comparator.compare(left[i], right[j]) <= 0) {
                merged[k++] = left[i++];
            } else {
                merged[k++] = right[j++];
            }
        }

        while (i < left.length) merged[k++] = left[i++];
        while (j < right.length) merged[k++] = right[j++];

        return merged;
    }
}
