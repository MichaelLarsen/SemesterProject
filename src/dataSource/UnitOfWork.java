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
    private ArrayList<Room> newRoomList;
    private ArrayList<RoomGuest> newGuestInRoomList;

    public UnitOfWork() {
        newBookingList = new ArrayList<>();
        newRoomList = new ArrayList<>();
        newGuestInRoomList = new ArrayList<>();
    }

    public boolean bookRoom(Booking booking) {
        boolean bookingSuccess = false;
        if (!newBookingList.contains(booking)) {
            newBookingList.add(booking);
            bookingSuccess = true;
        }
        return bookingSuccess;
    }

    boolean addGuestToRoom(RoomGuest roomguest) {
        boolean addGuestSuccess = false;
        if (!newGuestInRoomList.contains(roomguest)) {
            newGuestInRoomList.add(roomguest);
            addGuestSuccess = true;
        }
        return addGuestSuccess;
    }

    public boolean updateRoomDB(Room room) {
        boolean updateSuccess = false;
        if (!newRoomList.contains(room)) {
            newRoomList.add(room);
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
        if (!newRoomList.isEmpty()) {
            commitSuccess = commitSuccess && roomMapper.updateRoomDB(newRoomList, con);

        }
        if (!newGuestInRoomList.isEmpty()) {
            commitSuccess = commitSuccess && roomGuestMapper.addGuestToRoom(newGuestInRoomList, con);
        }
        System.out.println("newBookingList size " + newBookingList.size());
        System.out.println("newRoomList size " + newRoomList.size());
        System.out.println("newGuestInRoomList size " + newRoomList.size());
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
