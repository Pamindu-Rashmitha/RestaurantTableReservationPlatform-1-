package util;

import model.Reservation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {
    public ReservationManager() {
    }

    public List<Reservation> getAllReservations(String filePath) {
        Reservation[] reservations = loadReservationsFromFile(filePath);
        reservations = mergeSortReservations(reservations);
        // Convert back to List for compatibility with existing code
        List<Reservation> result = new ArrayList<>();
        for (Reservation res : reservations) {
            result.add(res);
        }
        return result;
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
        // Convert List to array
        Reservation[] reservations = new Reservation[tempList.size()];
        for (int i = 0; i < tempList.size(); i++) {
            reservations[i] = tempList.get(i);
        }
        return reservations;
    }

    public boolean addReservation(Reservation reservation, String filePath) {
        Reservation[] reservations = loadReservationsFromFile(filePath);
        for (Reservation r : reservations) {
            if (r.getReservationId().equals(reservation.getReservationId())) {
                return false;
            }
        }
        Reservation[] updatedReservations = new Reservation[reservations.length + 1];
        System.arraycopy(reservations, 0, updatedReservations, 0, reservations.length);
        updatedReservations[reservations.length] = reservation;
        saveReservations(updatedReservations, filePath);
        return true;
    }

    public void saveReservations(Reservation[] reservations, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Reservation reservation : reservations) {
                writer.write(reservation.getReservationId() + "," + reservation.getUserId() + "," +
                        reservation.getDate() + "," + reservation.getTime() + "," +
                        reservation.getNumberOfGuests() + "," + reservation.getStatus());
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

    public Reservation[] mergeSortReservations(Reservation[] reservations) {
        if (reservations == null || reservations.length <= 1) {
            return reservations;
        }
        int mid = reservations.length / 2;
        Reservation[] left = new Reservation[mid];
        Reservation[] right = new Reservation[reservations.length - mid];
        // Copy elements to left and right arrays
        System.arraycopy(reservations, 0, left, 0, mid);
        System.arraycopy(reservations, mid, right, 0, reservations.length - mid);
        left = mergeSortReservations(left);
        right = mergeSortReservations(right);
        return merge(left, right);
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

        while (i < left.length) {
            merged[k++] = left[i++];
        }

        while (j < right.length) {
            merged[k++] = right[j++];
        }

        return merged;
    }

    public boolean updateReservation(Reservation updatedRes, String filePath) {
        Reservation[] reservations = loadReservationsFromFile(filePath);
        for (int i = 0; i < reservations.length; i++) {
            if (reservations[i].getReservationId().equals(updatedRes.getReservationId())) {
                reservations[i] = updatedRes;
                saveReservations(reservations, filePath);
                return true;
            }
        }
        return false;
    }

    public boolean removeReservationsByUser(String userId, String filePath) {
        Reservation[] reservations = loadReservationsFromFile(filePath);
        int count = 0;
        for (Reservation reservation : reservations) {
            if (!reservation.getUserId().equals(userId)) {
                count++;
            }
        }
        Reservation[] newReservations = new Reservation[count];
        int index = 0;
        for (Reservation reservation : reservations) {
            if (!reservation.getUserId().equals(userId)) {
                newReservations[index++] = reservation;
            }
        }
        saveReservations(newReservations, filePath);
        return reservations.length != newReservations.length;
    }
}