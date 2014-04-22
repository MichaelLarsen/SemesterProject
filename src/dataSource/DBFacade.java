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

    private CustomerMapper customerMapper;
    private RoomMapper roomMapper;
    private BookingMapper bookingMapper;
    private Connection con;
    private UnitOfWork unitOfWork = new UnitOfWork();

    // Singleton for at sikre at der kun er en forbindelse samt at give global adgang til domænet.
    private static DBFacade instance;

    private DBFacade() {
        customerMapper = new CustomerMapper();
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
    
    public Customer getCustomerDB(int bookingOwnerId) {
        Customer customer;
        con = null;
        try {
            con = openConnection();
            customer = customerMapper.getCustomerDB(bookingOwnerId, con);
        }
        finally {
            closeConnection(con);
        }
        return customer;
    }

    public Customer getGuestFromID(int customerId) {
        Customer customer;
        con = null;
        try {
            con = openConnection();
            customer = customerMapper.getGuestFromID(customerId, con);
        }
        finally {
            closeConnection(con);
        }
        return customer;
    }
    
    //returns arraylist of customers
    public ArrayList<Customer> getCustomersFromDB() {
        con = null;
        ArrayList<Customer> tempCustomerList;
        try {
            con = openConnection();
            tempCustomerList = customerMapper.getCustomersFromDB(con);
        }
        finally {
            closeConnection(con);
        }
        return tempCustomerList;
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

    public ArrayList<Booking> getCustomerBookingsFromDB(Customer customer) {
        con = null;
        ArrayList<Booking> tempBookingList;
        try {
            con = openConnection();
            tempBookingList = bookingMapper.getCustomerBookingsFromDB(customer, con);
        }
        finally {
            closeConnection(con);
        }
        return tempBookingList;
    }

    public ArrayList<Customer> getGuestsInRoomFromDB(Booking booking) {
        con = null;
        ArrayList<Customer> roomGuestList;
        try {
            con = openConnection();
            roomGuestList = bookingMapper.getGuestsInBooking(booking, con);
        }
        finally {
            closeConnection(con);
        }
        return roomGuestList;
    }
    
     public ArrayList<Customer> searchForGuestDB(String status, String... names) {
        con = null;
        ArrayList<Customer> guestsFound;
        try {
            con = openConnection();
            guestsFound = customerMapper.searchForGuestDB(status, con, names);
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

    public boolean updateGuestsInRoomDB(RoomGuest roomGuest) {
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

    public boolean addGuestToRoom(RoomGuest roomguest) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.addGuestToRoom(roomguest);
    }

    public boolean createCustomer(Customer customer) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.createCustomer(customer);
    }

    public boolean updateCustomerDB(Customer customer) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.updateCustomerDB(customer);
    }

    public boolean deleteBookingFromDB(int bookingId) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.deleteBookingFromDB(bookingId);
    }
}
