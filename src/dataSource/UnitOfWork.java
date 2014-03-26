/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Booking;
import domain.Customer;
import domain.Room;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class UnitOfWork {

    private ArrayList<Booking> bookingList;

    public UnitOfWork() {
        bookingList = new ArrayList<>();
    }

    public boolean bookRoom(Booking booking) {
        boolean bookingSuccess = false;
        if (!bookingList.contains(booking)) {
            bookingList.add(booking);
            bookingSuccess = true;
        }
        return bookingSuccess;
    }
    
    public boolean commitTransaction(Connection con){
        
        BookingMapper bookingMapper = new BookingMapper();
        CustomerMapper customerMapper = new CustomerMapper();
        RoomMapper roomMapper = new RoomMapper();
        
        bookingMapper.
                
                return true;
                
        
    }
            
}
