/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import domain.Control;
import domain.Customer;
import domain.Room;
import javax.swing.DefaultListModel;

/**
 *
 * @author Gruppe 4: Andreas, Michael og Sebastian
 */
public class GUI extends javax.swing.JFrame {

    private Control ctr;
    private DefaultListModel<Customer> customerModel;
    private DefaultListModel<Room> roomModel;

    /**
     * Creates new form GUI
     */
    public GUI() {

        ctr = new Control();
        initComponents();
        customerModel = new DefaultListModel<>();
        roomModel = new DefaultListModel<>();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        getRoomButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        roomJList = new javax.swing.JList();
        getCustomersButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        customerJList = new javax.swing.JList();
        selectButton = new javax.swing.JButton();
        statusJLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        getRoomButton.setText("Get Rooms");
        getRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getRoomButtonActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(roomJList);

        getCustomersButton.setText("Get Customers");
        getCustomersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getCustomersButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(customerJList);

        selectButton.setText("Select");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        statusJLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(getCustomersButton)
                    .addComponent(getRoomButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(selectButton)
                        .addGap(31, 31, 31)
                        .addComponent(statusJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(263, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(getRoomButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(getCustomersButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(statusJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(78, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
//Bruger jlist til at hente rum og derfor også kunne vælge rum i form af objekt.
    private void getRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getRoomButtonActionPerformed
        roomModel.removeAllElements();
        for (int i = 0; i < ctr.getRoomsFromDB().size(); i++) {
            roomModel.addElement(ctr.getRoomsFromDB().get(i));
        }
        roomJList.setModel(roomModel);
    }//GEN-LAST:event_getRoomButtonActionPerformed

    private void getCustomersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getCustomersButtonActionPerformed
        customerModel.removeAllElements();
        for (int i = 0; i < ctr.getCustomersFromDB().size(); i++) {
            customerModel.addElement(ctr.getCustomersFromDB().get(i));
        }
        customerJList.setModel(customerModel);
    }//GEN-LAST:event_getCustomersButtonActionPerformed

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        if (roomJList.getSelectedValue() == null || customerJList.getSelectedValue() == null) {
            statusJLabel.setText("You must select a room and a customer!");
        }
        else {
            Room room = (Room) roomJList.getSelectedValue(); //typecast til room-objekt
            Customer customer = (Customer) customerJList.getSelectedValue(); ////typecast til customer-objekt
            boolean status;
            status = ctr.bookCustomerToRoom(room, customer);
            if (status) {
                statusJLabel.setText("Customer booked room " + room.getRoomNo());
                customerJList.setModel(customerModel);
                roomJList.setModel(roomModel);
            }
            else {
                statusJLabel.setText("Room is occupied!");
            }
        }
    }//GEN-LAST:event_selectButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList customerJList;
    private javax.swing.JButton getCustomersButton;
    private javax.swing.JButton getRoomButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList roomJList;
    private javax.swing.JButton selectButton;
    private javax.swing.JLabel statusJLabel;
    // End of variables declaration//GEN-END:variables
}