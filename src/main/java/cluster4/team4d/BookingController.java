package cluster4.team4d;

import java.util.UUID;

public class BookingController {
    private final TourDB tourDB;
    private final BookingDB bookingDB;

    public BookingController(TourDB tourDB, BookingDB bookingDB) {
        this.tourDB = tourDB;
        this.bookingDB = bookingDB;
    }

    public Booking createBooking(UUID tourId, String customerEmail, int groupSize, boolean pickupSelected, String hotelName) {
        if (tourId == null) return null;
        if (customerEmail.isEmpty()) return null;
        if (groupSize < 1) return null;

        Tour tour = tourDB.selectTour(tourId);
        if (!tour.hasCapacityFor(groupSize)) {
            // TODO:
            return null;
        }

        tour.reserveSpots(groupSize);
        boolean isTourUpdated = tourDB.updateTour(tour);

        if (!isTourUpdated) {
            return null;
        }

        UUID bookingId = UUID.randomUUID();
        int totalPrice = tour.calculatePrice(groupSize);
        boolean pickup = pickupSelected && tour.getPickupOffered();

        Booking booking = new Booking(bookingId, tourId, customerEmail, "Pending", groupSize, totalPrice, pickup, hotelName);
        booking.confirm();
        boolean isBookingInserted = bookingDB.insertBooking(booking);
        return isBookingInserted ? booking : null;
    }

    public Booking getBooking(UUID bookingId) {
        return bookingDB.selectBooking(bookingId);
    }

    public boolean cancelBooking(UUID bookingId) {
        Booking booking = bookingDB.selectBooking(bookingId);

        if (booking == null) {
            // TODO:
            return false;
        }

        if (booking.getStatus().equals("Cancelled")) {
            // TODO:
            return false;
        }

        booking.cancel();
        boolean isBookingUpdated = bookingDB.updateBooking(booking);

        Tour tour = tourDB.selectTour(booking.getTourId());
        tour.freeSpots(booking.getGroupSize());
        boolean isTourUpdated = tourDB.updateTour(tour);

        return isBookingUpdated && isTourUpdated;
    }
}
