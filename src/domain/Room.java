/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

import java.util.ArrayList;

/**
 *
 * @author Seb
 */
public class Room
{
    private int roomNo;
    private String roomType;
    private int price;
    
    
    
    public Room(int roomNo, String roomType, int price)
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
    
    public String getRoomType()
    {
        return roomType;
    }
        
    public void setRoomType(String roomType)
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
