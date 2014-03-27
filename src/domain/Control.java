/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dataSource.DBFacade;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public boolean commitTransaction()  {
        boolean saved = false;
        try {
            saved = DBFacade.commitTransaction();
        }
        catch (SQLException e) {
            System.out.println("Fejl ved at gemme transaction!");
            System.out.println(e.getMessage());
        }
        return saved;
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

//    public boolean bookCustomerToRoom(Room room, Customer customer) {
//        boolean status = false;
//        boolean check = false;
//        int oldRoomNo = customer.getRoomNo();
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
//    public boolean updateRoomDB(Room room) {
//        boolean updateSuccess;
//        updateSuccess = DBFacade.updateRoomDB(room);
//        return updateSuccess;
//    }
    public boolean bookRoom(Room room, Customer customer) {
        boolean bookingSuccess = false;

        // Tjekker om rummet allerede er i bookinglisten,
        // hvilket betyder at det er booket
        for (int i = 0; i < bookingList.size(); i++) {
            if (bookingList.get(i).getRoomNo() == room.getRoomNo()) {
                bookingSuccess = false;

            }
            else {
                Booking newBooking = newBooking(room, customer);
                bookingSuccess = DBFacade.bookRoom(newBooking);

            }

        }
        return bookingSuccess;

    }

    public Booking newBooking(Room room, Customer customer) {
        Date date = new Date(2014, 06, 22);

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
}
