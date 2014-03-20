/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Date;

/**
 *
 * @author Seb
 */
public class Customer {

    private int customerId;
    private String firstName;
    private String lastName;
    private String street;
    private String zipcode;
    private String city;
    private String country;
    private String email;
    private String agency;
    private String checkInDate;
    private int numberOfNights;
    private int roomNo;
    //private boolean paid_deposit = false;
    //private double debt;

    public Customer(int customerId, String firstName, String lastName, String street, String zipcode, String city, String country, String email, String agency, String checkInDate, int numberOfNights, int roomNo) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
        this.email = email;
        this.agency = agency;
        this.checkInDate = checkInDate;
        this.numberOfNights = numberOfNights;
        this.roomNo = roomNo;
        //this.paid_deposit = paid_deposit;
        //this.debt = debt;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

//    public boolean isPaid_deposit()
//    {
//        return paid_deposit;
//    }
//    public void setPaid_deposit(boolean paid_deposit)
//    {
//        this.paid_deposit = paid_deposit;
//    }
//    public double getDebt() {
//        return debt;
//    }
//
//    public void setDebt(double debt) {
//        this.debt = debt;
//    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }
    
    @Override
    public String toString() {
        String str = "";
        String date = checkInDate;
        str = "CustomerID: " + customerId + " Name: " + lastName + ", "+ firstName + " CheckIn: " + date.substring(0,11) + " NoOfNights: " + numberOfNights + " RoomNumber: " + roomNo + "\n";
        return str;
    }
}
