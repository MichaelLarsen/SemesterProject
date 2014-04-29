/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import domain.*;
import java.awt.Color;
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
    private Booking chosenBooking;
    private DefaultListModel<Guest> guestModel;
    private DefaultListModel<Booking> newBookingModel;
    private DefaultListModel bookingDetailModel;
    private DefaultListModel addedGuestsModel;
    private DefaultTableModel guestTableModel;
    private DefaultTableModel roomTableModel;
    private DefaultTableModel bookingTableModel;

    /**
     * Creates new form GUI
     */
    public GUI() {
        ctr = new Control();
        initComponents();
        guestModel = new DefaultListModel<>();
        newBookingModel = new DefaultListModel<>();
        bookingDetailModel = new DefaultListModel<>();
        addedGuestsModel = new DefaultListModel<>();
        startDefaultTableModel();
        saveGuestChangesButton.setEnabled(false);

        refreshModel(guestModel);
        startRoomTable();
        startGuestTable();
        startBookingTable();
        refreshGuestTable(guestTableModel);
        refreshBookingTable(bookingTableModel);
    }

    private void startDefaultTableModel() {
        guestTableModel = new DefaultTableModel() {
            public boolean isCellEditable(int rowIndex, int columnIndex) {  // Sørger for at vores guestTable bliver instantieret og bliver låst, så man ikke kan ændre data i rækkerne.
                return false;
            }
        };
        roomTableModel = new DefaultTableModel() {
            public boolean isCellEditable(int rowIndex, int columnIndex) {  // Sørger for at vores guestTable bliver instantieret og bliver låst, så man ikke kan ændre data i rækkerne.
                return false;
            }
        };
        bookingTableModel = new DefaultTableModel() {
            public boolean isCellEditable(int rowIndex, int columnIndex) {  // Sørger for at vores guestTable bliver instantieret og bliver låst, så man ikke kan ændre data i rækkerne.
                return false;
            }
        };
    }

    private void startRoomTable() {
        roomTableModel.setColumnCount(0);
        roomTableModel.addColumn("Room Number");
        roomTableModel.addColumn("Type");
        roomTableModel.addColumn("Room Size");
        roomTableModel.addColumn("Price in USD");
    }

    private void startBookingTable() {
        bookingTableModel.setColumnCount(0);
        bookingTableModel.addColumn("Booking id");
        bookingTableModel.addColumn("Booking owner(ID)");
        bookingTableModel.addColumn("Room number");
        bookingTableModel.addColumn("Agency");
        bookingTableModel.addColumn("Check in");
        bookingTableModel.addColumn("Check out");
    }

    private void startGuestTable() {
        guestTableModel.setColumnCount(0);
        guestTableModel.addColumn("Guest ID");
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

    private void refreshRoomTable(DefaultTableModel roomTableModel, Date checkIn, Date checkOut) {
        roomTableModel.setRowCount(0);
        roomTableModel.getDataVector().removeAllElements();
        roomTableModel.fireTableDataChanged();
        ArrayList<Room> roomList;
        roomList = ctr.getAvailableRoomsDB(checkIn, checkOut);
        for (Room room : roomList) {
            Object[] roomArray = new Object[4];
            roomArray[0] = room.getRoomNo();
            roomArray[1] = room.getRoomTypeString();
            roomArray[2] = room.getRoomSize();
            roomArray[3] = room.getPrice();
            roomTableModel.addRow(roomArray);
        }
        roomTable.setModel(roomTableModel);
    }

    private void refreshBookingTable(DefaultTableModel bookingTableModel) {
        bookingTableModel.setRowCount(0);
        bookingTableModel.getDataVector().removeAllElements();
        bookingTableModel.fireTableDataChanged();
        ArrayList<Booking> bookingList;
        bookingList = ctr.getBookingsFromDB();
        for (Booking booking : bookingList) {
            Object[] bookingArray = new Object[6];
            bookingArray[0] = booking.getBookingId();
            bookingArray[1] = booking.getBookingOwnerId();
            bookingArray[2] = booking.getRoomNo();
            bookingArray[3] = booking.getAgency();
            bookingArray[4] = booking.getCheckInDate();
            bookingArray[5] = booking.getCheckOutDate();
            bookingTableModel.addRow(bookingArray);
        }
        bookingTable.setModel(bookingTableModel);
    }

    private void refreshGuestTable(DefaultTableModel guestTableModel) {
        guestTableModel.setRowCount(0);
        ArrayList<Guest> guestList;
        guestList = ctr.getGuestsFromDB();

        for (Guest guest : guestList) {
            Object[] guestInfoArray = new Object[10];

            guestInfoArray[0] = guest.getGuestId();
            guestInfoArray[1] = guest.getFirstName();
            guestInfoArray[2] = guest.getLastName();
            guestInfoArray[3] = guest.getStreet();
            guestInfoArray[4] = guest.getZipcode();
            guestInfoArray[5] = guest.getCity();
            guestInfoArray[6] = guest.getCountry();
            guestInfoArray[7] = guest.getEmail();
            guestInfoArray[8] = guest.getPhone1();
            guestInfoArray[9] = guest.getPhone2();
            guestTableModel.addRow(guestInfoArray);
        }
        guestTable.setModel(guestTableModel);
    }

    //med en model kan man sende ligeså mange dates med som man vil
    private void refreshModel(DefaultListModel model, Date... dates) {
        if (model.equals(guestModel)) {
            ArrayList<Guest> guestList;
            guestList = ctr.getGuestsFromDB();
            guestModel.clear();
            for (int i = 0; i < guestList.size(); i++) {
                guestModel.addElement(guestList.get(i));
            }
            guestJList.setModel(guestModel);
            guestJList2.setModel(guestModel);
        }

        if (model.equals(bookingDetailModel)) {
            bookingDetailModel.clear();
            ArrayList<Guest> roomGuestList;
            roomGuestList = ctr.getBookingDetailsFromDB(chosenBooking);

            for (int i = 0; i < roomGuestList.size(); i++) {
                bookingDetailModel.addElement(roomGuestList.get(i));
            }
            bookingDetailsJList.setModel(bookingDetailModel);
        }
    }

    private void clearGuestFields() {
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
        saveGuestChangesButton.setEnabled(false);
    }

    private boolean getGuestFieldData(String str) {
        boolean actionSuccess = false;
        String firstName = "", lastName = "", street = "", zipcode = "", city = "", country = "", email = "";
        int guestId = 0, phone1 = 0, phone2 = 0;

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
            if (str.equals("saveGuest")) {
                guestId = Integer.parseInt(guestIdLabel.getText());
                Guest guest = new Guest(guestId, firstName, lastName, street, zipcode, city, country, email, phone1, phone2);
                actionSuccess = ctr.updateGuestDB(guest);
            }
            if (str.equals("createGuest")) {
                Guest guest = new Guest(firstName, lastName, street, zipcode, city, country, email, phone1, phone2);
                actionSuccess = ctr.createGuest(guest);
            }
            ctr.commitTransaction();
            refreshGuestTable(guestTableModel);
            refreshModel(guestModel);
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
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        introductionLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        guestJList = new javax.swing.JList();
        bookRoomButton = new javax.swing.JButton();
        saveBookingButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        newBookingJList = new javax.swing.JList();
        checkIn = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        checkOut = new org.jdesktop.swingx.JXDatePicker();
        jLabel2 = new javax.swing.JLabel();
        checkAvailabilityButton = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        roomTable = new javax.swing.JTable();
        searchLastNameField = new javax.swing.JTextField();
        searchFirstNameField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        searchGuestButton = new javax.swing.JButton();
        newBookingUndoButton = new javax.swing.JButton();
        newBookingStatusLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        deleteBookingButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        bookingTable = new javax.swing.JTable();
        guestsToBookingButton = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        bookingDetailsJList1 = new javax.swing.JList();
        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        bookingIdLabel3 = new javax.swing.JLabel();
        bookingOwnerLabel3 = new javax.swing.JLabel();
        roomNoLabel3 = new javax.swing.JLabel();
        agencyLabel3 = new javax.swing.JLabel();
        checkInLabel3 = new javax.swing.JLabel();
        checkOutLabel3 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        bookingIdLabel = new javax.swing.JLabel();
        bookingOwnerLabel = new javax.swing.JLabel();
        roomNoLabel = new javax.swing.JLabel();
        agencyLabel = new javax.swing.JLabel();
        checkInLabel = new javax.swing.JLabel();
        checkOutLabel = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        bookingDetailsJList = new javax.swing.JList();
        jLabel16 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        guestJList2 = new javax.swing.JList();
        searchFirstNameField2 = new javax.swing.JTextField();
        searchLastNameField2 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        searchGuestButton2 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        addedGuestsJList = new javax.swing.JList();
        addGuestToRoomButton = new javax.swing.JButton();
        clearAddedGuestsButton = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        saveNewGuestsButton = new javax.swing.JButton();
        guestPanel4 = new javax.swing.JPanel();
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
        editGuestButton = new javax.swing.JButton();
        saveGuestChangesButton = new javax.swing.JButton();
        clearGuestFieldsButton = new javax.swing.JButton();
        guestIdLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        getLogButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentation/image/Casablanca_logo.jpg"))); // NOI18N

        introductionLabel.setText("Casablanca Holiday Center Booking System");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(logo)
                .addGap(186, 186, 186)
                .addComponent(introductionLabel)
                .addContainerGap(555, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(logo))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(introductionLabel)))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Home", jPanel9);

        guestJList.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane1.setViewportView(guestJList);

        bookRoomButton.setText("Book room");
        bookRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookRoomButtonActionPerformed(evt);
            }
        });

        saveBookingButton.setText("Save Booking");
        saveBookingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBookingButtonActionPerformed(evt);
            }
        });

        newBookingJList.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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

        roomTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane9.setViewportView(roomTable);

        searchLastNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchLastNameFieldKeyTyped(evt);
            }
        });

        searchFirstNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchFirstNameFieldKeyTyped(evt);
            }
        });

        jLabel4.setText("First name:");

        jLabel5.setText("Last name:");

        searchGuestButton.setText("Search");
        searchGuestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchGuestButtonActionPerformed(evt);
            }
        });

        newBookingUndoButton.setText("Undo booking");
        newBookingUndoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBookingUndoButtonActionPerformed(evt);
            }
        });

        jLabel13.setText("New bookings");

        jLabel14.setText("Guests");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(checkOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(checkAvailabilityButton)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bookRoomButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(searchFirstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(searchLastNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(searchGuestButton)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(saveBookingButton)
                                        .addGap(28, 28, 28)
                                        .addComponent(newBookingUndoButton))
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)))
                            .addComponent(jLabel14))
                        .addContainerGap(181, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(newBookingStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 773, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchLastNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchFirstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchGuestButton))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14)
                                .addGap(3, 3, 3)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(newBookingUndoButton)
                            .addComponent(saveBookingButton)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bookRoomButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 171, Short.MAX_VALUE)
                .addComponent(newBookingStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane.addTab("New Booking", jPanel1);

        deleteBookingButton.setText("Delete booking");
        deleteBookingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBookingButtonActionPerformed(evt);
            }
        });

        bookingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        bookingTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        bookingTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookingTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(bookingTable);

        guestsToBookingButton.setText("Add guests to booking");
        guestsToBookingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guestsToBookingButtonActionPerformed(evt);
            }
        });

        bookingDetailsJList1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane10.setViewportView(bookingDetailsJList1);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Booking details"));

        jLabel25.setText("Booking ID:");

        jLabel26.setText("Booking owner (ID):");

        jLabel27.setText("Room number:");

        jLabel28.setText("Agency:");

        jLabel29.setText("Check in:");

        jLabel30.setText("Check out:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel28)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(agencyLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roomNoLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bookingOwnerLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bookingIdLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(checkOutLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(checkInLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(bookingIdLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel26)
                    .addComponent(bookingOwnerLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27)
                    .addComponent(roomNoLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28)
                    .addComponent(agencyLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(checkInLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(checkOutLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addGap(28, 28, 28))
        );

        jLabel15.setText("Guests in room");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jLabel15))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(guestsToBookingButton)
                        .addGap(40, 40, 40)
                        .addComponent(deleteBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 902, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(guestsToBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Booking overview", jPanel2);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Booking details"));

        jLabel3.setText("Booking ID:");

        jLabel6.setText("Booking owner (ID):");

        jLabel7.setText("Room number:");

        jLabel8.setText("Agency:");

        jLabel9.setText("Check in:");

        jLabel10.setText("Check out:");

        bookingDetailsJList.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane5.setViewportView(bookingDetailsJList);

        jLabel16.setText("Guests in room");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(agencyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(roomNoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bookingOwnerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bookingIdLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(checkOutLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(checkInLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(bookingIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(bookingOwnerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(roomNoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(agencyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(checkInLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(checkOutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Add guest"));

        guestJList2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane8.setViewportView(guestJList2);

        searchFirstNameField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchFirstNameField2KeyTyped(evt);
            }
        });

        searchLastNameField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchLastNameField2KeyTyped(evt);
            }
        });

        jLabel11.setText("First name:");

        jLabel12.setText("Last name:");

        searchGuestButton2.setText("Search");
        searchGuestButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchGuestButton2ActionPerformed(evt);
            }
        });

        addedGuestsJList.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane6.setViewportView(addedGuestsJList);

        addGuestToRoomButton.setText("Add guest to booking");
        addGuestToRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGuestToRoomButtonActionPerformed(evt);
            }
        });

        clearAddedGuestsButton.setText("Clear");
        clearAddedGuestsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAddedGuestsButtonActionPerformed(evt);
            }
        });

        jLabel17.setText("Guests");

        jLabel18.setText("Guests to add");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel10Layout.createSequentialGroup()
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(searchFirstNameField2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel17))
                                    .addGap(30, 30, 30)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel10Layout.createSequentialGroup()
                                            .addComponent(searchLastNameField2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(searchGuestButton2))
                                        .addGroup(jPanel10Layout.createSequentialGroup()
                                            .addComponent(jLabel12)
                                            .addGap(0, 0, Short.MAX_VALUE)))))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(clearAddedGuestsButton)
                                .addGap(36, 36, 36))))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(addGuestToRoomButton))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchGuestButton2)
                    .addComponent(searchFirstNameField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchLastNameField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel17)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addGuestToRoomButton)
                .addGap(3, 3, 3)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearAddedGuestsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 18, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(saveNewGuestsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(373, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveNewGuestsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );

        jTabbedPane.addTab("Booking details", jPanel3);

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

        editGuestButton.setText("Edit guest");
        editGuestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editGuestButtonActionPerformed(evt);
            }
        });

        saveGuestChangesButton.setText("Save changes");
        saveGuestChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveGuestChangesButtonActionPerformed(evt);
            }
        });

        clearGuestFieldsButton.setText("Clear");
        clearGuestFieldsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearGuestFieldsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout guestPanel4Layout = new javax.swing.GroupLayout(guestPanel4);
        guestPanel4.setLayout(guestPanel4Layout);
        guestPanel4Layout.setHorizontalGroup(
            guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(guestPanel4Layout.createSequentialGroup()
                .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(guestPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(addressLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(countryLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(phone1Label, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(phone2Label, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mandatoryLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, guestPanel4Layout.createSequentialGroup()
                                .addComponent(newGuestLabel)
                                .addGap(18, 18, 18)
                                .addComponent(guestIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, guestPanel4Layout.createSequentialGroup()
                                .addComponent(lastNameLabel)
                                .addGap(18, 18, 18)
                                .addComponent(lastNameTextField))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, guestPanel4Layout.createSequentialGroup()
                                .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(streetLabel)
                                    .addComponent(zipcodeLabel)
                                    .addComponent(cityLabel)
                                    .addComponent(emailLabel)
                                    .addComponent(firstNameLabel))
                                .addGap(27, 27, 27)
                                .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(emailTextField)
                                    .addComponent(phone2TextField)
                                    .addComponent(phone1TextField)
                                    .addComponent(countryTextField)
                                    .addComponent(cityTextField)
                                    .addComponent(streetTextField)
                                    .addComponent(firstNameTextField)
                                    .addGroup(guestPanel4Layout.createSequentialGroup()
                                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(zipcodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(createGuestButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(saveGuestChangesButton, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
                                        .addGap(63, 63, 63)))))
                        .addGap(40, 40, 40)
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 837, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editGuestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(guestPanel4Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(clearGuestFieldsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(287, Short.MAX_VALUE))
        );
        guestPanel4Layout.setVerticalGroup(
            guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(guestPanel4Layout.createSequentialGroup()
                .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(guestPanel4Layout.createSequentialGroup()
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newGuestLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(guestIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(firstNameLabel)
                            .addComponent(firstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lastNameLabel)
                            .addComponent(lastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addComponent(addressLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(streetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(streetLabel))
                        .addGap(18, 18, 18)
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zipcodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(zipcodeLabel))
                        .addGap(18, 18, 18)
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cityLabel)
                            .addComponent(cityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(countryLabel)
                            .addComponent(countryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(phone1Label)
                            .addComponent(phone1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(phone2Label)
                            .addComponent(phone2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emailLabel)
                            .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mandatoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guestPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createGuestButton)
                    .addComponent(editGuestButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveGuestChangesButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clearGuestFieldsButton)
                .addContainerGap(88, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Guests", guestPanel4);

        logTextArea.setColumns(20);
        logTextArea.setRows(5);
        jScrollPane3.setViewportView(logTextArea);

        getLogButton.setText("Get log");
        getLogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getLogButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1299, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(getLogButton))
                .addContainerGap(151, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(getLogButton)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Log", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clearGuestFieldsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearGuestFieldsButtonActionPerformed
        clearGuestFields();
        createGuestButton.setEnabled(true);
        guestIdLabel.setText("");
        newGuestLabel.setText("Register new guest:");
    }//GEN-LAST:event_clearGuestFieldsButtonActionPerformed

    private void saveGuestChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveGuestChangesButtonActionPerformed
        boolean status;
        status = getGuestFieldData("saveGuest");
        if (status) {
            clearGuestFields();
            createGuestButton.setEnabled(true);
            guestIdLabel.setText("");
            newGuestLabel.setText("Register new guest:");
        }
        else {
            jOptionPane.showMessageDialog(this, "An error occured. No changes were saved.", "Save failed", jOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_saveGuestChangesButtonActionPerformed

    private void editGuestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editGuestButtonActionPerformed
        if (guestTable.getSelectedRow() > -1) {
            int selectedRowIndex = guestTable.getSelectedRow();
            int guestId = (Integer) guestTableModel.getValueAt(selectedRowIndex, 0);
            Guest guest = ctr.getGuestFromID(guestId);

            if (guest != null) {
                guestIdLabel.setText(Integer.toString(guest.getGuestId()));
                firstNameTextField.setText(guest.getFirstName());
                lastNameTextField.setText(guest.getLastName());
                streetTextField.setText(guest.getStreet());
                zipcodeTextField.setText(guest.getZipcode());
                cityTextField.setText(guest.getCity());
                countryTextField.setText(guest.getCountry());
                phone1TextField.setText(Integer.toString(guest.getPhone1()));
                phone2TextField.setText(Integer.toString(guest.getPhone2()));
                emailTextField.setText(guest.getEmail());

                newGuestLabel.setText("Guest ID:");
                createGuestButton.setEnabled(false);
                saveGuestChangesButton.setEnabled(true);
            }
        }
        else {
            jOptionPane.showMessageDialog(this, "You must first select a guest.", "Select guest", jOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_editGuestButtonActionPerformed

    private void createGuestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createGuestButtonActionPerformed
        boolean status = false;
        status = getGuestFieldData("createGuest");
        if (status) {
            clearGuestFields();
        }
        else {
            jOptionPane.showMessageDialog(this, "An error occured. Guest was not created.", "Create failed", jOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_createGuestButtonActionPerformed

    private void phone2TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phone2TextFieldKeyTyped
        phone2Label.setForeground(Color.black);
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_phone2TextFieldKeyTyped

    private void emailTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTextFieldKeyTyped
        emailLabel.setForeground(Color.black);
        int strLength = emailTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 70 || c == KeyEvent.VK_SPACE) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_emailTextFieldKeyTyped

    private void countryTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_countryTextFieldKeyTyped
        countryLabel.setForeground(Color.black);
        int strLength = countryTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 30 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_countryTextFieldKeyTyped

    private void phone1TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phone1TextFieldKeyTyped
        phone1Label.setForeground(Color.black);
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {   // Vi tillader at der indtastes tal fra 0-9 og DELETE og BACKSPACE. Indtaster man andre tegn får man en fejl-lyd.
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_phone1TextFieldKeyTyped

    private void cityTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cityTextFieldKeyTyped
        cityLabel.setForeground(Color.black);
        int strLength = cityTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 30 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_cityTextFieldKeyTyped

    private void streetTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_streetTextFieldKeyTyped
        streetLabel.setForeground(Color.black);
        int strLength = emailTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 100) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_streetTextFieldKeyTyped

    private void zipcodeTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zipcodeTextFieldKeyTyped
        zipcodeLabel.setForeground(Color.black);
        int strLength = zipcodeTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 10 || !(Character.isAlphabetic(c) || Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_zipcodeTextFieldKeyTyped

    private void lastNameTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lastNameTextFieldKeyTyped
        lastNameLabel.setForeground(Color.black);
        int strLength = lastNameTextField.getText().length();
        char c = evt.getKeyChar();
        if (strLength == 30 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_lastNameTextFieldKeyTyped

    private void firstNameTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_firstNameTextFieldKeyTyped
        firstNameLabel.setForeground(Color.black);
        int strLength = firstNameTextField.getText().length();      // Vi checker længden på vores String så vi kan begrænse dens max længde i vores IF-statement nedenfor.
        firstNameTextField.setForeground(Color.BLACK);
        char c = evt.getKeyChar();
        if (strLength == 40 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_firstNameTextFieldKeyTyped

    private void searchFirstNameField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFirstNameField2KeyTyped
        int strLength = searchFirstNameField2.getText().length();      // Vi checker længden på vores String så vi kan begrænse dens max længde i vores IF-statement nedenfor.
        char c = evt.getKeyChar();
        if (strLength == 40 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_searchFirstNameField2KeyTyped

    private void searchGuestButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchGuestButton2ActionPerformed
        guestModel.clear();
        String firstName = searchFirstNameField2.getText();
        String lastName = searchLastNameField2.getText();
        ArrayList<Guest> foundGuests = new ArrayList<>();
        if (!firstName.equals("") || !lastName.equals("")) {
            if (lastName.equals("")) {
                foundGuests = ctr.searchForGuestDB("firstName", firstName);
            }
            if (firstName.equals("")) {
                foundGuests = ctr.searchForGuestDB("lastName", lastName);
            }
            if (!firstName.equals("") && !lastName.equals("")) {
                foundGuests = ctr.searchForGuestDB("both", firstName, lastName);
            }
            for (int i = 0; i < foundGuests.size(); i++) {
                guestModel.addElement(foundGuests.get(i));
            }
            guestJList2.setModel(guestModel);
        }
        else {
            refreshModel(guestModel);
        }
    }//GEN-LAST:event_searchGuestButton2ActionPerformed

    private void saveNewGuestsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveNewGuestsButtonActionPerformed
        boolean commitSuccess;
        if (!addedGuestsModel.isEmpty()) {
            int reply = jOptionPane.showConfirmDialog(this, "Are you sure you want to save?", "Save?", jOptionPane.YES_NO_OPTION);
            if (reply == jOptionPane.YES_OPTION) {
                commitSuccess = ctr.commitTransaction();
                if (commitSuccess) {
                    jOptionPane.showMessageDialog(this, "Guests added to booking!");
                    refreshModel(bookingDetailModel);
                    addedGuestsModel.clear();
                }
                else {
                    jOptionPane.showMessageDialog(this, "Something went wrong in adding the guest(s) to the booking. No changes have been saved.", "Error adding guest(s)", jOptionPane.ERROR_MESSAGE);
                    addedGuestsModel.clear();
                }
            }
        }
        else {
            jOptionPane.showMessageDialog(this, "There is nothing to save.");
        }
    }//GEN-LAST:event_saveNewGuestsButtonActionPerformed

    private void addGuestToRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addGuestToRoomButtonActionPerformed
        boolean addGuestSuccess;
        if (chosenBooking != null && guestJList2.getSelectedValue() != null) {
            Guest guest = (Guest) guestJList2.getSelectedValue();
            if (!addedGuestsModel.contains(guest)) {
                if (ctr.checkRoomAvailability(chosenBooking, addedGuestsModel.getSize()) == true) {
                    addGuestSuccess = ctr.addGuestToRoom(guest, chosenBooking);
                    if (addGuestSuccess) {
                        addedGuestsModel.addElement(guest);
                        addedGuestsJList.setModel(addedGuestsModel);
                        jOptionPane.showMessageDialog(this, guest.getFirstName() + " " + guest.getLastName() + " added to roomNo " + chosenBooking.getRoomNo());
                    }
                    else {
                        jOptionPane.showMessageDialog(this, "Something went wrong in adding the guest to the room!\n Check for double booking.", "Error.", jOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else {
                    jOptionPane.showMessageDialog(this, "Cannot add guest because room is full!", "Room is full.", jOptionPane.INFORMATION_MESSAGE);
                }
            }
            else {
                jOptionPane.showMessageDialog(this, "Guest already added!", "Error.", jOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_addGuestToRoomButtonActionPerformed

    private void guestsToBookingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guestsToBookingButtonActionPerformed
        jTabbedPane.setSelectedIndex(3);
    }//GEN-LAST:event_guestsToBookingButtonActionPerformed

    private void bookingTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookingTableMouseClicked
        if (bookingTable.getSelectedRow() > -1) {
            int selectedRowIndex = bookingTable.getSelectedRow();
            int bookingId = (Integer) bookingTableModel.getValueAt(selectedRowIndex, 0);
            ArrayList<Booking> bookingList;
            bookingList = ctr.getBookings();

            for (Booking booking : bookingList) {
                if (booking.getBookingId() == bookingId) {
                    chosenBooking = booking;
                }
            }
            if (chosenBooking != null) {
                bookingDetailModel.clear();
                ArrayList<Guest> roomGuestList;
                roomGuestList = ctr.getBookingDetailsFromDB(chosenBooking);

                for (int i = 0; i < roomGuestList.size(); i++) {
                    bookingDetailModel.addElement(roomGuestList.get(i));
                }

                bookingDetailsJList.setModel(bookingDetailModel);
                bookingIdLabel.setText("" + (Integer) bookingTableModel.getValueAt(selectedRowIndex, 0));
                bookingOwnerLabel.setText("" + (Integer) bookingTableModel.getValueAt(selectedRowIndex, 1));
                roomNoLabel.setText("" + (Integer) bookingTableModel.getValueAt(selectedRowIndex, 2));
                agencyLabel.setText("" + bookingTableModel.getValueAt(selectedRowIndex, 3));
                checkInLabel.setText("" + bookingTableModel.getValueAt(selectedRowIndex, 4));
                checkOutLabel.setText("" + bookingTableModel.getValueAt(selectedRowIndex, 5));

                bookingDetailsJList1.setModel(bookingDetailModel);
                bookingIdLabel3.setText("" + (Integer) bookingTableModel.getValueAt(selectedRowIndex, 0));
                bookingOwnerLabel3.setText("" + (Integer) bookingTableModel.getValueAt(selectedRowIndex, 1));
                roomNoLabel3.setText("" + (Integer) bookingTableModel.getValueAt(selectedRowIndex, 2));
                agencyLabel3.setText("" + bookingTableModel.getValueAt(selectedRowIndex, 3));
                checkInLabel3.setText("" + bookingTableModel.getValueAt(selectedRowIndex, 4));
                checkOutLabel3.setText("" + bookingTableModel.getValueAt(selectedRowIndex, 5));

                if (evt.getClickCount() == 2) {
                    jTabbedPane.setSelectedIndex(3);
                }
            }
        }
    }//GEN-LAST:event_bookingTableMouseClicked

    private void deleteBookingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBookingButtonActionPerformed
        int reply = jOptionPane.showConfirmDialog(this, "Are you sure you want to delete this booking?", "Delete?", jOptionPane.YES_NO_OPTION);
        if (reply == jOptionPane.YES_OPTION) {
            boolean deleteSuccess;
            boolean commitSuccess;
            if (bookingTable.getSelectedRow() > -1) {
                int selectedRowIndex = bookingTable.getSelectedRow();
                int bookingId = (Integer) bookingTableModel.getValueAt(selectedRowIndex, 0);
                deleteSuccess = ctr.deleteBookingFromDB(bookingId);
                if (deleteSuccess) {
                    commitSuccess = ctr.commitTransaction();
                    if (commitSuccess) {
                        jOptionPane.showMessageDialog(this, "Booking deleted!");
                        refreshBookingTable(bookingTableModel);
                    }
                    else {
                        jOptionPane.showMessageDialog(this, "Fail in commit", "Commit failed", jOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    jOptionPane.showMessageDialog(this, "Something went wrong with the deletion", "Delete failed", jOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                jOptionPane.showMessageDialog(this, "You must choose a booking!");
            }
        }
    }//GEN-LAST:event_deleteBookingButtonActionPerformed

    private void searchGuestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchGuestButtonActionPerformed
        guestModel.clear();
        String firstName = searchFirstNameField.getText();
        String lastName = searchLastNameField.getText();
        ArrayList<Guest> foundGuests = new ArrayList<>();
        if (!firstName.equals("") || !lastName.equals("")) {
            if (lastName.equals("")) {
                foundGuests = ctr.searchForGuestDB("firstName", firstName);
            }
            if (firstName.equals("")) {
                foundGuests = ctr.searchForGuestDB("lastName", lastName);
            }
            if (!firstName.equals("") && !lastName.equals("")) {
                foundGuests = ctr.searchForGuestDB("both", firstName, lastName);
            }
            for (int i = 0; i < foundGuests.size(); i++) {
                guestModel.addElement(foundGuests.get(i));
            }
            guestJList.setModel(guestModel);
        }
        else {
            refreshModel(guestModel);
        }
    }//GEN-LAST:event_searchGuestButtonActionPerformed

    private void searchFirstNameFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFirstNameFieldKeyTyped
        int strLength = firstNameTextField.getText().length();      // Vi checker længden på vores String så vi kan begrænse dens max længde i vores IF-statement nedenfor.
        char c = evt.getKeyChar();
        if (strLength == 40 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_searchFirstNameFieldKeyTyped

    private void checkAvailabilityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkAvailabilityButtonActionPerformed
        Date checkInDate = checkIn.getDate();
        Date checkOutDate = checkOut.getDate();
        if (checkInDate != null && checkOutDate != null && !checkInDate.equals(checkOutDate) && checkOutDate.after(checkInDate)) {  // checker at begge datoer er valgt, og at checkOut dato er efter checkIn dato.
            refreshRoomTable(roomTableModel, checkInDate, checkOutDate);
        }
        else {
            jOptionPane.showMessageDialog(this, "You must choose a check-in and a check-out date. Check-in date must be before check-out date.", "Invalid dates!", jOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_checkAvailabilityButtonActionPerformed

    private void saveBookingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBookingButtonActionPerformed
        boolean commitSuccess;
        if (!newBookingModel.isEmpty()) {
            int reply = jOptionPane.showConfirmDialog(this, "Are you sure you want to save?", "Save?", jOptionPane.YES_NO_OPTION);
            if (reply == jOptionPane.YES_OPTION) {
                commitSuccess = ctr.saveBooking();
                ArrayList<Booking> bookingsNotSaved = ctr.getBookingsNotSaved();
                if (bookingsNotSaved.size() > 0) {
                    String stringShow = "";
                    for (Booking booking : bookingsNotSaved) {
                        String str = "Room: " + booking.getRoomNo() + " is already occupied from " + booking.getCheckInDate() + " to " + booking.getCheckOutDate() + "\n";
                        stringShow = stringShow.concat(str);
                    }
                    jOptionPane.showMessageDialog(this, stringShow);
                }
                if (commitSuccess) {
                    newBookingModel.clear();
                    newBookingStatusLabel.setText("Booking(s) were saved!");
                    refreshBookingTable(bookingTableModel);
                    refreshRoomTable(roomTableModel, checkIn.getDate(), checkOut.getDate());
                }
                else {
                    jOptionPane.showMessageDialog(this, "Something went wrong with saving the booking!", "Booking Error! - rollback", jOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else {
            newBookingStatusLabel.setText("There is no bookings to be saved.");
        }
    }//GEN-LAST:event_saveBookingButtonActionPerformed

    private void bookRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookRoomButtonActionPerformed
        if (roomTable.getSelectedRow() > -1) {
            Room chosenRoom = null;
            int selectedRowIndex = roomTable.getSelectedRow();
            int roomNo = (Integer) roomTableModel.getValueAt(selectedRowIndex, 0);
            ArrayList<Room> roomList;
            roomList = ctr.getRooms();

            for (Room room : roomList) {
                if (room.getRoomNo() == roomNo) {
                    chosenRoom = room;
                }
            }
            if (chosenRoom != null && guestJList.getSelectedValue() != null) {
                Booking newBooking = null;
                Guest guest = (Guest) guestJList.getSelectedValue(); ////typecast til guest-objekt
                newBooking = ctr.bookRoom(chosenRoom, guest, checkIn.getDate(), checkOut.getDate());
                if (newBooking != null) {
                    newBookingModel.addElement(newBooking);
                    newBookingJList.setModel(newBookingModel);
                    newBookingStatusLabel.setText("Room booked by " + guest.getFirstName() + " " + guest.getLastName());
                    roomTableModel.removeRow(selectedRowIndex);
                }
                else {
                    jOptionPane.showMessageDialog(this, "Room is already booked for that period!", "Doublebooking", jOptionPane.INFORMATION_MESSAGE);
                }
            }
            else {
                jOptionPane.showMessageDialog(this, "You must select a room and a guest!");
            }
        }
    }//GEN-LAST:event_bookRoomButtonActionPerformed

    private void newBookingUndoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBookingUndoButtonActionPerformed
        if (newBookingJList.getSelectedValue() != null) {
            Booking booking = (Booking) newBookingJList.getSelectedValue();
            newBookingModel.removeElement(booking);
            ctr.undoNewBooking(booking);
            newBookingStatusLabel.setText("Booking was removed.");
            refreshRoomTable(roomTableModel, checkIn.getDate(), checkOut.getDate());
        }
        else {
            jOptionPane.showMessageDialog(this, "You must select a booking to remove", "Remove booking.", jOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_newBookingUndoButtonActionPerformed

    private void getLogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getLogButtonActionPerformed
        logTextArea.setText("");
        ArrayList<String> bookingLogStrings;
        bookingLogStrings = ctr.getBookingLog();
        for (int i = 0; i < bookingLogStrings.size(); i++) {
            logTextArea.append(bookingLogStrings.get(i));
        }
        ArrayList<String> guestLogStrings;
        guestLogStrings = ctr.getGuestLog();
        for (int i = 0; i < guestLogStrings.size(); i++) {
            logTextArea.append(guestLogStrings.get(i));
        }
    }//GEN-LAST:event_getLogButtonActionPerformed

    private void searchLastNameFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchLastNameFieldKeyTyped
        int strLength = searchLastNameField.getText().length();      // Vi checker længden på vores String så vi kan begrænse dens max længde i vores IF-statement nedenfor.
        char c = evt.getKeyChar();
        if (strLength == 30 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_searchLastNameFieldKeyTyped

    private void searchLastNameField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchLastNameField2KeyTyped
        int strLength = searchLastNameField2.getText().length();      // Vi checker længden på vores String så vi kan begrænse dens max længde i vores IF-statement nedenfor.
        char c = evt.getKeyChar();
        if (strLength == 30 || !(Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_searchLastNameField2KeyTyped

    private void clearAddedGuestsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearAddedGuestsButtonActionPerformed
        if (!addedGuestsModel.isEmpty()) {
            addedGuestsModel.clear();
            ctr.clearNewBookingDetails();
        }
    }//GEN-LAST:event_clearAddedGuestsButtonActionPerformed

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneStateChanged
        if (jTabbedPane.getSelectedIndex() == 2) {
            refreshBookingTable(bookingTableModel);
        }
    }//GEN-LAST:event_jTabbedPaneStateChanged

    private void checkInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkInActionPerformed
        roomTableModel.setRowCount(0);
    }//GEN-LAST:event_checkInActionPerformed

    private void checkOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkOutActionPerformed
        roomTableModel.setRowCount(0);
    }//GEN-LAST:event_checkOutActionPerformed

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
    private javax.swing.JLabel agencyLabel;
    private javax.swing.JLabel agencyLabel3;
    private javax.swing.JButton bookRoomButton;
    private javax.swing.JList bookingDetailsJList;
    private javax.swing.JList bookingDetailsJList1;
    private javax.swing.JLabel bookingIdLabel;
    private javax.swing.JLabel bookingIdLabel3;
    private javax.swing.JLabel bookingOwnerLabel;
    private javax.swing.JLabel bookingOwnerLabel3;
    private javax.swing.JTable bookingTable;
    private javax.swing.JButton checkAvailabilityButton;
    private org.jdesktop.swingx.JXDatePicker checkIn;
    private javax.swing.JLabel checkInLabel;
    private javax.swing.JLabel checkInLabel3;
    private org.jdesktop.swingx.JXDatePicker checkOut;
    private javax.swing.JLabel checkOutLabel;
    private javax.swing.JLabel checkOutLabel3;
    private javax.swing.JLabel cityLabel;
    private javax.swing.JTextField cityTextField;
    private javax.swing.JButton clearAddedGuestsButton;
    private javax.swing.JButton clearGuestFieldsButton;
    private javax.swing.JLabel countryLabel;
    private javax.swing.JTextField countryTextField;
    private javax.swing.JButton createGuestButton;
    private javax.swing.JButton deleteBookingButton;
    private javax.swing.JButton editGuestButton;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameTextField;
    private javax.swing.JButton getLogButton;
    private javax.swing.JLabel guestIdLabel;
    private javax.swing.JList guestJList;
    private javax.swing.JList guestJList2;
    private javax.swing.JPanel guestPanel4;
    private javax.swing.JTable guestTable;
    private javax.swing.JButton guestsToBookingButton;
    private javax.swing.JLabel introductionLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JOptionPane jOptionPane;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JTextField lastNameTextField;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JLabel logo;
    private javax.swing.JLabel mandatoryLabel;
    private javax.swing.JList newBookingJList;
    private javax.swing.JLabel newBookingStatusLabel;
    private javax.swing.JButton newBookingUndoButton;
    private javax.swing.JLabel newGuestLabel;
    private javax.swing.JLabel phone1Label;
    private javax.swing.JTextField phone1TextField;
    private javax.swing.JLabel phone2Label;
    private javax.swing.JTextField phone2TextField;
    private javax.swing.JLabel roomNoLabel;
    private javax.swing.JLabel roomNoLabel3;
    private javax.swing.JTable roomTable;
    private javax.swing.JButton saveBookingButton;
    private javax.swing.JButton saveGuestChangesButton;
    private javax.swing.JButton saveNewGuestsButton;
    private javax.swing.JTextField searchFirstNameField;
    private javax.swing.JTextField searchFirstNameField2;
    private javax.swing.JButton searchGuestButton;
    private javax.swing.JButton searchGuestButton2;
    private javax.swing.JTextField searchLastNameField;
    private javax.swing.JTextField searchLastNameField2;
    private javax.swing.JLabel streetLabel;
    private javax.swing.JTextField streetTextField;
    private javax.swing.JLabel zipcodeLabel;
    private javax.swing.JTextField zipcodeTextField;
    // End of variables declaration//GEN-END:variables
}
