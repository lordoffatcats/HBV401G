import cluster4.team4d.Booking;
import cluster4.team4d.BookingDB;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookingDBMock implements BookingDB {
    private final Map<UUID, Booking> bookings = new HashMap<>();

    @Override
    public boolean insertBooking(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
        return true;
    }

    @Override
    public Booking selectBooking(UUID bookingId) {
        return bookings.get(bookingId);
    }

    @Override
    public boolean updateBooking(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
        return true;
    }
}
