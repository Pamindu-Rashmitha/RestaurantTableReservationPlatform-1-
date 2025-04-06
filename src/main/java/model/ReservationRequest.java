package model;

public class ReservationRequest {
   public String customerName;
   public int partySize;
   public String time;

    public ReservationRequest(String customerName, int partySize, String time) {
        this.customerName = customerName;
        this.partySize = partySize;
        this.time = time;
    }

    @Override
    public String toString() {
        return customerName + " (Party of " + partySize + ") at " + time;
    }
}
