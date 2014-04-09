/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dataSource.DBFacade;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class Control {

    private DBFacade DBFacade;
    private ArrayList<Customer> customerList;
    private ArrayList<Room> roomList;
    private ArrayList<Booking> bookingList;

    public Control() {
        DBFacade = DBFacade.getInstance();
        customerList = new ArrayList<>();
        roomList = getRoomsFromDB();
    }

    public boolean commitTransaction() {
        boolean commitSuccess = false;
        try {
            commitSuccess = DBFacade.commitTransaction();
            if (commitSuccess) {
//                clearTempRoomGuestList();
            }
        }
        catch (SQLException e) {
            System.out.println("Fejl ved at gemme transaction!");
            System.out.println(e.getMessage());
        }
        return commitSuccess;
    }

    public final ArrayList<Room> getRoomsFromDB() {
        roomList = DBFacade.getRoomFromDB();
        return roomList;
    }

    public ArrayList<Customer> getCustomersFromDB() {
        customerList = DBFacade.getCustomersFromDB();
        return customerList;
    }

    public ArrayList<Booking> getBookingsFromDB() {
        bookingList = DBFacade.getBookingsFromDB();
        return bookingList;
    }

    public boolean updateRoomDB(Room room) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateRoomDB(room);
        return updateSuccess;
    }

    private boolean updateBookingDB(Booking booking) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateBookingDB(booking);
        return updateSuccess;
    }

    private boolean updateGuestsInRoomDB(RoomGuest roomGuest) {
        boolean updateSuccess;
        updateSuccess = DBFacade.updateGuestsInRoomDB(roomGuest);
        return updateSuccess;
    }

    public Booking bookRoom(Room room, Customer customer, Date checkIn, Date checkOut) {
        Boolean bookingAddedSuccess;
        boolean doubleBooking;
        Booking newBooking = null;

        doubleBooking = checkForDoubleBooking(room, checkIn, checkOut);
        if (doubleBooking == false) {
            newBooking = new Booking(customer.getCustomerId(), room.getRoomNo(), "", checkIn, checkOut);
            bookingAddedSuccess = DBFacade.bookRoom(newBooking);
            if (bookingAddedSuccess == true && !bookingList.contains(newBooking)) {
                bookingList.add(newBooking);
            }
        }
        return newBooking;
    }

    public ArrayList<Customer> getGuestsInRoom(Booking booking) {
        return DBFacade.getGuestsInRoomFromDB(booking);
    }

    public ArrayList<Room> getAvailableRoomsDB(Date checkInDate, Date checkOutDate) {
        Date bookingStartDate;
        Date bookingEndDate;
        Room room;
        ArrayList<Room> availableRoomList = new ArrayList<>();

        for (int i = 0; i < roomList.size(); i++) {
            room = roomList.get(i);
            boolean doubleBooking = false;
            for (int j = 0; j < bookingList.size(); j++) {
                if ((bookingList.get(j).getRoomNo() == room.getRoomNo())) {
                    bookingStartDate = bookingList.get(j).getCheckInDate();
                    bookingEndDate = bookingList.get(j).getCheckOutDate();
                    //Januar starter på 00, så 03 er fx april.
//                    System.out.println("checkInDate: " + checkInDate + " checkOutDate: " + checkOutDate);
//                    System.out.println("bookingStartDate: " + bookingStartDate + " bookingEndDate: " + bookingEndDate);
                    if ((checkInDate.before(bookingStartDate) && checkOutDate.before(bookingStartDate))
                            || (checkInDate.after(bookingEndDate) && checkOutDate.after(bookingEndDate))
                            || checkInDate.equals(bookingEndDate) || checkOutDate.equals(bookingStartDate)) {
                        doubleBooking = false;
                        availableRoomList.add(room);
//                        System.out.println("Sådan - ledigt!");
                    }
                    else {
                        doubleBooking = true;
//                        System.out.println("Doublebooking!");
                    }
                }
            }
            if (doubleBooking == false && !availableRoomList.contains(room)) {
                availableRoomList.add(room);
            }
        }
        return availableRoomList;
    }

    public boolean checkForDoubleBooking(Room room, Date checkInDate, Date checkOutDate) {
        boolean doubleBooking = false;
        for (int j = 0; j < bookingList.size(); j++) {
            if ((bookingList.get(j).getRoomNo() == room.getRoomNo())) {
                Date bookingStartDate = bookingList.get(j).getCheckInDate();
                Date bookingEndDate = bookingList.get(j).getCheckOutDate();
                //Januar starter på 00, så 03 er fx april.
//                System.out.println("checkInDate: " + checkInDate + " checkOutDate: " + checkOutDate);
//                System.out.println("bookingStartDate: " + bookingStartDate + " bookingEndDate: " + bookingEndDate);
                if ((checkInDate.before(bookingStartDate) && checkOutDate.before(bookingStartDate))
                        || (checkInDate.after(bookingEndDate) && checkOutDate.after(bookingEndDate))
                        || checkInDate.equals(bookingEndDate) || checkOutDate.equals(bookingStartDate)) {
                    doubleBooking = false;
//                    System.out.println("Sådan - ledigt!");
                }
                else {
                    doubleBooking = true;
//                    System.out.println("Doublebooking!");
                }
            }
        }
        return doubleBooking;
    }

    public boolean addGuestToRoom(Customer customer, Booking booking) {
        ArrayList<Booking> tempBookingList;
        tempBookingList = DBFacade.getCustomerBookingsFromDB(customer); //henter evt. bookings hvori kunden allerede indgår

        boolean addGuestSuccess = false;
        boolean doubleBooking = false;

        if (!tempBookingList.isEmpty()) {   //checker om kunden allerede bor på andre bookinger i samme periode
            doubleBooking = checkCustomerForDoubleBooking(tempBookingList, booking);
        }
        if (doubleBooking == false) {
            RoomGuest roomGuest = new RoomGuest(customer.getCustomerId(), booking.getBookingId());
            addGuestSuccess = DBFacade.addGuestToRoom(roomGuest);
        }
        return addGuestSuccess;
    }

    public boolean checkRoomAvailability(Booking booking, int arraySize) {
        boolean available = false;
        ArrayList<Customer> tempRoomGuestList = DBFacade.getGuestsInRoomFromDB(booking);  // vi henter en liste over allerede eksisterende kunder på bookingen
        for (int i = 0; i < roomList.size(); i++) {
            if (roomList.get(i).getRoomNo() == booking.getRoomNo()) {
                if (roomList.get(i).getRoomSize() > arraySize + tempRoomGuestList.size()) {   // vi checker at antallet af folk vi vil tilføje (arraySize) + antallet af folk allerede i bookingen (tempRoomGuestList) ikke overstiger getRoomSize.
                    available = true;
                }
            }
        }
        return available;
    }

