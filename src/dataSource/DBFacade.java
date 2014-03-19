/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import java.sql.Connection;

/**
 *
 * @author Seb
 */
public class DBFacade {

    private Mapper om;
    private Connection con;
    // Singleton for at sikre at der kun er en forbindelse samt at give global adgang til domænet.
    private static DBFacade instance;

    private DBFacade() {
        om = new Mapper();
        con = new DBConnector().getConnection(); // Forbindelsen frigivet når programmet bliver lukket af garbage collector
    }

    public static DBFacade getInstance() {
        if (instance == null) {
            instance = new DBFacade();
        }
        return instance;
    }
    // Singleton slutning
    
    
}