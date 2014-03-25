/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dataSource.DBFacade;
import java.util.ArrayList;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class Control {

    private DBFacade DBFacade;
    private ArrayList<Customer> customerList;
    private ArrayList<Room> roomList;

    public Control() {
        DBFacade = DBFacade.getInstance();
        customerList = new ArrayList<>();
        roomList = new ArrayList<>();
    }

    public ArrayList<Room> getRoomsFromDB() {
        roomList = DBFacade.getRoomFromDB();
        return roomList;
    }

    public ArrayList<Customer> getCustomersFromDB() {
        customerList = DBFacade.getCustomersFromDB();
        return customerList;
    }

//    public boolean bookCustomerToRoom(Room room, Customer customer) {
//        boolean status = false;
//        boolean check = false;
//        int oldRoomNo = customer.getRoomNo();
//
//        if (room.getEmptyBeds() > 0) {
//            int i = 0;
//            while (!check && i < roomList.size() && oldRoomNo != 0) {
//                if (oldRoomNo == roomList.get(i).getRoomNo()) {
//                    roomList.get(i).decrementOccupiedBeds();
//                    updateRoomDB(roomList.get(i));
//                    check = true;
//                }
//                i++;
//            }
//            int newRoomNumber = room.getRoomNo();
//            customer.setRoomNo(newRoomNumber);
//            room.incrementOccupiedBeds();
//            updateCustomerDB(customer);
//            updateRoomDB(room);
//            status = true;
//        }
//        return status;
//    }
//
//    public boolean updateCustomerDB(Customer customer) {
//        boolean updateSuccess;
//        updateSuccess = DBFacade.updateCustomerDB(customer);
//        return updateSuccess;
//    }
//
//    public boolean updateRoomDB(Room room) {
//        boolean updateSuccess;
//        updateSuccess = DBFacade.updateRoomDB(room);
//        return updateSuccess;
//    }
}
