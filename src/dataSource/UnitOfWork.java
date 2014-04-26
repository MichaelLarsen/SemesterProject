/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Booking;
import domain.Guest;
import domain.Room;
import domain.BookingDetail;
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
    private ArrayList<BookingDetail> newGuestInRoomList;
    private ArrayList<BookingDetail> updateGuestInRoomList;
    private ArrayList<Guest> newGuestList;
    private ArrayList<Guest> dirtyGuestList;
    private ArrayList<Integer> deleteBookingsList;

    public UnitOfWork() {
        newBookingList = new ArrayList<>();
        updateRoomList = new ArrayList<>();
        newGuestInRoomList = new ArrayList<>();
        updateBookingList = new ArrayList<>();
        updateGuestInRoomList = new ArrayList<>();
        newGuestList = new ArrayList<>();
        dirtyGuestList = new ArrayList<>();
        deleteBookingsList = new ArrayList<>();
    }

    public boolean createGuest(Guest guest) {
        boolean createGuestSuccess = false;
        if (!newGuestList.contains(guest)) {
            newGuestList.add(guest);
            createGuestSuccess = true;
        }
        return createGuestSuccess;
    }

    public boolean updateGuestDB(Guest guest) {
        boolean updateGuestSuccess = false;
        if (!dirtyGuestList.contains(guest)) {
            dirtyGuestList.add(guest);
            updateGuestSuccess = true;
        }
        return updateGuestSuccess;
    }

    public boolean bookRoom(Booking booking) {
        boolean bookingSuccess = false;
        if (!newBookingList.contains(booking)) {
            newBookingList.add(booking);
            bookingSuccess = true;
        }
        return bookingSuccess;
    }

    public boolean addGuestToRoom(BookingDetail roomguest) {
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

    /**
     * benyttes ikke
     * TODO slet?
     */
    public boolean updateGuestsInRoomDB(BookingDetail roomGuest) {
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
        GuestMapper guestMapper = new GuestMapper();
        RoomMapper roomMapper = new RoomMapper();
        BookingDetailMapper bookingDetailMapper = new BookingDetailMapper();

        if (!newGuestList.isEmpty()) {
            commitSuccess = commitSuccess && guestMapper.createGuest(newGuestList, con);
        }
        if (!dirtyGuestList.isEmpty()) {
            commitSuccess = commitSuccess && guestMapper.updateGuestDB(dirtyGuestList, con);
        }
        if (!newBookingList.isEmpty()) {
            commitSuccess = commitSuccess && bookingMapper.addBooking(newBookingList, con);
        }
        if (!updateRoomList.isEmpty()) {
            commitSuccess = commitSuccess && roomMapper.updateRoomDB(updateRoomList, con);
        }
        if (!newGuestInRoomList.isEmpty()) {
            commitSuccess = commitSuccess && bookingDetailMapper.addGuestToRoom(newGuestInRoomList, con);
        }
        if (!updateBookingList.isEmpty()) {
            commitSuccess = commitSuccess && bookingMapper.updateBooking(updateBookingList, con);
        }
        if (!updateGuestInRoomList.isEmpty()) {
            commitSuccess = commitSuccess && bookingDetailMapper.updateGuestInRoom(updateGuestInRoomList, con);
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

    boolean undoNewBooking(Booking booking) {
        boolean undoSuccess = false;
        int i = 0;
        while (undoSuccess == false && i < newBookingList.size()) {
            if (booking.getBookingId() == newBookingList.get(i).getBookingId()) {
                newBookingList.remove(i);
                undoSuccess = true;
            }
            i++;
        }
        return undoSuccess;
    }
}
