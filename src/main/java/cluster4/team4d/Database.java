package cluster4.team4d;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
 Utility klasi fyrir tengingar við gagnagrunn með JDBC.
 */
public class Database {
    String databaseUrl;

    public Database(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    /**
     * Býr til nýja tengingu við gagnagrunninn.
     * @return JDBC Connection við gagnagrunninn.
     * @throws SQLException
     */
    Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(this.databaseUrl);
        conn.createStatement().execute("PRAGMA foreign_keys = ON");
        return conn;
    }

    /**
     * Býr tíl nýtt sqlite3 database
     * @param filename path á grunninum.
     */
    public static void createdb(String filename) {
        Database db = new Database("jdbc:sqlite:" + filename);

        try (Connection conn = db.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE tours (
                    tourId TEXT PRIMARY KEY, -- text for UUID
                    title TEXT NOT NULL,
                    description TEXT NOT NULL,
                    pricePerPerson INTEGER,
                    location TEXT,
                    pickupOffered INTEGER CHECK (pickupOffered IN (0,1)),
                    availableSpots INTEGER,
                    dateTime TEXT -- ISO8601 string ("YYYY-MM-DD HH:MM:SS.SSS")
                );
            """);

            stmt.execute("""
                CREATE TABLE bookings (
                    bookingId TEXT PRIMARY KEY, -- text for UUID
                    tourId TEXT NOT NULL,
                    customerEmail TEXT NOT NULL,
                    status TEXT CHECK (status IN ('Pending', 'Confirmed', 'Cancelled')),
                    groupSize INTEGER  NOT NULL,
                    totalPrice INTEGER  NOT NULL,
                    pickupSelected INTEGER CHECK (pickupSelected IN (0,1)), -- pseudo bool dálkur því sqlite er ekki með bool
                    hotelName TEXT,
                    FOREIGN KEY (tourId) REFERENCES tours(tourId) ON DELETE CASCADE,
                    CHECK (pickupSelected = 0 OR hotelName IS NOT NULL) -- ef pickup er valið, þá nafn á hóteli.
                );
            """);
            System.out.println("Database tables created.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Eyðir sqlite gagnagrunn
     * @param filename path á grunninum.
     */
    public static void deletedb(String filename) {
        File dbFile = new File(filename);
        if (dbFile.delete()) {
            System.out.println("Database deleted successfully.");
        } else {
            System.out.println("Failed to delete the database.");
        }
    }
}
