/*
 * Semester Projekt, Datamatiker 2. semester
 * Gruppe 4: Andreas, Michael og Sebastian
 */
package domain;

import dataSource.DBFacade;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Tager sig af hovedelen af den logiske del af koden og håndterer kommunikation
 * til de andre lag.
 *
 * @author Sebastian, Michael og Andreas
 */
public class Control {

    private DBFacade DBFacade;
    private ArrayList<Room> roomList;
    private ArrayList<Booking> bookingList;
    private ArrayList<Booking> notSavedBookings;

    public Control() {
        DBFacade = DBFacade.getInstance();
        roomList = getRoomsFromDB();
    }

    /**
     * Commit'er alle ændringer foretaget inden for transaktionen.
     *
     * @return      True, hvis alle transaktionens ændringer går igennem, ellers returneres FALSE og alle ændringer bliver rollback'et.
     */
    public boolean commitTransaction() {
        boolean commitSuccess = false;
        try {
            commitSuccess = DBFacade.commitTransaction();
        }
        catch (SQLException e) {
            System.out.println("Fejl ved commit af transaktion!");
            System.out.println("Fail in Control.commitTransaction()");
            System.out.println(e.getMessage());
        }
        return commitSuccess;
    }

    /**
     * Henter alle bookinger fra databasen.
     *
     * @return       Liste med alle Bookings i databasen.
     */
    public ArrayList<Booking> getBookingsFromDB() {
        bookingList = DBFacade.getBookingsFromDB();
        return bookingList;
    }

    /**
     * Returnerer liste over Bookings uden at opdatere den i databasen.
     * - Bruges til til at vise booking-details i booking-oversigt uden at gå i databasen unødigt.
     *
     * @return      Liste af Bookings, som den så ud sidst den blev opdateret.
     */
    public ArrayList<Booking> getBookings() {
        return bookingList;
    }
    
    /**
     * Tilføjer bookingId for Booking som skal slettes, til liste af Bookings som skal slettes.
     *
     * @param bookingId     BookingID for booking som skal slettes.
     * @return              TRUE, hvis Booking ikke allerede er tilføjet.
     */
    public boolean deleteBookingFromDB(int bookingId) {
        return DBFacade.deleteBookingFromDB(bookingId);
    }
    
    /**
     * Henter alle booking-logs fra databasen.
     *  
     * @return       Liste med alle Booking-logs som strings.
     */
    public ArrayList<String> getBookingLog() {
        return DBFacade.getBookingLog();
    }
    
    /**
     * Tilføjer booking til liste af bookings som skal oprettes.
     * Checker at rummet ikke allerede er booked i den ønskede periode. 
     * - Bruges ved oprettelse af ny booking.
     *
     * @param room          Rum som ønskes booked.
     * @param guest         Gæst som ejer bookingen.
     * @param checkIn       Check-in dato.
     * @param checkOut      Check-out dato.
     * @return              Booking som er oprettet med pågældende gæst og rum.
     */
    public Booking bookRoom(Room room, Guest guest, Date checkIn, Date checkOut) {
        boolean doubleBooking;
        bookingList = DBFacade.getBookingsFromDB();                                                     // Vi opdaterer listen af bookings fra databasen, så checkForDoubleBooking() har de nyeste data før den kører.
        Booking newBooking = null;
        doubleBooking = checkForDoubleBooking(room, checkIn, checkOut);                                 // Checker at rummet er ledigt i perioden. FALSE hvis rummet er ledigt.
        if (doubleBooking == false) {
            newBooking = new Booking(guest.getGuestId(), room.getRoomNo(), "", checkIn, checkOut);      // Ny booking laves.
            DBFacade.bookRoom(newBooking);                                                              // Booking tilføjes til nye bookings som skal persisteres.
        }
        return newBooking;
    }
    
    /**
     * Fjerner Booking fra listen af Bookings som skal oprettes.
     * - Bruges hvis brugeren fortryder oprettelse af Booking, før den er persisteret.
     *
     * @param booking       Booking som ønskes fjernet.
     * @return              TRUE, hvis Booking findes og bliver fjernet fra listen.
     */
    public boolean undoNewBooking(Booking booking) {
        boolean undoSuccess = DBFacade.undoNewBooking(booking);
        return undoSuccess;
    }
    
