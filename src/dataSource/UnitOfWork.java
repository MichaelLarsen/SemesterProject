/*
 * Semester Projekt, Datamatiker 2. semester
 * Gruppe 4: Andreas, Michael og Sebastian
 */
package dataSource;

import domain.Booking;
import domain.Guest;
import domain.Room;
import domain.BookingDetail;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * UnitOfWork klassen holder styr på vores transaktioner og sørger for at ændringer rulles tilbage,
 * hvis ikke alle transaktionens ændringer lykkes.
 *
 * @author Sebastian, Michael og Andreas
 */
public class UnitOfWork {

    private ArrayList<Booking> newBookingList;
    private ArrayList<Booking> dirtyBookingList;
    private ArrayList<Room> dirtyRoomList;
    private ArrayList<BookingDetail> newBookingDetailList;
    private ArrayList<BookingDetail> dirtyBookingDetailList;
    private ArrayList<Guest> newGuestList;
    private ArrayList<Guest> dirtyGuestList;
    private ArrayList<Integer> deleteBookingsList;

    /**
     * Constructor som instansierer vores lister.
     */
    public UnitOfWork() {
        newBookingList = new ArrayList<>();
        dirtyRoomList = new ArrayList<>();
        newBookingDetailList = new ArrayList<>();
        dirtyBookingList = new ArrayList<>();
        dirtyBookingDetailList = new ArrayList<>();
        newGuestList = new ArrayList<>();
        dirtyGuestList = new ArrayList<>();
        deleteBookingsList = new ArrayList<>();
    }

    /**
     * Commit'er alle ændringer foretaget inden for transaktionen.
     *
     * @param con               Forbindelse til databasen.
     * @return                  True, hvis alle transaktionens ændringer går igennem, ellers returneres FALSE og alle ændringer bliver rollback'et.
     * @throws SQLException     Kastes videre til DBFacade og dernæst Control, som catch'er den.
     */
    public boolean commitTransaction(Connection con) throws SQLException {
        boolean commitSuccess = true;
        con.setAutoCommit(false);       // AutoCommit false så ændringerne først bliver commit'et når vi ved at alle ændringer er gået igennem.

        BookingMapper bookingMapper = new BookingMapper();
        GuestMapper guestMapper = new GuestMapper();
        RoomMapper roomMapper = new RoomMapper();
        BookingDetailMapper bookingDetailMapper = new BookingDetailMapper();

        if (!newGuestList.isEmpty()) {
            commitSuccess = commitSuccess && guestMapper.createGuest(newGuestList, con);        // Denne syntax sørger for at commitSuccess hele tiden kun bliver TRUE, når alle hidtidige ændringer er gået igennem. Bare en fejler bliver den FALSE.
        }
        if (!dirtyGuestList.isEmpty()) {
            commitSuccess = commitSuccess && guestMapper.updateGuestDB(dirtyGuestList, con);
        }
        if (!newBookingList.isEmpty()) {
            commitSuccess = commitSuccess && bookingMapper.addBooking(newBookingList, con);
        }
        if (!dirtyRoomList.isEmpty()) {
            commitSuccess = commitSuccess && roomMapper.updateRoomDB(dirtyRoomList, con);
        }
        if (!newBookingDetailList.isEmpty()) {
            commitSuccess = commitSuccess && bookingDetailMapper.addGuestToRoom(newBookingDetailList, con);
        }
        if (!dirtyBookingList.isEmpty()) {
            commitSuccess = commitSuccess && bookingMapper.updateBooking(dirtyBookingList, con);
        }
        if (!dirtyBookingDetailList.isEmpty()) {
            commitSuccess = commitSuccess && bookingDetailMapper.updateBookingDetail(dirtyBookingDetailList, con);
        }
        if (!deleteBookingsList.isEmpty()) {
            commitSuccess = commitSuccess && bookingMapper.deleteBookingFromDB(deleteBookingsList, con);
        }
        if (!commitSuccess) {
            con.rollback();
            System.out.println("Fejl ved commitTransaction - rollback!");
            System.out.println("Fail in UnitOfWork.commitTransaction()");
        }
        else {
            con.commit();
            System.out.println("Transactioner er commited!");
        }
        return commitSuccess;
    }
    
    /**
     * Tilføjer gæst til liste af gæster som skal oprettes.
     * - Bruges ved oprettelse af ny gæst.
     *
     * @param guest             Guest-objekter som skal persisteres.
     * @return                  TRUE, hvis gæsten ikke allerede er tilføjet.
     */
    public boolean createGuest(Guest guest) {
        boolean createGuestSuccess = false;
        if (!newGuestList.contains(guest)) {
            newGuestList.add(guest);
            createGuestSuccess = true;
        }
        return createGuestSuccess;
    }

