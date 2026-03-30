package cluster4.team4d;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public boolean getPickupOffered() {
        return this.pickupOffered;
    }

    public int getAvailableSpots() {
        return this.availableSpots;
    }

    public int calculatePrice(int groupSize) {
        return groupSize*this.pricePerPerson;
    }

    public boolean hasCapacityFor(int groupSize) {
        return groupSize <= this.availableSpots;
    }

    public void reserveSpots(int groupSize) {
        if (hasCapacityFor(groupSize)) {
            this.availableSpots -= groupSize;
        }
    }

    public void freeSpots(int groupSize) {
        this.availableSpots += groupSize;
    }
}
