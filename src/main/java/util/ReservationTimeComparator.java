package util;

import model.Reservation;
import java.util.Comparator;

public class ReservationTimeComparator implements Comparator<Reservation> {
    @Override
    public int compare(Reservation r1, Reservation r2) {
        // Compare dates first
        int dateCompare = r1.getDate().compareTo(r2.getDate());
        if (dateCompare != 0) {
            return dateCompare;
        }
        // If dates are equal, compare times
        return r1.getTime().compareTo(r2.getTime());
    }
}