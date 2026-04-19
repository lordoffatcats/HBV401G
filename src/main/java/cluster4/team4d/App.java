package cluster4.team4d;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Scanner;
import java.util.UUID;

public class App {

    // Initialize database access
    private final static TourDB tourDB = new TourDatabase("jdbc:sqlite:./grunnur.db");
    private final static BookingDB bookingDB = new BookingDatabase("jdbc:sqlite:./grunnur.db");

    // Initialize the controllers
    private final static TourController tourController = new TourController(tourDB);
    private final static BookingController bookingController = new BookingController(tourDB, bookingDB);

    // Initialize input scanner
    private final static Scanner scanner = new Scanner(System.in);

    static void main() {
        Database.createdb("./grunnur.db");
        System.out.println("Day tour booking system (test app)");
        System.out.println();

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. List day tours");
            System.out.println("2. Search day tours");
            System.out.println("3. Get tour by ID");
            System.out.println("4. Create booking");
            System.out.println("5. Get booking");
            System.out.println("6. Cancel booking");
            System.out.println("9. Seed database");
            System.out.println("10. Exit");
            System.out.println();
            System.out.print("Pick your option: ");

            int option = Integer.parseInt(scanner.nextLine());
            switch(option) {
                case 1:
                    listDayTours();
                    break;
                case 2:
                    searchDayTours();
                    break;
                case 3:
                    findTourById();
                    break;
                case 4:
                    createBooking();
                    break;
                case 5:
                    getBooking();
                    break;
                case 6:
                    cancelBooking();
                    break;
                case 9:
                    seedDatabase();
                    break;
                case 10: return;
                default:
                    System.out.println("Unrecognized option, please try again.");
                    break;
            }

            System.out.println();
        }
    }

    private static void listDayTours() {
        Collection<Tour> tours = tourController.listTours();
        for (Tour tour : tours) {
            System.out.println(tour.getTourId() + " | " + tour.getTitle());
        }
    }

    private static void searchDayTours() {
        System.out.println("Enter your search keyword: ");
        String keyword = scanner.nextLine();
        System.out.println();
        Collection<Tour> tours = tourController.findTours(keyword);
        for (Tour tour : tours) {
            System.out.println(tour.getTourId() + " | " + tour.getTitle());
        }
    }

    private static void findTourById() {
        System.out.println("Enter your tour ID: ");
        String tourId = scanner.nextLine();
        Tour tour = tourController.getTourById(UUID.fromString(tourId));
        if (tour == null) {
            System.out.println("No tour found with the provided ID.");
            return;
        }

        System.out.println();
        System.out.println("Tour | " + tour.getTitle());
        System.out.println(tour.getDescription());
        System.out.println("Availability: " + tour.getAvailableSpots());
        System.out.println("Time: " + tour.getDateTime());
        System.out.println("Price per person: " + tour.getPricePerPerson());
        System.out.println("Location: " + tour.getLocation());
        System.out.println("Offers pickup: " + tour.getPickupOffered());
    }

    private static void createBooking() {
        System.out.println("Enter the ID of the tour you want to book: ");
        String tourId = scanner.nextLine();
        Tour tour = tourController.getTourById(UUID.fromString(tourId));
        if (tour == null) {
            System.out.println("No tour found with the provided ID.");
            return;
        }

        System.out.println("Enter your email: ");
        String email = scanner.nextLine();

        System.out.println("Enter your group size: ");
        int groupSize = Integer.parseInt(scanner.nextLine());
        if (!tour.hasCapacityFor(groupSize)) {
            System.out.println("The tour does not have enough seats available.");
            return;
        }

        boolean pickupSelected = false;
        String hotelName = "";
        if (tour.getPickupOffered()) {
            System.out.println("Do you want a hotel pickup? (true/false)");
            pickupSelected = Boolean.parseBoolean(scanner.nextLine());

            if (pickupSelected) {
                System.out.println("What is the name of the hotel?");
                hotelName = scanner.nextLine();
            }
        }

        Booking booking = bookingController.createBooking(UUID.fromString(tourId), email, groupSize, pickupSelected, hotelName);
        if (booking == null) {
            System.out.println("Something went wrong: the booking failed.");
            return;
        }

        System.out.println();
        System.out.println("Booking confirmed | " + booking.getBookingId());
        System.out.println("Status: " + booking.getStatus());
        System.out.println("Group size: " + booking.getGroupSize());
        System.out.println("Price: " + booking.getTotalPrice());
    }

    private static void getBooking() {
        System.out.println("Enter your booking ID: ");
        String bookingId = scanner.nextLine();
        Booking booking = bookingController.getBooking(UUID.fromString(bookingId));
        if (booking == null) {
            System.out.println("No booking found with the provided ID.");
            return;
        }

        System.out.println();
        System.out.println("Booking | " + booking.getBookingId());
        System.out.println("Email: " + booking.getEmail());
        System.out.println("Status: " + booking.getStatus());
        System.out.println("Group size: " + booking.getGroupSize());
        System.out.println("Price: " + booking.getTotalPrice());
        System.out.println("Pickup selected: " + booking.getPickupSelected());
        System.out.println("Hotel name: " + booking.getHotelName());
    }

    private static void cancelBooking() {
        System.out.println("Enter your booking ID: ");
        String bookingId = scanner.nextLine();
        Booking booking = bookingController.getBooking(UUID.fromString(bookingId));
        if (booking == null) {
            System.out.println("No booking found with the provided ID.");
            return;
        }

        boolean result = bookingController.cancelBooking(UUID.fromString(bookingId));
        if (!result) {
            System.out.println("Something went wrong: could not cancel booking.");
            return;
        }

        System.out.println();
        System.out.println("Booking has successfully been cancelled.");
    }

    private static void seedDatabase() {
        Tour tour1 = new Tour(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "Golden Circle Highlights",
                "Classic Golden Circle tour with Thingvellir, Geysir, and Gullfoss.",
                14990,
                "Reykjavik",
                true,
                18,
                LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0)
        );
        Tour tour2 = new Tour(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "South Coast Waterfalls",
                "Full-day trip to Seljalandsfoss, Skogafoss, and Reynisfjara black sand beach.",
                17990,
                "Reykjavik",
                true,
                16,
                LocalDateTime.now().plusDays(2).withHour(8).withMinute(30).withSecond(0).withNano(0)
        );
        Tour tour3 = new Tour(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Reykjavik Food Walk",
                "Guided tasting tour through central Reykjavik with local Icelandic dishes.",
                12990,
                "Reykjavik",
                false,
                10,
                LocalDateTime.now().plusDays(3).withHour(13).withMinute(0).withSecond(0).withNano(0)
        );
        Tour tour4 = new Tour(
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                "Sky Lagoon Evening Visit",
                "Relaxing evening trip to Sky Lagoon with transport from downtown Reykjavik.",
                19990,
                "Kopavogur",
                true,
                12,
                LocalDateTime.now().plusDays(4).withHour(18).withMinute(0).withSecond(0).withNano(0)
        );

        tourDB.insertTour(tour1);
        tourDB.insertTour(tour2);
        tourDB.insertTour(tour3);
        tourDB.insertTour(tour4);

        System.out.println("Seeded 4 tours into the database.");
    }
}
