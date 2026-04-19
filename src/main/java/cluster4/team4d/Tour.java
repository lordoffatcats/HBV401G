package cluster4.team4d;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The tour model class that contains and keeps track of
 * domain information regarding tours.
 */
public class Tour {
    private final UUID tourId;
    private final String title;
    private final String description;
    private final int pricePerPerson;
    private final String location;
    private final boolean pickupOffered;
    private int availableSpots;
    private final LocalDateTime dateTime;

    public Tour(UUID tourId, String title, String description, int pricePerPerson, String location, boolean pickupOffered, int availableSpots, LocalDateTime dateTime) {
        this.tourId = tourId;
        this.title = title;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
        this.location = location;
        this.pickupOffered = pickupOffered;
        this.availableSpots = availableSpots;
        this.dateTime = dateTime;
    }

    public UUID getTourId() {
        return this.tourId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPricePerPerson() {
        return this.pricePerPerson;
    }

    public String getLocation() {
        return this.location;
    }

    public boolean getPickupOffered() {
        return this.pickupOffered;
    }

    public int getAvailableSpots() {
        return this.availableSpots;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    /**
     * Computes the total price for a provided group size.
     * @param groupSize The group size.
     * @return The total price for the group size.
     */
    public int calculatePrice(int groupSize) {
        return groupSize*this.pricePerPerson;
    }

    /**
     * Checks if the tour has capacity for a given group size.
     * @param groupSize The group size.
     * @return A boolean indicating if spots are available, or not.
     */
    public boolean hasCapacityFor(int groupSize) {
        return groupSize <= this.availableSpots;
    }

    /**
     * Reserves spots on the tour if there is capacity.
     * @param groupSize The number of spots to reserve.
     */
    public void reserveSpots(int groupSize) {
        if (hasCapacityFor(groupSize)) {
            this.availableSpots -= groupSize;
        }
    }

    /**
     * Releases the specified number of reserved seats.
     * @param groupSize The number of seats to release.
     */
    public void freeSpots(int groupSize) {
        this.availableSpots += groupSize;
    }

}
