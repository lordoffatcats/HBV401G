package cluster4.team4d;

import java.util.UUID;

/**
 * BookingController exposes methods for external parties
 * to view, edit and otherwise manipulate bookings
 */
public class BookingController {
    private final TourDB tourDB;
    private final BookingDB bookingDB;

    public BookingController(TourDB tourDB, BookingDB bookingDB) {
        this.tourDB = tourDB;
        this.bookingDB = bookingDB;
    }

    /**
     * Create a new booking for a given tour.
     * @param tourId The UUID of the tour to book seats for.
     * @param email The email to associate the booking to.
     * @param groupSize The size of the group to book seats for.
     * @param pickupSelected A boolean indicating whether pickup was selected.
     *                       Forced to false if the tour does not offer pickup.
     * @param hotelName The name of the hotel to pick up from. Forced to an empty
     *                  string if the tour does not offer pickup.
     * @return The created Booking model if successful, null otherwise.
     */
    public Booking createBooking(UUID tourId, String email, int groupSize, boolean pickupSelected, String hotelName) {
        // Input validation.
        if (tourId == null) return null;
        if (email == null || email.isEmpty()) return null;
        if (groupSize < 1) return null;

        // Check if tour exists nad has capacity for the requested group size.
        Tour tour = tourDB.selectTour(tourId);
        if (tour == null || !tour.hasCapacityFor(groupSize)) {
            return null;
        }

        // Reserve the spots for the group and update the tour
        tour.reserveSpots(groupSize);
        boolean isTourUpdated = tourDB.updateTour(tour);

        if (!isTourUpdated) {
            return null;
        }

        // Create the booking and insert into DB.
        UUID bookingId = UUID.randomUUID();
        int totalPrice = tour.calculatePrice(groupSize);
        boolean pickup = pickupSelected && tour.getPickupOffered();

        Booking booking = new Booking(bookingId, tourId, email, "Pending", groupSize, totalPrice, pickup, hotelName);
        booking.confirm();
        boolean isBookingInserted = bookingDB.insertBooking(booking);
        return isBookingInserted ? booking : null;
    }

    /**
     * Get a stored booking from the database.
     * @param bookingId The UUID of the booking to get from database.
     * @return The Booking model if booking exists, otherwise null.
     */
    public Booking getBooking(UUID bookingId) {
        return bookingDB.selectBooking(bookingId);
    }

    /**
     * Cancel a confirmed booking, and release the reserved seats.
     * @param bookingId The UUID of the booking to cancel.
     * @return A boolean value indicating success or failure.
     */
    public boolean cancelBooking(UUID bookingId) {
        // Verify booking exists and is not already cancelled.
        Booking booking = bookingDB.selectBooking(bookingId);
        if (booking == null || booking.getStatus().equals("Cancelled")) {
            return false;
        }

        // Cancel the booking, and update the database.
        booking.cancel();
        boolean isBookingUpdated = bookingDB.updateBooking(booking);

        // Release the reserved seats and update the tour in database.
        Tour tour = tourDB.selectTour(booking.getTourId());
        tour.freeSpots(booking.getGroupSize());
        boolean isTourUpdated = tourDB.updateTour(tour);

        return isBookingUpdated && isTourUpdated;
    }
}