    /**
     * Commit'er de nye bookings brugeren har oprettet, efter at have checket 
     * for doubleBookings. 
     * Control.bookRoom() har allerede checket for doubleBooking, men vi checker 
     * også her, i fald en anden bruger har booked samme rum imellemtiden.
     * 
     * @return      TRUE, hvis commit lykkes og bookings bliver oprettet.
     */
    public boolean saveBooking() {
        boolean commitSuccess = false;
        boolean doubleBooking = false;
        bookingList = DBFacade.getBookingsFromDB();
        notSavedBookings = new ArrayList<>();
        ArrayList<Booking> bookingsForSaving = DBFacade.getBookingsFromUOF();
        ArrayList<Booking> bookingsForSavingCopy = new ArrayList<>(bookingsForSaving);
        Room newRoom = null;
        //ConcurrentModificationException: man kan ikke kører løkken igennem og ændre på listen samtidig. 
        //Derfor har vi lavet en kopi af listen
        for (Booking booking : bookingsForSavingCopy) {
            for (int i = 0; i < roomList.size(); i++) {
                if (roomList.get(i).getRoomNo() == booking.getRoomNo()) {
                    newRoom = roomList.get(i);
                }
            }
            doubleBooking = checkForDoubleBooking(newRoom, booking.getCheckInDate(), booking.getCheckOutDate());        // Checker at rummet er ledigt i perioden.
            if (doubleBooking) {
                DBFacade.removeBookingFromUOF(booking);     // Sletter rum fra vores UnitOfWork.
                notSavedBookings.add(booking);              // Bruges til at give brugeren besked på hvilke bookings der fejlede.
            }
        }
        commitSuccess = commitTransaction();
        return commitSuccess;
    }

    /**
     * Sender liste af bookings som ikke kunne gemmes til GUI, så brugeren kan
     * informeres.
     *
     * @return      Liste af bookings som fejlede.
     */
    public ArrayList<Booking> getBookingsNotSaved() {
        return notSavedBookings;
    }
    
    /**
      * Henter gæster som bor på booking/room, til visning i bookingoversigt.
      *
      * @param booking       Bookingen vi ønsker gæsterne for.
      * @return              Liste med gæster fra bookingen.
      */
    public ArrayList<Guest> getBookingDetailsFromDB(Booking booking) {
        return DBFacade.getBookingDetailsFromDB(booking);
    }

    /**
     * Checker at gæsten ikke allerede har en booking i perioden. 
     * Laver BookingDetail og tilføjer den til liste af BookingDetails som skal oprettes.
     * - Bruges til at tilføje en gæst til en booking/værelse.
     *
     * @param guest         Gæst som skal tilføjes til booking.
     * @param booking       Booking gæsten skal tilføjes til.
     * @return              TRUE, hvis BookingDetail ikke allerede er tilføjet.
     */
    public boolean addGuestToRoom(Guest guest, Booking booking) {
        boolean addGuestSuccess = false;
        boolean doubleBooking = false;
        ArrayList<Booking> tempBookingList;
        tempBookingList = DBFacade.getGuestBookingsFromDB(guest);                                               // Henter evt. bookings hvori kunden allerede indgår
        if (!tempBookingList.isEmpty()) {                                                                       // Checker om kunden allerede bor på andre bookinger i samme periode
            doubleBooking = checkGuestForDoubleBooking(tempBookingList, booking);
        }
        if (doubleBooking == false) {
            BookingDetail bookingDetail = new BookingDetail(guest.getGuestId(), booking.getBookingId());
            addGuestSuccess = DBFacade.addGuestToRoom(bookingDetail);
        }
        return addGuestSuccess;
    }
    
    public void clearNewBookingDetails() {
        DBFacade.clearNewBookingDetails();
    }
    
    /**
     * Checker om rum er ledigt ved at sammenligne den ønskede periode med alle bookings.
     *
     * @param room
     * @param checkInDate
     * @param checkOutDate
     * @return
     */
    public boolean checkForDoubleBooking(Room room, Date checkInDate, Date checkOutDate) {
        boolean doubleBooking = false;
        int i = 0;
        while (doubleBooking == false && i < bookingList.size()) {
            if ((bookingList.get(i).getRoomNo() == room.getRoomNo())) {
                Date bookingStartDate = bookingList.get(i).getCheckInDate();
                Date bookingEndDate = bookingList.get(i).getCheckOutDate();
                if ((checkInDate.before(bookingStartDate) && checkOutDate.before(bookingStartDate))
                        || (checkInDate.after(bookingEndDate) && checkOutDate.after(bookingEndDate))
                        || checkInDate.equals(bookingEndDate) || checkOutDate.equals(bookingStartDate)) {       // Her checkes alle mulige måder rummet
                    doubleBooking = false;
                }
                else {
                    doubleBooking = true;
                }
            }
            i++;
        }
        return doubleBooking;
    }
    
