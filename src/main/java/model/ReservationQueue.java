package model;

import java.util.LinkedList;

/**
 * Circular-array queue for confirmed tables plus a FIFO waiting list.
 * MAX_TABLES is hard-coded to match the restaurant’s capacity.
 */
public class ReservationQueue {
    /** Adjust this to the number of physical tables */
    private static final int MAX_TABLES = 2;

    /* ------- internal storage ------- */
    private final Reservation[] queue;                 // confirmed reservations
    private final LinkedList<Reservation> waitingList; // overflow / waiting list

    private int front;   // index of first confirmed reservation
    private int rear;    // index of last confirmed reservation
    private int size;    // number of confirmed reservations (<= MAX_TABLES)

    public ReservationQueue() {
        queue        = new Reservation[MAX_TABLES];
        waitingList  = new LinkedList<>();
        front = 0;
        rear  = -1;
        size  = 0;
    }

    /* ---------- state helpers ---------- */
    public boolean isFull()  { return size == MAX_TABLES; }
    public boolean isEmpty() { return size == 0;          }
    public int     size()    { return size;               }

    /* ---------- core operations ---------- */

    /** Enqueue: CONFIRMED if space, otherwise WAITING list */
    public void enqueue(Reservation reservation) {
        if (isFull()) {
            reservation.setStatus("WAITING");
            waitingList.add(reservation);
        } else {
            rear = (rear + 1) % MAX_TABLES;
            queue[rear] = reservation;
            reservation.setStatus("CONFIRMED");
            size++;
        }
    }

    /**
     * Remove and return a confirmed reservation by ID.
     * Returns null if the ID isn’t in the confirmed queue.
     */
    public Reservation remove(String reservationId) {
        for (int i = 0; i < size; i++) {
            int idx = (front + i) % MAX_TABLES;
            if (queue[idx] != null && queue[idx].getReservationId().equals(reservationId)) {
                Reservation removed = queue[idx];

                // shift subsequent elements left (circular)
                for (int j = idx; j != rear; j = (j + 1) % MAX_TABLES) {
                    int next = (j + 1) % MAX_TABLES;
                    queue[j] = queue[next];
                }
                queue[rear] = null;
                rear = (rear - 1 + MAX_TABLES) % MAX_TABLES;
                size--;
                return removed;
            }
        }
        return null;
    }

    /** Peek at the 0-based position in the confirmed queue, or null if out of range */
    public Reservation peek(int position) {
        if (position < 0 || position >= size) return null;
        return queue[(front + position) % MAX_TABLES];
    }

    /* ---------- waiting-list access ---------- */
    public LinkedList<Reservation> getWaitingList() {
        return waitingList;
    }

    /* ---------- reset ---------- */
    public void clear() {
        for (int i = 0; i < MAX_TABLES; i++) queue[i] = null;
        front = 0;
        rear  = -1;
        size  = 0;
        waitingList.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Queue: ");
        for (int i = 0; i < size; i++) {
            sb.append(queue[(front + i) % MAX_TABLES]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("] WaitingList: ").append(waitingList);
        return sb.toString();
    }
}

