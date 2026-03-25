import cluster4.team4d.Tour;
import cluster4.team4d.TourDB;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class TourDBMock implements TourDB {
    private final HashMap<UUID, Tour> tours = new HashMap<>();

    @Override
    public Collection<Tour> selectTours() {
        return tours.values();
    }

    @Override
    public Tour selectTour(UUID tourId) {
        return tours.get(tourId);
    }

    @Override
    public boolean insertTour(Tour tour) {
        tours.put(tour.getTourId(), tour);
        return true;
    }

    @Override
    public boolean updateTour(Tour tour) {
        tours.put(tour.getTourId(), tour);
        return true;
    }
}
