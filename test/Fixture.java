
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
			
			st.addBatch("delete from rooms");
			st.addBatch("delete from bookings");
			st.addBatch("delete from guests");
			st.addBatch("delete from booking_details");
			st.addBatch("delete from booking_log");

			// insert tuples
			String insert1 = "insert into bookings values ";
                        String insert2 = "insert into rooms values ";
                        String insert3 = "insert into guests values ";
                        String insert4 = "insert into booking_details values ";
                        String insert5 = "insert into booking_log values ";
                        
			st.addBatch(insert1+"(3000001, 2000001, 1009, 'Star Tours', to_date('22-04-2014', 'DD-MM-YYYY'), to_date('29-04-2014', 'DD-MM-YYYY')");
			st.addBatch(insert2+"(1009, 'FAMILY', 100)");
			st.addBatch(insert3+"(2000001, 'Michael', 'Larsen', 'Søborg Torv 7', 2860, 'Søborg', 'Denmark', 'larsen_max@hotmail.com', 25462013, null");
			st.addBatch(insert4+"(2000001, 3000001)");
			st.addBatch(insert5+"()");
                       

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
