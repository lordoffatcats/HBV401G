package cluster4.team4d;

import java.util.Collection;
import java.util.UUID;

public interface TourDB {
    /**
     * Get all Tours from the database.
     * @return A collection of all tours in the database.
     */
    Collection<Tour> selectTours();

    /**
     * Get all Tours from database that match a specified keyword.
     * @param keyword Keyword to search by.
     * @return A collection of tours that match the specified keyword.
     */
    Collection<Tour> searchTours(String keyword);

    /**
     * Get a tour by its UUID.
     * @param tourId The UUID of the tour to fetch.
     * @return Domain model of the Tour if successful, null otherwise.
     */
    Tour selectTour(UUID tourId);

    /**
     * Insert a Tour using its domain model.
     * @param tour The Tour to insert.
     * @return A boolean value indicating success or failure.
     */
    boolean insertTour(Tour tour);

    /**
     * Update a given tour by its domain model.
     * @param tour The tour to update.
     * @return A boolean value indicating success or failure.
     */
    boolean updateTour(Tour tour);
}
