package util;

import model.Reservation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {
    private ReservationQueue reservationQueue;
    private String filePath;

    public ReservationManager() {
        this.reservationQueue = new ReservationQueue();
        this.filePath = "WEB-INF/reservations.txt";
    }

    public List<Reservation> getAllReservations(String filePath) {
        List<Reservation> reservations = new ArrayList();

        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Reservation reservation = new Reservation(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4]), parts[5]);
                    reservations.add(reservation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mergeSortReservations(reservations);
    }

    public boolean addReservation(Reservation reservation, String filePath) {
        List<Reservation> reservations = this.getAllReservations(filePath);

        for(Reservation r : reservations) {
            if (r.getReservationId().equals(reservation.getReservationId())) {
                return false;
            }
        }

        reservation.setStatus("Pending");
        reservationQueue.enqueue(reservation); // Add to queue instead of directly to file
        return true;
    }

    public void saveReservations(List<Reservation> reservations, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for(Reservation reservation : reservations) {
                String var10001 = reservation.getReservationId();
                writer.write(var10001 + "," + reservation.getUserId() + "," + reservation.getDate() + "," + reservation.getTime() + "," + reservation.getNumberOfGuests() + "," + reservation.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean processNextReservation() {
        Reservation reservation = reservationQueue.dequeue();
        if (reservation != null) {
            reservation.setStatus("Confirmed");
            List<Reservation> reservations = getAllReservations(filePath);
            reservations.add(reservation);
            saveReservations(reservations, filePath);
            return true;
        }
        return false;
    }

    public ReservationQueue getReservationQueue() {
        return reservationQueue;
    }

    public List<Reservation> getReservationsByUser(String userId, String filePath) {
        List<Reservation> userReservations = new ArrayList();

        for(Reservation reservation : this.getAllReservations(filePath)) {
            if (reservation.getUserId().equals(userId)) {
                userReservations.add(reservation);
            }
        }

        return userReservations;
    }

    public Reservation getReservationById(String reservationId, String filePath) {
        for(Reservation r : this.getAllReservations(filePath)) {
            if (r.getReservationId().equals(reservationId)) {
                return r;
            }
        }
        return null;
    }

    public List<Reservation> mergeSortReservations(List<Reservation> reservations) {
        if (reservations.size() <= 1) {
            return reservations;
        } else {
            int mid = reservations.size() / 2;
            List<Reservation> left = this.mergeSortReservations(new ArrayList(reservations.subList(0, mid)));
            List<Reservation> right = this.mergeSortReservations(new ArrayList(reservations.subList(mid, reservations.size())));
            return this.merge(left, right);
        }
    }

    private List<Reservation> merge(List<Reservation> left, List<Reservation> right) {
        List<Reservation> merged = new ArrayList();
        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            if ((new ReservationTimeComparator()).compare((Reservation) left.get(i), (Reservation) right.get(j)) <= 0) {
                merged.add((Reservation) left.get(i));
                ++i;
            } else {
                merged.add((Reservation) right.get(j));
                ++j;
            }
        }

        merged.addAll(left.subList(i, left.size()));
        merged.addAll(right.subList(j, right.size()));
        return merged;
    }

    public boolean updateReservation(Reservation updatedRes, String filePath) {
        List<Reservation> reservations = this.getAllReservations(filePath);

        for(int i = 0; i < reservations.size(); ++i) {
            if (((Reservation)reservations.get(i)).getReservationId().equals(updatedRes.getReservationId())) {
                reservations.set(i, updatedRes);
                this.saveReservations(reservations, filePath);
                return true;
            }
        }
        return false;
    }

    public boolean removeReservationsByUser(String userId, String filePath) {
        List<Reservation> reservations = this.getAllReservations(filePath);
        boolean removed = reservations.removeIf((reservation) -> reservation.getUserId().equals(userId));
        if (removed) {
            this.saveReservations(reservations, filePath);
        }
        return removed;
    }
}