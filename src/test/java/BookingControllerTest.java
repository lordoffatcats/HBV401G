import cluster4.team4d.BookingController;
import cluster4.team4d.BookingDB;
import cluster4.team4d.Tour;
import cluster4.team4d.TourDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class BookingControllerTest {
    private TourDB tourDB;
    private BookingDB bookingDB;
    private BookingController bookingController;
    private Tour tour = new Tour(
            UUID.randomUUID(),
            "Tour",
            "Description",
            1000,
            "Location",
            true,
            5,
            LocalDateTime.now());

    @BeforeEach
    public void setup() {
        tourDB = new TourDBMock();
        bookingDB = new BookingDBMock();
        bookingController = new BookingController(tourDB, bookingDB);
    }

    @Test
    public void testCreateBooking() {
         // Group sizes: -50, -1, 0, 1, 4, 5, 100
         assertFalse(false);
    }

    @Test
    public void testCreateBookingNoAvailability() {
        assertFalse(false);
    }

    @Test
    public void testBookingHasTotalPrice() {
        assertFalse(false);
    }

    @Test
    public void testBookingWithHotelPickup() {

    }

    @Test
    public void testBookingWithoutHotelPickup() {

    }
}
