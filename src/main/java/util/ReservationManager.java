package util;

import model.Reservation;
import model.ReservationQueue;

import java.io.*;
import java.util.*;


public class ReservationManager {


    private final ReservationQueue activeReservations = new ReservationQueue();



    // Load file and rebuild queue + waiting list
    private void loadCurrentState(String filePath) {
        activeReservations.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String ln;
            while ((ln = br.readLine()) != null) {
                String[] p = ln.split(",");
                if (p.length != 6) continue;
                Reservation r = new Reservation(p[0], p[1], p[2], p[3],
                        Integer.parseInt(p[4]), p[5]);
                if ("CONFIRMED".equalsIgnoreCase(r.getStatus())) {
                    if (!activeReservations.isFull()) {
                        activeReservations.enqueue(r);
                    }
                    else {
                        r.setStatus("WAITING"); activeReservations.getWaitingList().add(r);
                    }
                } else if ("WAITING".equalsIgnoreCase(r.getStatus())) {
                    activeReservations.getWaitingList().add(r);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //save reservations
    public void saveReservations(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // queue first
            for (int i = 0; i < activeReservations.size(); i++) {
                Reservation r = activeReservations.peek(i);
                bw.write(csv(r)); bw.newLine();
            }
            // waiting list
            for (Reservation r : activeReservations.getWaitingList()) {
                bw.write(csv(r)); bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String csv(Reservation r) {
        return String.join(",", r.getReservationId(), r.getUserId(), r.getDate(),
                r.getTime(), String.valueOf(r.getNumberOfGuests()), r.getStatus());
    }

    //Add reservation
    public boolean addReservation(Reservation r, String path) {
        loadCurrentState(path);
        if (getReservationById(r.getReservationId(), path) != null){
            return false;
        }
        if (activeReservations.isFull()) {
            r.setStatus("WAITING"); activeReservations.getWaitingList().add(r);
        }
        else {
            r.setStatus("CONFIRMED"); activeReservations.enqueue(r);
        }
        saveReservations(path);
        return true;
    }

    //cancel reservation
    public boolean cancelReservationAndPromote(String id, String path) {
        loadCurrentState(path);
        Reservation removed = activeReservations.remove(id);
        if (removed == null) {
            removed = removeFromWaitingList(id);
        }
        if (removed == null) {
            return false;
        }

        removed.setStatus("CANCELLED");
        if (!activeReservations.isFull() && !activeReservations.getWaitingList().isEmpty()) {
            Reservation next = activeReservations.getWaitingList().poll();
            next.setStatus("CONFIRMED");
            activeReservations.enqueue(next);
        }
        saveReservations(path);
        return true;
    }

    //update reservation
    public boolean updateReservation(Reservation upd, String path) {
        if (!cancelReservationAndPromote(upd.getReservationId(), path)) {
            return false;
        }
        return addReservation(upd, path);
    }

    //remove a reservation
    public boolean removeReservationsByUser(String userId, String path) {
        loadCurrentState(path);
        boolean removed = false;

        // queue
        for (int i = 0; i < activeReservations.size();) {
            Reservation r = activeReservations.peek(i);
            if (r != null && r.getUserId().equals(userId)) {
                activeReservations.remove(r.getReservationId());
                removed = true;
            } else i++;
        }
        // waiting list
        Iterator<Reservation> it = activeReservations.getWaitingList().iterator();
        while (it.hasNext()) {
            if (it.next().getUserId().equals(userId)) {
                it.remove(); removed = true;
            }
        }
        // fill vacancies
        while (!activeReservations.isFull() && !activeReservations.getWaitingList().isEmpty()) {
            Reservation nxt = activeReservations.getWaitingList().poll();
            nxt.setStatus("CONFIRMED");
            activeReservations.enqueue(nxt);
        }
        if (removed) saveReservations(path);
        return removed;
    }



    public Reservation getReservationById(String id, String path) {
        loadCurrentState(path);
        for (int i = 0; i < activeReservations.size(); i++) {
            Reservation r = activeReservations.peek(i);
            if (r != null && r.getReservationId().equals(id)) return r;
        }
        for (Reservation r : activeReservations.getWaitingList()) {
            if (r.getReservationId().equals(id)) return r;
        }
        return null;
    }

    public List<Reservation> getWaitingReservations(String path) {
        loadCurrentState(path);
        return new ArrayList<>(activeReservations.getWaitingList());
    }

    public List<Reservation> getReservationsByUser(String user, String path) {
        loadCurrentState(path);
        List<Reservation> list = new ArrayList<>();
        for (int i = 0; i < activeReservations.size(); i++) {
            Reservation r = activeReservations.peek(i);
            if (r != null && r.getUserId().equals(user)) list.add(r);
        }
        for (Reservation r : activeReservations.getWaitingList()) {
            if (r.getUserId().equals(user)) list.add(r);
        }
        return list;
    }



    //get the position in waiting list
    public int getWaitingListPosition(Reservation res) {
        int pos = 1;
        for (Reservation r : activeReservations.getWaitingList()) {
            if (r.getReservationId().equals(res.getReservationId())) return pos;
            pos++;
        }
        return -1;
    }



    private Reservation removeFromWaitingList(String id) {
        Iterator<Reservation> it = activeReservations.getWaitingList().iterator();
        while (it.hasNext()) {
            Reservation r = it.next();
            if (r.getReservationId().equals(id)) { it.remove(); return r; }
        }
        return null;
    }

    public Reservation[] mergeSortReservations(Reservation[] reservations) {
        if (reservations == null || reservations.length <= 1) {
            return reservations;
        }
        //dividing the array
        int mid = reservations.length / 2;
        Reservation[] left = new Reservation[mid];
        Reservation[] right = new Reservation[reservations.length - mid];

        // Copy elements to left and right arrays
        System.arraycopy(reservations, 0, left, 0, mid);
        System.arraycopy(reservations, mid, right, 0, reservations.length - mid);

        //dividing the left and right again
        left = mergeSortReservations(left);
        right = mergeSortReservations(right);
        return merge(left, right);
    }

    //merge the sorted left and right array
    private Reservation[] merge(Reservation[] left, Reservation[] right) {
        //create a new array "merged" to store the merged result
        Reservation[] merged = new Reservation[left.length + right.length];

        int i = 0, j = 0, k = 0;

        //create an object from ReservationTimeComparator
        ReservationTimeComparator comparator = new ReservationTimeComparator();

        //compare elements from left and right
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

    public List<Reservation> getConfirmedReservationsSorted(String path) {
        //  Load everything into  activeReservations queue
        loadCurrentState(path);

        // Grab the confirmed reservations as a List
        List<Reservation> confirmed = new ArrayList<>();
        for (int i = 0; i < activeReservations.size(); i++) {
            confirmed.add(activeReservations.peek(i));
        }

        // Convert List -> Array
        Reservation[] arr = confirmed.toArray(new Reservation[0]);

        // Sort the array in-place using your mergeSortReservations
        Reservation[] sortedArr = mergeSortReservations(arr);

        // Convert back to List and return
        List<Reservation> sortedList = new ArrayList<>(sortedArr.length);
        for (Reservation r : sortedArr) {
            sortedList.add(r);
        }
        return sortedList;
    }
}

