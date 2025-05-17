package util;

import model.Reservation;

public class ReservationQueue {
    private class Node {
        Reservation data;
        Node next;
        Node(Reservation data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node front;
    private Node rear;
    private int size;

    public ReservationQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    public void enqueue(Reservation reservation) {
        Node newNode = new Node(reservation);
        if (isEmpty()) {
            front = newNode;
        } else {
            rear.next = newNode;
        }
        rear = newNode;
        size++;
    }

    public Reservation dequeue() {
        if (isEmpty()) {
            return null;
        }
        Reservation data = front.data;
        front = front.next;
        size--;
        if (isEmpty()) {
            rear = null;
        }
        return data;
    }

    public Reservation peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Reservation[] toArray() {
        Reservation[] array = new Reservation[size];
        Node current = front;
        int index = 0;
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        return array;
    }
}