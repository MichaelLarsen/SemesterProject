/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author Darkeonz
 */
public class Booking {

    private Room room;
    private ArrayList<Customer> roomGuestList;
    private Customer roomBooker;
    private Date checkInDate;
    private int numberOfNights;
    private String agency;

    public Booking(Room room, ArrayList<Customer> customerList, Date checkInDate, Customer roomBooker, int numberOfNights, String agency) {
        customerList = new ArrayList<>();
        this.room = room;
        this.roomGuestList = customerList;
        this.roomBooker = roomBooker;
        this.checkInDate = checkInDate;
        this.numberOfNights = numberOfNights;
        this.agency = agency;
    
    }

    public ArrayList<Customer> getCustomerList() {
        return roomGuestList;
    }

    public void addCustomerToRoom(Customer customer) {
        roomGuestList.add(customer);
    }

    public Customer getRoomBooker() {
        return roomBooker;
    }

    public void setRoomBooker(Customer roomBooker) {
        this.roomBooker = roomBooker;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ArrayList<Customer> getCustomer() {
        return roomGuestList;
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
