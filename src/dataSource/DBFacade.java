/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import domain.Customer;
import domain.Room;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author Seb
 */
public class DBFacade {

    private Mapper mapper;
    private Connection con;
    // Singleton for at sikre at der kun er en forbindelse samt at give global adgang til domænet.
    private static DBFacade instance;

    private DBFacade() {
        mapper = new Mapper();
        con = new DBConnector().getConnection(); // Forbindelsen frigivet når programmet bliver lukket af garbage collector
    }

    public static DBFacade getInstance() {
        if (instance == null) {
            instance = new DBFacade();
        }
        return instance;
    }
    // Singleton slutning

    public boolean addCustomerToRoom(Customer currentCustomer) {
        boolean status;
        status = mapper.addCustomerToRoom(currentCustomer, con);
        return status;
    }

    public ArrayList<Room> getRoomFromDB() {
        ArrayList<Room> roomList;
        roomList = mapper.getRoomsFromDB(con);
        return roomList;
    }

    public ArrayList<Customer> getCustomersFromDB() {
        ArrayList<Customer> customerList;
        customerList = mapper.getCustomersFromDB(con);
        return customerList;
    }
}