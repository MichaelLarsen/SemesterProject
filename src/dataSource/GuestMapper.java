/*
 * Semester Projekt, Datamatiker 2. semester
 * Gruppe 4: Andreas, Michael og Sebastian
 */
package dataSource;

import domain.Guest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * GuestMapper klassen håndterer persistering af data mellem programmet og
 * databasens GUESTS tabel.
 * 
 * @author Sebastian, Michael og Andreas
 */
public class GuestMapper {

    /**
     * Metoden henter en guest fra databasen via guestId.
     *
     * @param guestId       ID for den gæst man ønsker hentet ud.
     * @param con           Forbindelse til databasen.
     * @return guest        Guest-objekt.
     */
    public Guest getGuestFromID(int guestId, Connection con) {
        Guest guest = null;
        String SQLString = "SELECT * "
                + "FROM guests "
                + "WHERE guest_id = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, guestId);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                guest = new Guest(rs);      // Vi sender vores ResultSet direkte videre til vores constructor
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper.getGuestFromID()");
            System.out.println(e.getMessage());
        }
        finally     // SKAL køres efter try/catch statement
        {
            try {
                statement.close();      // Vi beder eksplicit om at lukke/release vores statement, hvormed vores ResultSet også bliver lukket.
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper.getGuestFromID()");
                System.out.println(e.getMessage());
            }
        }
        return guest;
    }

    /**
     * Persisterer gæst i databasen.
     * - Bruges ved oprettelse af ny gæst.
     *
     * @param newGuestList      Liste med Guest-objekter som skal persisteres. Kun én gæst af gangen pt.
     * @param con               Forbindelse til databasen.
     * @return                  TRUE, hvis INSERTs lykkes. Rækker tilføjet == antallet af Guests i liste.
     */
    public boolean createGuest(ArrayList<Guest> newGuestList, Connection con) {
        int guestsCreated = 0;
        String SQLString = "INSERT INTO guests VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                guestsCreated += statement.executeUpdate();      // guestCreated bliver = newGuestList.size(), hvis Update går igennem
                log(newId, ActionType.CREATE, con);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper.createGuest()");
            System.out.println(e.getMessage());
        }
        finally
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper.createGuest()");
                System.out.println(e.getMessage());
            }
        }
        return guestsCreated == newGuestList.size();
    }

    /**
     * Henter næste unikke guestId i databasen.
     * - Bruges til oprettelse af ny gæst. Hentes først når vi persisterer den nye gæst.
     *
     * @param con   Forbindelse til databasen.
     * @return      Næste guestId.
     */
    public int getNewGuestsId(Connection con) {
        int nextGuestId = 0;
        String SQLString = "SELECT guest_id_seq.NEXTVAL FROM SYS.DUAL";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                nextGuestId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper.getNewGuestId()");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper.getNewGuestId()");
                System.out.println(e.getMessage());
            }
        }
        return nextGuestId;
    }

    /**
     * Henter alle gæster fra databasen (GUESTS-tabel).
     * - Bruges til at vise tabel med gæster i GUI.
     *  
     * @param con   Forbindelse til databasen.
     * @return      Liste med alle gæster fra databasen.
     */
    public ArrayList<Guest> getGuestsFromDB(Connection con) {
        Guest guest = null;
        ArrayList<Guest> guestList = new ArrayList<>();
        String SQLString = "SELECT * "
                + "FROM guests "
                + "ORDER BY guest_id DESC";
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
            System.out.println("Fail in GuestMapper.getGuestsFromDB()");
            System.out.println(e.getMessage());
        }
        finally
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper.getGuestsFromDB()");
                System.out.println(e.getMessage());
            }
        }
        return guestList;
    }

    /**
     * Persisterer ændringer lavet til en gæst.
     * - Bruges i Gæst-fanen i GUI, til at lave ændringer i en gæsts information.
     * 
     * @param dirtyGuestList    Liste med Guest-objekter som er blevet ændret og skal persisteres. Kun 1 gæst af gangen pt.
     * @param con               Forbindelse til databasen.
     * @return                  TRUE, hvis UPDATEs lykkes. Rækker tilføjet == antallet af Guests i liste.
     */
    public boolean updateGuestDB(ArrayList<Guest> dirtyGuestList, Connection con) {
        int rowsUpdated = 0;
        String SQLString = "UPDATE guests "
                + "SET first_name = ?, last_name = ?, street = ?, zipcode = ?, city = ?, country = ?, email = ?, phone_1 = ?, phone_2 = ? "
                + "WHERE guest_id = ?";
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
                rowsUpdated += statement.executeUpdate();
                log(guest.getGuestId(), ActionType.UPDATE, con);
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper.UpdateGuestDB()");
            System.out.println(e.getMessage());
        }
        finally
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper.UpdateGuestDB()");
                System.out.println(e.getMessage());
            }
        }
        return rowsUpdated == dirtyGuestList.size(); //hvis dette passer returneres true, ellers false
    }

    /**
     * Søger efter fornavn, efternavn, eller begge dele i databasen. Sammenligning sker i upper case.
     * - Bruges til at søge efter en specifik gæst i GUI.
     * 
     * @param status        Angiver om der skal søges på fornavn, efternavn eller begge.
     * @param con           Forbindelse til databasen.
     * @param names         Navn(e) der skal søges efter.
     * @return              Liste af Guests som matcher søgekriterierne.
     */
    public ArrayList<Guest> searchForGuestDB(String status, Connection con, String... names) {
        Guest guest = null;
        String SQLString = "";
        ArrayList<Guest> guestList = new ArrayList<>();
        if (status.equals("both")) {
            SQLString = "SELECT * "
                    + "FROM guests "
                    + "WHERE UPPER(first_name) LIKE UPPER(?) AND UPPER(last_name) LIKE UPPER(?)";
        }
        if (status.equals("firstName")) {
            SQLString = "SELECT * "
                    + "FROM guests "
                    + "WHERE UPPER(first_name) LIKE UPPER(?)";
        }
        if (status.equals("lastName")) {
            SQLString = "SELECT * "
                    + "FROM guests "
                    + "WHERE UPPER(last_name) LIKE UPPER(?)";
        }
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            if (status.equals("both")) {
                statement.setString(1, "%" + names[0] + "%");       // Her indsættes "%navn%" på "?", for at opnå den ønskede søgning "WHERE first_name LIKE '%navn%'".
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
            System.out.println("Fail in GuestMapper.searchForGuestDB()");
            System.out.println(e.getMessage());
        }
        finally
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper.searchForGuestDB()");
                System.out.println(e.getMessage());
            }
        }
        return guestList;
    }

    /**
     * Enumerationen bruges når vi indsætter en guest-log i databasen til at angive,
     * hvilken type guest-log der er tale om: 
     * Ny gæst eller ændre gæst.
     */
    public enum ActionType {

        CREATE(1), UPDATE(2);
        private int actionType;

        private ActionType(int state) {
            this.actionType = state;
        }
    }

    /**
     * Persisterer guest-log i databasen (GUEST_LOG-tabel). 
     * Bruges hver gang vi kalder en metode, som ændrer(insert/update) en gæst i databasen.
     * - tilsvarende log-metode for ændring af Booking er BookingMapper.log().
     *
     * @param guest_id      guestId for gæst som er blevet oprettet/ændret.
     * @param action        Typen af ændring som er foretaget (opret/ændre) i form af enumeration ActionType.
     * @param con           Forbindelse til databasen.
     */
    public static void log(int guest_id, ActionType action, Connection con) {
        String SQLString = "INSERT INTO guest_log(id, action, guest_id, logdate, content) "                                        //laver ny log for ændret gæst
                + "SELECT guest_log_id_seq.NEXTVAL, ?, ?, CURRENT_TIMESTAMP(3), SYS.DBMS_XMLGEN.GETXML('SELECT * FROM guests "     //henter næste unikke ID og indsætter det sammen med actiontype og guestId på '?'. CURRENT_TIMESTAMP(3) giver os dato + tid med præcision 3.
                + "WHERE guest_id = " + guest_id + "') XMLSTR FROM DUAL";                                                          //Vi henter data fra den table vi vil logge, som xml da det kan bruges af mange programmer og i mange sammenhænge
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, action.actionType);
            statement.setInt(2, guest_id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper.log()");
            System.out.println(e.getMessage());
        }
        finally
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper.log()");
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Henter alle guest-logs fra databasen (GUEST_LOG-tabel).
     * - Bruges til at vise logs i Log-fanen i GUI.
     *
     * @param con       Forbindelse til databasen.
     * @return          Liste med guest-logs fra databasen, som strings.
     */
    public ArrayList<String> getLog(Connection con) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        String SQLString = "SELECT * "
                + "FROM guest_log "
                + "ORDER BY id";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                stringArrayList.add("LogID: " + rs.getInt("id") + " GuestID: " + rs.getInt("guest_id") + " Logdate: " + rs.getDate("logdate") + " Action: " + rs.getInt("action") + " XML: " + rs.getString("content") + "\n");
            }
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper.getLog()");
            System.out.println(e.getMessage());
        }
        finally
        {
            try {
                statement.close();
            }
            catch (SQLException e) {
                System.out.println("Fail in GuestMapper.getLog()");
                System.out.println(e.getMessage());
            }
        }
        return stringArrayList;
    }
}
