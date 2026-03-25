package cluster4.team4d;

import java.util.UUID;

public interface BookingDB {
    boolean insertBooking(Booking booking);
    Booking selectBooking(UUID bookingId);
    boolean updateBooking(Booking booking);
}
