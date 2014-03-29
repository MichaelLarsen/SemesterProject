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
public class Room {

    private int roomNo;
    private int price;
    private RoomType roomType;
    private String roomTypeString;
    private String isBookedString;
    private int occupiedBeds;

    //her oprettes en enumeration, som definerer mulige rum og rumstørrelser
    private enum RoomType {

        SINGLE(1), DOUBLE(2), FAMILY(5);
        private int roomSize;

        private RoomType(int roomSize) {
            this.roomSize = roomSize;
        }
    }

    public Room(int roomNo, String type, int price, int occupiedBeds, String booked) {

        this.roomNo = roomNo;
        this.price = price;
        this.occupiedBeds = occupiedBeds;
        this.roomTypeString = type;
        this.isBookedString = booked;

        if (type.equals("SINGLE")) {
            roomType = RoomType.SINGLE;
        }
        if (type.equals("DOUBLE")) {
            roomType = RoomType.DOUBLE;
        }
        if (type.equals("FAMILY")) {
            roomType = RoomType.FAMILY;
        }
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public String getRoomTypeString() {
        return roomTypeString;
    }
    
    public void setIsBookedString(String str){
        this.isBookedString = str;
    }
    
    public String getIsBookedString(){
        return isBookedString;
    }

    public int getRoomSize() {
        return roomType.roomSize;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOccupiedBeds() {
        return occupiedBeds;
    }

    public int getEmptyBeds() {
        return roomType.roomSize - occupiedBeds;
    }

    public void incrementOccupiedBeds() {
        if (roomType.roomSize > occupiedBeds) {
            occupiedBeds++;
            System.out.println("Customer added to room +1");
        }
        else {
            System.out.println("Room is full!");
        }
    }

    public void decrementOccupiedBeds() {
        if (occupiedBeds > 0) {
            occupiedBeds--;
            System.out.println("Customer removed from room -1");
        }
    }

    @Override
    public String toString() {
        String str = "";

        str = "RoomNo: " + roomNo + "       Type: " + roomTypeString + "        Price ($): " + price
                + "     Booked: " + isBookedString + "     Guests: "
                + occupiedBeds + "/" + roomType.roomSize + "\n";
        return str;
    }
}
