import cluster4.team4d.Booking;
import cluster4.team4d.BookingDB;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A mock object for the BookingDB interface. It uses a simple
 * HashMap structure for database lookup.
 */
public class BookingDBMock implements BookingDB {
    private final Map<UUID, Booking> bookings = new HashMap<>();

    @Override
    public boolean insertBooking(Booking booking) {
        // Insert a booking into the HashMap
        // It's used by the Controller in the success cases
        bookings.put(booking.getBookingId(), booking);
        return true;
    }

    @Override
    public Booking selectBooking(UUID bookingId) {
        // Get a booking from the HashMap
        return bookings.get(bookingId);
    }

    @Override
    public boolean updateBooking(Booking booking) {
        // Not used in our cases, but would be used in the cancel booking flow.
        // Implemented for completion.
        bookings.put(booking.getBookingId(), booking);
        return true;
    }
}
