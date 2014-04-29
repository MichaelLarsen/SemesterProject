/*
 * Semester Projekt, Datamatiker 2. semester
 * Gruppe 4: Andreas, Michael og Sebastian
 */
package dataSource;

import domain.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * RoomMapper klassen håndterer kommunikation mellem programmet og
 * databasens ROOMS tabel.
 *
 * @author Sebastian, Michael og Andreas
 */
public class RoomMapper {

    /**
     * Henter alle rum fra databasen (ROOMS-tabel).
     * - Bruges til visning i GUI, samt til at lave bookings og tilføje gæster til dem.
     *
     * @param con   Forbindelse til databasen.
     * @return      Liste af alle rum i databasen.
     */
    public ArrayList<Room> getRoomsFromDB(Connection con) {
        Room room = null;
        ArrayList<Room> roomList = new ArrayList<>();
        String SQLString = "SELECT * "
                + "FROM ROOMS";
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
            System.out.println("Fail in RoomMapper.getRoomsFromDB()");
            System.out.println(e.getMessage());
        }
        finally
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in RoomMapper.getRoomsFromDB()");
                System.out.println(e.getMessage());
            }
        }
        return roomList;
    }

    /**
     * Benyttes ikke pt. da vi indtil videre ikke tillader at man ændrer på rum.
     *
     * TODO slet? benyttes ikke
     *
     * @param newRoomList       Liste af rum som er ændret.
     * @param con               Forbindelse til databasen.
     * @return                  TRUE, hvis UPDATE lykkes. Rækker ændret == antallet af Rooms i liste.
     */
    public boolean updateRoomDB(ArrayList<Room> newRoomList, Connection con) {
        int rowsUpdated = 0;
        String SQLString = "UPDATE rooms "
                + "SET room_type = ?, price = ? "
                + "WHERE room_no = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < newRoomList.size(); i++) {
                statement.setString(1, newRoomList.get(i).getRoomTypeString());
                statement.setInt(2, newRoomList.get(i).getPrice());
                statement.setInt(3, newRoomList.get(i).getRoomNo());
            }

            rowsUpdated += statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Fail in RoomMapper.UpdateRoomDB()");
            System.out.println(e.getMessage());
        }
        finally
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in RoomMapper.UpdateRoomDB()");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == newRoomList.size();
    }
}
