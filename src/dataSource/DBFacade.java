/*
 * Semester Projekt, Datamatiker 2. semester
 * Gruppe 4: Andreas, Michael og Sebastian
 */
package dataSource;

import domain.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Facade-klasse som styrer kommunikation mellem vores Domain- og DataSource-lag.
 * Sørger for global adgang til domænet og holder styr på vores forbindelse, så
 * vi ikke opretter unødige forbindelser til databasen.
 *
 * @author Sebastian, Michael og Andreas
 */
public class DBFacade {

    private GuestMapper guestMapper;
    private RoomMapper roomMapper;
    private BookingMapper bookingMapper;
    private BookingDetailMapper bookingDetailMapper;
    private Connection con;
    private UnitOfWork unitOfWork = new UnitOfWork();

    private static DBFacade instance;

    /**
     * Private constructor til Singleton.
     */
    private DBFacade() {
        guestMapper = new GuestMapper();
        roomMapper = new RoomMapper();
        bookingMapper = new BookingMapper();
        bookingDetailMapper = new BookingDetailMapper();
        
        con = null;
    }

    /**
     * Public static metode, som er vores Singletons ny constructor.
     *
     * @return      Instans af DBFacade, hvis den ikke allerede er instansieret.
     */
    public static DBFacade getInstance() {      
        if (instance == null) {
            instance = new DBFacade();
        }
        return instance;
    }

    /**
     * Opretter forbindelse til databasen, såfremt vi ikke allerede har én åben.
     *
     * @return       Forbindelse til databasen, ny eller allerede eksisterende.
     */
    private Connection openConnection() {
        if (con == null) {
            try {
                con = new DBConnector().getConnection();
            }
            catch (Exception e) {
                System.out.println("Failed to establish connection.");
                System.out.println("Fail in DBFacade.openConnection()");
                System.out.println(e.getMessage());
            }
        }
        return con;
    }

