/*
 * Semester Projekt, Datamatiker 2. semester
 * Gruppe 4: Andreas, Michael og Sebastian
 */

package domain;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Gæst klassen repræsenterer hotellets gæst.
 *
 * @author Sebastian, Michael og Andreas
 */
public class Guest {

    private int guestId;
    private String firstName;
    private String lastName;
    private String street;
    private String zipcode;
    private String city;
    private String country;
    private String email;
    private int phone1;
    private int phone2;

    /**
     * Constructor som bruges til at instansierer allerede eksisterende gæster,
     * hentet fra databasen.
     *
     * @param rs    ResultSet indeholder gæstens parametre fra databasen.
     */
    public Guest(ResultSet rs) {
        super();
        try {
            this.guestId = rs.getInt("guest_id");
            this.firstName = rs.getString("first_name");
            this.lastName = rs.getString("last_name");;
            this.street = rs.getString("street");
            this.zipcode = rs.getString("zipcode");
            this.city = rs.getString("city");
            this.country = rs.getString("country");
            this.email = rs.getString("email");
            this.phone1 = rs.getInt("phone_1");
            this.phone2 = rs.getInt("phone_2");
        }
        catch (SQLException e) {
            System.out.println("Fail in GuestMapper/Guest - Guest");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Constructor som bruges når vi opretter en ny gæst. I modsætning til
     * ovenstående constructor, indeholder den ikke guestId, som vi først henter
     * i databasen i samme transaktion, for at sikre data-integritet.
     * - Bruges af GUI.getGuestFieldData()
     *
     * @param firstName
     * @param lastName
     * @param street
     * @param zipcode
     * @param city
     * @param country
     * @param email
     * @param phone1
     * @param phone2
     */
    public Guest(String firstName, String lastName, String street, String zipcode, String city, String country, String email, int phone1, int phone2) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
        this.email = email;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }
    
    /**
     * Constructor som bruges når vi ændrer en allerede eksisterende gæsts 
     * informationer, hvorfor den allerede indeholder guestId. 
     * - Bruges af GUI.getGuestFieldData()
     *
     * @param guestId
     * @param firstName
     * @param lastName
     * @param street
     * @param zipcode
     * @param city
     * @param country
     * @param email
     * @param phone1
     * @param phone2
     */
    public Guest(int guestId, String firstName, String lastName, String street, String zipcode, String city, String country, String email, int phone1, int phone2) {
        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
        this.email = email;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone1() {
        return phone1;
    }

    public void setPhone1(int privatePhone) {
        this.phone1 = privatePhone;
    }

    public int getPhone2() {
        return phone2;
    }

    public void setPhone2(int workPhone) {
        this.phone2 = workPhone;
    }

    @Override
    public String toString() {
        String str = "";
        str = "ID: " + guestId + "   " + firstName + " " + lastName + "\n";
        return str;
    }
}
