/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
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
    private int privatePhone;
    private int workPhone;
    private int roomNo;

    public Customer(int customerId, String firstName, String lastName, String street, String zipcode, String city, String country, String email, int privatePhone, int workPhone) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
        this.email = email;
        this.privatePhone = privatePhone;
        this.workPhone = workPhone;

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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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

    public int getPrivatePhone() {
        return privatePhone;
    }

    public void setPrivatePhone(int privatePhone) {
        this.privatePhone = privatePhone;
    }

    public int getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(int workPhone) {
        this.workPhone = workPhone;
    }
    
    public void setRoomNo(int roomNo){
        this.roomNo = roomNo;
    }
    
    public int getRoomNo() {
        return roomNo;
    }

    @Override
    public String toString() {
        String str = "";
        str = "CustomerID: " + customerId + " Name: " + lastName + ", " + firstName + "\n";
        return str;
    }
}
