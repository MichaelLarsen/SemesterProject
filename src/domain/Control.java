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
        roomList = new ArrayList<>();
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

    public ArrayList<Room> getRoomsFromDB() {
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

//    public Booking bookRoom(Room room, Customer customer) {
//        Booking newBooking = null;
//        boolean isDoubleBooking = false;
//        int i = 0;
//
//        // Tjekker om rummet allerede er i bookinglisten,
//        // hvilket betyder at det er booket
//        while (isDoubleBooking == false && i < bookingList.size()) {
//            if (bookingList.get(i).getRoomNo() == room.getRoomNo()) {
//                isDoubleBooking = true;
//            }
//            i++;
//        }
//        if (isDoubleBooking == false) {
//            newBooking = newBooking(room, customer);
//            //OBS
//            room.setIsBookedString("YES");
//            updateRoomDB(room);
//            //OBS
//            DBFacade.bookRoom(newBooking);
//            bookingList.add(newBooking);
//            System.out.println("bookingList size " + bookingList.size());
//        }
//        return newBooking;
//    }
    public Booking newBooking(Room room, Customer customer) {
        Date date = new Date(114, 06, 22);
        Date date1 = new Date(114, 06, 29);

        Booking newBooking = new Booking(
                getNewBookingId(),
                customer,
                room,
                "",
                date,
                date1
        );
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
        roomList = getRoomsFromDB();
        for (int i = 0; i < roomList.size(); i++) {
            room = roomList.get(i);
            for (int j = 0; j < bookingList.size(); j++) {
                if ((bookingList.get(j).getRoom().equals(room))) {
                    bookingStartDate = bookingList.get(j).getCheckInDate();
                    bookingEndDate = bookingList.get(j).getCheckOutDate();
                    //Januar starter på 00, så 03 er fx april.
                    System.out.println("checkInDate: " + checkInDate + " checkOutDate: " + checkOutDate);
                    System.out.println("bookingStartDate: " + bookingStartDate + " bookingEndDate: " + bookingEndDate);
                    if ((checkInDate.before(bookingStartDate) && checkOutDate.before(bookingStartDate))
                            || (checkInDate.after(bookingEndDate) && checkOutDate.after(bookingEndDate))
                            || checkInDate.equals(bookingEndDate) || checkOutDate.equals(bookingStartDate)) {
                        availableRoomList.add(room);
                        System.out.println("Sådan - ledigt!");
                    }
                    else {
                        System.out.println("Doublebooking!");
                    }
                }
            }
        }
        return availableRoomList;
    }
}

//        if ((checkInDate.equals((bookingStartDate)) || (checkOutDate.equals(bookingEndDate))) 
//                || (checkInDate.after(bookingStartDate) && checkInDate.before(bookingEndDate)) 
//                || (checkOutDate.after(bookingStartDate) && checkOutDate.before(bookingEndDate))
//                || (checkInDate.before(bookingStartDate) && checkOutDate.after(bookingEndDate))) {
//            System.out.println("Doublebooking!");
//        }
//        else
//        {
//            System.out.println("Sådan!");
//        }

