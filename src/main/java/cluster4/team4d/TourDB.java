package cluster4.team4d;

import java.util.Collection;
import java.util.UUID;

public interface TourDB {
    Collection<Tour> selectTours();
    Collection<Tour> searchTours(String keyword);
    Tour selectTour(UUID tourId);
    boolean insertTour(Tour tour);
    boolean updateTour(Tour tour);
}
