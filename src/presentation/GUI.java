/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import domain.Booking;
import domain.Control;
import domain.Customer;
import domain.Room;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sebastian, Michael og Andreas
 */
public class GUI extends javax.swing.JFrame {

    private Control ctr;
    private DefaultListModel<Customer> customerModel;
    private DefaultListModel<Room> roomModel;
    private DefaultListModel<Booking> bookingModel;
    private DefaultListModel<Booking> newBookingModel;
    private DefaultListModel infoBookingModel;
    private DefaultListModel addedGuestsModel;
    private DefaultTableModel guestTableModel;

    /**
     * Creates new form GUI
     */
    public GUI() {

        ctr = new Control();
        initComponents();
        customerModel = new DefaultListModel<>();
        roomModel = new DefaultListModel<>();
        bookingModel = new DefaultListModel<>();
        newBookingModel = new DefaultListModel<>();
        infoBookingModel = new DefaultListModel<>();
        addedGuestsModel = new DefaultListModel<>();
        guestTableModel = new DefaultTableModel();

//        refreshModel(roomModel);
        refreshModel(customerModel);
        refreshModel(bookingModel);
        refreshGuestTable(guestTableModel);
    }

    private void refreshGuestTable(DefaultTableModel guestTableModel) {
        guestTableModel.setColumnCount(0);
        guestTableModel.addColumn("Customer ID");
        guestTableModel.addColumn("First name");
        guestTableModel.addColumn("Last name");
        guestTableModel.addColumn("Street");
        guestTableModel.addColumn("Zipcode");
        guestTableModel.addColumn("City");
        guestTableModel.addColumn("Country");
        guestTableModel.addColumn("E-mail");
        guestTableModel.addColumn("Phone #1");
        guestTableModel.addColumn("Phone #2");

        ArrayList<Customer> guestList;
        guestList = ctr.getCustomersFromDB();

        for (Customer customer : guestList) {
            Object[] guestInfoArray = new Object[10];

            guestInfoArray[0] = customer.getCustomerId();
            guestInfoArray[1] = customer.getFirstName();
            guestInfoArray[2] = customer.getLastName();
            guestInfoArray[3] = customer.getStreet();
            guestInfoArray[4] = customer.getZipcode();
            guestInfoArray[5] = customer.getCity();
            guestInfoArray[6] = customer.getCountry();
            guestInfoArray[7] = customer.getEmail();
            guestInfoArray[8] = customer.getPrivatePhone();
            guestInfoArray[9] = customer.getWorkPhone();
            guestTableModel.addRow(guestInfoArray);
        }
        guestTable.setModel(guestTableModel);
    }

