/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dataSource;

import domain.Booking;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class BookingMapper {

   public ArrayList<Booking> getBookingsFromDB(Connection con) {
        Booking booking = null;
        ArrayList<Booking> bookingList = new ArrayList<>();
        String SQLString = "select * from BOOKINGS";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                booking = new Booking(rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getDate(5),
                        rs.getInt(6));
                bookingList.add(booking);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - getBookingsFromDB");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - getBookingsFromDB");
                System.out.println(e.getMessage());
            }
        }
        return bookingList;
    }
    
}
