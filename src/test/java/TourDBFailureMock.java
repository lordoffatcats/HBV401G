import cluster4.team4d.Tour;
import cluster4.team4d.TourDB;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Simulates a storage failure by returning false from updateTour(...) to
 * test controller's failure path when faced with database failures.
 */
public class TourDBFailureMock implements TourDB {
    private final HashMap<UUID, Tour> tours = new HashMap<>();

    @Override
    public Collection<Tour> selectTours() {
        // Not used by our test cases, but would be used for listing tours.
        return tours.values();
    }

    @Override
    public Tour selectTour(UUID tourId) {
        // Used by the Controller to validate tour existence
        // and fetching the domain model object to update available spots.
        // Implemented here so code gets to the update failure.
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
        // The failure case, instantly returns false to
        // simulate database failure.
        return false;
    }
}
