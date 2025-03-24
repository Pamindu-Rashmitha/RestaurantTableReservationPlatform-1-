package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private String reservationId;
    private String userId;
    private String date;
    private String time;
    private int numberOfGuests;
    private String status;

    public Reservation(String reservationId, String userId, String date, String time, int numberOfGuests, String status) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
    }


    public String getReservationId() {
        return reservationId;
    }
    public String getUserId() {
        return userId;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    public String getStatus() {
        return status;
    }


    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

