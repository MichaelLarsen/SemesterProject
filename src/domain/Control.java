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
        boolean check = false;
        int oldRoomNo = customer.getRoomNo();

        if (room.getEmptyBeds() > 0) {
            int i = 0;
            while (!check && i < roomList.size() && oldRoomNo != 0) {

                if (oldRoomNo == roomList.get(i).getRoomNo()) {
                    roomList.get(i).decrementOccupiedBeds();
                    //UPDATE DATABASE
                    check = true;
                }
                i++;
            }

            int newRoomNumber = room.getRoomNo();
            customer.setRoomNo(newRoomNumber);
            room.incrementOccupiedBeds();
            //UPDATE DATABASE
            status = true;
        }
        return status;
    }
}