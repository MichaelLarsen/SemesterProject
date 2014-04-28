/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dataSource.DBFacade;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class Control {

    private DBFacade DBFacade;
    private ArrayList<Guest> guestList;
    private ArrayList<Room> roomList;
    private ArrayList<Booking> bookingList;
    private ArrayList<Booking> notSavedBookings;

    public Control() {
        DBFacade = DBFacade.getInstance();
        roomList = getRoomsFromDB();
    }

    public boolean commitTransaction() {
        boolean commitSuccess = false;
        try {
            commitSuccess = DBFacade.commitTransaction();
            if (commitSuccess) {
//                clearTempRoomGuestList();
            }
        }
        catch (SQLException e) {
            System.out.println("Fejl ved at gemme transaction!");
            System.out.println(e.getMessage());
        }
        return commitSuccess;
    }

    public final ArrayList<Room> getRoomsFromDB() {
        roomList = DBFacade.getRoomFromDB();
        return roomList;
    }

    public ArrayList<Room> getRooms() {
        return roomList;
    }

    public ArrayList<Booking> getBookings() {
        return bookingList;
    }

    public ArrayList<Guest> getGuestsFromDB() {
        guestList = DBFacade.getGuestsFromDB();
        return guestList;
    }

    public ArrayList<Booking> getBookingsFromDB() {
        bookingList = DBFacade.getBookingsFromDB();
        return bookingList;
    }

    public ArrayList<String> getBookingLog() {
        return DBFacade.getBookingLog();
    }

    public ArrayList<String> getGuestLog() {
        return DBFacade.getGuestLog();
    }

    public ArrayList<Guest> searchForGuestDB(String status, String... names) {
        return DBFacade.searchForGuestDB(status, names);
    }

    public boolean updateRoomDB(Room room) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateRoomDB(room);
        return updateSuccess;
    }

    /**
     * Benyttes ikke TODO slet?
     */
    public boolean updateBookingDB(Booking booking) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateBookingDB(booking);
        return updateSuccess;
    }

    /**
     * Benyttes ikke pt, da vi endnu ikke har implementeret mulighed for at
     * fjerne gæst fra en booking. TODO slet?
     */
    public boolean updateGuestsInRoomDB(BookingDetail roomGuest) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateGuestsInRoomDB(roomGuest);
        return updateSuccess;
    }

    public boolean deleteBookingFromDB(int bookingId) {
        return DBFacade.deleteBookingFromDB(bookingId);
    }

    public Booking bookRoom(Room room, Guest guest, Date checkIn, Date checkOut) {
        Boolean bookingAddedSuccess;
        boolean doubleBooking;
        Booking newBooking = null;

        doubleBooking = checkForDoubleBooking(room, checkIn, checkOut);
        if (doubleBooking == false) {
            newBooking = new Booking(guest.getGuestId(), room.getRoomNo(), "", checkIn, checkOut);
            bookingAddedSuccess = DBFacade.bookRoom(newBooking);
            if (bookingAddedSuccess == true && !bookingList.contains(newBooking)) {
                bookingList.add(newBooking);
            }
        }
        return newBooking;
    }

    public ArrayList<Guest> getGuestsInRoom(Booking booking) {
        return DBFacade.getGuestsInRoomFromDB(booking);
    }

    public ArrayList<Room> getAvailableRoomsDB(Date checkInDate, Date checkOutDate) {
//        Date bookingStartDate;
//        Date bookingEndDate;
        bookingList = DBFacade.getBookingsFromDB();
        Room room;
        ArrayList<Room> availableRoomList = new ArrayList<>();
        for (int i = 0; i < roomList.size(); i++) {
            room = roomList.get(i);
            boolean doubleBooking;
            doubleBooking = checkForDoubleBooking(room, checkInDate, checkOutDate);
//            boolean doubleBooking = false;
//            int j = 0;
//            while (doubleBooking == false && j < bookingList.size()) {
//                if ((bookingList.get(j).getRoomNo() == room.getRoomNo())) {
//                    bookingStartDate = bookingList.get(j).getCheckInDate();
//                    bookingEndDate = bookingList.get(j).getCheckOutDate();
//                    //Januar starter på 00, så 03 er fx april.
////                    System.out.println("checkInDate: " + checkInDate + " checkOutDate: " + checkOutDate);
////                    System.out.println("bookingStartDate: " + bookingStartDate + " bookingEndDate: " + bookingEndDate);
//                    if ((checkInDate.before(bookingStartDate) && checkOutDate.before(bookingStartDate))
//                            || (checkInDate.after(bookingEndDate) && checkOutDate.after(bookingEndDate))
//                            || checkInDate.equals(bookingEndDate) || checkOutDate.equals(bookingStartDate)) {
//                        doubleBooking = false;
////                        availableRoomList.add(room);
////                        System.out.println("Sådan - ledigt!");
//                    }
//                    else {
//                        doubleBooking = true;
////                        System.out.println("Doublebooking!");
//                    }
//                }
//                j++;
//            }
            if (doubleBooking == false && !availableRoomList.contains(room)) {
                availableRoomList.add(room);
            }
        }
        return availableRoomList;
    }

    public boolean checkForDoubleBooking(Room room, Date checkInDate, Date checkOutDate) {
        boolean doubleBooking = false;
        int j = 0;
        while (doubleBooking == false && j < bookingList.size()) {
            if ((bookingList.get(j).getRoomNo() == room.getRoomNo())) {
                Date bookingStartDate = bookingList.get(j).getCheckInDate();
                Date bookingEndDate = bookingList.get(j).getCheckOutDate();
                //Januar starter på 00, så 03 er fx april.
//                    System.out.println("checkInDate: " + checkInDate + " checkOutDate: " + checkOutDate);
//                    System.out.println("bookingStartDate: " + bookingStartDate + " bookingEndDate: " + bookingEndDate);
                if ((checkInDate.before(bookingStartDate) && checkOutDate.before(bookingStartDate))
                        || (checkInDate.after(bookingEndDate) && checkOutDate.after(bookingEndDate))
                        || checkInDate.equals(bookingEndDate) || checkOutDate.equals(bookingStartDate)) {
                    doubleBooking = false;
//                        availableRoomList.add(room);
//                        System.out.println("Sådan - ledigt!");
                }
                else {
                    doubleBooking = true;
//                        System.out.println("Doublebooking!");
                }
            }
            j++;
        }
            return doubleBooking;
    }
