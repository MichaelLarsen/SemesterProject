/*
 * Semester Projekt, Datamatiker 2. semester
 * Gruppe 4: Andreas, Michael og Sebastian
 */
package domain;

/**
 * Room klassen repræsenterer hotellets værelser.
 *
 * @author Sebastian, Michael og Andreas
 */
public class Room {

    private int roomNo;
    private int price;
    private RoomType roomType;
    private String roomTypeString;

    /**
     * Enumerationen bruges til at definere mulige rum-type og indeholder ligeledes
     * rummets størrelse.
     */
    private enum RoomType {

        SINGLE(1), DOUBLE(2), FAMILY(5);
        private int roomSize;

        private RoomType(int roomSize) {
            this.roomSize = roomSize;
        }
    }

    /**
     * Constructor som instansierer rum-objekter henter fra databasen.
     *
     * @param roomNo        Nummer på rummet.
     * @param type          Rummets type; Single, Double, Family.
     * @param price         Rummets pris.
     */
    public Room(int roomNo, String type, int price) {

        this.roomNo = roomNo;
        this.price = price;
        this.roomTypeString = type;
        if (type.equals("SINGLE")) {
            roomType = RoomType.SINGLE;
        }
        if (type.equals("DOUBLE")) {
            roomType = RoomType.DOUBLE;
        }
        if (type.equals("FAMILY")) {
            roomType = RoomType.FAMILY;
        }
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public String getRoomTypeString() {
        return roomTypeString;
    }

    public int getRoomSize() {
        return roomType.roomSize;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        String str = "RoomNo: " + roomNo + "       Type: " + roomTypeString + "        Price ($): " + price;
        return str;
    }
}
