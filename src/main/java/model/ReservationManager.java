package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {

    public List<Reservation> getAllReservations(String filePath) {
        List<Reservation> reservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Reservation reservation = new Reservation(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4]), parts[5]);
                    reservations.add(reservation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reservations;
    }

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

    private void saveReservations(List<Reservation> reservations, String filePath) {
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
}
