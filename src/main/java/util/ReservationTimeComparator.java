package util;

import model.Reservation;

import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ReservationTimeComparator implements Comparator<Reservation> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public int compare(Reservation r1, Reservation r2) {
        try {
            LocalTime time1 = LocalTime.parse(r1.getTime(), formatter);
            LocalTime time2 = LocalTime.parse(r2.getTime(), formatter);
            return time1.compareTo(time2);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing time: " + r1.getTime() + " or " + r2.getTime());
            return 0; // Or handle differently
        }
    }
}