    /**
     * Lukker forbindelse til databasen.
     *
     * @param con        Forbindelse til databasen.
     */
    private void closeConnection(Connection con) {
        try {
            con.close();
        }
        catch (SQLException e) {
            System.out.println("Error in closing connection.");
            System.out.println("Fail in DBFacade.closeConnection()");
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Starter ny transaktion ved at instansiere nyt UnitOfWork objekt.
     */
    public void openNewTransaction() {
        unitOfWork = new UnitOfWork();
    }

    /**
     * Commit'er alle ændringer foretaget inden for transaktionen.
     *
     * @return                  True, hvis alle transaktionens ændringer går igennem, ellers returneres FALSE og alle ændringer bliver rollback'et.
     * @throws SQLException     Kastes videre til Control, som catch'er den.
     */
    public boolean commitTransaction() throws SQLException {
        boolean commitSuccess = false;
        if (unitOfWork != null) {
            con = null;
            try {
                con = openConnection();
                commitSuccess = unitOfWork.commitTransaction(con);
            }
            finally {
                closeConnection(con);
            }
            unitOfWork = null;      // unitOfWork sættes til garbage-collection, da vi ikke længere skal bruge den og vores transaktion er hermed afsluttet.
        }
        return commitSuccess;
    }

    /**
     * Henter alle rum fra databasen.
     *
     * @return       Liste med alle Rooms i databasen.
     */
    public ArrayList<Room> getRoomFromDB() {
        con = null;
        ArrayList<Room> tempRoomList;
        try {
            con = openConnection();
            tempRoomList = roomMapper.getRoomsFromDB(con);
        }
        finally {
            closeConnection(con);
        }
        return tempRoomList;
    }

    /**
     * Henter gæst med specifikt guestId fra databasen.
     *
     * @param guestId        GuestId på gæst som ønskes.
     * @return               Guest-objekt med ønskede bookingId.
     */
    public Guest getGuestFromID(int guestId) {
        Guest guest;
        con = null;
        try {
            con = openConnection();
            guest = guestMapper.getGuestFromID(guestId, con);
        }
        finally {
            closeConnection(con);
        }
        return guest;
    }

    /**
     * Henter alle gæster fra databasen.
     *
     * @return       Liste med alle Guests i databasen.
     */
    public ArrayList<Guest> getGuestsFromDB() {
        con = null;
        ArrayList<Guest> tempGuestList;
        try {
            con = openConnection();
            tempGuestList = guestMapper.getGuestsFromDB(con);
        }
        finally {
            closeConnection(con);
        }
        return tempGuestList;
    }

    /**
     * Henter alle bookinger fra databasen.
     *
     * @return       Liste med alle Bookings i databasen.
     */
    public ArrayList<Booking> getBookingsFromDB() {
        con = null;
        ArrayList<Booking> tempBookingList;
        try {
            con = openConnection();
            tempBookingList = bookingMapper.getBookingsFromDB(con);
        }
        finally {
            closeConnection(con);
        }
        return tempBookingList;
    }

    /**
     * Henter alle booking-logs fra databasen.
     *  
     * @return       Liste med alle Booking-logs som strings.
     */
    public ArrayList<String> getBookingLog() {
        con = null;
        ArrayList<String> tempBookingLogList;
        try {
            con = openConnection();
            tempBookingLogList = bookingMapper.getLog(con);
        }
        finally {
            closeConnection(con);
        }
        return tempBookingLogList;
    }

    /**
     * Henter alle guest-logs fra databasen.
     * 
     * @return       Liste med alle Guest-logs som strings.
     */
    public ArrayList<String> getGuestLog() {
        con = null;
        ArrayList<String> tempGuestLogList;
        try {
            con = openConnection();
            tempGuestLogList = guestMapper.getLog(con);
        }
        finally {
            closeConnection(con);
        }
        return tempGuestLogList;
    }

    /**
     * Henter alle bookinger gæsten sover på (ikke dem han er ejer af). 
     * - Bruges til at checke at gæsten ikke bookes til flere værelser i samme tidsrum.
     *
     * @param guest      Guest for hvem Bookings ønskes.
     * @return           Liste med Bookings gæsten findes på.
     */
    public ArrayList<Booking> getGuestBookingsFromDB(Guest guest) {
        con = null;
        ArrayList<Booking> tempBookingList;
        try {
            con = openConnection();
            tempBookingList = bookingMapper.getGuestBookingsFromDB(guest, con);
        }
        finally {
            closeConnection(con);
        }
        return tempBookingList;
    }

     /**
      * Henter gæster som bor på booking/room, til visning i bookingoversigt.
      *
      * @param booking       Bookingen vi ønsker gæsterne for.
      * @return              Liste med gæster fra bookingen.
      */
    public ArrayList<Guest> getBookingDetailsFromDB(Booking booking) {
        con = null;
        ArrayList<Guest> roomGuestList;
        try {
            con = openConnection();
            roomGuestList = bookingDetailMapper.getBookingDetailsFromDB(booking, con);
        }
        finally {
            closeConnection(con);
        }
        return roomGuestList;
    }

    /**
     * Søger efter fornavn, efternavn, eller begge dele i databasen. Sammenligning sker i upper case.
     * - Bruges til at søge efter en specifik gæst i GUI.
     * 
     * @param status        Angiver om der skal søges på fornavn, efternavn eller begge.
     * @param names         Navn(e) der skal søges efter.
     * @return              Liste af Guests som matcher søgekriterierne.
     */
    public ArrayList<Guest> searchForGuestDB(String status, String... names) {
        con = null;
        ArrayList<Guest> guestsFound;
        try {
            con = openConnection();
            guestsFound = guestMapper.searchForGuestDB(status, con, names);
        }
        finally {
            closeConnection(con);
        }
        return guestsFound;
    }

    /**
     * Tilføjer Boom til liste af Rooms som skal ændres. 
     * - Bruges ikke pt.
     * 
     * TODO slet? Benyttes ikke
     * 
     * @param room      Room som skal ændres.
     * @return          TRUE, hvis Room ikke allerede er tilføjet.
     */
    public boolean updateRoomDB(Room room) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.updateRoomDB(room);
    }

    /**
     * Tilføjer Booking til liste af Bookings som skal ændres. 
     * - Bruges ikke pt.
     * 
     * TODO slet? Benyttes ikke
     * 
     * @param booking       Booking som skal ændres.
     * @return              TRUE, hvis Booking ikke allerede er tilføjet.
     */
    public boolean updateBookingDB(Booking booking) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.updateBookingDB(booking);
    }

