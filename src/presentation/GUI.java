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
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JTable;
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
        startDefaultTableModel();
        saveChangesButton.setEnabled(false);

        refreshModel(customerModel);
        refreshModel(bookingModel);
        startGuestTable();
        refreshGuestTable(guestTableModel);
    }

    private void startDefaultTableModel() {
        guestTableModel = new DefaultTableModel() {
            public boolean isCellEditable(int rowIndex, int columnIndex) {  // Sørger for at vores guestTable bliver instantioeret og bliver låst, så man ikke kan ændre data i rækkerne.
                return false;
            }
        };
    }

    private void startGuestTable() {
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
    }

    private void refreshGuestTable(DefaultTableModel guestTableModel) {
        guestTableModel.setRowCount(0);
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
            guestInfoArray[8] = customer.getPhone1();
            guestInfoArray[9] = customer.getPhone2();
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

    private void clearCustomerFields() {
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        streetTextField.setText("");
        zipcodeTextField.setText("");
        cityTextField.setText("");
        countryTextField.setText("");
        phone1TextField.setText("");
        phone2TextField.setText("");
        emailTextField.setText("");

        createGuestButton.setEnabled(true);
        saveChangesButton.setEnabled(false);
    }

    private boolean getCustomerFieldData(String str) {
        boolean actionSuccess = false;
        String firstName = "", lastName = "", street = "", zipcode = "", city = "", country = "", email = "";
        int customerId = 0, phone1 = 0, phone2 = 0;

        if (!firstNameTextField.getText().isEmpty()) {
            firstName = firstNameTextField.getText();
        }
        else {
            firstNameLabel.setForeground(Color.red);
        }
        if (!lastNameTextField.getText().isEmpty()) {
            lastName = lastNameTextField.getText();
        }
        else {
            lastNameLabel.setForeground(Color.red);
        }
        if (!streetTextField.getText().isEmpty()) {
            street = streetTextField.getText();
        }
        else {
            streetLabel.setForeground(Color.red);
        }
        if (!zipcodeTextField.getText().isEmpty()) {
            zipcode = zipcodeTextField.getText();
        }
        else {
            zipcodeLabel.setForeground(Color.red);
        }
        if (!cityTextField.getText().isEmpty()) {
            city = cityTextField.getText();
        }
        else {
            cityLabel.setForeground(Color.red);
        }
        if (!countryTextField.getText().isEmpty()) {
            country = countryTextField.getText();
        }
        else {
            countryLabel.setForeground(Color.red);
        }
        if (!emailTextField.getText().isEmpty()) {
            email = emailTextField.getText();
        }
        else {
            emailLabel.setForeground(Color.red);
        }
        if (!phone1TextField.getText().isEmpty()) {
            phone1 = Integer.parseInt(phone1TextField.getText());
        }
        else {
            phone1Label.setForeground(Color.red);
        }
        if (!phone2TextField.getText().isEmpty()) {
            phone2 = Integer.parseInt(phone2TextField.getText());
        }

        if (!(firstName.isEmpty() || lastName.isEmpty() || street.isEmpty() || zipcode.isEmpty() || city.isEmpty() || country.isEmpty() || email.isEmpty() || phone1 == 0)) {
            if (str.equals("saveCustomer")) {
                customerId = Integer.parseInt(customerIdLabel.getText());
                Customer customer = new Customer(customerId, firstName, lastName, street, zipcode, city, country, email, phone1, phone2);
                actionSuccess = ctr.updateCustomerDB(customer);
            }
            if (str.equals("createCustomer")) {
                Customer customer = new Customer(firstName, lastName, street, zipcode, city, country, email, phone1, phone2);
                actionSuccess = ctr.createCustomer(customer);
            }
            ctr.commitTransaction();
            refreshGuestTable(guestTableModel);
            refreshModel(customerModel);
        }
        return actionSuccess;
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
        firstNameLabel = new javax.swing.JLabel();
        lastNameLabel = new javax.swing.JLabel();
        streetLabel = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        zipcodeLabel = new javax.swing.JLabel();
        cityLabel = new javax.swing.JLabel();
        countryLabel = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        phone1Label = new javax.swing.JLabel();
        phone2Label = new javax.swing.JLabel();
        newGuestLabel = new javax.swing.JLabel();
        createGuestButton = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        guestTable = new javax.swing.JTable();
        mandatoryLabel = new javax.swing.JLabel();
        editCustomerButton = new javax.swing.JButton();
        saveChangesButton = new javax.swing.JButton();
        clearCustomerFieldsButton = new javax.swing.JButton();
        customerIdLabel = new javax.swing.JLabel();

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
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(saveBookingButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1))))
                .addContainerGap(123, Short.MAX_VALUE))
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        firstNameLabel.setText("First name *");

        lastNameLabel.setText("Family name *");

        streetLabel.setText("Street *");

        addressLabel.setText("Address:");

        zipcodeLabel.setText("Zipcode *");

        cityLabel.setText("City *");

        countryLabel.setText("Country *");

        emailLabel.setText("E-mail *");

        phone1Label.setText("Phone #1 *");

        phone2Label.setText("Phone #2");

        newGuestLabel.setText("Register new guest:");

        createGuestButton.setText("Create guest");
        createGuestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createGuestButtonActionPerformed(evt);
            }
        });

        guestTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        guestTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane7.setViewportView(guestTable);

        mandatoryLabel.setText("* mandatory");

        editCustomerButton.setText("Edit guest");
        editCustomerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerButtonActionPerformed(evt);
            }
        });

        saveChangesButton.setText("Save changes");
        saveChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveChangesButtonActionPerformed(evt);
            }
        });

        clearCustomerFieldsButton.setText("Clear");
        clearCustomerFieldsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearCustomerFieldsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(addressLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(countryLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(phone1Label, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(phone2Label, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mandatoryLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(newGuestLabel)
                                .addGap(18, 18, 18)
                                .addComponent(customerIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(lastNameLabel)
                                .addGap(18, 18, 18)
                                .addComponent(lastNameTextField))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(streetLabel)
                                    .addComponent(zipcodeLabel)
                                    .addComponent(cityLabel)
                                    .addComponent(emailLabel)
                                    .addComponent(firstNameLabel))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(emailTextField)
                                    .addComponent(phone2TextField)
                                    .addComponent(phone1TextField)
                                    .addComponent(countryTextField)
                                    .addComponent(cityTextField)
                                    .addComponent(streetTextField)
                                    .addComponent(firstNameTextField)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(zipcodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(createGuestButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(saveChangesButton, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
                                        .addGap(63, 63, 63)))))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 837, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(clearCustomerFieldsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newGuestLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(firstNameLabel)
                            .addComponent(firstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lastNameLabel)
                            .addComponent(lastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addComponent(addressLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(streetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(streetLabel))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zipcodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(zipcodeLabel))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cityLabel)
                            .addComponent(cityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(countryLabel)
                            .addComponent(countryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(phone1Label)
                            .addComponent(phone1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(phone2Label)
                            .addComponent(phone2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emailLabel)
                            .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mandatoryLabel))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createGuestButton)
                    .addComponent(editCustomerButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveChangesButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clearCustomerFieldsButton)
                .addContainerGap(154, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Customers", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1202, Short.MAX_VALUE)
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

    private void createGuestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createGuestButtonActionPerformed
        boolean status = false;
        status = getCustomerFieldData("createCustomer");
        if (status) {
            clearCustomerFields();
        }
        else {
            jOptionPane.showMessageDialog(this, "An error occured. Customer was not created.", "Create failed", jOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_createGuestButtonActionPerformed

    private void phone1TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phone1TextFieldKeyTyped
        phone1Label.setForeground(Color.black);
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {   // Vi tillader at der indtastes tal fra 0-9 og DELETE og BACKSPACE. Indtaster man andre tegn får man en fejl-lyd.
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_phone1TextFieldKeyTyped

    private void phone2TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phone2TextFieldKeyTyped
        phone2Label.setForeground(Color.black);
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_phone2TextFieldKeyTyped

    private void firstNameTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_firstNameTextFieldKeyTyped
        firstNameLabel.setForeground(Color.black);
        int strLength = firstNameTextField.getText().length();      // Vi checker længden på vores String så vi kan begrænse dens max længde i vores IF-statement nedenfor.
        firstNameTextField.setForeground(Color.BLACK);
        char c = evt.getKeyChar();
        if (strLength == 20 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_firstNameTextFieldKeyTyped

    private void lastNameTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lastNameTextFieldKeyTyped
        lastNameLabel.setForeground(Color.black);
        int strLength = lastNameTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 30 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_lastNameTextFieldKeyTyped

    private void zipcodeTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zipcodeTextFieldKeyTyped
        zipcodeLabel.setForeground(Color.black);
        int strLength = zipcodeTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 10 || !(Character.isAlphabetic(c) || Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_zipcodeTextFieldKeyTyped

    private void cityTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cityTextFieldKeyTyped
        cityLabel.setForeground(Color.black);
        int strLength = cityTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 30 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_cityTextFieldKeyTyped

    private void countryTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_countryTextFieldKeyTyped
        countryLabel.setForeground(Color.black);
        int strLength = countryTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 30 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_countryTextFieldKeyTyped

    private void emailTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTextFieldKeyTyped
        emailLabel.setForeground(Color.black);
        int strLength = emailTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 70 || c == KeyEvent.VK_SPACE) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_emailTextFieldKeyTyped

    private void streetTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_streetTextFieldKeyTyped
        streetLabel.setForeground(Color.black);
        int strLength = emailTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 100) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_streetTextFieldKeyTyped

    private void editCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCustomerButtonActionPerformed
        if (guestTable.getSelectedRow() > -1) {
            int selectedRowIndex = guestTable.getSelectedRow();
            int customerId = (Integer) guestTableModel.getValueAt(selectedRowIndex, 0);
            Customer customer = ctr.getGuestFromID(customerId);

            if (customer != null) {
                customerIdLabel.setText(Integer.toString(customer.getCustomerId()));
                firstNameTextField.setText(customer.getFirstName());
                lastNameTextField.setText(customer.getLastName());
                streetTextField.setText(customer.getStreet());
                zipcodeTextField.setText(customer.getZipcode());
                cityTextField.setText(customer.getCity());
                countryTextField.setText(customer.getCountry());
                phone1TextField.setText(Integer.toString(customer.getPhone1()));
                phone2TextField.setText(Integer.toString(customer.getPhone2()));
                emailTextField.setText(customer.getEmail());

                newGuestLabel.setText("Customer ID:");
                createGuestButton.setEnabled(false);
                saveChangesButton.setEnabled(true);
            }
        }
        else {
            jOptionPane.showMessageDialog(this, "You must first select a customer.", "Select customer", jOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_editCustomerButtonActionPerformed

    private void clearCustomerFieldsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearCustomerFieldsButtonActionPerformed
        clearCustomerFields();
        createGuestButton.setEnabled(true);
        customerIdLabel.setText("");
        newGuestLabel.setText("Register new guest:");
    }//GEN-LAST:event_clearCustomerFieldsButtonActionPerformed

    private void saveChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveChangesButtonActionPerformed
        boolean status;
        status = getCustomerFieldData("saveCustomer");
        if (status) {
            clearCustomerFields();
            createGuestButton.setEnabled(true);
            customerIdLabel.setText("");
            newGuestLabel.setText("Register new guest:");
        }
        else {
            jOptionPane.showMessageDialog(this, "An error occured. No changes were saved.", "Save failed", jOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_saveChangesButtonActionPerformed

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
    private javax.swing.JButton addGuestToRoomButton;
    private javax.swing.JList addedGuestsJList;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JButton bookRoomButton;
    private javax.swing.JList bookingInfoJList;
    private javax.swing.JList bookingJList;
    private javax.swing.JButton checkAvailabilityButton;
    private org.jdesktop.swingx.JXDatePicker checkIn;
    private org.jdesktop.swingx.JXDatePicker checkOut;
    private javax.swing.JLabel cityLabel;
    private javax.swing.JTextField cityTextField;
    private javax.swing.JButton clearCustomerFieldsButton;
    private javax.swing.JLabel countryLabel;
    private javax.swing.JTextField countryTextField;
    private javax.swing.JButton createGuestButton;
    private javax.swing.JLabel customerIdLabel;
    private javax.swing.JList customerJList;
    private javax.swing.JList customerJList2;
    private javax.swing.JButton editCustomerButton;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameTextField;
    private javax.swing.JTable guestTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JTextField lastNameTextField;
    private javax.swing.JLabel mandatoryLabel;
    private javax.swing.JList newBookingJList;
    private javax.swing.JLabel newGuestLabel;
    private javax.swing.JLabel phone1Label;
    private javax.swing.JTextField phone1TextField;
    private javax.swing.JLabel phone2Label;
    private javax.swing.JTextField phone2TextField;
    private javax.swing.JList roomJList;
    private javax.swing.JButton saveBookingButton;
    private javax.swing.JButton saveChangesButton;
    private javax.swing.JButton saveNewGuestsButton;
    private javax.swing.JLabel streetLabel;
    private javax.swing.JTextField streetTextField;
    private javax.swing.JLabel zipcodeLabel;
    private javax.swing.JTextField zipcodeTextField;
    // End of variables declaration//GEN-END:variables
}
