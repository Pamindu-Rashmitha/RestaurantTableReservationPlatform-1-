package model;

import java.util.LinkedList;


public class ReservationQueue {

    private static final int MAX_TABLES = 4;

    //storage
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

    public boolean isFull()  {
        return size == MAX_TABLES;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    //CONFIRMED if there is space, otherwise put to WAITING list
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


    public Reservation peek(int position) {
        if (position < 0 || position >= size) return null;
        return queue[(front + position) % MAX_TABLES];
    }

    //access the waiting list
    public LinkedList<Reservation> getWaitingList() {
        return waitingList;
    }


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

