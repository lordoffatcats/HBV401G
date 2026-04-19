package cluster4.team4d;

import java.util.Collection;
import java.util.UUID;

/**
 * TourController exposes methods for external parties
 * to view, search and find Tours.
 */
public class TourController {
    private final TourDB tourDB;

    public TourController(TourDB tourDB) {
        this.tourDB = tourDB;
    }

    /**
     * List all tours.
     * @return A list containing all tours.
     */
    public Collection<Tour> listTours() {
        return tourDB.selectTours();
    }

    /**
     * Get a tour by its UUID.
     * @param tourId The UUID of the tour to fetch.
     * @return The Tour model if successful, null otherwise.
     */
    public Tour getTourById(UUID tourId) {
        return tourDB.selectTour(tourId);
    }

    /**
     * Search Tours by their titles.
     * @param keyword Keyword to search by.
     * @return A list of Tours that match the provided keyword.
     */
    public Collection<Tour> findTours(String keyword) {
        return tourDB.searchTours(keyword);
    }
}
