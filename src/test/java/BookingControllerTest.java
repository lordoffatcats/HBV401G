import cluster4.team4d.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BookingControllerTest {
    private TourDB tourDB;
    private BookingDB bookingDB;
    private BookingController bookingController;
    private Tour testTour;

    @BeforeEach
    public void setup() {
        tourDB = new TourDBMock();
        bookingDB = new BookingDBMock();
        bookingController = new BookingController(tourDB, bookingDB);
    }

    @AfterEach
    void tearDown() {
        tourDB = null;
        bookingDB = null;
        bookingController = null;
        testTour = null;
    }

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "3, true",
            "5, true",
            "-50, false",
            "-1, false",
            "0, false",
            "6, false",
            "50, false"
    })
    public void testCreateBookingSeats(int seats, boolean isValid) {
        // Tests creating a booking with various amounts of seats.
        // [1-5] seats should return a valid Booking, and anything else should return null.
        testTour = new Tour(
                UUID.randomUUID(),
                "Test Tour",
                "Description",
                1000,
                "Location",
                true,
                5,
                LocalDateTime.now());
        tourDB.insertTour(testTour);
        Booking booking = bookingController.createBooking(
                testTour.getTourId(),
                "test@test.com",
                seats,
                false,
                null
        );
        if (isValid) {
            assertNotNull(booking);
        } else {
            assertNull(booking);
        }
    }

    @Test
    public void testCreateBookingNoAvailability() {
        //Veit ekki alveg hvað var átt við hér
        assertFalse(false);
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1000",
        "3, 3000",
        "5, 5000",
    })
    public void testBookingHasTotalPrice(int seats, int correctTotalPrice) {
        // Tests whether total price calculation is correct.
        testTour = new Tour(
                UUID.randomUUID(),
                "Test Tour",
                "Description",
                1000,
                "Location",
                true,
                5,
                LocalDateTime.now());
        tourDB.insertTour(testTour);
        Booking booking = bookingController.createBooking(
                testTour.getTourId(),
                "test@test.com",
                seats,
                false,
                null
        );
        assertEquals(booking.getTotalPrice(), correctTotalPrice);
    }

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "false, true, false",
            "true, false, true",
            "false, false, true",
    })
    public void testBookingWithHotelPickup(boolean pickupOffered, boolean pickupSelected, boolean isValid) {
        // Tests whether booking for tour is created for
        // combinations of tour offering pickup and pickup selected.
        // If no pickup is offered, but pickup is selected, createBooking should return null.
        testTour = new Tour(
                UUID.randomUUID(),
                "Test Tour",
                "Description",
                1000,
                "Location",
                pickupOffered,
                5,
                LocalDateTime.now());
        tourDB.insertTour(testTour);
        Booking booking = bookingController.createBooking(
                testTour.getTourId(),
                "test@test.com",
                1,
                pickupSelected,
                "Test Hotel"
        );
        if (isValid) {
            assertNotNull(booking);
        } else {
            assertNull(booking);
        }
    }
}
