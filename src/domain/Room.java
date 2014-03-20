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
    private String roomType;
    private int price;
    private int occupied;

    public Room(int roomNo, String roomType, int price, int occupied) {
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.price = price;
        this.occupied = occupied;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    public int getOccupied() {
        return occupied;
    }
    
    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    @Override
    public String toString() {
        String booked = "no";
        if (occupied == 1) {
            booked = "yes";
        }
        String str = "";
        str = "RoomNo: " +roomNo+ " Type: " + roomType + " Price($): " + price + " Occupied: " + booked + "\n";
        return str;
    }

}