//        boolean addGuestSuccess = false;
//        boolean doubleBooking = false;
//        boolean check = false;
//        ArrayList<Customer> guestsInBooking = DBFacade.getGuestsInRoomFromDB(booking);
//        for (int i = 0; i < guestsInBooking.size(); i++) {
//            booking.addCustomerForBooking(guestsInBooking.get(i));
//        }
//        //Tjekker om kunden allerede er på den valgte booking
//        for (int i = 0; i < booking.getCustomersForBooking().size(); i++) {
//            System.out.println("check er false");
//            if (booking.getCustomersForBooking().get(i).getCustomerId() == customer.getCustomerId()) {
//                check = true;
//                System.out.println("check er true");
//            }
//        }
//        if (check == false) {
//            System.out.println("inde i if - check = false");
//            RoomGuest roomGuest = new RoomGuest(customer.getCustomerId(), booking.getBookingId());
//            doubleBooking = checkCustomerForDoubleBooking(roomGuest, booking.getCheckInDate(), booking.getCheckOutDate());
//            System.out.println("doublebooking er "+doubleBooking);
//            if (doubleBooking == false) {
//                System.out.println("inde i if - doublebooking=false");
//                for (int i = 0; i < roomList.size(); i++) {
//                    if (booking.getRoomNo() == roomList.get(i).getRoomNo()) {
//                        if (tempRoomGuestList.size() + booking.getOccupiedBeds() < roomList.get(i).getRoomSize()) {    // checker at værelset ikke er fyldt, og at kunden ikke allerede er gæst på værelset                      
//                            tempRoomGuestList.add(roomGuest);
//                            addGuestSuccess = DBFacade.addGuestToRoom(roomGuest);
//                            System.out.println("Guest added " + roomGuest.getCustomerId());
//                        }
//                    }
//                }
//            }
//        }
//
//        //Tjekker om kunden er blevet tilføjet tidligere
//        for (int i = 0; i < tempRoomGuestList.size(); i++) {
//            if (tempRoomGuestList.get(i).getCustomerId() == customer.getCustomerId()) {
//                check = true;
//            }
//        }
//        if (check == false) {
//            System.out.println("check er false!");
//            // TODO: kan man refakturere hele metoden så den bliver simplere?
//            for (int i = 0; i < bookingList.size(); i++) {
//                if (bookingList.get(i).getCustomersForBooking().contains(customer)) {   // checker om kunden allerede er gæst på en anden booking
//                    System.out.println("Første if løkke");
//                    for (int j = 0; j < roomList.size(); j++) {
//                        if (bookingList.get(i).getRoomNo() == roomList.get(j).getRoomNo()) {
//                            System.out.println("Anden if løkke");
//                            doubleBooking = checkForDoubleBooking(roomList.get(j), booking.getCheckInDate(), booking.getCheckOutDate());  // Hvis kunden findes på en anden booking, checker vi her om de 2 bookinger overlapper
//                            System.out.println("doubleBooking før er: " + doubleBooking);
//                        }
//                    }
//                }
//            }
//            if (doubleBooking == false && check == false) {
//                RoomGuest roomGuest = new RoomGuest(customer.getCustomerId(), booking.getBookingId());
//                System.out.println("doubleBooking efter er: " + doubleBooking + " tempRoomGuest er: " + check);
//                for (int i = 0; i < roomList.size(); i++) {
//                    if (booking.getRoomNo() == roomList.get(i).getRoomNo()) {
//                        if (tempRoomGuestList.size() + booking.getOccupiedBeds() < roomList.get(i).getRoomSize()) {    // checker at værelset ikke er fyldt, og at kunden ikke allerede er gæst på værelset                      
//                            tempRoomGuestList.add(roomGuest);
//                            addGuestSuccess = DBFacade.addGuestToRoom(roomGuest);
//                            System.out.println("Guest added " + roomGuest.getCustomerId());
//                        }
//                    }
//                }
//            }
//        }
//            return addGuestSuccess;
//        }
    public ArrayList<Booking> getCustomerBookingsFromDB(Customer customer) {
        return DBFacade.getCustomerBookingsFromDB(customer);
    }

    private boolean checkCustomerForDoubleBooking(ArrayList<Booking> oldBookingList, Booking newBooking) {
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

    public void createCustomer(Customer customer) {
        
    }
}
