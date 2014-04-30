/*
 * Semester Projekt, Datamatiker 2. semester
 * Gruppe 4: Andreas, Michael og Sebastian
 */

package domain;

/**
 * BookingDetail klassen repræsenterer gæster tilføjet til en booking.
 *
 * @author Andreas, Michael & Sebastian
 */
public class BookingDetail {
    private int guestId;
    private int bookingId;

    /**
     * Constructor som består af gæstens ID og bookingens ID.
     *
     * @param guestId       Gæstens ID.
     * @param bookingId     Bookingens ID.
     */
    public BookingDetail(int guestId, int bookingId) {
        this.guestId = guestId;
        this.bookingId = bookingId;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
}
