/*
 * Semester Projekt, Datamatiker 2. semester
 * Gruppe 4: Andreas, Michael og Sebastian
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
     * @return          Liste med alle Bookings fra databasen.
     */
    public ArrayList<Booking> getBookingsFromDB(Connection con) {
        Booking booking = null;
        ArrayList<Booking> bookingList = new ArrayList<>();
        String SQLString = "SELECT * "
                + "FROM bookings "
                + "ORDER BY booking_id DESC";
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
            System.out.println("Fail in BookingMapper.getBookingsFromDB()");
            System.out.println(e.getMessage());
        }
        finally {    // SKAL køres efter try/catch statement
            try {
                statement.close();  // Vi beder eksplicit om at lukke/release vores statement, hvormed vores ResultSet også bliver lukket.
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper.getBookingsFromDB()");
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

        String SQLString = "SELECT booking_id_seq.nextval "
                + "FROM sys.dual";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                nextBookingId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper.getNewBookingId()");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper.getNewBookingId()");
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
            System.out.println("Fail in BookingMapper.addBooking()");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper.addBooking()");
                System.out.println(e.getMessage());
            }
        }
        return bookingAdded == newBookingList.size();   // hvis antallet af rows UPDATED(/deleted/created) passer med størrelsen på listen af Bookings, ved vi at alle bookings er blevet tilføjet og vi returnerer TRUE.
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
            System.out.println("Fail in BookingMapper.getGuestsInRoom()");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper.getGuestsInRoom()");
                System.out.println(e.getMessage());
            }
        }
        return roomGuestList;
    }

    /**
     * Persisterer ændringer til booking(s) i databasen.
     * - Gælder ikke tilføjelse/fjernelse af gæster, som håndteres i BookingDetailMapper.addGuestToRoom().
     *
     * Metoden benyttes ikke pt., da vi ikke blev færdige med at implementere denne user story.
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
            System.out.println("Fail in BookingMapper.updateBookingDB()");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper.updateBookingDB()");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == updateBookingList.size();
    }

    /**
     * Henter alle bookinger gæsten sover på (ikke dem han er ejer af). 
     * Bruges til at checke at gæsten ikke bookes til flere værelser i samme tidsrum.
     *
     * @param guest         Guest for hvem Bookings ønskes.
     * @param con           Forbindelse til databasen.
     * @return              Liste med Bookings gæsten findes på.
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
            System.out.println("Fail in BookingMapper.getGuestBookingsFromDB()");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper.getGuestBookingsFromDB()");
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
        String SQLString1 = "DELETE FROM booking_details " // først slettes eventuelle gæster på bookingen
                + "WHERE booking_id = ?";
        String SQLString2 = "DELETE FROM bookings " // herefter slettes selve bookingen
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
            System.out.println("Fail in BookingMapper.deleteBookingFromDB()");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement1.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper.deleteBookingFromDB()");
                System.out.println(e.getMessage());
            }
        }
        return rowsDeleted == deleteBookingsList.size();
    }

    /**
     * Enumerationen bruges når vi indsætter en booking-log i databasen til at angive,
     * hvilken type booking-log der er tale om: 
     * Ny booking, ændre booking, slet booking, eller tilføj Guest til Booking.
     */
    public enum ActionType {

        CREATE(1), UPDATE(2), DELETE(3), ADDED_GUEST(4);
        private int actionType;

        private ActionType(int state) {
            this.actionType = state;
        }
    }

    /**
     * Persisterer booking-log i databasen (BOOKING_LOG-tabel). 
     * Bruges hver gang vi kalder en metode, som ændrer(insert/update/delete) en Booking i databasen.
     * - tilsvarende log-metode for ændring af Guest er GuestMapper.log().
     *
     * @param bookingId     bookingId for booking som er blevet oprettet/ændret/slettet.
     * @param action        Typen af ændring som er foretaget (opret/ændre/slet) i form af enumeration ActionType.
     * @param con           Forbindelse til databasen.
     */
    public static void log(int bookingId, ActionType action, Connection con) {
        String SQLString = "INSERT INTO booking_log (id, action, booking_id, logdate, content) "                                            //laver ny log for ændret booking
                + "SELECT booking_log_id_seq.NEXTVAL, ?, ?, CURRENT_TIMESTAMP(3), SYS.DBMS_XMLGEN.GETXML('SELECT "                          //henter næste unikke ID og indsætter det sammen med actiontype og bookingID på '?'. CURRENT_TIMESTAMP(3) giver os dato + tid med præcision 3.
                + "(SELECT LISTAGG(CONCAT(CONCAT(g.first_name, '' ''), g.last_name), '','') WITHIN GROUP (ORDER BY 1) AS Fullname "         //henter de gæster som bor på bookingen med fornavn og efternavn aggregeret som en kolonne og gruppere dem som Fullname
                + "FROM guests g JOIN booking_details bd ON bd.guest_id = g.guest_id AND bd.booking_id = " + bookingId + ") AS Guests, "
                + "b.* FROM bookings b WHERE b.booking_id = " + bookingId + "') XMLSTR FROM DUAL";                                          //Vi henter data fra den table vi vil logge, som xml da det kan bruges af mange programmer og i mange sammenhænge
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, action.actionType);
            statement.setInt(2, bookingId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper.log()");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper.log()");
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Henter alle booking-logs fra databasen (BOOKING_LOG-tabel).
     * - Bruges til at vise logs i Log-fanen i GUI.
     *
     * @param con       Forbindelse til databasen.
     * @return          Liste med booking-logs fra databasen, som strings.
     */
    public ArrayList<String> getLog(Connection con) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        String SQLString = "SELECT * "
                + "FROM booking_log "
                + "ORDER BY id";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                stringArrayList.add("LogID: " + rs.getInt("id") + " BookingID: " + rs.getInt("booking_id") + " LogDate: " + rs.getTimestamp("logdate") + " Action: " + rs.getInt("action") + " XML: " + rs.getString("content") + "\n");
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in BookingMapper.getLog()");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in BookingMapper.getLog()");
                System.out.println(e.getMessage());
            }
        }
        return stringArrayList;
    }
}
