/*
 * Semester Projekt, Datamatiker 2. semester
 * Gruppe 4: Andreas, Michael og Sebastian
 */
package dataSource;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DBConnector klassen håndterer forbindelsen til databasen.
 *
 * @author Andreas, Michael & Sebastian
 */
public class DBConnector {

    private static final String ID = "SEM2_GR04";                                           // Login:       SEM2_GR04 / SEM2_TEST_GR04
    private static final String PW = "SEM2_GR04";                                           // Password:    SEM2_GR04 / SEM2_TEST_GR04
    private static final String URL = "jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat";    // URL for the database
    
    /**
     * Loader JDBC driver og instansierer vores Connection-objekt.
     * 
     * @return      Forbindelse til databasen i form af Connection-objekt.
     */
    public Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");     // Her initialiseres JDBC driveren.
            con = DriverManager.getConnection(URL, ID, PW);       // Her oprettes forbindelsen til databasen, ved at kalde den statiske metode DriverManager.getConnection(String url, String id, String pw)
        }
        catch (Exception e) {
            System.out.println("Did not load driver or username/password correctly. Try restarting the application!");
            System.out.println("Fail in DBConnector.getConnection()");
            System.out.println(e);
        }
        return con;
    }

    /**
     * Lukker forbindelse til databasen.
     *
     * @param con   Forbindelse til databasen.
     */
    public void releaseConnection(Connection con) {
        try {
            con.close();
        }
        catch (Exception e) {
            System.out.println("Error closing connection.");
            System.out.println("Fail in DBConnector.releaseConnection()");
            System.err.println(e.getMessage());
        }
    }
}
