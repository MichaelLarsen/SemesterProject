/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dataSource;

import domain.Booking;
import domain.Guest;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael
 */
public class BookingMapperTest {
    Connection con;
    private final String id = "SEM2_TEST_GR04";
    private final String pw = "SEM2_TEST_GR04";
    BookingMapper bm;
    private ArrayList<Booking> bookingList;
    
    @Before
    public void setUp() {
        getConnection();
        Fixture.setUp(con);
        bm = new BookingMapper();
        bookingList = new ArrayList<>();
    }
    
    @After
    public void tearDown() {
        releaseConnection();
    }

    /**
     * Test of getBookingsFromDB method, of class BookingMapper.
     */
    @Test
    public void testGetBookingsFromDB() {
        System.out.println("getBookingsFromDB");
        ArrayList<Booking> result = bm.getBookingsFromDB(con);
        assertEquals(result.size(), 1);
    }

    /**
     * Test of getNewBookingId method, of class BookingMapper.
     */
    @Test
    public void testGetNewBookingId() {
        System.out.println("getNewBookingId");
        int result = bm.getNewBookingId(con);
        int newId = 3000020;
        assertEquals(result, newId);
    }

    /**
     * Test of addBooking method, of class BookingMapper.
     */
    @Test
    public void testAddBooking() {
        System.out.println("addBooking");
        Date checkIn = new Date(1451,6,22);
        Date checkOut = new Date(1451,6,29);
        Booking booking = new Booking(2000001, 1009, "Star Tours", checkIn, checkOut);
        bookingList.add(booking);
        boolean result = bm.addBooking(bookingList, con);
        assertTrue(result==true);
    }

    /**
     * Test of getGuestsInBooking method, of class BookingMapper.
     */
    @Test
    public void testGetGuestsInBooking() {
        System.out.println("getGuestsInBooking");
        Booking booking = null;
        ArrayList<Booking> bookings = bm.getBookingsFromDB(con);
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId() == 3000001) {
                booking = bookings.get(i);
            }
        }
        ArrayList<Guest> result = bm.getGuestsInBooking(booking, con);
        assertTrue(result.size()==1);
    }

    /**
     * Test of updateBooking method, of class BookingMapper.
     */
    @Test
    public void testUpdateBooking() {
        System.out.println("updateBooking");
        ArrayList<Booking> updateBookingList = null;
        Connection con = null;
        BookingMapper instance = new BookingMapper();
        boolean expResult = false;
        boolean result = instance.updateBooking(updateBookingList, con);
        assertEquals(expResult, result);
    }

    /**
     * Test of getGuestBookingsFromDB method, of class BookingMapper.
     */
    @Test
    public void testGetGuestBookingsFromDB() {
        System.out.println("getGuestBookingsFromDB");
        Guest guest = null;
        Connection con = null;
        BookingMapper instance = new BookingMapper();
        ArrayList<Booking> expResult = null;
        ArrayList<Booking> result = instance.getGuestBookingsFromDB(guest, con);
        assertEquals(expResult, result);
    }

    /**
     * Test of deleteBookingFromDB method, of class BookingMapper.
     */
    @Test
    public void testDeleteBookingFromDB() {
        System.out.println("deleteBookingFromDB");
        ArrayList<Integer> deleteBookingsList = null;
        Connection con = null;
        BookingMapper instance = new BookingMapper();
        boolean expResult = false;
        boolean result = instance.deleteBookingFromDB(deleteBookingsList, con);
        assertEquals(expResult, result);
    }

    /**
     * Test of log method, of class BookingMapper.
     */
    @Test
    public void testLog() {
        System.out.println("log");
        int booking_id = 0;
        BookingMapper.ActionType action = null;
        Connection con = null;
        BookingMapper.log(booking_id, action, con);
    }

    /**
     * Test of getLog method, of class BookingMapper.
     */
    @Test
    public void testGetLog() {
        System.out.println("getLog");
        Connection con = null;
        BookingMapper instance = new BookingMapper();
        ArrayList<String> expResult = null;
        ArrayList<String> result = instance.getLog(con);
        assertEquals(expResult, result);
    }
    
    private void getConnection() {
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat", id, pw);
        }
        catch (SQLException e) {
            System.out.println("fail in getConnection() - Did you add your Username and Password");
            System.out.println(e);
        }
    }

    public void releaseConnection() {
        try {
            con.close();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
    
}
