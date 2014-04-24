/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dataSource.BookingMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael
 */
public class BookingMapperTest {

    Connection con;
    private final String id = "";
    private final String pw = "";
    BookingMapper bm;

    public BookingMapperTest() {
        getConnection();
		Fixture.setUp(con);
		bm = new BookingMapper();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    private void getConnection()
	  {
	    try 
	    { 
	      con = DriverManager.getConnection("jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat", id, pw );  
	    }
	    catch (SQLException e) 
	    {   System.out.println("fail in getConnection() - Did you add your Username and Password");
	        System.out.println(e); }    
	  }
	  public void releaseConnection()
	  {
	      try{
	          con.close();
	      }
	      catch (Exception e)
	      { System.err.println(e);}
	  }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
