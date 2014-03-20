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

    public boolean addRoomToCustomer() {
        boolean status;     
        if (currentCustomer !=null) { 
        }
        status = DBFacade.addCustomerToRoom(currentCustomer);
        return status;
    }
    public ArrayList<Room> getRoomFromDB(){
        roomList = DBFacade.getRoomFromDB();
        return roomList;
    }
}

