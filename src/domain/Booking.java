/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

// Bruges for at kunne sende date mellem SQL og Java
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class Booking {

    private int bookingId;
    private int bookingOwner;
    private int roomNo;
    private String agency;
    private Date checkInDate;
    private int numberOfNights;
    private ArrayList<Customer> roomGuestList;

    public Booking(int bookingId, int bookingOwner, int roomNo, String agency, Date checkInDate, int numberOfNights) {
        this.bookingId = bookingId;
        this.bookingOwner = bookingOwner;
        this.roomNo = roomNo;
        this.agency = agency;
        this.checkInDate = checkInDate;
        this.numberOfNights = numberOfNights;
        this.roomGuestList = new ArrayList<>();

    }

    public ArrayList<Customer> getCustomersForBooking() {
        return roomGuestList;
    }

    public void addCustomerForBooking(Customer customer) {
        roomGuestList.add(customer);
    }

    @Override
    public String toString() {
        String str = "";
        str = "BookingID: " + bookingId + " RoomNumber: " + roomNo + " BookingOwner: " + bookingOwner + " date: " + checkInDate;
        return str;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getBookingOwner() {
        return bookingOwner;
    }

    public void setBookingOwner(int bookingOwner) {
        this.bookingOwner = bookingOwner;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
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

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }
}
