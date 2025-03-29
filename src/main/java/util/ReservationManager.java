package util;

import model.Reservation;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {

    /**
     * Retrieves all reservations from the specified file.
     * @param filePath The path to the reservations file.
     * @return A list of Reservation objects parsed from the file.
     */
    public List<Reservation> getAllReservations(String filePath) {
        List<Reservation> reservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Expected format: id,userId,date,time,guests,status
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Reservation reservation = new Reservation(
                            parts[0],           // id
                            parts[1],           // userId
                            parts[2],           // date
                            parts[3],           // time
                            Integer.parseInt(parts[4]), // guests
                            parts[5]            // status
                    );
                    reservations.add(reservation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // In production, consider using a logging framework
        }
        return reservations;
    }

    /**
     * Adds a new reservation to the file if its ID is unique.
     * @param reservation The reservation to add.
     * @param filePath The path to the reservations file.
     * @return true if the reservation was added, false if a duplicate ID exists.
     */
    public boolean addReservation(Reservation reservation, String filePath) {
        List<Reservation> reservations = getAllReservations(filePath);
        for (Reservation r : reservations) {
            if (r.getReservationId().equals(reservation.getReservationId())) {
                return false; // Duplicate reservation ID
            }
        }
        reservations.add(reservation);
        saveReservations(reservations, filePath);
        return true;
    }

    /**
     * Retrieves all reservations for a specific user.
     * @param userId The ID of the user.
     * @param filePath The path to the reservations file.
     * @return A list of reservations associated with the user.
     */
    public List<Reservation> getReservationsByUser(String userId, String filePath) {
        List<Reservation> userReservations = new ArrayList<>();
        List<Reservation> allReservations = getAllReservations(filePath);
        for (Reservation reservation : allReservations) {
            if (reservation.getUserId().equals(userId)) {
                userReservations.add(reservation);
            }
        }
        return userReservations;
    }

    /**
     * Saves the list of reservations to the specified file.
     * @param reservations The list of reservations to save.
     * @param filePath The path to the reservations file.
     */
    public void saveReservations(List<Reservation> reservations, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Reservation reservation : reservations) {
                writer.write(reservation.getReservationId() + "," + reservation.getUserId() + "," +
                        reservation.getDate() + "," + reservation.getTime() + "," +
                        reservation.getNumberOfGuests() + "," + reservation.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // In production, consider using a logging framework
        }
    }

    /**
     * Retrieves a reservation by its ID.
     * @param reservationId The ID of the reservation to find.
     * @param filePath The path to the reservations file.
     * @return The matching Reservation object, or null if not found.
     */
    public Reservation getReservationById(String reservationId, String filePath) {
        List<Reservation> reservations = getAllReservations(filePath);
        for (Reservation r : reservations) {
            if (r.getReservationId().equals(reservationId)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Updates an existing reservation in the file.
     * @param updatedRes The updated reservation object.
     * @param filePath The path to the reservations file.
     * @return true if the reservation was updated, false if not found.
     */
    public boolean updateReservation(Reservation updatedRes, String filePath) {
        List<Reservation> reservations = getAllReservations(filePath);
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId().equals(updatedRes.getReservationId())) {
                reservations.set(i, updatedRes);
                saveReservations(reservations, filePath);
                return true;
            }
        }
        return false;
    }

    public boolean removeReservationsByUser(String userId, String filePath) {
        List<Reservation> reservations = getAllReservations(filePath);
        boolean removed = reservations.removeIf(reservation -> reservation.getUserId().equals(userId));
        if (removed) {
            saveReservations(reservations, filePath);
        }
        return removed;
    }


}
