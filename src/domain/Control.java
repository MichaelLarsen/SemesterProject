/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dataSource.DBFacade;
import java.util.ArrayList;

/**
 *
 * @author Gruppe 4: Andreas, Michael og Sebastian
 */
public class Control {

    private DBFacade DBFacade;
    private Customer currentCustomer;
    private ArrayList<Customer> customerList = new ArrayList<>();
    private ArrayList<Room> roomList;

    public Control() {
        DBFacade = DBFacade.getInstance();
    }

    public ArrayList<Room> getRoomsFromDB() {
        roomList = DBFacade.getRoomFromDB();
        return roomList;
    }

    public ArrayList<Customer> getCustomersFromDB() {
        customerList = DBFacade.getCustomersFromDB();
        return customerList;
    }

    public boolean bookCustomerToRoom(Room room, Customer customer) {
        boolean status = false;
        if (room.getOccupied() == 0) {
            int roomNumber = room.getRoomNo();
            customer.setRoomNo(roomNumber);
            room.setOccupied(1);
            status = true;
        }
        return status;
    }
}
