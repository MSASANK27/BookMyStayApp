import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.Queue;

// -------------------- Reservation --------------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- Room Inventory --------------------
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }
}

// -------------------- Abstract Room --------------------
abstract class Room {
    int beds;
    int size;
    double price;

    Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    abstract void display(int available);
}

// -------------------- Room Types --------------------
class SingleRoom extends Room {
    SingleRoom() {
        super(1, 250, 1500.0);
    }

    void display(int available) {
        System.out.println("Single Room:");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price: " + price);
        System.out.println("Available: " + available + "\n");
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super(2, 400, 2500.0);
    }

    void display(int available) {
        System.out.println("Double Room:");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price: " + price);
        System.out.println("Available: " + available + "\n");
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super(3, 750, 5000.0);
    }

    void display(int available) {
        System.out.println("Suite Room:");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price: " + price);
        System.out.println("Available: " + available + "\n");
    }
}

// -------------------- Booking Queue --------------------
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

// -------------------- Room Search --------------------
class RoomSearchService {

    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        if (availability.getOrDefault("Single", 0) > 0) {
            singleRoom.display(availability.get("Single"));
        }

        if (availability.getOrDefault("Double", 0) > 0) {
            doubleRoom.display(availability.get("Double"));
        }

        if (availability.getOrDefault("Suite", 0) > 0) {
            suiteRoom.display(availability.get("Suite"));
        }
    }
}

// -------------------- Main Class --------------------
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to Hotel Booking System\n");

        RoomInventory inventory = new RoomInventory();

        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomSearchService service = new RoomSearchService();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Add booking requests
        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Subha", "Double"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Suite"));

        System.out.println("Available Rooms:\n");
        service.searchAvailableRooms(inventory, single, dbl, suite);

        System.out.println("Processing Booking Requests:\n");

        while (bookingQueue.hasPendingRequests()) {
            Reservation r = bookingQueue.getNextRequest();
            System.out.println("Booking for Guest: "
                    + r.getGuestName() + " | Room: " + r.getRoomType());
        }
    }
}