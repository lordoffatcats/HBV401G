package cluster4.team4d;

import java.util.UUID;

public class Booking {
    private final UUID bookingId;
    private final UUID tourId;
    private final String customerEmail;
    private String status;
    private final int groupSize;
    private final int totalPrice;
    private final boolean pickupSelected;
    private final String hotelName;

    public Booking(UUID bookingId, UUID tourId, String customerEmail, String status, int groupSize, int totalPrice, boolean pickupSelected, String hotelName) {
        this.bookingId = bookingId;
        this.tourId = tourId;
        this.customerEmail = customerEmail;
        this.status = status;
        this.groupSize = groupSize;
        this.totalPrice = totalPrice;
        this.pickupSelected = pickupSelected;
        this.hotelName = hotelName;
    }

    public UUID getBookingId() {
        return this.bookingId;
    }

    public UUID getTourId() {
        return this.tourId;
    }

    public String getStatus() {
        return this.status;
    }

    public int getGroupSize() {
        return this.groupSize;
    }

    public void confirm() {
        this.status = "Confirmed";
    }

    public void cancel() {
        this.status = "Cancelled";
    }
}
