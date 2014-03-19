/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dataSource.DBFacade;

/**
 *
 * @author Gruppe 4: Andreas, Michael og Sebastian
 */
public class Control {

    private DBFacade DBFacade;

    public Control() {
        DBFacade = DBFacade.getInstance();
    }
}
