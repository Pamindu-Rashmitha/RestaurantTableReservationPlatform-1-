package util;

import model.Reservation;
import model.ReservationQueue;

import java.io.*;
import java.util.*;

/**
 * Central business-logic layer for the restaurant platform.
 *  – Flat-file persistence (reservations.txt)
 *  – Confirmed-queue + waiting-list logic
 *  – Atomic add / cancel / update operations
 */
public class ReservationManager {

    /** runtime queue (confirmed) + waiting list */
    private final ReservationQueue activeReservations = new ReservationQueue();

    /* ---------- Low-level helpers ---------------------------------------------------- */

    /** Load file → rebuild queue + waiting list */
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
                    if (!activeReservations.isFull()) activeReservations.enqueue(r);
                    else { r.setStatus("WAITING"); activeReservations.getWaitingList().add(r); }
                } else if ("WAITING".equalsIgnoreCase(r.getStatus())) {
                    activeReservations.getWaitingList().add(r);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    /** Persist queue + waiting list (skip CANCELLED) */
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
        } catch (IOException e) { e.printStackTrace(); }
    }
    private static String csv(Reservation r) {
        return String.join(",", r.getReservationId(), r.getUserId(), r.getDate(),
                r.getTime(), String.valueOf(r.getNumberOfGuests()), r.getStatus());
    }

    /* ---------- CRUD API ------------------------------------------------------------- */

    /** Add → returns false if duplicate id */
    public boolean addReservation(Reservation r, String path) {
        loadCurrentState(path);
        if (getReservationById(r.getReservationId(), path) != null) return false;
        if (activeReservations.isFull()) { r.setStatus("WAITING"); activeReservations.getWaitingList().add(r); }
        else { r.setStatus("CONFIRMED"); activeReservations.enqueue(r); }
        saveReservations(path);
        return true;
    }

    /** Cancel → auto-promote; returns false if id not found */
    public boolean cancelReservationAndPromote(String id, String path) {
        loadCurrentState(path);
        Reservation removed = activeReservations.remove(id);
        if (removed == null) removed = removeFromWaitingList(id);
        if (removed == null) return false;

        removed.setStatus("CANCELLED");
        if (!activeReservations.isFull() && !activeReservations.getWaitingList().isEmpty()) {
            Reservation next = activeReservations.getWaitingList().poll();
            next.setStatus("CONFIRMED");
            activeReservations.enqueue(next);
        }
        saveReservations(path);
        return true;
    }

    /** Update: remove-then-add to re-evaluate queue status */
    public boolean updateReservation(Reservation upd, String path) {
        if (!cancelReservationAndPromote(upd.getReservationId(), path)) return false;
        return addReservation(upd, path);
    }

    /** Remove *all* reservations for a user (admin purge) */
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
            if (it.next().getUserId().equals(userId)) { it.remove(); removed = true; }
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

    /* ---------- Queries -------------------------------------------------------------- */

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

    public List<Reservation> getConfirmedReservations(String path) {
        loadCurrentState(path);
        List<Reservation> list = new ArrayList<>();
        for (int i = 0; i < activeReservations.size(); i++) list.add(activeReservations.peek(i));
        return list;
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

    public List<Reservation> getAllReservations(String path) {
        loadCurrentState(path);
        List<Reservation> all = new ArrayList<>(getConfirmedReservations(path));
        all.addAll(getWaitingReservations(path));
        return all;
    }

    /** 1-based waiting-list position; −1 if not in waiting list */
    public int getWaitingListPosition(Reservation res) {
        int pos = 1;
        for (Reservation r : activeReservations.getWaitingList()) {
            if (r.getReservationId().equals(res.getReservationId())) return pos;
            pos++;
        }
        return -1;
    }

    /* ---------- Internal ------------------------------------------------------------- */

    private Reservation removeFromWaitingList(String id) {
        Iterator<Reservation> it = activeReservations.getWaitingList().iterator();
        while (it.hasNext()) {
            Reservation r = it.next();
            if (r.getReservationId().equals(id)) { it.remove(); return r; }
        }
        return null;
    }
}

