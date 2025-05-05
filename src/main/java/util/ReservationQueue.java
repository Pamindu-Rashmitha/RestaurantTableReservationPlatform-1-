package util;

import model.Reservation;
import java.util.LinkedList;
import java.util.Queue;

public class ReservationQueue {
    private Queue<Reservation> reservationQueue;

    public ReservationQueue() {
        this.reservationQueue = new LinkedList<>();
    }

    /*
     * Adds a reservation request to the queue.
     */
    public void enqueue(Reservation reservation) {
        reservationQueue.offer(reservation);
    }

    /*
     * Removes and returns the next reservation request from the queue.
     */
    public Reservation dequeue() {
        return reservationQueue.poll();
    }

    /*
     * Checks if the queue is empty.
     */
    public boolean isEmpty() {
        return reservationQueue.isEmpty();
    }

    /*
     * Returns the number of reservation requests in the queue.
     */
    public int size() {
        return reservationQueue.size();
    }

    /*
     * Peeks at the next reservation request without removing it.
     */
    public Reservation peek() {
        return reservationQueue.peek();
    }
}
