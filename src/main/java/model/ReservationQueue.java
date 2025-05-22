package model;

public class ReservationQueue {
    private static final int MAX_TABLES = 10;
    private final Reservation[] queue;
    private int front;
    private int rear;
    private int size;

    public ReservationQueue() {
        queue = new Reservation[MAX_TABLES];
        front = 0;
        rear = -1;
        size = 0;
    }

    // Add reservation to queue
    public void enqueue(Reservation reservation) {
        if (isFull()) {
            System.out.println("All tables occupied! Reservation added to waiting list");
            return;
        }

        rear = (rear + 1) % MAX_TABLES;
        queue[rear] = reservation;
        size++;
    }

    // Remove reservation from queue
    public Reservation dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("No reservations in queue");
        }

        Reservation temp = queue[front];
        queue[front] = null;
        front = (front + 1) % MAX_TABLES;
        size--;
        return temp;
    }

    // Check available tables
    public int availableTables() {
        return MAX_TABLES - size;
    }

    // Queue status checks
    public boolean isFull() {
        return size == MAX_TABLES;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public Reservation remove(String reservationId) {
        int count = 0;
        int index = front;
        while (count < size) {
            if (queue[index].getReservationId().equals(reservationId)) {
                Reservation removed = queue[index];
                // Shift remaining elements
                for (int i = index; i != rear; i = (i+1)%MAX_TABLES) {
                    queue[i] = queue[(i+1)%MAX_TABLES];
                }
                if (rear == front) rear = -1;
                else rear = (rear-1+MAX_TABLES)%MAX_TABLES;
                size--;
                return removed;
            }
            index = (index + 1) % MAX_TABLES;
            count++;
        }
        return null;
    }

    // Get current reservations
    public void displayReservations() {
        System.out.println("\nCurrent Reservations:");
        int count = 0;
        int index = front;

        while (count < size) {
            System.out.println(queue[index]);
            index = (index + 1) % MAX_TABLES;
            count++;
        }
    }
}
