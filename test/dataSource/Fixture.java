package dataSource;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Michael
 */
public class Fixture {

    static void setUp(Connection con) {
        try {
			
			Statement st = con.createStatement();

			// start transaction
			con.setAutoCommit(false);

			// create table
                        st.addBatch("drop sequence booking_log_id_seq");
                        st.addBatch("drop sequence booking_id_seq");
                        
			st.addBatch("delete from booking_log");     
			st.addBatch("delete from booking_details");
                        st.addBatch("delete from bookings");
			st.addBatch("delete from guests");
                        st.addBatch("delete from rooms");

			// insert tuples
                        st.addBatch("create sequence booking_log_id_seq start with 1");
                        st.addBatch("create sequence booking_id_seq start with 3000020");
                        
                        st.addBatch("insert into guests values(2000001, 'Michael', 'Larsen', 'Søborg Torv 7', 2860, 'Søborg', 'Denmark', 'larsen_max@hotmail.com', 25462013, null)");
                        st.addBatch("insert into guests values(2000002, 'Hans', 'Olsen', 'Søborg Torv 7', 2860, 'Søborg', 'Denmark', 'larsen_max@hotmail.com', 25462013, null)");
			st.addBatch("insert into rooms values(1008, 'FAMILY', 100)");
                        st.addBatch("insert into rooms values(1009, 'FAMILY', 100)");
                        st.addBatch("insert into bookings values(3000001, 2000001, 1009, 'Star Tours', to_date('22-04-2014', 'DD-MM-YYYY'), to_date('29-04-2014', 'DD-MM-YYYY'))");
                        st.addBatch("insert into bookings values(3000002, 2000002, 1009, 'Star Tours', to_date('10-04-2014', 'DD-MM-YYYY'), to_date('20-04-2014', 'DD-MM-YYYY'))");
                        st.addBatch("insert into bookings values(3000003, 2000001, 1008, 'Star Tours', to_date('22-04-2014', 'DD-MM-YYYY'), to_date('29-04-2014', 'DD-MM-YYYY'))");
			st.addBatch("insert into booking_details values(2000001, 3000001)");
			st.addBatch("insert into booking_details values(2000002, 3000001)");
			st.addBatch("insert into booking_details values(2000001, 3000002)");
			st.addBatch("INSERT INTO BOOKING_LOG (Id, Action, Booking_Id, Logdate, Content) "
                                + "SELECT 1, 1, 3000001, CURRENT_TIMESTAMP(3), SYS.Dbms_Xmlgen.Getxml('SELECT "
                                + "(SELECT LISTAGG(CONCAT(CONCAT(g.FIRST_NAME, '' ''), g.LAST_NAME), '','') WITHIN GROUP (ORDER BY 1) AS Fullname FROM GUESTS g JOIN BOOKING_DETAILS bd ON bd.GUEST_ID = g.GUEST_ID AND bd.BOOKING_ID = 3000001) AS GUESTS, "
                                + "b.* FROM Bookings b WHERE b.booking_id = 3000001') xmlstr FROM Dual");
                       

			int[] opcounts = st.executeBatch();
			

			// end transaction
			con.commit();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.out.println("Fail in Fixture.setup()");
			System.out.println(e.getMessage());
		}
    }
    
}