    /**
     * Man kunne have kundens bookings for en specifik periode direkte i 
     * database via SQL statement, men vi har valgt at gøre det i koden.
     *
     * @param oldBookingList
     * @param newBooking
     * @return 
     */
    private boolean checkGuestForDoubleBooking(ArrayList<Booking> oldBookingList, Booking newBooking) {
        boolean doubleBooking = false;
        Date newCheckInDate = newBooking.getCheckInDate();
        Date newCheckOutDate = newBooking.getCheckOutDate();
        int i = 0;
        while (doubleBooking == false && oldBookingList.size() > i) {
            Date oldCheckInDate = oldBookingList.get(i).getCheckInDate();
            Date oldCheckOutDate = oldBookingList.get(i).getCheckOutDate();
            if ((newCheckInDate.before(oldCheckInDate) && newCheckOutDate.before(oldCheckInDate))
                    || (newCheckInDate.after(oldCheckOutDate) && newCheckOutDate.after(oldCheckOutDate))
                    || newCheckInDate.equals(oldCheckOutDate) || newCheckOutDate.equals(oldCheckInDate)) {
                doubleBooking = false;
            }
            else {
                doubleBooking = true;
            }
            i++;
        }
        return doubleBooking;
    }
    
    public final ArrayList<Room> getRoomsFromDB() {
        roomList = DBFacade.getRoomFromDB();
        return roomList;
    }

    public ArrayList<Room> getRooms() {
        return roomList;
    }
    
    /**
     * Finder ledige rum for perioden.
     * - Bruges til at populerer listen af ledige rum i GUI.
     *
     * @param checkInDate       Check-in dato.
     * @param checkOutDate      Check-out dato.
     * @return                  Liste af ledige rum for perioden.
     */
    public ArrayList<Room> getAvailableRoomsDB(Date checkInDate, Date checkOutDate) {
        bookingList = DBFacade.getBookingsFromDB();                                         // Henter alle Bookings fra databasen.
        Room room;
        ArrayList<Room> availableRoomList = new ArrayList<>();
        for (int i = 0; i < roomList.size(); i++) {
            room = roomList.get(i);
            boolean doubleBooking;
            doubleBooking = checkForDoubleBooking(room, checkInDate, checkOutDate);         // Checker om rummet er ledigt i perioden, så det kan tilføjes til listen af ledige rum for perioden.
            if (doubleBooking == false && !availableRoomList.contains(room)) {              // Checker at rum ikke bliver vist 2 gange, muligvis unødvendigt check pga. WHILE-loop i checkForDoubleBooking().
                availableRoomList.add(room);
            }
        }
        return availableRoomList;
    }
    
    public boolean checkRoomAvailability(Booking booking, int arraySize) {
        boolean available = false;
        ArrayList<Guest> tempRoomGuestList = DBFacade.getBookingDetailsFromDB(booking);  // vi henter en liste over allerede eksisterende kunder på bookingen
        for (int i = 0; i < roomList.size(); i++) {
            if (roomList.get(i).getRoomNo() == booking.getRoomNo()) {
                if (roomList.get(i).getRoomSize() > arraySize + tempRoomGuestList.size()) {   // vi checker at antallet af folk vi vil tilføje (arraySize) + antallet af folk allerede i bookingen (tempRoomGuestList) ikke overstiger getRoomSize.
                    available = true;
                }
            }
        }
        return available;
    }
    
    public ArrayList<Guest> getGuestsFromDB() {
        ArrayList<Guest> guestList;
        guestList = DBFacade.getGuestsFromDB();
        return guestList;
    }
    
    public boolean createGuest(Guest guest) {
        return DBFacade.createGuest(guest);
    }

    public Guest getGuestFromID(int guestId) {
        return DBFacade.getGuestFromID(guestId);
    }

    public boolean updateGuestDB(Guest guest) {
        return DBFacade.updateGuestDB(guest);
    }
    
    public ArrayList<Guest> searchForGuestDB(String status, String... names) {
        return DBFacade.searchForGuestDB(status, names);
    }

    public ArrayList<Booking> getGuestBookingsFromDB(Guest guest) {
        return DBFacade.getGuestBookingsFromDB(guest);
    }

    public ArrayList<String> getGuestLog() {
        return DBFacade.getGuestLog();
    }

    /**
     * TODO slet? Benyttes ikke
     */
    public boolean updateRoomDB(Room room) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateRoomDB(room);
        return updateSuccess;
    }

    /**
     * TODO slet? Benyttes ikke
     */
    public boolean updateBookingDB(Booking booking) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateBookingDB(booking);
        return updateSuccess;
    }

    /**
     * Benyttes ikke pt, da vi endnu ikke har implementeret mulighed for at
     * fjerne gæst fra en booking. 
     *
     * TODO slet? Benyttes ikke
     */
    public boolean updateGuestsInRoomDB(BookingDetail roomGuest) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateGuestsInRoomDB(roomGuest);
        return updateSuccess;
    }
}
