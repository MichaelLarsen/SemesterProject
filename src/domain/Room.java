/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author Seb
 */
public class Room {

    private int roomNo;
    private int price;
    private RoomType roomType;
    private int occupiedBeds;

    //her oprettes en enumeration, som definerer mulige rum og rumstørrelser
    public enum RoomType {

        SINGLE(1), DOUBLE(2), FAMILY(5);
        private int roomSize;

        private RoomType(int roomSize) {
            this.roomSize = roomSize;
        }
    }

    public Room(int roomNo, String type, int price, int occupiedBeds) {
        this.roomNo = roomNo;
        this.price = price;
        this.occupiedBeds = occupiedBeds;
        if (type.equals("Single")) {
            roomType = RoomType.SINGLE;
        }
        if (type.equals("Double")) {
            roomType = RoomType.DOUBLE;
        }
        if (type.equals("Family")) {
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
    
    public int getRoomSize() {
        return roomType.roomSize;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
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

    public void setOccupiedBeds(int occupied) {
        this.occupiedBeds = occupied;
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
        String booked = "";
        if (occupiedBeds > 0) {
            booked = "Yes";
        }
        else {
            booked = "No";
        }
        String str = "";
        str = "RoomNo: " + roomNo + " Type: " + roomType + " Price($): " + price
                + " Occupied: " + booked + " Guests: " + 
                occupiedBeds + "/" + roomType.roomSize + "\n";
        return str;
    }

}