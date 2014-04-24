/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Guest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class GuestMapper {

    /**
     * Metoden henter en guest fra databasen via guestId.
     *
     * @param guestId
     * @param con
     * @return guest
     */
    public Guest getGuestFromID(int guestId, Connection con) {
        Guest guest = null;
        String SQLString = "select * from GUESTS "
                + "where guest_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, guestId);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                guest = new Guest(rs);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper - getGuestFromID");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper - getGuestFromID");
                System.out.println(e.getMessage());
            }
        }
        return guest;
    }

    public boolean createGuest(ArrayList<Guest> newGuestList, Connection con) {
        int guestsCreated = 0;
        String SQLString = "insert into GUESTS values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < newGuestList.size(); i++) {
                int newId = getNewGuestsId(con);
                statement.setInt(1, newId);
                statement.setString(2, newGuestList.get(i).getFirstName());
                statement.setString(3, newGuestList.get(i).getLastName());
                statement.setString(4, newGuestList.get(i).getStreet());
                statement.setString(5, newGuestList.get(i).getZipcode());
                statement.setString(6, newGuestList.get(i).getCity());
                statement.setString(7, newGuestList.get(i).getCountry());
                statement.setString(8, newGuestList.get(i).getEmail());
                statement.setInt(9, newGuestList.get(i).getPhone1());
                statement.setInt(10, newGuestList.get(i).getPhone2());
                guestsCreated += statement.executeUpdate(); // guestCreated bliver = newGuestList.size(), hvis Update går igennem
                log(newId, ActionType.CREATE, con);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper - createGuest");
            System.out.println(e.getMessage());
        }
        finally // Skal lukke statement
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper - createGuest");
                System.out.println(e.getMessage());
            }
        }
        return guestsCreated == newGuestList.size();
    }

    public int getNewGuestsId(Connection con) {
        int nextGuestId = 0;

        String SQLString = "select guest_id_seq.nextval from SYS.DUAL";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                nextGuestId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper - getNewGuestId");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper - getNewGuestId");
                System.out.println(e.getMessage());
            }
        }
        return nextGuestId;
    }

    public ArrayList<Guest> getGuestsFromDB(Connection con) {
        Guest guest = null;
        ArrayList<Guest> guestList = new ArrayList<>();
        String SQLString = "select * from GUESTS "
                + "order by guest_id desc";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                guest = new Guest(rs);
                guestList.add(guest);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper - getGuestsFromDB");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper - getGuestsFromDB");
                System.out.println(e.getMessage());
            }
        }
        return guestList;
    }

    public boolean updateGuestDB(ArrayList<Guest> dirtyGuestList, Connection con) {
        int rowsUpdated = 0; //hvis rowsInserted sættes == 1 er kunden booket til værelset
        String SQLString = "update GUESTS"
                + " set first_name = ?, last_name = ?, street = ?, zipcode = ?, city = ?, country = ?, email = ?, phone_1 = ?, phone_2 = ?"
                + " where guest_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (Guest guest : dirtyGuestList) {
                statement.setString(1, guest.getFirstName());
                statement.setString(2, guest.getLastName());
                statement.setString(3, guest.getStreet());
                statement.setString(4, guest.getZipcode());
                statement.setString(5, guest.getCity());
                statement.setString(6, guest.getCountry());
                statement.setString(7, guest.getEmail());
                statement.setInt(8, guest.getPhone1());
                statement.setInt(9, guest.getPhone2());
                statement.setInt(10, guest.getGuestId());
                rowsUpdated += statement.executeUpdate(); //rowsInserted bliver = 1, hvis Update går igennem
                log(guest.getGuestId(), ActionType.UPDATE, con);
            }

        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper - UpdateGuestDB");
            System.out.println(e.getMessage());
        }
        finally // Skal køres efter catch
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper - UpdateGuestDB");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == dirtyGuestList.size(); //hvis dette passer returneres true ellers false
    }

    public ArrayList<Guest> searchForGuestDB(String status, Connection con, String... names) {
        Guest guest = null;
        String SQLString = "";
        ArrayList<Guest> guestList = new ArrayList<>();
        if (status.equals("both")) {
            SQLString = "select * from GUESTS "
                    + "where upper(first_name) LIKE upper(?) AND upper(last_name) LIKE upper(?)";
        }
        if (status.equals("firstName")) {
            SQLString = "select * from GUESTS "
                    + "where upper(first_name) LIKE upper(?)";
        }
        if (status.equals("lastName")) {
            SQLString = "select * from GUESTS "
                    + "where upper(last_name) LIKE upper(?)";
        }

        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            if (status.equals("both")) {
                statement.setString(1, "%" + names[0] + "%");
                statement.setString(2, "%" + names[1] + "%");
            }
            else {
                statement.setString(1, "%" + names[0] + "%");
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                guest = new Guest(rs);
                guestList.add(guest);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper - searchForGuestDB");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper - searchForGuestDB");
                System.out.println(e.getMessage());
            }
        }
        return guestList;
    }

    public Guest getGuestDB(int bookingOwnerId, Connection con) {
        Guest guest = null;
        String SQLString = "select * from GUESTS"
                + " where guest_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, bookingOwnerId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                guest = new Guest(rs);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper - getGuestDB");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper - getGuestDB");
                System.out.println(e.getMessage());
            }
        }
        return guest;
    }

    public enum ActionType {

        CREATE(1), UPDATE(2);
        private int actionType;

        private ActionType(int state) {
            this.actionType = state;
        }
    }

    public static void log(int guest_id, ActionType action, Connection con) {
        String SQLString = "INSERT INTO GUEST_LOG (Id, Action, Guest_Id, Logdate, Content) "
                + "SELECT GUEST_LOG_ID_SEQ.Nextval, ?, ?, CURRENT_TIMESTAMP(3), SYS.Dbms_Xmlgen.Getxml('SELECT * FROM GUESTS "
                + "WHERE GUEST_ID = " + guest_id + "') xmlstr FROM Dual";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, action.actionType);
            statement.setInt(2, guest_id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper - log");
            System.out.println(e.getMessage());
        }
        finally // Skal lukke statement
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper - log");
                System.out.println(e.getMessage());
            }
        }
    }

    public ArrayList<String> getLog(Connection con) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        String SQLString = "select * from guest_log"
                + " order by id";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                stringArrayList.add("LogID: " + rs.getInt("id") + " GuestID: " + rs.getInt("guest_id") + " Logdate: " + rs.getDate("logdate") + " Action: " + rs.getInt("action") + " XML: " + rs.getString("content") + "\n");
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper - getLog");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper - getLog");
                System.out.println(e.getMessage());
            }
        }
        return stringArrayList;
    }

}
