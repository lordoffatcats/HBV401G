import cluster4.team4d.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the fixture for BookingController.createBooking(...), which handles our business logic
 * for when day tours get booked. In these tests we utilize JUnit parameterized tests to test our
 * boundary and representative values with the same code. In here we test most of the success and
 * failure cases, and also include a test for database failures in one path.
 */
public class BookingControllerTest {
    private TourDB tourDB;
    private BookingDB bookingDB;
    private BookingController bookingController;

    private final int pricePerPerson = 1000;
    private final int bookableSpots = 10;

    @BeforeEach
    public void setup() {
        // Fresh mocks and controller object created per test
        tourDB = new TourDBMock();
        bookingDB = new BookingDBMock();

        // skítamix til að testa sqlite database
        //Database.createdb("./grunnur.db");
        //tourDB = new TourDatabase("jdbc:sqlite:./grunnur.db");
        //bookingDB = new BookingDatabase("jdbc:sqlite:./grunnur.db");

        bookingController = new BookingController(tourDB, bookingDB);
    }

    @AfterEach
    void tearDown() {
        // Reset the test environment after each test
        tourDB = null;
        bookingDB = null;
        bookingController = null;

        // skítamix til að testa sqlite database
        //Database.deletedb("./grunnur.db");
    }

    @ParameterizedTest
    @CsvSource({
            "1", // Lower boundary
            "5", // Representative
            "10", // Upper boundary
    })
    public void testCreateBookingReducesSpots(int spotsToBook) {
        // Arrange
        Tour tour = createTour(pricePerPerson, bookableSpots, true); // Create dummy tour
        tourDB.insertTour(tour); // Insert into mock, used by Controller

        // Act
        // Execute createBooking on the Controller
        Booking booking = bookingController.createBooking(
                tour.getTourId(),
                "test@test.com",
                spotsToBook,
                false,
                null);

        // Fetch updated tour from database (the original tour object created above is not modified even though database is updated)
        Tour updatedTour = tourDB.selectTour(tour.getTourId());

        // Assert
        assertNotNull(booking); // Booking should be returned
        assertEquals(bookableSpots - spotsToBook, updatedTour.getAvailableSpots()); // Seats should be reduced
        assertEquals("Confirmed", booking.getStatus());
    }

    @ParameterizedTest
    @CsvSource({
            "-50", // Representative below lower bound
            "-1", // Lower boundary value
            "0", // Lower boundary value
            "11", // Upper boundary value
            "50" // Representative above upper bound
    })
    public void testCreateBookingDoesNotReduceSpots(int spotsToBook) {
        // Arrange
        Tour tour = createTour(pricePerPerson, bookableSpots, true);
        tourDB.insertTour(tour);

        // Act
        // Execute createBooking on the Controller
        Booking booking = bookingController.createBooking(
                tour.getTourId(),
                "test@test.com",
                spotsToBook,
                false,
                null);

        // Assert
        assertNull(booking);
        assertEquals(bookableSpots, tour.getAvailableSpots());
    }

    @Test
    public void testCreateBookingCalculatesTotalPrice() {
        // Arrange
        final int spotsToBook = 5;
        Tour tour = createTour(pricePerPerson, bookableSpots, true); // Create dummy tour
        tourDB.insertTour(tour); // Insert into mock, used by Controller

        // Act
        // Execute createBooking on the Controller
        Booking booking = bookingController.createBooking(
                tour.getTourId(),
                "test@test.com",
                spotsToBook,
                false,
                null);

        // Assert
        assertNotNull(booking); // Booking should be returned
        assertEquals(pricePerPerson*spotsToBook, booking.getTotalPrice()); // Price should be multiplicative
    }

    @Test
    public void testCreateBookingWithHotelPickupAllowed() {
        // Arrange
        final int spotsToBook = 5;
        final String hotelName = "Hotel Name";
        Tour tour = createTour(pricePerPerson, bookableSpots, true); // Create dummy tour
        tourDB.insertTour(tour); // Insert into mock, used by Controller

        // Act
        // Execute createBooking on the Controller
        Booking booking = bookingController.createBooking(
                tour.getTourId(),
                "test@test.com",
                spotsToBook,
                true,
                hotelName);

        // Assert
        assertNotNull(booking); // Booking should be returned
        assertTrue(booking.getPickupSelected()); // Pickup should be reflected on booking
        assertEquals(hotelName, booking.getHotelName()); // Hotel Name should be reflected on booking
    }

    @ParameterizedTest
    @CsvSource({ "true", "false" }) // Test both binary cases
    public void testCreateBookingHotelPickupDisabled(boolean pickupSelected) {
        // Arrange
        final int spotsToBook = 5;
        final String hotelName = "Hotel Name";
        Tour tour = createTour(pricePerPerson, bookableSpots, false); // Create dummy tour
        tourDB.insertTour(tour); // Insert into mock, used by Controller

        // Act
        // Execute createBooking on the Controller
        Booking booking = bookingController.createBooking(
                tour.getTourId(),
                "test@test.com",
                spotsToBook,
                pickupSelected,
                hotelName);

        // Assert
        assertNotNull(booking); // Booking should be returned
        assertFalse(booking.getPickupSelected()); // Pickup should not be allowed
    }

    @Test
    public void testCreateBookingInvalidTourId() {
        // Arrange

        // Act
        // Execute createBooking on the Controller
        Booking booking = bookingController.createBooking(
                null,
                "test@test.com",
                1,
                false,
                null);

        // Assert
        assertNull(booking); // No booking should have been made
    }

    @Test
    public void testCreateBookingTourNotFound() {
        // Arrange
        UUID tourId = UUID.randomUUID(); // Random UUID that is not present in DB mock

        // Act
        // Execute createBooking on the Controller
        Booking booking = bookingController.createBooking(
                tourId,
                "test@test.com",
                1,
                false,
                null);

        // Assert
        assertNull(booking); // No booking should have been made
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    public void testCreateBookingInvalidEmail(String email) {
        // Arrange
        Tour tour = createTour(pricePerPerson, bookableSpots, true);
        tourDB.insertTour(tour);

        // Act
        // Execute createBooking on the Controller
        Booking booking = bookingController.createBooking(
                tour.getTourId(),
                email,
                1,
                false,
                null);

        // Assert
        assertNull(booking); // No booking should have been made
    }

    @Test
    public void testCreateBookingTourUpdateFails() {
        // Arrange
        TourDB tourDBMock = new TourDBFailureMock();
        Tour tour = createTour(pricePerPerson, bookableSpots, true);
        tourDBMock.insertTour(tour);
        BookingController controller = new BookingController(tourDBMock, bookingDB);

        // Act
        // Execute createBooking on the Controller
        Booking booking = controller.createBooking(
                tour.getTourId(),
                "test@test.com",
                1,
                false,
                null);

        // Assert
        assertNull(booking); // No booking should have been made
    }

    private Tour createTour(int pricePerPerson, int spots, boolean pickupOffered) {
        return new Tour(
                UUID.randomUUID(),
                "Test Tour",
                "Description",
                pricePerPerson,
                "Location",
                pickupOffered,
                spots,
                LocalDateTime.now());
    }
}
