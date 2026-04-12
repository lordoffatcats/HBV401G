package cluster4.team4d;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class TourDatabase implements TourDB {
    Database db;
    public TourDatabase(String databaseUrl) {
        this.db = new Database(databaseUrl);
    }

    @Override
    public Collection<Tour> selectTours() {
        String sql = """
            SELECT tourId, title, description, pricePerPerson, location, pickupOffered, availableSpots, dateTime
            FROM tours
        """;

        List<Tour> tours = new ArrayList<>();

        try (Connection conn = this.db.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tour tour = new Tour(
                        UUID.fromString(rs.getString("tourId")),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("pricePerPerson"),
                        rs.getString("location"),
                        rs.getBoolean("pickupOffered"),
                        rs.getInt("availableSpots"),
                        LocalDateTime.parse(rs.getString("dateTime"))
                );
                tours.add(tour);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tours;
    }

    @Override
    public Collection<Tour> searchTours(String keyword) {
        String sql = """
            SELECT tourId, title, description, pricePerPerson, location, pickupOffered, availableSpots, dateTime
            FROM tours
            WHERE title LIKE ?
        """;

        List<Tour> tours = new ArrayList<>();

        try (Connection conn = this.db.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Tour tour = new Tour(
                        UUID.fromString(rs.getString("tourId")),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("pricePerPerson"),
                        rs.getString("location"),
                        rs.getBoolean("pickupOffered"),
                        rs.getInt("availableSpots"),
                        LocalDateTime.parse(rs.getString("dateTime"))
                );
                tours.add(tour);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tours;
    }

    @Override
    public Tour selectTour(UUID tourId) {
        try (Connection conn = this.db.connect()) {
            String sql = """
                SELECT tourId, title, description, pricePerPerson, location, pickupOffered, availableSpots, dateTime
                FROM tours
                WHERE tourId = ?
            """;
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, tourId.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Tour(
                    UUID.fromString(rs.getString("tourId")),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("pricePerPerson"),
                    rs.getString("location"),
                    rs.getBoolean("pickupOffered"),
                    rs.getInt("availableSpots"),
                    LocalDateTime.parse(rs.getString("dateTime"))
                );
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertTour(Tour tour) {
        try (Connection conn = this.db.connect()) {
            String sql = """
                INSERT INTO tours (tourId, title, description, pricePerPerson, location, pickupOffered, availableSpots, dateTime)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, tour.getTourId().toString());
            ps.setString(2, tour.getTitle());
            ps.setString(3, tour.getDescription());
            ps.setInt(4, tour.getPricePerPerson());
            ps.setString(5, tour.getLocation());
            ps.setInt(6, tour.getPickupOffered()? 1 : 0);
            ps.setInt(7, tour.getAvailableSpots());
            ps.setString(8, tour.getDateTime().toString());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTour(Tour tour) {
        String sql = """
            UPDATE tours
            SET title = ?,
                description = ?,
                pricePerPerson = ?,
                location = ?,
                pickupOffered = ?,
                availableSpots = ?,
                dateTime = ?
            WHERE tourId = ?
        """;

        //TODO: mögulega update-a öll booking með sama tourId.

        try (Connection conn = this.db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tour.getTitle());
            ps.setString(2, tour.getDescription());
            ps.setInt(3, tour.getPricePerPerson());
            ps.setString(4, tour.getLocation());
            ps.setInt(5, tour.getPickupOffered() ? 1 : 0);
            ps.setInt(6, tour.getAvailableSpots());
            ps.setString(7, tour.getDateTime().toString());
            ps.setString(8, tour.getTourId().toString());

            int rowsUpdated = ps.executeUpdate();

            return rowsUpdated == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