    //med en model kan man sende ligeså mange dates med som man vil
    private void refreshModel(DefaultListModel model, Date... dates) {
        if (model.equals(customerModel)) {
            ArrayList<Customer> customerList;
            customerList = ctr.getCustomersFromDB();
            customerModel.clear();
            for (int i = 0; i < customerList.size(); i++) {
                customerModel.addElement(customerList.get(i));
            }
            customerJList.setModel(customerModel);
            customerJList2.setModel(customerModel);
        }
        if (model.equals(roomModel)) {
            if (dates.length == 2) {
                Date date1 = dates.length > 0 ? dates[0] : null;
                Date date2 = dates.length > 1 ? dates[1] : null;
                ArrayList<Room> roomList;
                roomList = ctr.getAvailableRoomsDB(date1, date2);
                roomModel.clear();
                for (int i = 0; i < roomList.size(); i++) {
                    roomModel.addElement(roomList.get(i));
                }
                roomJList.setModel(roomModel);
            }
        }
        if (model.equals(bookingModel)) {
            ArrayList<Booking> bookingList;
            bookingList = ctr.getBookingsFromDB();
            bookingModel.clear();
            for (int i = 0; i < bookingList.size(); i++) {
                bookingModel.addElement(bookingList.get(i));
            }
            bookingJList.setModel(bookingModel);
        }
        if (model.equals(infoBookingModel)) {
            infoBookingModel.clear();
            Booking booking = (Booking) bookingJList.getSelectedValue();
            ArrayList<Customer> roomGuestList;
            roomGuestList = ctr.getGuestsInRoom(booking);

            for (int i = 0; i < roomGuestList.size(); i++) {
                infoBookingModel.addElement(roomGuestList.get(i));
            }
            bookingInfoJList.setModel(infoBookingModel);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jOptionPane = new javax.swing.JOptionPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        roomJList = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        customerJList = new javax.swing.JList();
        bookRoomButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        saveBookingButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        newBookingJList = new javax.swing.JList();
        checkIn = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        checkOut = new org.jdesktop.swingx.JXDatePicker();
        jLabel2 = new javax.swing.JLabel();
        checkAvailabilityButton = new javax.swing.JButton();
        BookingDetailButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        bookingJList = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        addedGuestsJList = new javax.swing.JList();
        addGuestToRoomButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        bookingInfoJList = new javax.swing.JList();
        jScrollPane8 = new javax.swing.JScrollPane();
        customerJList2 = new javax.swing.JList();
        saveNewGuestsButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        firstNameTextField = new javax.swing.JTextField();
        lastNameTextField = new javax.swing.JTextField();
        zipcodeTextField = new javax.swing.JTextField();
        streetTextField = new javax.swing.JTextField();
        cityTextField = new javax.swing.JTextField();
        phone1TextField = new javax.swing.JTextField();
        countryTextField = new javax.swing.JTextField();
        emailTextField = new javax.swing.JTextField();
        phone2TextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        guestTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        roomJList.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Rooms"));
        jScrollPane2.setViewportView(roomJList);

        customerJList.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Guests"));
        jScrollPane1.setViewportView(customerJList);

        bookRoomButton.setText("Book room");
        bookRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookRoomButtonActionPerformed(evt);
            }
        });

        jButton1.setText("Delete booking");

        saveBookingButton.setText("Save Booking");
        saveBookingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBookingButtonActionPerformed(evt);
            }
        });

        newBookingJList.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "New bookings"));
        jScrollPane4.setViewportView(newBookingJList);

        checkIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkInActionPerformed(evt);
            }
        });

        jLabel1.setText("Check in:");

        checkOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkOutActionPerformed(evt);
            }
        });

        jLabel2.setText("Check out:");

        checkAvailabilityButton.setText("Check availability");
        checkAvailabilityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkAvailabilityButtonActionPerformed(evt);
            }
        });

        BookingDetailButton.setText("Booking details");
        BookingDetailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BookingDetailButtonActionPerformed(evt);
            }
        });

        bookingJList.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Bookings"));
        bookingJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookingJListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(bookingJList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bookRoomButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(saveBookingButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)))
                        .addGap(18, 18, 18)
                        .addComponent(BookingDetailButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(checkOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(checkAvailabilityButton)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkAvailabilityButton))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(231, 231, 231)
                        .addComponent(bookRoomButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(138, 138, 138)
                                        .addComponent(BookingDetailButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(saveBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(40, 40, 40))
        );

        jTabbedPane1.addTab("Booking", jPanel1);

        addedGuestsJList.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Added guests"));
        jScrollPane6.setViewportView(addedGuestsJList);

        addGuestToRoomButton.setText("Add guest to booking");
        addGuestToRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGuestToRoomButtonActionPerformed(evt);
            }
        });

        bookingInfoJList.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Booking details"));
        bookingInfoJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookingInfoJListMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(bookingInfoJList);

        customerJList2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Guests"));
        jScrollPane8.setViewportView(customerJList2);

        saveNewGuestsButton.setText("Save changes");
        saveNewGuestsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveNewGuestsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addGuestToRoomButton)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveNewGuestsButton))
                .addGap(566, 566, 566))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jScrollPane5)
                .addGap(36, 36, 36)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addGuestToRoomButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveNewGuestsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                    .addComponent(jScrollPane6))
                .addContainerGap(64, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Booking details", jPanel3);

        firstNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                firstNameTextFieldKeyTyped(evt);
            }
        });

        lastNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lastNameTextFieldKeyTyped(evt);
            }
        });

        zipcodeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                zipcodeTextFieldKeyTyped(evt);
            }
        });

        streetTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                streetTextFieldKeyTyped(evt);
            }
        });

        cityTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cityTextFieldKeyTyped(evt);
            }
        });

        phone1TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                phone1TextFieldKeyTyped(evt);
            }
        });

        countryTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                countryTextFieldKeyTyped(evt);
            }
        });

        emailTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                emailTextFieldKeyTyped(evt);
            }
        });

        phone2TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                phone2TextFieldKeyTyped(evt);
            }
        });

        jLabel3.setText("First name");

        jLabel4.setText("Family name");

        jLabel5.setText("Street");

        jLabel6.setText("Address:");

        jLabel7.setText("zipcode");

        jLabel8.setText("City");

        jLabel9.setText("Country");

        jLabel10.setText("E-mail");

        jLabel11.setText("Phone #1");

        jLabel12.setText("Phone #2");

        jLabel13.setText("New guest:");

        jButton2.setText("Create new guest");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        guestTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Customer ID", "First name", "Last name", "Street", "Zipcode", "City", "Country", "E-mail", "Phone #1", "Phone #2"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane7.setViewportView(guestTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel13)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(firstNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addComponent(lastNameTextField)))
                    .addComponent(jLabel12)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(countryTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                                    .addComponent(zipcodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(phone1TextField)
                                    .addComponent(phone2TextField)
                                    .addComponent(emailTextField))
                                .addComponent(cityTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(streetTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 221, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 703, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(firstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(lastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addComponent(jLabel6)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(streetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zipcodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(countryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(phone1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(phone2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addComponent(jButton2)
                .addContainerGap(192, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Customers", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addGuestToRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addGuestToRoomButtonActionPerformed
        boolean addGuestSuccess;
        if (newBookingJList.getSelectedValue() == null) {
            if (newBookingJList.getSelectedValue() == null) {
                if (bookingJList.getSelectedValue() != null && customerJList2.getSelectedValue() != null) {
                    Customer customer = (Customer) customerJList2.getSelectedValue();
                    Booking booking = (Booking) bookingJList.getSelectedValue();
                    if (!addedGuestsModel.contains(customer)) {
                        if (ctr.checkRoomAvailability(booking, addedGuestsModel.getSize()) == true) {
                            addGuestSuccess = ctr.addGuestToRoom(customer, booking);
                            if (addGuestSuccess) {
                                addedGuestsModel.addElement(customer);
                                addedGuestsJList.setModel(addedGuestsModel);
                                jOptionPane.showMessageDialog(this, customer.getFirstName() + " " + customer.getLastName() + " added to roomNo " + booking.getRoomNo());
                            }
                            else {
                                jOptionPane.showMessageDialog(this, "Something went wrong in adding the guest to the room!", "Error!", jOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else {
                            jOptionPane.showMessageDialog(this, "Cannot add customer because room is full!", "Room is full!", jOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    else {
                        jOptionPane.showMessageDialog(this, "Customer already added!", "Warning!", jOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else {
                    jOptionPane.showMessageDialog(this, "You must select a booking and a customer!");
                }
            }

        }
        // Hvis man har lavet en booking, men endnu ikke har gemt den køres nedenstående
        else {
            boolean commitSuccess = false;
            int reply = jOptionPane.showConfirmDialog(this, "You must save your new booking, before you can add guests.\nDo you want to save the booking now?", "Save booking?", jOptionPane.YES_NO_OPTION);
            if (reply == jOptionPane.YES_OPTION) {
                commitSuccess = ctr.commitTransaction();
                if (commitSuccess) {
                    newBookingModel.clear();
                    refreshModel(bookingModel);
                    refreshModel(roomModel, checkIn.getDate(), checkOut.getDate());
                    jOptionPane.showMessageDialog(this, "Booking saved!");
                }
                else {
                    jOptionPane.showMessageDialog(this, "Something went wrong with saving the booking!", "Saving error!", jOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_addGuestToRoomButtonActionPerformed

    private void bookingInfoJListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookingInfoJListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_bookingInfoJListMouseClicked

    private void checkAvailabilityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkAvailabilityButtonActionPerformed
        Date checkInDate = checkIn.getDate();
        Date checkOutDate = checkOut.getDate();
        if (checkInDate != null && checkOutDate != null && !checkInDate.equals(checkOutDate) && checkOutDate.after(checkInDate)) {  // checker at begge datoer er valgt, og at checkOut dato er efter checkIn dato.
            refreshModel(bookingModel);
            refreshModel(roomModel, checkInDate, checkOutDate);
        }
        else {
            jOptionPane.showMessageDialog(this, "You must choose a check-in and a check-out date. Check-in date must be before check-out date.", "Invalid dates!", jOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_checkAvailabilityButtonActionPerformed

    private void checkInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkInActionPerformed
        roomModel.clear();
    }//GEN-LAST:event_checkInActionPerformed

    private void bookingJListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookingJListMouseClicked
        infoBookingModel.clear();
        Booking booking = (Booking) bookingJList.getSelectedValue();
        ArrayList<Customer> roomGuestList;
        roomGuestList = ctr.getGuestsInRoom(booking);

        for (int i = 0; i < roomGuestList.size(); i++) {
            infoBookingModel.addElement(roomGuestList.get(i));
        }
        bookingInfoJList.setModel(infoBookingModel);
    }//GEN-LAST:event_bookingJListMouseClicked

    private void saveBookingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBookingButtonActionPerformed
        boolean commitSuccess;
        int reply = jOptionPane.showConfirmDialog(this, "Are you sure you want to save?", "Save?", jOptionPane.YES_NO_OPTION);
        if (reply == jOptionPane.YES_OPTION) {
            commitSuccess = ctr.commitTransaction();
            if (commitSuccess) {
                newBookingModel.clear();
                jOptionPane.showMessageDialog(this, "Booking er gemt!");
                refreshModel(bookingModel);
                refreshModel(roomModel, checkIn.getDate(), checkOut.getDate());
            }
            else {
                jOptionPane.showMessageDialog(this, "Something went wrong with saving the booking!", "Booking Error! - rollback", jOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_saveBookingButtonActionPerformed

    private void bookRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookRoomButtonActionPerformed
        Booking newBooking = null;
        if (roomJList.getSelectedValue() == null || customerJList.getSelectedValue() == null) {
            jOptionPane.showMessageDialog(this, "You must select a room and a customer!");
        }
        else {
            Room room = (Room) roomJList.getSelectedValue(); //typecast til room-objekt
            Customer customer = (Customer) customerJList.getSelectedValue(); ////typecast til customer-objekt
            newBooking = ctr.bookRoom(room, customer, checkIn.getDate(), checkOut.getDate());
            if (newBooking != null) {
                newBookingModel.addElement(newBooking);
                newBookingJList.setModel(newBookingModel);
                jOptionPane.showMessageDialog(this, "Room booked by " + customer.getFirstName() + " " + customer.getLastName());
            }
            else {
                jOptionPane.showMessageDialog(this, "Room is already booked for that period!", "Doublebooking", jOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_bookRoomButtonActionPerformed

    private void BookingDetailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BookingDetailButtonActionPerformed
        bookingJList.setEnabled(false);
    }//GEN-LAST:event_BookingDetailButtonActionPerformed

    private void checkOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkOutActionPerformed
        roomModel.clear();
    }//GEN-LAST:event_checkOutActionPerformed

    private void saveNewGuestsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveNewGuestsButtonActionPerformed
        boolean commitSuccess;
        int reply = jOptionPane.showConfirmDialog(this, "Are you sure you want to save?", "Save?", jOptionPane.YES_NO_OPTION);
        if (reply == jOptionPane.YES_OPTION) {
            commitSuccess = ctr.commitTransaction();
            if (commitSuccess) {
                jOptionPane.showMessageDialog(this, "Guests added to booking!");
                refreshModel(infoBookingModel);
                addedGuestsModel.clear();
            }
            else {
                jOptionPane.showMessageDialog(this, "Something went wrong in adding the guest(s) to the booking. No changes have been saved.", "Error adding guest(s)", jOptionPane.ERROR_MESSAGE);
                addedGuestsModel.clear();
            }
        }
    }//GEN-LAST:event_saveNewGuestsButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String street = streetTextField.getText();
        String zipcode = zipcodeTextField.getText();
        String city = cityTextField.getText();
        String country = countryTextField.getText();
        String email = emailTextField.getText();
        int phone1 = Integer.parseInt(phone1TextField.getText());
        int phone2 = Integer.parseInt(phone2TextField.getText());

        Customer customer = new Customer(firstName, lastName, street, zipcode, city, country, email, phone1, phone2);
        ctr.createCustomer(customer);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void phone1TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phone1TextFieldKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {   // Vi tillader at der indtastes tal fra 0-9 og DELETE og BACKSPACE. Indtaster man andre tegn får man en fejl-lyd.
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_phone1TextFieldKeyTyped

    private void phone2TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phone2TextFieldKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_phone2TextFieldKeyTyped

    private void firstNameTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_firstNameTextFieldKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_firstNameTextFieldKeyTyped

    private void lastNameTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lastNameTextFieldKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_lastNameTextFieldKeyTyped

    private void streetTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_streetTextFieldKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isAlphabetic(c) || Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_streetTextFieldKeyTyped

    private void zipcodeTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zipcodeTextFieldKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isAlphabetic(c) || Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_zipcodeTextFieldKeyTyped

    private void cityTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cityTextFieldKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_cityTextFieldKeyTyped

    private void countryTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_countryTextFieldKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_countryTextFieldKeyTyped

    private void emailTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTextFieldKeyTyped
        char c = evt.getKeyChar();
        if (c == KeyEvent.VK_SPACE) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_emailTextFieldKeyTyped

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
            java.util.logging.Logger.getLogger(GUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JButton BookingDetailButton;
    private javax.swing.JButton addGuestToRoomButton;
    private javax.swing.JList addedGuestsJList;
    private javax.swing.JButton bookRoomButton;
    private javax.swing.JList bookingInfoJList;
    private javax.swing.JList bookingJList;
    private javax.swing.JButton checkAvailabilityButton;
    private org.jdesktop.swingx.JXDatePicker checkIn;
    private org.jdesktop.swingx.JXDatePicker checkOut;
    private javax.swing.JTextField cityTextField;
    private javax.swing.JTextField countryTextField;
    private javax.swing.JList customerJList;
    private javax.swing.JList customerJList2;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JTextField firstNameTextField;
    private javax.swing.JTable guestTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JOptionPane jOptionPane;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField lastNameTextField;
    private javax.swing.JList newBookingJList;
    private javax.swing.JTextField phone1TextField;
    private javax.swing.JTextField phone2TextField;
    private javax.swing.JList roomJList;
    private javax.swing.JButton saveBookingButton;
    private javax.swing.JButton saveNewGuestsButton;
    private javax.swing.JTextField streetTextField;
    private javax.swing.JTextField zipcodeTextField;
    // End of variables declaration//GEN-END:variables
}
