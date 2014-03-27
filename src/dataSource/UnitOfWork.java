/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Booking;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class UnitOfWork {

    private ArrayList<Booking> newBookingList;

    public UnitOfWork() {
        newBookingList = new ArrayList<>();
    }

    public boolean bookRoom(Booking booking) {
        boolean bookingSuccess = false;
        if (!newBookingList.contains(booking)) {
            newBookingList.add(booking);
            bookingSuccess = true;
        }
        return bookingSuccess;
    }

    public boolean commitTransaction(Connection con) throws SQLException {
        boolean commitSuccess;
        con.setAutoCommit(false);

        BookingMapper bookingMapper = new BookingMapper();
        CustomerMapper customerMapper = new CustomerMapper();
        RoomMapper roomMapper = new RoomMapper();

        commitSuccess = bookingMapper.addBooking(newBookingList, con);
        
        if (!commitSuccess) {
            con.rollback();
            System.out.println("Fejl i commitTransaction!");
            //kast en exception! fejl i commitTransaction
        }
        else {
            con.commit();
            System.out.println("Transactioner er commited!");
        }
        return commitSuccess;

    }

}
