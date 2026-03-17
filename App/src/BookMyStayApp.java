import java.util.*;

// ---------------- Reservation ----------------
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

// ---------------- Booking Queue ----------------
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

// ---------------- Room Inventory ----------------
class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 2);
        roomAvailability.put("Double", 1);
        roomAvailability.put("Suite", 1);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

// ---------------- Room Allocation ----------------
class RoomAllocationService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> assignedRoomsByType = new HashMap<>();

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();
        Map<String, Integer> availability = inventory.getRoomAvailability();

        int available = availability.getOrDefault(roomType, 0);

        if (available <= 0) {
            System.out.println("❌ No rooms available for " + roomType +
                    " (Guest: " + reservation.getGuestName() + ")");
            return;
        }

        String roomId = generateRoomId(roomType);

        allocatedRoomIds.add(roomId);

        assignedRoomsByType.putIfAbsent(roomType, new HashSet<>());
        assignedRoomsByType.get(roomType).add(roomId);

        inventory.updateAvailability(roomType, available - 1);

        System.out.println("✅ Booking confirmed for Guest: "
                + reservation.getGuestName() + ", Room ID: " + roomId);
    }

    private String generateRoomId(String roomType) {
        int count = assignedRoomsByType
                .getOrDefault(roomType, new HashSet<>()).size() + 1;
        return roomType + "-" + count;
    }
}

// ---------------- Main ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=== Booking Request Queue ===");
        System.out.println("=== Room Allocation Processing ===\n");

        BookingRequestQueue queue = new BookingRequestQueue();

        // Add requests
        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));
        queue.addRequest(new Reservation("Rahul", "Double"));
        queue.addRequest(new Reservation("Kiran", "Double")); // extra (will fail)

        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();

        // Process queue
        while (queue.hasPendingRequests()) {
            Reservation r = queue.getNextRequest();

            System.out.println("Processing: "
                    + r.getGuestName() + " → " + r.getRoomType());

            service.allocateRoom(r, inventory);
        }

        System.out.println("\n=== Final Availability ===");
        System.out.println(inventory.getRoomAvailability());
    }
}