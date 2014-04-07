/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

// Bruges for at kunne sende date mellem SQL og Java
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private ArrayList<Customer> guestsInBooking;

       public Booking(ResultSet rs) {
        super();
        
        try {
            this.bookingId = rs.getInt("Booking_Id");
            this.bookingOwnerId = rs.getInt("Booking_Owner");
            this.roomNo = rs.getInt("Room_No");
            this.agency = rs.getString("Agency");
            this.checkInDate = rs.getDate("Check_In_Date");
            this.checkOutDate = rs.getDate("Check_Out_Date");
            this.guestsInBooking = new ArrayList<>();
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - Booking");
            System.out.println(e.getMessage());
        }       
    }
    
    public Booking(int bookingOwnerId, int roomNo, String agency, Date checkInDate, Date checkOutDate) {
        this.bookingOwnerId = bookingOwnerId;
        this.roomNo = roomNo;
        this.agency = agency;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
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
        return guestsInBooking;
    }

    public void addCustomerForBooking(Customer customer) {
        guestsInBooking.add(customer);
    }
    
    public int getOccupiedBeds(){
        return guestsInBooking.size();
    }
    
    @Override
    public String toString() {
        String str = "BookingId:    " + bookingId +"    RoomNo: " + roomNo + "     OwnerID: " + bookingOwnerId 
                + "     CheckIn: " + checkInDate + "    CheckOut: " + checkOutDate;
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
}
