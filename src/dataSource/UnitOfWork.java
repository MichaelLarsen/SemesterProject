/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Booking;
import domain.Customer;
import domain.Room;
import domain.RoomGuest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class UnitOfWork {

    private ArrayList<Booking> newBookingList;
    private ArrayList<Booking> updateBookingList;
    private ArrayList<Room> updateRoomList;
    private ArrayList<RoomGuest> newGuestInRoomList;
    private ArrayList<RoomGuest> updateGuestInRoomList;
    private ArrayList<Customer> newCustomerList;
    private ArrayList<Customer> dirtyCustomerList;
    private ArrayList<Integer> deleteBookingsList;

    public UnitOfWork() {
        newBookingList = new ArrayList<>();
        updateRoomList = new ArrayList<>();
        newGuestInRoomList = new ArrayList<>();
        updateBookingList = new ArrayList<>();
        updateGuestInRoomList = new ArrayList<>();
        newCustomerList = new ArrayList<>();
        dirtyCustomerList = new ArrayList<>();
        deleteBookingsList = new ArrayList<>();
    }

    public boolean createCustomer(Customer customer) {
        boolean createCustomerSuccess = false;
        if (!newCustomerList.contains(customer)) {
            newCustomerList.add(customer);
            createCustomerSuccess = true;
        }
        return createCustomerSuccess;
    }

    public boolean updateCustomerDB(Customer customer) {
        boolean updateCustomerSuccess = false;
        if (!dirtyCustomerList.contains(customer)) {
            dirtyCustomerList.add(customer);
            updateCustomerSuccess = true;
        }
        return updateCustomerSuccess;
    }

    public boolean bookRoom(Booking booking) {
        boolean bookingSuccess = false;
        if (!newBookingList.contains(booking)) {
            newBookingList.add(booking);
            bookingSuccess = true;
        }
        return bookingSuccess;
    }

    public boolean addGuestToRoom(RoomGuest roomguest) {
        boolean addGuestSuccess = false;
        if (!newGuestInRoomList.contains(roomguest)) {
            newGuestInRoomList.add(roomguest);
            addGuestSuccess = true;
        }
        return addGuestSuccess;
    }

    public boolean updateRoomDB(Room room) {
        boolean updateSuccess = false;
        if (!updateRoomList.contains(room)) {
            updateRoomList.add(room);
            updateSuccess = true;
        }
        return updateSuccess;
    }

    boolean updateBookingDB(Booking booking) {
        boolean updateSuccess = false;
        if (!updateBookingList.contains(booking)) {
            updateBookingList.add(booking);
            updateSuccess = true;
        }
        return updateSuccess;
    }

    public boolean updateGuestsInRoomDB(RoomGuest roomGuest) {
        boolean updateSuccess = false;
        if (!updateGuestInRoomList.contains(roomGuest)) {
            updateGuestInRoomList.add(roomGuest);
            updateSuccess = true;
        }
        return updateSuccess;
    }
    
    public boolean deleteBookingFromDB(int bookingId) {
        boolean deleteSuccess = false;
        if (!deleteBookingsList.contains(bookingId)) {
            deleteBookingsList.add(bookingId);
            deleteSuccess = true;
        }
        return deleteSuccess;
    }

    public boolean commitTransaction(Connection con) throws SQLException {
        boolean commitSuccess = true;
        con.setAutoCommit(false);

        BookingMapper bookingMapper = new BookingMapper();
        CustomerMapper customerMapper = new CustomerMapper();
        RoomMapper roomMapper = new RoomMapper();
        RoomGuestMapper roomGuestMapper = new RoomGuestMapper();

        if (!newCustomerList.isEmpty()) {
            commitSuccess = commitSuccess && customerMapper.createCustomer(newCustomerList, con);
        }
        if (!dirtyCustomerList.isEmpty()) {
            commitSuccess = commitSuccess && customerMapper.updateCustomerDB(dirtyCustomerList, con);
        }
        if (!newBookingList.isEmpty()) {
            commitSuccess = commitSuccess && bookingMapper.addBooking(newBookingList, con);
        }
        if (!updateRoomList.isEmpty()) {
            commitSuccess = commitSuccess && roomMapper.updateRoomDB(updateRoomList, con);
        }
        if (!newGuestInRoomList.isEmpty()) {
            commitSuccess = commitSuccess && roomGuestMapper.addGuestToRoom(newGuestInRoomList, con);
        }
        if (!updateBookingList.isEmpty()) {
            commitSuccess = commitSuccess && bookingMapper.updateBooking(updateBookingList, con);
        }
        if (!updateGuestInRoomList.isEmpty()) {
            commitSuccess = commitSuccess && roomGuestMapper.updateGuestInRoom(updateGuestInRoomList, con);
        }
        if (!deleteBookingsList.isEmpty()) {
            commitSuccess = commitSuccess && bookingMapper.deleteBookingFromDB(deleteBookingsList, con);
        }
        if (!commitSuccess) {
            con.rollback();
            System.out.println("Fejl i commitTransaction!");
            //kast en exception! fejl i commitTransaction
        }
        else {
            con.commit();
            newBookingList.clear();
            System.out.println("Transactioner er commited!");
        }
        return commitSuccess;
    }

    
}
