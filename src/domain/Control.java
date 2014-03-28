/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dataSource.DBFacade;
import java.sql.Date;
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
    
    public boolean addGuestToRoom(Customer customer, Room room) {
        boolean addGuestSuccess = false;
        boolean guestExists = false;
        ArrayList<Customer> tempGuestInBooking;
        // TODO: Fix metoden!! kan den ikke være mindre?
        for (int i = 0; i < bookingList.size(); i++) {
            tempGuestInBooking = bookingList.get(i).getCustomersForBooking();
            for (int j = 0; j < tempGuestInBooking.size(); j++) {
                if (tempGuestInBooking.get(i).getCustomerId() == customer.getCustomerId()) {
                    guestExists = true;
                }
            }
        }
        if (!guestExists) {
            for (int i = 0; i < bookingList.size(); i++) {
                 if (bookingList.get(i).getRoomNo() == room.getRoomNo() && room.getEmptyBeds() > 0) {
                    RoomGuest roomGuest = new RoomGuest(customer.getCustomerId(),bookingList.get(i).getBookingId());
                    bookingList.get(i).addCustomerForBooking(customer); //Tilføjer customer til listen i den specifikke booking
                    addGuestSuccess = DBFacade.addGuestToRoom(roomGuest);
                }
            }
        }
        return addGuestSuccess;
    }
//        for (int i = 0; i < tempGuestInBooking.size(); i++) {
//            
//        }
//            if (tempGuestInBooking.get(i).getCustomerId() == customer.getCustomerId()) {
//                if (room.getRoomNo()) {
//                    
//                }
//            }
//            if (bookingList.get(i).getRoomNo() == room.getRoomNo() && room.getEmptyBeds() > 0) {
//                RoomGuest roomGuest = new RoomGuest(customer.getCustomerId(),bookingList.get(i).getBookingId());
//                bookingList.get(i).addCustomerForBooking(customer);
//                
//                addGuestSuccess = DBFacade.addGuestToRoom(roomGuest);
//            }
//        }

//    public boolean bookCustomerToRoom(Room room, Customer customer) {
//        boolean status = false;
//        boolean check = false;
//
//        if (room.getEmptyBeds() > 0) {
//            int i = 0;
//            while (!check && i < roomList.size() && oldRoomNo != 0) {
//                if (oldRoomNo == roomList.get(i).getRoomNo()) {
//                    roomList.get(i).decrementOccupiedBeds();
//                    updateRoomDB(roomList.get(i));
//                    check = true;
//                }
//                i++;
//            }
//            int newRoomNumber = room.getRoomNo();
//            customer.setRoomNo(newRoomNumber);
//            room.incrementOccupiedBeds();
//            updateCustomerDB(customer);
//            updateRoomDB(room);
//            status = true;
//        }
//        return status;
//    }
//
//    public boolean updateCustomerDB(Customer customer) {
//        boolean updateSuccess;
//        updateSuccess = DBFacade.updateCustomerDB(customer);
//        return updateSuccess;
//    }
//
    public boolean updateRoomDB(Room room) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateRoomDB(room);
        return updateSuccess;
    }
    public Booking bookRoom(Room room, Customer customer) {
        Booking newBooking = null;
        boolean isDoubleBooking = false;
        int i = 0;

        // Tjekker om rummet allerede er i bookinglisten,
        // hvilket betyder at det er booket
        while (isDoubleBooking == false && i < bookingList.size()) {
            if (bookingList.get(i).getRoomNo() == room.getRoomNo()) {
                isDoubleBooking = true;
            }
            i++;
        }
        if (isDoubleBooking == false) {
            newBooking = newBooking(room, customer);
            //OBS
            room.setIsBookedString("YES");
            updateRoomDB(room);
            //OBS
            DBFacade.bookRoom(newBooking);
            bookingList.add(newBooking);
            System.out.println("bookingList size " + bookingList.size());
        }
        return newBooking;
    }

    public Booking newBooking(Room room, Customer customer) {
        Date date = new Date(114, 06, 22);

        Booking newBooking = new Booking(
                getNewBookingId(),
                customer.getCustomerId(),
                room.getRoomNo(),
                "",
                date,
                7
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
}
