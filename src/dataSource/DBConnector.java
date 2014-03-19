/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author ANDREAS!!! og lidt sebastian og michael
 */
public class DBConnector
{

    private static String id = "SEM2_TEST_GR04";
    private static String pw = "SEM2_TEST_GR04";

    public Connection getConnection()
    {
        Connection con = null;
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@delfi.lyngbyes.dk:1521:KNORD", id, pw);

        }
        catch (Exception e)
        {
            System.out.println("Did not load driver or username/password correctly!");
            System.out.println("error in DBConnector.getConnection()");
            System.out.println(e);
        }

        return con;
    }

    public void releaseConnection(Connection con)
    {
        try
        {
            con.close();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
