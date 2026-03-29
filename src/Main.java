import models.Booking;
import models.Guest;
import models.Room;
import services.BookingService;
import services.GuestService;
import services.RoomService;

import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static RoomService roomService;
    private static GuestService guestService;
    private static BookingService bookingService;

    public static void main(String[] args) {
        roomService    = new RoomService(new Room());
        guestService   = new GuestService(new Guest());
        bookingService = new BookingService(new Booking(), roomService, guestService);

        boolean running = true;
        while (running) {
            System.out.println("\n1. Rooms");
            System.out.println("2. Guests");
            System.out.println("3. Bookings");
            System.out.println("4. Reports");
            System.out.println("0. Exit");
            switch (readInt("> ")) {
                case 1 -> roomMenu();
                case 2 -> guestMenu();
                case 3 -> bookingMenu();
                case 4 -> reportsMenu();
                case 0 -> running = false;
                default -> System.out.println("Invalid.");
            }
        }
    }

    static void roomMenu() {
        System.out.println("\n1. All rooms");
        System.out.println("2. Available rooms");
        System.out.println("3. Room by ID");
        System.out.println("4. Add room");
        System.out.println("5. Remove room");
        System.out.println("0. Back");
        switch (readInt("> ")) {
            case 1 -> {
                Map<Integer, Map<String, String>> rooms = roomService.getAllRooms();
                if (rooms.isEmpty()) { System.out.println("No rooms."); break; }
                rooms.forEach((id, r) ->
                    System.out.println(id + ". " + r.getOrDefault("type","-") + " | Rs." + r.getOrDefault("price","-") + " | " + r.getOrDefault("status","-")));
            }
            case 2 -> {
                List<Map<String, String>> rooms = roomService.getAvailableRooms();
                if (rooms.isEmpty()) { System.out.println("No rooms available."); break; }
                for (Map<String, String> r : rooms)
                    System.out.println(r.get("id") + ". " + r.getOrDefault("type","-") + " | Rs." + r.getOrDefault("price","-"));
            }
            case 3 -> {
                int id = readInt("Room ID: ");
                Map<String, String> r = roomService.getRoomById(id);
                if (r == null) { System.out.println("Not found."); break; }
                System.out.println("ID: " + id);
                System.out.println("Type: " + r.getOrDefault("type","-"));
                System.out.println("Price: Rs." + r.getOrDefault("price","-"));
                System.out.println("Status: " + r.getOrDefault("status","-"));
            }
            case 4 -> {
                String type  = readString("Type (Single/Double/Suite): ");
                String price = readString("Price per night (Rs.): ");
                System.out.println(roomService.addRoom(type, price) ? "Room added." : "Failed.");
            }
            case 5 -> {
                int id = readInt("Room ID: ");
                System.out.print("Confirm? (y/n): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("y"))
                    System.out.println(roomService.removeRoom(id) ? "Removed." : "Failed.");
                else System.out.println("Cancelled.");
            }
            case 0 -> {}
            default -> System.out.println("Invalid.");
        }
    }

    static void guestMenu() {
        System.out.println("\n1. All guests");
        System.out.println("2. Guest by ID");
        System.out.println("3. Search by name");
        System.out.println("4. Register guest");
        System.out.println("5. Update guest");
        System.out.println("0. Back");
        switch (readInt("> ")) {
            case 1 -> {
                Map<Integer, Map<String, String>> guests = guestService.getAllGuests();
                if (guests.isEmpty()) { System.out.println("No guests."); break; }
                guests.forEach((id, g) ->
                    System.out.println(id + ". " + g.getOrDefault("name","-") + " | " + g.getOrDefault("phone","-")));
            }
            case 2 -> {
                int id = readInt("Guest ID: ");
                Map<String, String> g = guestService.getGuestById(id);
                if (g == null) { System.out.println("Not found."); break; }
                System.out.println("ID: " + id);
                System.out.println("Name: " + g.getOrDefault("name","-"));
                System.out.println("Phone: " + g.getOrDefault("phone","-"));
            }
            case 3 -> {
                List<Map<String, String>> results = guestService.searchByName(readString("Name: "));
                if (results.isEmpty()) { System.out.println("Not found."); break; }
                for (Map<String, String> g : results)
                    System.out.println(g.get("id") + ". " + g.getOrDefault("name","-") + " | " + g.getOrDefault("phone","-"));
            }
            case 4 -> {
                String name  = readString("Name: ");
                String phone = readString("Phone: ");
                int id = guestService.registerGuest(name, phone);
                System.out.println("Registered. Guest ID: " + id);
            }
            case 5 -> {
                int id = readInt("Guest ID: ");
                Map<String, String> g = guestService.getGuestById(id);
                if (g == null) { System.out.println("Not found."); break; }
                String name  = readStringDefault("Name [" + g.getOrDefault("name","") + "]: ", g.getOrDefault("name",""));
                String phone = readStringDefault("Phone [" + g.getOrDefault("phone","") + "]: ", g.getOrDefault("phone",""));
                System.out.println(guestService.updateGuest(id, name, phone) ? "Updated." : "Failed.");
            }
            case 0 -> {}
            default -> System.out.println("Invalid.");
        }
    }

    static void bookingMenu() {
        System.out.println("\n1. New booking");
        System.out.println("2. Checkout");
        System.out.println("3. Cancel booking");
        System.out.println("4. View booking");
        System.out.println("5. Bookings by guest");
        System.out.println("0. Back");
        switch (readInt("> ")) {
            case 1 -> {
                int guestId = readInt("Guest ID: ");
                int roomId  = readInt("Room ID: ");
                String checkIn  = readString("Check-in (YYYY-MM-DD): ");
                String checkOut = readString("Check-out (YYYY-MM-DD): ");
                System.out.println(bookingService.book(guestId, roomId, checkIn, checkOut));
            }
            case 2 -> System.out.println(bookingService.checkout(readInt("Booking ID: ")));
            case 3 -> System.out.println(bookingService.cancel(readInt("Booking ID: ")));
            case 4 -> {
                int id = readInt("Booking ID: ");
                Map<String, String> b = bookingService.getBookingById(id);
                if (b == null) { System.out.println("Not found."); break; }
                printBooking(b, id);
            }
            case 5 -> {
                int guestId = readInt("Guest ID: ");
                List<Map<String, String>> bookings = bookingService.getBookingsByGuest(guestId);
                if (bookings.isEmpty()) { System.out.println("No bookings."); break; }
                for (Map<String, String> b : bookings)
                    printBooking(b, Integer.parseInt(b.get("id")));
            }
            case 0 -> {}
            default -> System.out.println("Invalid.");
        }
    }

    static void reportsMenu() {
        System.out.println("\n1. Active bookings");
        System.out.println("2. All bookings");
        System.out.println("3. All available rooms");
        System.out.println("0. Back");
        switch (readInt("> ")) {
            case 1 -> {
                List<Map<String, String>> active = bookingService.getActiveBookings();
                if (active.isEmpty()) { System.out.println("No active bookings."); break; }
                for (Map<String, String> b : active)
                    printBooking(b, Integer.parseInt(b.get("id")));
            }
            case 2 -> {
                List<Map<String, String>> all = bookingService.getAllBookings();
                if (all.isEmpty()) { System.out.println("No bookings yet."); break; }
                for (Map<String, String> b : all)
                    printBooking(b, Integer.parseInt(b.get("id")));
            }
            case 3 -> {
                List<Map<String, String>> rooms = roomService.getAvailableRooms();
                if (rooms.isEmpty()) { System.out.println("No rooms available."); break; }
                for (Map<String, String> r : rooms)
                    System.out.println(r.get("id") + ". " + r.getOrDefault("type","-") + " | Rs." + r.getOrDefault("price","-"));
            }
            case 0 -> {}
            default -> System.out.println("Invalid.");
        }
    }

    static void printBooking(Map<String, String> b, int id) {
        System.out.println("Booking: " + id);
        System.out.println("Guest: " + b.get("guestId"));
        System.out.println("Room: " + b.get("roomId"));
        System.out.println("Check-in: " + b.get("checkIn"));
        System.out.println("Check-out: " + b.get("checkOut"));
        System.out.println("Total: Rs." + b.get("totalAmount"));
        System.out.println("Status: " + b.get("status"));
        System.out.println();
    }

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a number."); }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Double.parseDouble(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a number."); }
        }
    }

    static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    static String readStringDefault(String prompt, String def) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        return line.isEmpty() ? def : line;
    }
}