//        boolean doubleBooking = false;
//        bookingList = DBFacade.getBookingsFromDB();
//        for (int j = 0; j < bookingList.size(); j++) {
//            if ((bookingList.get(j).getRoomNo() == room.getRoomNo())) {
//                Date bookingStartDate = bookingList.get(j).getCheckInDate();
//                Date bookingEndDate = bookingList.get(j).getCheckOutDate();
//                //Januar starter på 00, så 03 er fx april.
////                System.out.println("checkInDate: " + checkInDate + " checkOutDate: " + checkOutDate);
////                System.out.println("bookingStartDate: " + bookingStartDate + " bookingEndDate: " + bookingEndDate);
//                if ((checkInDate.before(bookingStartDate) && checkOutDate.before(bookingStartDate))
//                        || (checkInDate.after(bookingEndDate) && checkOutDate.after(bookingEndDate))
//                        || checkInDate.equals(bookingEndDate) || checkOutDate.equals(bookingStartDate)) {
//                    doubleBooking = false;
////                    System.out.println("Sådan - ledigt!");
//                }
//                else {
//                    doubleBooking = true;
////                    System.out.println("Doublebooking!");
//                }
//            }
//        }
    

    public boolean addGuestToRoom(Guest guest, Booking booking) {
        ArrayList<Booking> tempBookingList;
        tempBookingList = DBFacade.getGuestBookingsFromDB(guest); //henter evt. bookings hvori kunden allerede indgår

        boolean addGuestSuccess = false;
        boolean doubleBooking = false;

        if (!tempBookingList.isEmpty()) {   //checker om kunden allerede bor på andre bookinger i samme periode
            doubleBooking = checkGuestForDoubleBooking(tempBookingList, booking);
        }
        if (doubleBooking == false) {
            BookingDetail bookingDetail = new BookingDetail(guest.getGuestId(), booking.getBookingId());
            addGuestSuccess = DBFacade.addGuestToRoom(bookingDetail);
        }
        return addGuestSuccess;
    }

    public boolean checkRoomAvailability(Booking booking, int arraySize) {
        boolean available = false;
        ArrayList<Guest> tempRoomGuestList = DBFacade.getGuestsInRoomFromDB(booking);  // vi henter en liste over allerede eksisterende kunder på bookingen
        for (int i = 0; i < roomList.size(); i++) {
            if (roomList.get(i).getRoomNo() == booking.getRoomNo()) {
                if (roomList.get(i).getRoomSize() > arraySize + tempRoomGuestList.size()) {   // vi checker at antallet af folk vi vil tilføje (arraySize) + antallet af folk allerede i bookingen (tempRoomGuestList) ikke overstiger getRoomSize.
                    available = true;
                }
            }
        }
        return available;
    }

    public ArrayList<Booking> getGuestBookingsFromDB(Guest guest) {
        return DBFacade.getGuestBookingsFromDB(guest);
    }

    private boolean checkGuestForDoubleBooking(ArrayList<Booking> oldBookingList, Booking newBooking) {
        boolean doubleBooking = false;
        Date newCheckInDate = newBooking.getCheckInDate();
        Date newCheckOutDate = newBooking.getCheckOutDate();
        int i = 0;
        while (doubleBooking == false && oldBookingList.size() > i) {
            Date oldCheckInDate = oldBookingList.get(i).getCheckInDate();
            Date oldCheckOutDate = oldBookingList.get(i).getCheckOutDate();
            if ((newCheckInDate.before(oldCheckInDate) && newCheckOutDate.before(oldCheckInDate))
                    || (newCheckInDate.after(oldCheckOutDate) && newCheckOutDate.after(oldCheckOutDate))
                    || newCheckInDate.equals(oldCheckOutDate) || newCheckOutDate.equals(oldCheckInDate)) {
                doubleBooking = false;
            }
            else {
                doubleBooking = true;
            }
            i++;
        }
        return doubleBooking;
    }

    public boolean createGuest(Guest guest) {
        return DBFacade.createGuest(guest);
    }

    public Guest getGuestFromID(int guestId) {
        return DBFacade.getGuestFromID(guestId);
    }

    public boolean updateGuestDB(Guest guest) {
        return DBFacade.updateGuestDB(guest);
    }

    public boolean undoNewBooking(Booking booking) {
        boolean undoSuccess = DBFacade.undoNewBooking(booking);
        int i = 0;
        while (undoSuccess == true && i < bookingList.size()) {
            if (booking.getBookingId() == bookingList.get(i).getBookingId()) {
                bookingList.remove(i);
                undoSuccess = true;
            }
            i++;
        }
        return undoSuccess;
    }

    public boolean saveBooking() {
        boolean commitSuccess = false;
        boolean doubleBooking = false;
        bookingList = DBFacade.getBookingsFromDB();
        notSavedBookings = new ArrayList<>();
        ArrayList<Booking> bookingsForSaving = DBFacade.getBookingsFromUOF();
        ArrayList<Booking> bookingsForSavingCopy = new ArrayList<>(bookingsForSaving);
        Room newRoom = null;
        //ConcurrentModificationException: man kan ikke kører løkken igennem og ændre på listen samtidig. 
        //Derfor har vi lavet en kopi af listen
        for (Booking booking : bookingsForSavingCopy) {
            for (int i = 0; i < roomList.size(); i++) {
                if (roomList.get(i).getRoomNo() == booking.getRoomNo()) {
                    newRoom = roomList.get(i);
                }
            }
            doubleBooking = checkForDoubleBooking(newRoom, booking.getCheckInDate(), booking.getCheckOutDate());
            if (doubleBooking) {
                DBFacade.removeBookingFromUOF(booking);
                notSavedBookings.add(booking);
            }
            commitSuccess = commitTransaction();
        }

        return commitSuccess;
    }

    public ArrayList<Booking> getBookingsNotSaved() {
        return notSavedBookings;
    }

    public void clearNewBookingDetails() {
        DBFacade.clearNewBookingDetails();
    }

}
