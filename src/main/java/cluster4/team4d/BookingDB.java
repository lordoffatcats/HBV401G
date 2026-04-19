package cluster4.team4d;

import java.util.UUID;

public interface BookingDB {
    /**
     * Insert a Booking domain model into the database.
     * @param booking The Booking domain model to insert.
     * @return A boolean value indicating success or failure.
     */
    boolean insertBooking(Booking booking);

    /**
     * Get an existing booking from the database.
     * @param bookingId The UUID of the booking to fetch.
     * @return The Booking model if successful, null otherwise.
     */
    Booking selectBooking(UUID bookingId);

    /**
     * Updates an existing booking in the database.
     * @param booking The Booking model to update.
     * @return A boolean value indicating success or failure.
     */
    boolean updateBooking(Booking booking);
}
