package util;

import model.Reservation;

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

    public void saveReservations(List<Reservation> reservations, String filePath) {
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

    public Reservation getReservationById(String reservationId, String filePath) {
        List<Reservation> reservations = getAllReservations(filePath);
        for (Reservation r : reservations) {
            if (r.getReservationId().equals(reservationId)) {
                return r;
            }
        }
        return null;
    }

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


}
