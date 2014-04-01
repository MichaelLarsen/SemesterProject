/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class RoomMapper {

    public ArrayList<Room> getRoomsFromDB(Connection con) {
        Room room = null;
        ArrayList<Room> roomList = new ArrayList<>();
        String SQLString = "select * from ROOMS";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                room = new Room(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3));
                roomList.add(room);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in RoomMapper - getRoomsFromDB");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in RoomMapper - getRoomsFromDB");
                System.out.println(e.getMessage());
            }
        }
        return roomList;
    }

    public boolean updateRoomDB(ArrayList<Room> newRoomList, Connection con) {
        int rowsUpdated = 0; //hvis rowsInserted sættes == 1 er kunden booket til værelset
        String SQLString = "update ROOMS"
                + " set room_type = ?, price = ?"
                + " where room_no = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < newRoomList.size(); i++) {
                statement.setString(1, newRoomList.get(i).getRoomTypeString());
                statement.setInt(2, newRoomList.get(i).getPrice());
                statement.setInt(3, newRoomList.get(i).getRoomNo());
            }

            rowsUpdated += statement.executeUpdate(); //rowsInserted bliver = newRoomList.size(), hvis Update går igennem
        }
        catch (SQLException e) {
            System.out.println("Fail in RoomMapper - UpdateRoomDB");
            System.out.println(e.getMessage());
        }
        finally // Skal køres efter catch
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in RoomMapper - UpdateRoomDB");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == newRoomList.size(); //hvis dette passer returneres true ellers false  
    }
}
