package DbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Połaczenie z baza danych */
public class ConnectionManager {
    /**
     * Deklaracja zmiennych
     */
    private static String dbName        = "CookBook";
    private static String url           = "jdbc:mysql://localhost/";
    private static String driverName    = "com.mysql.jdbc.Driver";
    private static String username      = "root";
    private static String password      = "";
    private static Connection con;

    /**
     * Funkcje
     */
    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url + dbName, username, password);
            } catch (SQLException ex) {
                System.out.println("Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found.");
        }
        return con;
    }
}
