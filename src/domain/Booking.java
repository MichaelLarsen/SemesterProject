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
//import java.util.ArrayList;

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

    public Booking(ResultSet rs) {
        super();
        try {
            this.bookingId = rs.getInt("booking_id");
            this.bookingOwnerId = rs.getInt("booking_owner");
            this.roomNo = rs.getInt("room_no");
            if (rs.getString("agency") == null) {
                this.agency = "N/A";
            }
            else {
                this.agency = rs.getString("agency");
            }
            this.checkInDate = rs.getDate("check_in_date");
            this.checkOutDate = rs.getDate("check_out_date");
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper/Booking - Booking");
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

    @Override
    public String toString() {
        String str = "BookingId:    " + bookingId + "    RoomNo: " + roomNo + "     OwnerID: " + bookingOwnerId
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
