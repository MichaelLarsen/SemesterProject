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
    // Singleton for at sikre at der kun er en forbindelse samt at give global adgang til domænet.
    private static DBFacade instance;
    private UnitOfWork unitOfWork;

    private DBFacade() {
        unitOfWork = new UnitOfWork();
        customerMapper = new CustomerMapper();
        roomMapper = new RoomMapper();
        bookingMapper = new BookingMapper();
        con = new DBConnector().getConnection(); // Forbindelsen frigivet når programmet bliver lukket af garbage collector
    }

    public static DBFacade getInstance() {
        if (instance == null) {
            instance = new DBFacade();
        }
        return instance;
    }

    //Gemmer en transaction i database
    public boolean commitTransaction() throws SQLException {
        boolean commitSucces;
        commitSucces = unitOfWork.commitTransaction(con);
        con.close();
        return commitSucces;
    }
    // Singleton slutning

    //returns arraylist of rooms
    public ArrayList<Room> getRoomFromDB() {
        return roomMapper.getRoomsFromDB(con);
    }

    //returns arraylist of customers
    public ArrayList<Customer> getCustomersFromDB() {
        return customerMapper.getCustomersFromDB(con);
    }

    //returns arraylist of bookings
    public ArrayList<Booking> getBookingsFromDB() {
        return bookingMapper.getBookingsFromDB(con);
    }

//    public boolean updateCustomerDB(Customer customer) {
//        return customerMapper.updateCustomerDB(customer, con);
//    }
//
    public boolean updateRoomDB(Room room) {
        return unitOfWork.updateRoomDB(room);
    }

    public boolean updateBookingDB(Booking booking) {
        return unitOfWork.updateBookingDB(booking);
    }
    
    public boolean updateGuestsInRoomDB(RoomGuest roomGuest) {
        return unitOfWork.updateGuestsInRoomDB(roomGuest);
    }

    public boolean bookRoom(Booking booking) {
        return unitOfWork.bookRoom(booking);
    }

    public int getNewBookingId() {
        return bookingMapper.getNewBookingId(con);
    }

    public ArrayList<Customer> getGuestsInRoom(Booking booking) {
        return bookingMapper.getGuestsInRoom(booking, con);
    }

    public boolean addGuestToRoom(RoomGuest roomguest) {
        return unitOfWork.addGuestToRoom(roomguest);
    }
}
