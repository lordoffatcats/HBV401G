package cluster4.team4d;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BookingDatabase implements BookingDB{
    Database db;
    public BookingDatabase(String databaseUrl) {
        this.db = new Database(databaseUrl);
    }

    @Override
    public boolean insertBooking(Booking booking) {
        try (Connection conn = this.db.connect()) {
            String sql = """
                INSERT INTO bookings (bookingId, tourId, email, status, groupSize, totalPrice, pickupSelected, hotelName)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, booking.getBookingId().toString());
            ps.setString(2, booking.getTourId().toString());
            ps.setString(3, booking.getEmail());
            ps.setString(4, booking.getStatus());
            ps.setInt(5, booking.getGroupSize());
            ps.setInt(6, booking.getTotalPrice());
            ps.setInt(7, booking.getPickupSelected()? 1 : 0);
            ps.setString(8, booking.getHotelName());

            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Booking selectBooking(UUID bookingId) {
        try (Connection conn = this.db.connect()) {
            String sql = """
                SELECT bookingId, tourId, email, status, groupSize, totalPrice, pickupSelected, hotelName
                FROM bookings
                WHERE bookingId = ?;
            """;
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, bookingId.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Booking(
                        UUID.fromString(rs.getString("bookingId")),
                        UUID.fromString(rs.getString("tourId")),
                        rs.getString("email"),
                        rs.getString("status"),
                        rs.getInt("groupSize"),
                        rs.getInt("totalPrice"),
                        rs.getBoolean("pickupSelected"),
                        rs.getString("hotelName")
                );
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateBooking(Booking booking) {
        String sql = """
            UPDATE bookings
            SET tourId = ?,
                email = ?,
                status = ?,
                groupSize = ?,
                totalPrice = ?,
                pickupSelected = ?,
                hotelName = ?
            WHERE bookingId = ?;
        """;

        try (Connection conn = this.db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, booking.getTourId().toString());
            ps.setString(2, booking.getEmail());
            ps.setString(3, booking.getStatus());
            ps.setInt(4, booking.getGroupSize());
            ps.setInt(5, booking.getTotalPrice());
            ps.setInt(6, booking.getPickupSelected() ? 1 : 0);
            ps.setString(7, booking.getHotelName());
            ps.setString(8, booking.getBookingId().toString());

            int rowsUpdated = ps.executeUpdate();

            return rowsUpdated == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
