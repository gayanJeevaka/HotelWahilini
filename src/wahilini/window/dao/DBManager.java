package wahilini.window.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import wahilini.window.ui.CommonFeature;

public class DBManager {

    public static Connection JDBC_CONNECTION;

    static {

        try {
            String location = "jdbc:mysql://localhost/hotel_wahilini";
            String username = "root";
            String password = "root";
            JDBC_CONNECTION = DriverManager.getConnection(location, username, password);
        } catch (SQLException ex) {
            CommonFeature.printException("Exception from DBManager.static block ");
            System.out.println("Exception from DBManager.static block\n" + ex.getMessage());
        }
    }

    public static void getDBConnection() {
        String location = "jdbc:mysql://localhost/hotel_wahilini";
        String username = "root";
        String password = "root";
        try {
            if (JDBC_CONNECTION.isClosed()) {
                JDBC_CONNECTION = DriverManager.getConnection(location, username, password);
                System.out.println("Connection created twice");
            }

        } catch (SQLException ex) {
            CommonFeature.printException("Exception from DBManager.static block ");
            System.out.println("Exception from DBManager.static block\n" + ex.getMessage());
        }
    }

}
