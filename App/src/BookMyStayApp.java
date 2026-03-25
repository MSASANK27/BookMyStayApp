import java.io.*;
import java.util.*;

// Room Inventory Class
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        roomAvailability.put(type, count);
    }

    public Map<String, Integer> getAllRooms() {
        return roomAvailability;
    }

    public void setAllRooms(Map<String, Integer> data) {
        this.roomAvailability = data;
    }
}

// File Persistence Service
class FilePersistenceService {

    // Save inventory to file
    public void saveInventory(RoomInventory inventory, String filePath) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Map.Entry<String, Integer> entry : inventory.getAllRooms().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }

            System.out.println("Inventory saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    // Load inventory from file
    public void loadInventory(RoomInventory inventory, String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No valid inventory data found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            Map<String, Integer> data = new HashMap<>();

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("=");

                if (parts.length == 2) {
                    String roomType = parts[0];
                    int count = Integer.parseInt(parts[1]);
                    data.put(roomType, count);
                }
            }

            inventory.setAllRooms(data);
            System.out.println("Inventory loaded successfully.");

        } catch (Exception e) {
            System.out.println("Error loading inventory. Starting fresh.");
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        String filePath = "inventory.txt";

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistenceService = new FilePersistenceService();

        // System Recovery (Load)
        System.out.println("System Recovery");
        persistenceService.loadInventory(inventory, filePath);

        // Initialize if empty
        if (inventory.getAllRooms().isEmpty()) {
            inventory.addRoomType("Single", 5);
            inventory.addRoomType("Double", 3);
            inventory.addRoomType("Suite", 2);
        }

        // Display Inventory
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getAllRooms().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Save before exit
        persistenceService.saveInventory(inventory, filePath);
    }
}