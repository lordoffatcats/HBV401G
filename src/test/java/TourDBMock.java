import cluster4.team4d.Tour;
import cluster4.team4d.TourDB;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * A mock object for the TourDB interface. It uses a simple
 * HashMap structure for database lookup.
 */
public class TourDBMock implements TourDB {
    private final HashMap<UUID, Tour> tours = new HashMap<>();

    @Override
    public Collection<Tour> selectTours() {
        // Not used by our test cases, but would be used for listing tours.
        return tours.values();
    }

    @Override
    public Collection<Tour> searchTours(String keyword) {
        // Not used by our test cases, but would be used for searching tours.
        return tours.values();
    }

    @Override
    public Tour selectTour(UUID tourId) {
        // Used by the Controller to validate tour existence
        // and fetching the domain model object to update available spots.
        return tours.get(tourId);
    }

    @Override
    public boolean insertTour(Tour tour) {
        // Insert tour implemented to add mock tour objects to
        // HashMap database.
        tours.put(tour.getTourId(), tour);
        return true;
    }

    @Override
    public boolean updateTour(Tour tour) {
        // Updates the tour in the HashMap database,
        // implemented for the success case where the Controller
        // reduces the available spots on the object and then saves to DB.
        tours.put(tour.getTourId(), tour);
        return true;
    }
}
