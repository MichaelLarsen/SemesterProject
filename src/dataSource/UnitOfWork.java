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
    private ArrayList<Booking> dirtyBookingList;
    private ArrayList<Room> dirtyRoomList;
    private ArrayList<BookingDetail> newBookingDetailList;
    private ArrayList<BookingDetail> dirtyBookingDetailList;
    private ArrayList<Guest> newGuestList;
    private ArrayList<Guest> dirtyGuestList;
    private ArrayList<Integer> deleteBookingsList;

    public UnitOfWork() {
        newBookingList = new ArrayList<>();
        dirtyRoomList = new ArrayList<>();
        newBookingDetailList = new ArrayList<>();
        dirtyBookingList = new ArrayList<>();
        dirtyBookingDetailList = new ArrayList<>();
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
        if (!newBookingDetailList.contains(roomguest)) {
            newBookingDetailList.add(roomguest);
            addGuestSuccess = true;
        }
        return addGuestSuccess;
    }

    public boolean updateRoomDB(Room room) {
        boolean updateSuccess = false;
        if (!dirtyRoomList.contains(room)) {
            dirtyRoomList.add(room);
            updateSuccess = true;
        }
        return updateSuccess;
    }

    boolean updateBookingDB(Booking booking) {
        boolean updateSuccess = false;
        if (!dirtyBookingList.contains(booking)) {
            dirtyBookingList.add(booking);
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
        if (!dirtyBookingDetailList.contains(roomGuest)) {
            dirtyBookingDetailList.add(roomGuest);
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
        if (!dirtyRoomList.isEmpty()) {
            commitSuccess = commitSuccess && roomMapper.updateRoomDB(dirtyRoomList, con);
        }
        if (!newBookingDetailList.isEmpty()) {
            commitSuccess = commitSuccess && bookingDetailMapper.addGuestToRoom(newBookingDetailList, con);
        }
        if (!dirtyBookingList.isEmpty()) {
            commitSuccess = commitSuccess && bookingMapper.updateBooking(dirtyBookingList, con);
        }
        if (!dirtyBookingDetailList.isEmpty()) {
            commitSuccess = commitSuccess && bookingDetailMapper.updateBookingDetail(dirtyBookingDetailList, con);
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

    public boolean undoNewBooking(Booking booking) {
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

    public ArrayList<Booking> getBookings() {
        return newBookingList;
    }

    void removeBookingFromUOF(Booking booking) {
        newBookingList.remove(booking);
    }

    void clearNewBookingDetails() {
        newBookingDetailList.clear();
    }
}
