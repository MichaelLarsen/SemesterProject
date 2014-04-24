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
    private ArrayList<Booking> bookingListTest;
    
    @Before
    public void setUp() {
        getConnection();
        Fixture.setUp(con);
        bm = new BookingMapper();
        bookingListTest = new ArrayList<>();
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
        //test af 2 nye unikke id'er.
        System.out.println("getNewBookingId");
        int result1 = bm.getNewBookingId(con);
        int newId = 3000020;
        assertEquals(result1, newId);
        int result2 = bm.getNewBookingId(con);
        newId = 3000021;
        assertEquals(result2, newId);
        
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
        bookingListTest.add(booking);
        boolean result = bm.addBooking(bookingListTest, con);
        assertEquals(true, result);
    }

    /**
     * Test of getGuestsInBooking method, of class BookingMapper.
     */
    @Test
    public void testGetGuestsInBooking() {
        System.out.println("getGuestsInBooking");
        Booking booking = null;
        bookingListTest = bm.getBookingsFromDB(con);
        for (int i = 0; i < bookingListTest.size(); i++) {
            if (bookingListTest.get(i).getBookingId() == 3000001) {
                booking = bookingListTest.get(i);
            }
        }
        ArrayList<Guest> result = bm.getGuestsInBooking(booking, con);
        assertEquals(result.size(),1);
    }

    /**
     * Test of updateBooking method, of class BookingMapper.
     */
    @Test
    public void testUpdateBooking() {
        System.out.println("updateBooking");
        bookingListTest = bm.getBookingsFromDB(con);
        bookingListTest.get(0).setRoomNo(1008);
        boolean result = bm.updateBooking(bookingListTest, con);
        assertEquals(result, true);
    }

    /**
     * Test of getGuestBookingsFromDB method, of class BookingMapper.
     */
    @Test
    public void testGetGuestBookingsFromDB() {
        System.out.println("getGuestBookingsFromDB");
        //laver en ny gæst da vi ikke har en metode til at hente gæster ud fra bookingMapper
        Guest guest = new Guest(2000001, "Michael", "Larsen", "Søborg Torv 7",""+2860, "Søborg", "Denmark", "larsen_max@hotmail.com", 25462013,0);
        ArrayList<Booking> result = bm.getGuestBookingsFromDB(guest, con);
        assertEquals(result.size(), 1);
    }

    /**
     * Test of deleteBookingFromDB method, of class BookingMapper.
     */
    @Test
    public void testDeleteBookingFromDB() {
        System.out.println("deleteBookingFromDB");
        bookingListTest = bm.getBookingsFromDB(con);
        ArrayList<Integer> bookingTestNumbers = new ArrayList<>();
        for (int i = 0; i < bookingListTest.size(); i++) {
            bookingTestNumbers.add(bookingListTest.get(i).getBookingId());
        }
        boolean result = bm.deleteBookingFromDB(bookingTestNumbers, con);
        assertEquals(result, true);
    }

    /**
     * Test of log method, of class BookingMapper.
     */
    @Test
    public void testGetLog() {
        System.out.println("log");
        //tester ved at tilføje en booking
        Date checkIn = new Date(1451,6,22);
        Date checkOut = new Date(1451,6,29);
        System.out.println(checkIn +" til "+ checkOut);
        Booking booking = new Booking(2000002, 1009, "Star Tours", checkIn, checkOut);
        bookingListTest.add(booking);
        bm.addBooking(bookingListTest, con);
        ArrayList<String> stringTestList;
        stringTestList = bm.getLog(con);
        assertEquals(stringTestList.size(),1);
        
        
    }

    /**
     * Test of getLog method, of class BookingMapper.
     */
    
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
