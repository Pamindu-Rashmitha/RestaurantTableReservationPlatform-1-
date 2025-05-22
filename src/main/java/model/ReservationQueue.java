package model;

public class ReservationQueue {
    private static final int MAX_TABLES = 2;
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

    // Remove and return the front reservation
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

    // Remove a reservation by ID
    public Reservation remove(String reservationId) {
        int count = 0;
        int index = front;

        while (count < size) {
            if (queue[index].getReservationId().equals(reservationId)) {
                Reservation removed = queue[index];

                // Shift elements to fill the gap
                for (int i = index; i != rear; i = (i + 1) % MAX_TABLES) {
                    queue[i] = queue[(i + 1) % MAX_TABLES];
                }

                queue[rear] = null;
                rear = (rear - 1 + MAX_TABLES) % MAX_TABLES;
                size--;
                return removed;
            }
            index = (index + 1) % MAX_TABLES;
            count++;
        }

        return null;
    }

    // Display reservations in queue
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

    // Clear all reservations
    public void clear() {
        front = 0;
        rear = -1;
        size = 0;
        for (int i = 0; i < MAX_TABLES; i++) {
            queue[i] = null;
        }
    }

    public boolean isFull() {
        return size == MAX_TABLES;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int availableTables() {
        return MAX_TABLES - size;
    }

    public int getSize() {
        return size;
    }
}
