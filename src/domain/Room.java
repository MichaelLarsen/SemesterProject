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
public class Room
{
    private int roomNo;
    private enum roomType {Single, Double, Family}
    private int price;
    
    public Room(int roomNo, enum roomType, int price)
    {
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.price = price;
    }

    public int getRoomNo()
    {
        return roomNo;
    }

    public void setRoomNo(int roomNo)
    {
        this.roomNo = roomNo;
    }
    
    public enum getRoomType()
    {
        return roomType;
    }
        
    public void setRoomType(enum roomType)
    {
        this.roomType = roomType;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }
    
}
