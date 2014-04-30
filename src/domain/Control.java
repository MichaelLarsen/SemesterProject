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
    private Booking currentBooking;
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
    public Booking createBooking(Room room, Guest guest, Date checkIn, Date checkOut) {
        boolean doubleBooking;
        bookingList = DBFacade.getBookingsFromDB();                                                     // Vi opdaterer listen af bookings fra databasen, så checkForDoubleBooking() har de nyeste data før den kører.
        Booking newBooking = null;
        doubleBooking = checkForDoubleBooking(room, checkIn, checkOut);                                 // Checker at rummet er ledigt i perioden. FALSE hvis rummet er ledigt.
        if (doubleBooking == false) {
            newBooking = new Booking(guest.getGuestId(), room.getRoomNo(), "", checkIn, checkOut);      // Ny booking laves.
            DBFacade.createBooking(newBooking);                                                              // Booking tilføjes til nye bookings som skal persisteres.
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
     * Control.createBooking() har allerede checket for doubleBooking, men vi checker 
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
    public boolean createBookingDetail(Guest guest, Booking booking) {
        boolean addGuestSuccess = false;
        boolean doubleBooking = false;
        ArrayList<Booking> tempBookingList;
        tempBookingList = DBFacade.getGuestBookingsFromDB(guest);                                               // Henter alle bookings hvori kunden allerede indgår
        if (!tempBookingList.isEmpty()) {                                                                       
            doubleBooking = checkGuestForDoubleBooking(tempBookingList, booking);                               // Checker om kunden allerede bor på andre bookinger i samme periode
        }
        if (doubleBooking == false) {
            BookingDetail bookingDetail = new BookingDetail(guest.getGuestId(), booking.getBookingId());
            addGuestSuccess = DBFacade.addGuestToRoom(bookingDetail);
        }
        return addGuestSuccess;
    }
    
    /**
     * Fjerner alle gæster som er tilføjet til en Booking, men som endnu ikke er persisteret.
     * - Bruges til at clear listen af gæster man har tilføjet til en Booking, men endnu ikke gemt, i GUI.
     */
    public void clearNewBookingDetails() {
        DBFacade.clearNewBookingDetails();
    }
    
    /**
     * Checker om rum er ledigt i en given periode, ved at sammenligne med alle bookings.
     * - Bruges når vi opretter en booking, når vi commit'er en booking og når vi skal 
     *   vise alle ledige rum for en given periode i GUI.
     *
     * @param room              Rum som ønskes booked.
     * @param checkInDate       Check-in dato.
     * @param checkOutDate      Check-out dato.
     * @return                  TRUE, hvis rummet ER booked/optaget i perioden, ellers FALSE.
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
                        || checkInDate.equals(bookingEndDate) || checkOutDate.equals(bookingStartDate)) {       // Her checkes alle mulige kombinationer, hvorpå rummet kan være optaget.
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
     * Checker om en gæst allerede er på et rum i en given periode.
     * - Bruges når vi tilføjer en gæst til et værelse af Control.createBookingDetail().
     * 
     * Alternativt kunne man blot hente gæstens eventuelle bookings for en specifik periode direkte i 
     * database via SQL statement. Hvis der var nogle bookings, var gæsten allerede booked.
     * Vi har valgt at gøre det i programmet i stedet for databasen.
     *
     * @param oldBookingList        Liste med alle gæstens bookings fra databasen.
     * @param newBooking            Booking som vi ønsker at filføje gæsten til.
     * @return                      TRUE, hvis gæsten allerede ER booked/optaget i perioden, ellers FALSE.
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
    
    /**
     * Henter alle rum fra databasen.
     *
     * @return       Liste med alle Rooms i databasen.
     */
    public final ArrayList<Room> getRoomsFromDB() {
        roomList = DBFacade.getRoomFromDB();
        return roomList;
    }

    /**
     * Returnerer liste af rum fra programmets hukommelse.
     * - Bruges hver gang vi skal bruge hotellets rum. Da rum ikke kan ændres, er
     *   unødvendigt at hente dem fra databasen mere end én gang.
     *
     * @return      Liste med alle rum.
     */
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
    
    /**
     * Checker om der er ledige senge i bookingen/rummet.
     * - Bruges til at sikre at vi ikke overbooker et rum.
     *
     * @param booking       Booking for hvilken vi ønsker ledighed.
     * @param arraySize     Liste med gæster som foreløbigt ønskes tilføjet.
     * @return              TRUE, så længe der stadig er 
     */
    public int checkRoomAvailability(Booking booking, int arraySize) {
        int availability = 0;
        ArrayList<Guest> tempRoomGuestList = DBFacade.getBookingDetailsFromDB(booking);  // vi henter en liste over allerede eksisterende kunder på bookingen
        for (int i = 0; i < roomList.size(); i++) {
            if (roomList.get(i).getRoomNo() == booking.getRoomNo()) {
                if (roomList.get(i).getRoomSize() > arraySize + tempRoomGuestList.size()) {   // vi checker at antallet af folk vi vil tilføje (arraySize) + antallet af folk allerede i bookingen (tempRoomGuestList) ikke overstiger getRoomSize.
                    availability = roomList.get(i).getRoomSize() - (arraySize + tempRoomGuestList.size());
                }
            }
        }
        return availability;
    }
    
    /**
     * Henter alle gæster fra databasen.
     *
     * @return       Liste med alle Guests i databasen.
     */
    public ArrayList<Guest> getGuestsFromDB() {
        ArrayList<Guest> guestList;
        guestList = DBFacade.getGuestsFromDB();
        return guestList;
    }
    
    /**
     * Tilføjer gæst til liste af gæster som skal oprettes.
     * - Bruges ved oprettelse af ny gæst.
     *
     * @param guest     Guest-objekter som skal persisteres.
     * @return          TRUE, hvis gæsten ikke allerede er tilføjet.
     */
    public boolean createGuest(Guest guest) {
        return DBFacade.createGuest(guest);
    }

    /**
     * Henter gæst med specifikt guestId fra databasen.
     *
     * @param guestId        GuestId på gæst som ønskes.
     * @return               Guest-objekt med ønskede bookingId.
     */
    public Guest getGuestFromID(int guestId) {
        return DBFacade.getGuestFromID(guestId);
    }

    /**
     * Tilføjer gæst til liste af gæster som skal ændres.
     * - Bruges ved ændring af gæstens informationer.
     *
     * @param guest     Guest-objekter som skal opdateres.
     * @return          TRUE, hvis gæsten ikke allerede er tilføjet.
     */
    public boolean updateGuestDB(Guest guest) {
        return DBFacade.updateGuestDB(guest);
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
        return DBFacade.searchForGuestDB(status, names);
    }

    /**
     * Henter alle bookinger gæsten sover på (ikke dem han er ejer af). 
     * - Bruges til at checke at gæsten ikke bookes til flere værelser i samme tidsrum.
     *
     * @param guest      Guest for hvem Bookings ønskes.
     * @return           Liste med Bookings gæsten findes på.
     */
    public ArrayList<Booking> getGuestBookingsFromDB(Guest guest) {
        return DBFacade.getGuestBookingsFromDB(guest);
    }

    /**
     * Henter alle guest-logs fra databasen.
     * 
     * @return       Liste med alle Guest-logs som strings.
     */
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

    public void setCurrentBooking(Booking booking) {
        this.currentBooking = booking;
    }
    public Booking getCurrentBooking() {
        return currentBooking;
    }
}
