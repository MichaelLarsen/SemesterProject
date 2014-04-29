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
     * Test of getBookingsFromDB method, of class BookingMapper. Tester at vi
     * har fået de bookings ud vi skal (size = 2)
     */
    @Test
    public void testGetBookingsFromDB() {
        System.out.println("getBookingsFromDB");
        ArrayList<Booking> result = bm.getBookingsFromDB(con);
        assertEquals(3, result.size());
    }

    /**
     * Test of getNewBookingId method, of class BookingMapper. Tester 2 værdier
     * og sørger for at databasen holder dem unikke
     */
    @Test
    public void testGetNewBookingId() {
        System.out.println("getNewBookingId");
        int result1 = bm.getNewBookingId(con);
        assertEquals(3000020, result1);
        int result2 = bm.getNewBookingId(con);
        assertEquals(3000021, result2);

    }

    /**
     * Test of addBooking method, of class BookingMapper. Tester tilføjning af
     * ny booking
     */
    @Test
    public void testAddBooking() {
        System.out.println("addBooking");
        Date checkIn = new Date(1451, 6, 22);
        Date checkOut = new Date(1451, 6, 29);
        Booking booking = new Booking(2000001, 1009, "Star Tours", checkIn, checkOut);
        bookingListTest.add(booking);
        boolean result = bm.addBooking(bookingListTest, con);
        assertEquals(true, result);
    }

    /**
     * Test of updateBooking method, of class BookingMapper.
     */
    @Test
    public void testUpdateBooking() {
        System.out.println("updateBooking");
        bookingListTest = bm.getBookingsFromDB(con);
        //husk at det er descending, så index 0 i arraylist er faktisk den sidste booking
        assertEquals(1008, bookingListTest.get(0).getRoomNo());
        bookingListTest.get(0).setRoomNo(1009);
        boolean result = bm.updateBooking(bookingListTest, con);
        assertEquals(true, result);
        //Test om roomNo er ændret
        assertEquals(1009, bookingListTest.get(0).getRoomNo());
    }

    /**
     * Test of getGuestBookingsFromDB method, of class BookingMapper.
     */
    @Test
    public void testGetGuestBookingsFromDB() {
        System.out.println("getGuestBookingsFromDB");
        //laver en ny gæst da vi ikke har en metode til at hente gæster ud fra bookingMapper
        Guest guest = new Guest(2000001, "Michael", "Larsen", "Søborg Torv 7", "" + 2860, "Søborg", "Denmark", "larsen_max@hotmail.com", 25462013, 0);
        ArrayList<Booking> result = bm.getGuestBookingsFromDB(guest, con);
        //ser hvor mange rum gæsten sover på (expected 2)
        assertEquals(2, result.size());
    }

    /**
     * Test of deleteBookingFromDB method, of class BookingMapper.
     */
    @Test
    public void testDeleteBookingFromDB() {
        System.out.println("deleteBookingFromDB");
        bookingListTest = bm.getBookingsFromDB(con);
        //checker at der er 3 bookings som forventet
        assertEquals(3, bookingListTest.size());

        ArrayList<Integer> bookingTestNumbers = new ArrayList<>();
        bookingTestNumbers.add(bookingListTest.get(0).getBookingId()); //sletter den første booking(descending dvs. med bookingID: 3000003)
        boolean result = bm.deleteBookingFromDB(bookingTestNumbers, con);
        assertEquals(true, result);

        bookingListTest = bm.getBookingsFromDB(con);
        //checker at den har slettet 1 booking via dens bookingID
        assertEquals(2, bookingListTest.size());

        //checker at det er de rigtige 2 som ikke er slettet. Har slettet booking med ID: 3000003
        assertEquals(3000002, bookingListTest.get(0).getBookingId());
        assertEquals(3000001, bookingListTest.get(1).getBookingId());

    }

    /**
     * Test of log method, of class BookingMapper.
     */
    @Test
    public void testGetLog() {
        System.out.println("getLog");
        //tester ved at tilføje en booking idet der bliver oprettet en log ved create, update eller delete
        Date checkIn = new Date(1451, 6, 22);
        Date checkOut = new Date(1451, 6, 29);
        Booking booking = new Booking(2000002, 1009, "Star Tours", checkIn, checkOut);
        bookingListTest.add(booking);
        bm.addBooking(bookingListTest, con);
        ArrayList<String> stringTestList;
        stringTestList = bm.getLog(con);
        //da vi lige har oprettet en booking i databasen, burde vi også have lavet en log.
        //derfor må stringTestList være size 1
        assertEquals(2, stringTestList.size());
    }

    @Test
    public void testLog() {
        ArrayList<String> stringTestList;
        stringTestList = bm.getLog(con);
        //checker om der er 1 log da vi har tilfæjet en i set up
        assertEquals(1, stringTestList.size());
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
