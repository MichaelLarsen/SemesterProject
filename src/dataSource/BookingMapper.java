/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Booking;
import domain.Guest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * BookingMapper klassen håndterer persistering af data mellem programmet og
 * databasens BOOKINGS tabel.
 *
 * @author Andreas, Michael & Sebastian
 */
public class BookingMapper {

    /**
     * Henter alle bookings fra databasen, til visning i bookingoversigt.
     *
     * @param con       Forbindelse til databasen.
     * @return          Liste med alle bookings fra databasen.
     */
    public ArrayList<Booking> getBookingsFromDB(Connection con) {
        Booking booking = null;
        ArrayList<Booking> bookingList = new ArrayList<>();
        String SQLString = "select * from BOOKINGS "
                + "order by booking_id desc";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                booking = new Booking(rs);      // Vi sender vores ResultSet direkte videre til vores constructor
                bookingList.add(booking);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - getBookingsFromDB");
            System.out.println(e.getMessage());
        }
        finally {    // SKAL køres efter try/catch statement
            try {
                statement.close();  // Vi beder eksplicit om at lukke/release vores statement, inklusiv forbindelse
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - getBookingsFromDB");
                System.out.println(e.getMessage());
            }
        }
        return bookingList;
    }

    /**
     * Henter næste, unikke bookingId som databasen genererer, til brug ved
     * oprettelse af ny booking.
     *
     * @param con       Forbindelse til databasen.
     * @return          Næste bookingId.
     */
    public int getNewBookingId(Connection con) {
        int nextBookingId = 0;

        String SQLString = "select booking_id_seq.nextval from SYS.DUAL";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                nextBookingId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - getNewBookingId");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - getNewBookingId");
                System.out.println(e.getMessage());
            }
        }
        return nextBookingId;
    }

    /**
     * Persisterer booking i databasen, til brug ved oprettelse af ny booking.
     *
     * @param newBookingList    Liste af Booking-objekter, som skal persisteres.
     * @param con               Forbindelse til databasen.
     * @return                  TRUE, hvis INSERTs lykkes. Rækker tilføjet == antallet af Bookings i liste.
     */
    public boolean addBooking(ArrayList<Booking> newBookingList, Connection con) {
        int bookingAdded = 0;
        String SQLString = "INSERT INTO bookings VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < newBookingList.size(); i++) {
                int bookingId = getNewBookingId(con);
                statement.setInt(1, bookingId);
                statement.setInt(2, newBookingList.get(i).getBookingOwnerId());
                statement.setInt(3, newBookingList.get(i).getRoomNo());
                statement.setString(4, newBookingList.get(i).getAgency());
                statement.setDate(5, new java.sql.Date(newBookingList.get(i).getCheckInDate().getTime()));
                statement.setDate(6, new java.sql.Date(newBookingList.get(i).getCheckOutDate().getTime()));
                bookingAdded += statement.executeUpdate();
                log(bookingId, ActionType.CREATE, con);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - addBooking");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - addBooking");
                System.out.println(e.getMessage());
            }
        }
        return bookingAdded == newBookingList.size();
    }

    /**
     * Henter gæster som bor på booking/room, til visning i bookingoversigt.
     *
     * @param booking       Bookingen vi ønsker gæsterne for.
     * @param con           Forbindelse til databasen.
     * @return              Liste med gæster fra bookingen.
     */
    public ArrayList<Guest> getGuestsInBooking(Booking booking, Connection con) {
        ArrayList<Guest> roomGuestList = new ArrayList<>();
        Guest guest = null;
        String SQLString = "SELECT * "
                + "FROM GUESTS "
                + "JOIN booking_details "
                + "ON guests.guest_id = booking_details.guest_id "
                + "WHERE booking_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, booking.getBookingId());

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                guest = new Guest(rs);
                roomGuestList.add(guest);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - getGuestsInRoom");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - getGuestsInRoom");
                System.out.println(e.getMessage());
            }
        }
        return roomGuestList;
    }

    /**
     * Persisterer ændringer til booking(s) i databasen.
     * - Gælder ikke tilføjelse/fjernelse af gæster, som håndteres i BookingDetailMapper.addGuestToRoom().
     *
     * Metoden benyttes ikke pt., da vi ikke blev færdig med at implementere
     * denne user story.
     *
     * TODO slet?
     *
     * @param updateBookingList     Liste af ændrede bookings.
     * @param con                   Forbindelse til databasen.
     * @return                      TRUE, hvis UPDATEs lykkes. Rækker ændret == antallet af Bookings i liste.
     */
    public boolean updateBooking(ArrayList<Booking> updateBookingList, Connection con) {
        int rowsUpdated = 0;
        String SQLString = "UPDATE bookings "
                + "SET booking_owner = ?, room_no = ?, agency = ?, check_in_date = ?, check_out_date = ? "
                + "WHERE booking_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            for (int i = 0; i < updateBookingList.size(); i++) {
                statement.setInt(1, updateBookingList.get(i).getBookingOwnerId());
                statement.setInt(2, updateBookingList.get(i).getRoomNo());
                statement.setString(3, updateBookingList.get(i).getAgency());
                statement.setDate(4, new java.sql.Date(updateBookingList.get(i).getCheckInDate().getTime()));
                statement.setDate(5, new java.sql.Date(updateBookingList.get(i).getCheckOutDate().getTime()));
                statement.setInt(6, updateBookingList.get(i).getBookingId());
                rowsUpdated += statement.executeUpdate();
                log(updateBookingList.get(i).getBookingId(), ActionType.UPDATE, con);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - updateBookingDB");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - updateBookingDB");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == updateBookingList.size();
    }

    /**
     * Henter alle bookinger gæsten sover på (ikke dem han er ejer på). 
     * Bruges til at checke at gæsten ikke bookes til flere værelser i samme tidsrum.
     *
     * @param guest         Gæst for hvem bookings ønskes.
     * @param con           Forbindelse til databasen.
     * @return              Liste med bookinger gæsten
     */
    public ArrayList<Booking> getGuestBookingsFromDB(Guest guest, Connection con) {
        Booking booking = null;
        ArrayList<Booking> tempBookingList = new ArrayList<>();
        String SQLString = "SELECT * "
                + "FROM bookings b "
                + "JOIN booking_details bd ON bd.booking_id = b.booking_id "
                + "WHERE guest_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, guest.getGuestId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                booking = new Booking(rs);
                tempBookingList.add(booking);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - getGuestBookingsFromDB");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - getGuestBookingsFromDB");
                System.out.println(e.getMessage());
            }
        }
        return tempBookingList;
    }

    /**
     * Sletter booking fra databasen (BOOKINGS table), samt evt. gæster på bookingen (BOOKING_DETAILS).
     *
     * @param deleteBookingsList    Liste med bookingIds på bookings som skal slettes. Bemærk, som programmet er pt. sletter vi aldrig mere end én booking af gangen.
     * @param con                   Forbindelse til datgabasen.
     * @return                      TRUE, hvis DELETE lykkes. Rækker slettet == antallet af bookingIds i liste (altid 1).
     */
    public boolean deleteBookingFromDB(ArrayList<Integer> deleteBookingsList, Connection con) {
        int rowsDeleted = 0;
        String SQLString1 = "DELETE FROM booking_details "   // først slettes evt gæster på bookingen
                + "WHERE booking_id = ?";
        String SQLString2 = "DELETE FROM bookings "  // herefter slettes selve bookingen
                + "WHERE booking_id = ?";
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        try {
            statement1 = con.prepareStatement(SQLString1);
            statement2 = con.prepareStatement(SQLString2);
            for (int i = 0; i < deleteBookingsList.size(); i++) {
                log(deleteBookingsList.get(i), ActionType.DELETE, con);     // før sletning logges bookingens tilstand
                statement1.setInt(1, deleteBookingsList.get(i));
                statement2.setInt(1, deleteBookingsList.get(i));
                statement1.executeUpdate();
                rowsDeleted += statement2.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - deleteBookingFromDB");
            System.out.println(e.getMessage());
        }
        finally
        {
            try {
                statement1.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - deleteBookingFromDB");
                System.out.println(e.getMessage());
            }
        }
        return rowsDeleted == deleteBookingsList.size(); //hvis dette passer returneres true ellers false  
    }

    public enum ActionType {

        CREATE(1), UPDATE(2), DELETE(3), ADDED_GUEST(4);
        private int actionType;

        private ActionType(int state) {
            this.actionType = state;
        }
    }

    public static void log(int booking_id, ActionType action, Connection con) {
        String SQLString = "INSERT INTO BOOKING_LOG (Id, Action, Booking_Id, Logdate, Content) " //laver ny log for ændret booking
                + "SELECT BOOKING_LOG_ID_SEQ.Nextval, ?, ?, CURRENT_TIMESTAMP(3), SYS.Dbms_Xmlgen.Getxml('SELECT" //vælger alle bookings med en unik ID og indsætter actiontype og bookingID på '?'
                + " (SELECT LISTAGG(CONCAT(CONCAT(g.FIRST_NAME, '' ''), g.LAST_NAME), '','') WITHIN GROUP (ORDER BY 1) AS Fullname " //henter de gæster som bor på bookingen med fornavn og efternavn aggregeret som en kolonne og gruppere dem som Fullname
                + "FROM GUESTS g JOIN BOOKING_DETAILS bd ON bd.GUEST_ID = g.GUEST_ID AND bd.BOOKING_ID = " + booking_id + ") AS GUESTS, "
                + "b.* FROM Bookings b WHERE b.booking_id = " + booking_id + "') xmlstr FROM Dual"; //Vi henter data som xml da det kan bruges af mange programmer og bruges i mange sammenhænge

        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, action.actionType);
            statement.setInt(2, booking_id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - log");
            System.out.println(e.getMessage());
        }
        finally // Skal lukke statement
        {
            try {
                statement.close(); //lukker statements
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - log");
                System.out.println(e.getMessage());
            }
        }
    }

    public ArrayList<String> getLog(Connection con) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        String SQLString = "select * from booking_log"
                + " order by id";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                stringArrayList.add("LogID: " + rs.getInt("id") + " BookingID: " + rs.getInt("booking_id") + " LogDate: " + rs.getTimestamp("logdate") + " Action: " + rs.getInt("action") + " XML: " + rs.getString("content") + "\n");
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper - getLog");
            System.out.println(e.getMessage());
        }
        finally // must close statement
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper - getLog");
                System.out.println(e.getMessage());
            }
        }
        return stringArrayList;
    }
}
