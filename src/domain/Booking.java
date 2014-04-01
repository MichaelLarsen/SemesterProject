/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

// Bruges for at kunne sende date mellem SQL og Java
import java.util.Date;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class Booking {

    private int bookingId;
    private String agency;
    private Date checkInDate;
    private Date checkOutDate;
    private Room room;
    private Customer bookingOwner;
    private ArrayList<Customer> roomGuestList;

    public Booking(int bookingId, Customer bookingOwner, Room room, String agency, Date checkInDate, Date checkOutDate) {
        this.bookingId = bookingId;
        this.bookingOwner = bookingOwner;
        this.room = room;
        this.agency = agency;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomGuestList = new ArrayList<>();

    }

    public ArrayList<Customer> getCustomersForBooking() {
        return roomGuestList;
    }

    public void addCustomerForBooking(Customer customer) {
        roomGuestList.add(customer);
    }
    
    public int getOccupiedBeds(){
        return roomGuestList.size();
    }
    
    public int getRoomSize() {
        return room.getRoomSize();
    }

    @Override
    public String toString() {
        String str = "BookID: " + bookingId + "     RoomNo: " + room.getRoomNo() + "     OwnerID: " + bookingOwner.getCustomerId() 
                + "     CheckIn: " + checkInDate + "    CheckOut: " + checkOutDate + "   Guests: " + getOccupiedBeds() + "/" + getRoomSize();
        return str;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Customer getBookingOwner() {
        return bookingOwner;
    }

    public void setBookingOwner(Customer bookingOwner) {
        this.bookingOwner = bookingOwner;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

//    public void incrementOccupiedBeds() {
//        if (room.getRoomSize() > occupiedBeds) {
//            occupiedBeds = occupiedBeds + 1;
//            System.out.println("Customer added to room +1");
//        }
//        else {
//            System.out.println("Room is full!");
//        }
//    }
//
//    public void decrementOccupiedBeds() {
//        if (occupiedBeds > 0) {
//            occupiedBeds = occupiedBeds - 1;
//            System.out.println("Customer removed from room -1");
//        }
//    }
}
