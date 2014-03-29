/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Booking;
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

    public UnitOfWork() {
        newBookingList = new ArrayList<>();
        updateRoomList = new ArrayList<>();
        newGuestInRoomList = new ArrayList<>();
        updateBookingList = new ArrayList<>();
        updateGuestInRoomList = new ArrayList<>();
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

    public boolean commitTransaction(Connection con) throws SQLException {
        boolean commitSuccess = true;
        con.setAutoCommit(false);

        BookingMapper bookingMapper = new BookingMapper();
        CustomerMapper customerMapper = new CustomerMapper();
        RoomMapper roomMapper = new RoomMapper();
        RoomGuestMapper roomGuestMapper = new RoomGuestMapper();
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
        System.out.println("updateBookingList size " + updateBookingList.size());
        System.out.println("newBookingList size " + newBookingList.size());
        System.out.println("newRoomList size " + updateRoomList.size());
        System.out.println("newGuestInRoomList size " + updateRoomList.size());
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
