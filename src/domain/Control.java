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
    private ArrayList<Customer> customerList;
    private ArrayList<Room> roomList;
    private ArrayList<Booking> bookingList;

    public Control() {
        DBFacade = DBFacade.getInstance();
        customerList = new ArrayList<>();
        roomList = getRoomsFromDB();
    }

    public boolean commitTransaction() {
        boolean commitSuccess = false;
        try {
            commitSuccess = DBFacade.commitTransaction();
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

    public ArrayList<Customer> getCustomersFromDB() {
        customerList = DBFacade.getCustomersFromDB();
        return customerList;
    }

    public ArrayList<Booking> getBookingsFromDB() {
        bookingList = DBFacade.getBookingsFromDB();
        return bookingList;
    }

//    public boolean addGuestToRoom(Customer customer, Room room) {
//        boolean addGuestSuccess = false;
//        boolean guestExists = false;
//        ArrayList<Customer> tempGuestInBooking;
//        // TODO: Fix metoden!! kan den ikke være mindre?
//        for (int i = 0; i < bookingList.size(); i++) {
//            if (!bookingList.get(i).getCustomersForBooking().isEmpty()) {
//                tempGuestInBooking = bookingList.get(i).getCustomersForBooking();
//                for (int j = 0; j < tempGuestInBooking.size(); j++) {
//                    if (tempGuestInBooking.get(j).getCustomerId() == customer.getCustomerId()) {
//                        guestExists = true;
//                    }
//                }
//            }
//        }
//        if (!guestExists) {
//            for (int i = 0; i < bookingList.size(); i++) {
//                if (bookingList.get(i).getRoomNo() == room.getRoomNo() && room.getEmptyBeds() > 0) {
//                    RoomGuest roomGuest = new RoomGuest(customer.getCustomerId(), bookingList.get(i).getBookingId());
//                    bookingList.get(i).addCustomerForBooking(customer); //Tilføjer customer til listen i den specifikke booking
//                    addGuestSuccess = DBFacade.addGuestToRoom(roomGuest);
//                    room.incrementOccupiedBeds();
//                }
//            }
//            updateRoomDB(room);
//        }
//        return addGuestSuccess;
//    }
    public boolean updateRoomDB(Room room) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateRoomDB(room);
        return updateSuccess;
    }

    private boolean updateBookingDB(Booking booking) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateBookingDB(booking);
        return updateSuccess;
    }

    private boolean updateGuestsInRoomDB(RoomGuest roomGuest) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateGuestsInRoomDB(roomGuest);
        return updateSuccess;
    }

    public Booking bookRoom(Room room, Customer customer, Date checkIn, Date checkOut) {
        Boolean bookingAddedSuccess;
        boolean doubleBooking;
        Booking newBooking = null;

        doubleBooking = checkForDoubleBooking(room, checkIn, checkOut);
        if (doubleBooking == false) {
            newBooking = new Booking(getNewBookingId(), customer.getCustomerId(), room.getRoomNo(), "", checkIn, checkOut);
            bookingAddedSuccess = DBFacade.bookRoom(newBooking);
            if (bookingAddedSuccess == true && !bookingList.contains(newBooking)) {
                bookingList.add(newBooking);
            }
        }
        return newBooking;
    }

    public int getNewBookingId() {
        int newBookingId = 0;
        newBookingId = DBFacade.getNewBookingId();
        return newBookingId;
    }

    public ArrayList<Customer> getGuestsInRoom(Booking booking) {
        return DBFacade.getGuestsInRoom(booking);
    }

    public ArrayList<Room> getAvailableRoomsDB(Date checkInDate, Date checkOutDate) {
        Date bookingStartDate;
        Date bookingEndDate;
        Room room;
        ArrayList<Room> availableRoomList = new ArrayList<>();

        for (int i = 0; i < roomList.size(); i++) {
            room = roomList.get(i);
            boolean doubleBooking = false;
            for (int j = 0; j < bookingList.size(); j++) {
                if ((bookingList.get(j).getRoomNo() == room.getRoomNo())) {
                    bookingStartDate = bookingList.get(j).getCheckInDate();
                    bookingEndDate = bookingList.get(j).getCheckOutDate();
                    //Januar starter på 00, så 03 er fx april.
//                    System.out.println("checkInDate: " + checkInDate + " checkOutDate: " + checkOutDate);
//                    System.out.println("bookingStartDate: " + bookingStartDate + " bookingEndDate: " + bookingEndDate);
                    if ((checkInDate.before(bookingStartDate) && checkOutDate.before(bookingStartDate))
                            || (checkInDate.after(bookingEndDate) && checkOutDate.after(bookingEndDate))
                            || checkInDate.equals(bookingEndDate) || checkOutDate.equals(bookingStartDate)) {
                        doubleBooking = false;
                        availableRoomList.add(room);
//                        System.out.println("Sådan - ledigt!");
                    }
                    else {
                        doubleBooking = true;
//                        System.out.println("Doublebooking!");
                    }
                }
            }
            if (doubleBooking == false && !availableRoomList.contains(room)) {
                availableRoomList.add(room);
            }
        }
        return availableRoomList;
    }

    public boolean checkForDoubleBooking(Room room, Date checkInDate, Date checkOutDate) {
        boolean doubleBooking = false;
        for (int j = 0; j < bookingList.size(); j++) {
            if ((bookingList.get(j).getRoomNo() == room.getRoomNo())) {
                Date bookingStartDate = bookingList.get(j).getCheckInDate();
                Date bookingEndDate = bookingList.get(j).getCheckOutDate();
                //Januar starter på 00, så 03 er fx april.
//                System.out.println("checkInDate: " + checkInDate + " checkOutDate: " + checkOutDate);
//                System.out.println("bookingStartDate: " + bookingStartDate + " bookingEndDate: " + bookingEndDate);
                if ((checkInDate.before(bookingStartDate) && checkOutDate.before(bookingStartDate))
                        || (checkInDate.after(bookingEndDate) && checkOutDate.after(bookingEndDate))
                        || checkInDate.equals(bookingEndDate) || checkOutDate.equals(bookingStartDate)) {
                    doubleBooking = false;
//                    System.out.println("Sådan - ledigt!");
                }
                else {
                    doubleBooking = true;
//                    System.out.println("Doublebooking!");
                }
            }
        }
        return doubleBooking;
    }

    public boolean addGuestToRoom(Customer customer, Booking booking) {
        boolean addGuestSuccess = false;
        boolean doubleBooking = true;

        // TODO: kan man refakturere hele metoden så den bliver simplere?
        for (int i = 0; i < bookingList.size(); i++) {
            if (bookingList.get(i).getCustomersForBooking().contains(customer)) {   // checker om kunden allerede er gæst på en anden booking
                for (int j = 0; j < roomList.size(); j++) {
                    if (bookingList.get(i).getRoomNo() == roomList.get(j).getRoomNo()) {
                        doubleBooking = checkForDoubleBooking(roomList.get(j), booking.getCheckInDate(), booking.getCheckOutDate());  // Hvis kunden findes på en anden booking, checker vi her om de 2 bookinger overlapper
                    }
                }
            }
        }
        if (doubleBooking == false) {
            for (int i = 0; i < roomList.size(); i++) {
                if (booking.getRoomNo() == roomList.get(i).getRoomNo()) {
                    if (roomList.get(i).getRoomSize() > booking.getOccupiedBeds() && !booking.getCustomersForBooking().contains(customer)) {    // checker at værelset ikke er fyldt, og at kunden ikke allerede er gæst på værelset
                        RoomGuest roomGuest = new RoomGuest(customer.getCustomerId(), booking.getBookingId());
                        addGuestSuccess = DBFacade.addGuestToRoom(roomGuest);
                    }
                }
            }
        }
        return addGuestSuccess;
    }
}