    /**
     * Tilføjer BookingDetail til liste af BookingDetails som skal ændres.
     * - Bruges ikke pt.
     * 
     * TODO slet? benyttes ikke
     *
     * @param bookingDetail     BookingDetail som skal ændres.
     * @return                  TRUE, hvis Booking ikke allerede er tilføjet.
     */
    public boolean updateGuestsInRoomDB(BookingDetail bookingDetail) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.updateGuestsInRoomDB(bookingDetail);
    }

    /**
     * Tilføjer booking til liste af bookings som skal oprettes.
     * - Bruges ved oprettelse af ny booking.
     *
     * @param booking       Booking som skal oprettes.
     * @return              TRUE, hvis bookingen ikke allerede er tilføjet.
     */
    public boolean createBooking(Booking booking) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.createBooking(booking);
    }

    /**
     * Tilføjer BookingDetail til liste af BookingDetails som skal oprettes.
     * - Bruges til at tilføje en gæst til en booking/værelse.
     *
     * @param bookingDetail     BookingDetail som skal oprettes.
     * @return                  TRUE, hvis BookingDetail ikke allerede er tilføjet.
     */
    public boolean addGuestToRoom(BookingDetail bookingDetail) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.addGuestToRoom(bookingDetail);
    }

    /**
     * Tilføjer gæst til liste af gæster som skal oprettes.
     * - Bruges ved oprettelse af ny gæst.
     *
     * @param guest     Guest-objekter som skal persisteres.
     * @return          TRUE, hvis gæsten ikke allerede er tilføjet.
     */
    public boolean createGuest(Guest guest) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.createGuest(guest);
    }

    /**
     * Tilføjer gæst til liste af gæster som skal ændres.
     * - Bruges ved ændring af gæstens informationer.
     *
     * @param guest     Guest-objekter som skal opdateres.
     * @return          TRUE, hvis gæsten ikke allerede er tilføjet.
     */
    public boolean updateGuestDB(Guest guest) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.updateGuestDB(guest);
    }

    /**
     * Tilføjer bookingId for Booking som skal slettes, til liste af Bookings som skal slettes.
     *
     * @param bookingId     BookingID for booking som skal slettes.
     * @return              TRUE, hvis Booking ikke allerede er tilføjet.
     */
    public boolean deleteBookingFromDB(int bookingId) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.deleteBookingFromDB(bookingId);
    }

    /**
     * Fjerner Booking fra listen af Bookings som skal oprettes.
     * - Bruges hvis brugeren fortryder oprettelse af Booking, før den er persisteret.
     *
     * @param booking       Booking som ønskes fjernet.
     * @return              TRUE, hvis Booking findes og bliver fjernet fra listen.
     */
    public boolean undoNewBooking(Booking booking) {
        if (unitOfWork == null) {
            openNewTransaction();
        }
        return unitOfWork.undoNewBooking(booking);
    }

    /**
     * Bruges af Control.saveBooking() til at hente liste over nuværende Bookings som ønskes tilføjet,
 til at checke at det rum som man har tilføjet til oprettelse gennem createBooking(), ikke mens transaktionen laves er blevet gemt af en anden bruger,
 da vi ikke endnu har commit'et bookingen.
     *  
     * @return      Liste af bookings som er tilføjet til listen af Bookings som skal oprettes.
     */
    public ArrayList<Booking> getBookingsFromUOF() {
        return unitOfWork.getBookings();
    }

    /**
     * Bruges af Control.saveBooking() til at fjerne en Booking, såfremt vi finder,
     * at en anden bruger har booked samme rum, før vi fik persisteret vores nyoprettede booking.
     *  
     * @param booking      Booking som skal fjernes.
     */
    public void removeBookingFromUOF(Booking booking) {
        unitOfWork.removeBookingFromUOF(booking);
    }

    /**
     * Fjerner alle gæster som er tilføjet til en Booking, men som endnu ikke er persisteret.
     * - Bruges til at clear listen af gæster man har tilføjet til en Booking, men endnu ikke gemt, i GUI.
     */
    public void clearNewBookingDetails() {
        unitOfWork.clearNewBookingDetails();
    }
}
