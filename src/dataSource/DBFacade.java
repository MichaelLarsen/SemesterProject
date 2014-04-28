/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class DBFacade {

    private GuestMapper guestMapper;
    private RoomMapper roomMapper;
    private BookingMapper bookingMapper;
    private Connection con;
    private UnitOfWork unitOfWork = new UnitOfWork();

    // Singleton for at sikre at der kun er en forbindelse samt at give global adgang til domænet.
    private static DBFacade instance;

    private DBFacade() {
        guestMapper = new GuestMapper();
        roomMapper = new RoomMapper();
        bookingMapper = new BookingMapper();
        con = null;
    }

    public static DBFacade getInstance() {
        if (instance == null) {
            instance = new DBFacade();
        }
        return instance;
    }

    private Connection openConnection() {
        if (con == null) {
            try {
                con = new DBConnector().getConnection();
            }
            catch (Exception e) {
                System.out.println("Fail in establishing connection - DBFacade");
                System.out.println(e.getMessage());
            }
        }
        return con;
    }

    private void closeConnection(Connection con) {
        try {
            con.close();
        }
        catch (SQLException e) {
            System.out.println("Fail in closing connection - DBFacade");
            System.out.println(e.getMessage());
        }
    }

    //returns arraylist of rooms
    public ArrayList<Room> getRoomFromDB() {
        con = null;
        ArrayList<Room> tempRoomList;
        try {
            con = openConnection();
            tempRoomList = roomMapper.getRoomsFromDB(con);
        }
        finally {
            closeConnection(con);
        }
        return tempRoomList;
    }

    public Guest getGuestDB(int bookingOwnerId) {
        Guest guest;
        con = null;
        try {
            con = openConnection();
            guest = guestMapper.getGuestDB(bookingOwnerId, con);
        }
        finally {
            closeConnection(con);
        }
        return guest;
    }

    public Guest getGuestFromID(int guestId) {
        Guest guest;
        con = null;
        try {
            con = openConnection();
            guest = guestMapper.getGuestFromID(guestId, con);
        }
        finally {
            closeConnection(con);
        }
        return guest;
    }

    //returns arraylist of guests
    public ArrayList<Guest> getGuestsFromDB() {
        con = null;
        ArrayList<Guest> tempGuestList;
        try {
            con = openConnection();
            tempGuestList = guestMapper.getGuestsFromDB(con);
        }
        finally {
            closeConnection(con);
        }
        return tempGuestList;
    }

    //returns arraylist of bookings
    public ArrayList<Booking> getBookingsFromDB() {
        con = null;
        ArrayList<Booking> tempBookingList;
        try {
            con = openConnection();
            tempBookingList = bookingMapper.getBookingsFromDB(con);
        }
        finally {
            closeConnection(con);
        }
        return tempBookingList;
    }

    public ArrayList<String> getBookingLog() {
        con = null;
        ArrayList<String> tempBookingLogList;
        try {
            con = openConnection();
            tempBookingLogList = bookingMapper.getLog(con);
        }
        finally {
            closeConnection(con);
        }
        return tempBookingLogList;
    }

    public ArrayList<String> getGuestLog() {
        con = null;
        ArrayList<String> tempGuestLogList;
        try {
            con = openConnection();
            tempGuestLogList = guestMapper.getLog(con);
        }
        finally {
            closeConnection(con);
        }
        return tempGuestLogList;
    }

    public ArrayList<Booking> getGuestBookingsFromDB(Guest guest) {
        con = null;
        ArrayList<Booking> tempBookingList;
        try {
            con = openConnection();
            tempBookingList = bookingMapper.getGuestBookingsFromDB(guest, con);
        }
        finally {
            closeConnection(con);
        }
        return tempBookingList;
    }

    public ArrayList<Guest> getGuestsInRoomFromDB(Booking booking) {
        con = null;
        ArrayList<Guest> roomGuestList;
        try {
            con = openConnection();
            roomGuestList = bookingMapper.getGuestsInBooking(booking, con);
        }
        finally {
            closeConnection(con);
        }
        return roomGuestList;
    }

    public ArrayList<Guest> searchForGuestDB(String status, String... names) {
        con = null;
        ArrayList<Guest> guestsFound;
        try {
            con = openConnection();
            guestsFound = guestMapper.searchForGuestDB(status, con, names);
        }
        finally {
            closeConnection(con);
        }
        return guestsFound;
    }

    public void openNewTransaction() {
        unitOfWork = new UnitOfWork();
    }

    //Gemmer en transaction i database
    public boolean commitTransaction() throws SQLException {
        boolean commitSuccess = false;
        if (unitOfWork != null) {
            con = null;
            try {
                con = openConnection();
                commitSuccess = unitOfWork.commitTransaction(con);
            }
            finally {
                closeConnection(con);
            }
            unitOfWork = null;
        }
        return commitSuccess;
    }

    public boolean updateRoomDB(Room room) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.updateRoomDB(room);
    }

    public boolean updateBookingDB(Booking booking) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.updateBookingDB(booking);
    }

    /**
     * Benyttes ikke TODO slet?
     */
    public boolean updateGuestsInRoomDB(BookingDetail roomGuest) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.updateGuestsInRoomDB(roomGuest);
    }

    public boolean bookRoom(Booking booking) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.bookRoom(booking);
    }

    public boolean addGuestToRoom(BookingDetail roomguest) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.addGuestToRoom(roomguest);
    }

    public boolean createGuest(Guest guest) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.createGuest(guest);
    }

    public boolean updateGuestDB(Guest guest) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.updateGuestDB(guest);
    }

    public boolean deleteBookingFromDB(int bookingId) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.deleteBookingFromDB(bookingId);
    }

    public boolean undoNewBooking(Booking booking) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.undoNewBooking(booking);
    }

    public ArrayList<Booking> getBookingsFromUOF() {
        return unitOfWork.getBookings();
    }

    public void removeBookingFromUOF(Booking booking) {
        unitOfWork.removeBookingFromUOF(booking);
    }

    public void clearNewBookingDetails() {
        unitOfWork.clearNewBookingDetails();
    }
}