    /**
     * Tilføjer gæst til liste af gæster som skal ændres.
     * - Bruges ved ændring af gæstens informationer.
     *
     * @param guest             Guest-objekter som skal opdateres.
     * @return                  TRUE, hvis gæsten ikke allerede er tilføjet.
     */
    public boolean updateGuestDB(Guest guest) {
        boolean updateGuestSuccess = false;
        if (!dirtyGuestList.contains(guest)) {
            dirtyGuestList.add(guest);
            updateGuestSuccess = true;
        }
        return updateGuestSuccess;
    }

    /**
     * Tilføjer booking til liste af bookings som skal oprettes.
     * - Bruges ved oprettelse af ny booking.
     *
     * @param booking       Booking som skal oprettes.
     * @return              TRUE, hvis bookingen ikke allerede er tilføjet.
     */
    public boolean bookRoom(Booking booking) {
        boolean bookingSuccess = false;
        if (!newBookingList.contains(booking)) {
            newBookingList.add(booking);
            bookingSuccess = true;
        }
        return bookingSuccess;
    }

    /**
     * Tilføjer BookingDetail til liste af BookingDetails som skal oprettes.
     * - Bruges til at tilføje en gæst til en booking/værelse.
     *
     * @param bookingDetail     BookingDetail som skal oprettes.
     * @return                  TRUE, hvis BookingDetail ikke allerede er tilføjet.
     */
    public boolean addGuestToRoom(BookingDetail bookingDetail) {
        boolean addGuestSuccess = false;
        if (!newBookingDetailList.contains(bookingDetail)) {
            newBookingDetailList.add(bookingDetail);
            addGuestSuccess = true;
        }
        return addGuestSuccess;
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
        boolean updateSuccess = false;
        if (!dirtyRoomList.contains(room)) {
            dirtyRoomList.add(room);
            updateSuccess = true;
        }
        return updateSuccess;
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
    boolean updateBookingDB(Booking booking) {
        boolean updateSuccess = false;
        if (!dirtyBookingList.contains(booking)) {
            dirtyBookingList.add(booking);
            updateSuccess = true;
        }
        return updateSuccess;
    }

    /**
     * Tilføjer BookingDetail til liste af BookingDetails som skal ændres. 
     * - Bruges ikke pt.
     * 
     * 
     * TODO slet? benyttes ikke
     *
     * @param bookingDetail     BookingDetail som skal opdateres.
     * @return                  TRUE, hvis BookingDetail ikke allerede er tilføjet.
     */
    public boolean updateGuestsInRoomDB(BookingDetail bookingDetail) {
        boolean updateSuccess = false;
        if (!dirtyBookingDetailList.contains(bookingDetail)) {
            dirtyBookingDetailList.add(bookingDetail);
            updateSuccess = true;
        }
        return updateSuccess;
    }
    
    /**
     * Tilføjer bookingId for Booking som skal slettes, til liste af Bookings som skal slettes.
     *
     * @param bookingId     BookingID for booking som skal slettes.
     * @return              TRUE, hvis Booking ikke allerede er tilføjet.
     */
    public boolean deleteBookingFromDB(int bookingId) {
        boolean deleteSuccess = false;
        if (!deleteBookingsList.contains(bookingId)) {
            deleteBookingsList.add(bookingId);
            deleteSuccess = true;
        }
        return deleteSuccess;
    }

    /**
     * Fjerner Booking fra listen af Bookings som skal oprettes.
     * - Bruges hvis brugeren fortryder oprettelse af Booking, før den er persisteret.
     *
     * @param booking       Booking som ønskes fjernet.
     * @return              TRUE, hvis Booking findes og bliver fjernet fra listen.
     */
    public boolean undoNewBooking(Booking booking) {
        boolean undoSuccess = false;
        int i = 0;
        while (undoSuccess == false && i < newBookingList.size()) {
            if (booking.getBookingId() == newBookingList.get(i).getBookingId()) {
                newBookingList.remove(i);
                undoSuccess = true;
            }
            i++;
        }
        return undoSuccess;
    }

    /**
     * Bruges af Control.saveBooking() til at hente liste over nuværende Bookings som ønskes tilføjet,
     * til at checke at det rum som man har tilføjet til oprettelse gennem bookRoom(), ikke mens transaktionen laves er blevet gemt af en anden bruger,
     * da vi ikke endnu har commit'et bookingen.
     *  
     * @return      Liste af bookings som er tilføjet til listen af Bookings som skal oprettes.
     */
    public ArrayList<Booking> getBookings() {
        return newBookingList;
    }

    /**
     * Bruges af Control.saveBooking() til at fjerne en Booking, såfremt vi finder,
     * at en anden bruger har booked samme rum, før vi fik persisteret vores nyoprettede booking.
     *  
     * @param booking      Booking som skal fjernes.
     */
    public void removeBookingFromUOF(Booking booking) {
        newBookingList.remove(booking);
    }

    /**
     * Fjerner alle gæster som er tilføjet til en Booking, men som endnu ikke er persisteret.
     * - Bruges til at clear listen af gæster man har tilføjet til en Booking, men endnu ikke gemt, i GUI.
     */
    public void clearNewBookingDetails() {
        newBookingDetailList.clear();
    }
}
