package cluster4.team4d;

import java.util.UUID;

/**
 * The booking model class that contains and keeps track of
 * domain information regarding bookings.
 */
public class Booking {
    private final UUID bookingId;
    private final UUID tourId;
    private final String email;
    private String status;
    private final int groupSize;
    private final int totalPrice;
    private final boolean pickupSelected;
    private final String hotelName;

    public Booking(UUID bookingId, UUID tourId, String email, String status, int groupSize, int totalPrice, boolean pickupSelected, String hotelName) {
        this.bookingId = bookingId;
        this.tourId = tourId;
        this.email = email;
        this.status = status;
        this.groupSize = groupSize;
        this.totalPrice = totalPrice;
        this.pickupSelected = pickupSelected;
        this.hotelName = hotelName;
    }

    public String getEmail() {
        return email;
    }

    public int getTotalPrice() {
        return totalPrice;
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

    public boolean getPickupSelected() {
        return this.pickupSelected;
    }

    public String getHotelName() {
        return this.hotelName;
    }

    /**
     * Sets the booking status as "Confirmed".
     */
    public void confirm() {
        this.status = "Confirmed";
    }

    /**
     * Sets the booking status as "Cancelled".
     */
    public void cancel() {
        this.status = "Cancelled";
    }
}
