package cluster4.team4d;

import java.util.Collection;
import java.util.UUID;

public class TourController {
    private final TourDB tourDB;

    public TourController(TourDB tourDB) {
        this.tourDB = tourDB;
    }

    public Collection<Tour> listTours() {
        return tourDB.selectTours();
    }

    public Tour getTourById(UUID tourId) {
        return tourDB.selectTour(tourId);
    }

    public Collection<Tour> findTours(String keyword) {
        // TODO: Implement
        return null;
    }
}
