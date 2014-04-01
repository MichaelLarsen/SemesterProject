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
    private int roomNo;
    private int bookingOwnerId;
    private int occupiedBeds;
    private ArrayList<Customer> roomGuestList;

    public Booking(int bookingId, int bookingOwnerId, int roomNo, String agency, Date checkInDate, Date checkOutDate, int occupiedBeds) {
        this.bookingId = bookingId;
        this.bookingOwnerId = bookingOwnerId;
        this.roomNo = roomNo;
        this.agency = agency;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.occupiedBeds = occupiedBeds;
        this.roomGuestList = new ArrayList<>();

    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public int getBookingOwnerId() {
        return bookingOwnerId;
    }

    public void setBookingOwnerId(int bookingOwnerId) {
        this.bookingOwnerId = bookingOwnerId;
    }

    public ArrayList<Customer> getCustomersForBooking() {
        return roomGuestList;
    }

    public void addCustomerForBooking(Customer customer) {
        roomGuestList.add(customer);
    }
    
    public int getOccupiedBeds(){
        return occupiedBeds;
    }
   

    @Override
    public String toString() {
        String str = "BookID: " + bookingId + "     RoomNo: " + roomNo + "     OwnerID: " + bookingOwnerId 
                + "     CheckIn: " + checkInDate + "    CheckOut: " + checkOutDate + "   Guests: " + occupiedBeds;
        return str;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
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
