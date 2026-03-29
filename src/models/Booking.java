package models;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Booking {
    private Map<Integer, Map<String, String>> bookings = new HashMap<>();
    private static final String[] bookingMap = {"guestId", "roomId", "checkIn", "checkOut", "status", "totalAmount"};
    private static final String FILE_PATH = "data/bookings.csv";

    public Booking() {
        load();
    }

    private void load() {
        bookings.clear();
        try (Scanner scan = new Scanner(new File(FILE_PATH))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();
                if (line.isEmpty()) continue;
                parseLine(line);
            }
        } catch (Exception e) {
            // file may be empty, that's fine
        }
    }

    private void parseLine(String line) {
        try (Scanner s = new Scanner(line)) {
            s.useDelimiter(",");
            Map<String, String> details = new HashMap<>();
            int count = 0, id = 0;
            while (s.hasNext()) {
                String val = s.next().trim();
                if (count == 0) id = Integer.parseInt(val);
                else if (count - 1 < bookingMap.length) details.put(bookingMap[count - 1], val);
                count++;
            }
            bookings.put(id, details);
        } catch (Exception e) {
            System.out.println("Error reading booking.");
        }
    }

    public Map<Integer, Map<String, String>> getBookings() { return bookings; }

    public Map<String, String> getById(int id) {
        Map<String, String> b = bookings.get(id);
        if (b != null) { Map<String, String> result = new HashMap<>(b); result.put("id", String.valueOf(id)); return result; }
        return null;
    }

    public int getNextId() {
        return bookings.keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
    }

    public int createBooking(String guestId, String roomId, String checkIn, String checkOut, String totalAmount) {
        int id = getNextId();
        Map<String, String> b = new HashMap<>();
        b.put("guestId", guestId);
        b.put("roomId", roomId);
        b.put("checkIn", checkIn);
        b.put("checkOut", checkOut);
        b.put("status", "active");
        b.put("totalAmount", totalAmount);
        bookings.put(id, b);
        save();
        return id;
    }

    public boolean checkout(int id) {
        if (!bookings.containsKey(id)) return false;
        bookings.get(id).put("status", "checkedout");
        return save();
    }

    public boolean cancelBooking(int id) {
        if (!bookings.containsKey(id)) return false;
        bookings.get(id).put("status", "cancelled");
        return save();
    }

    public List<Map<String, String>> getByGuest(int guestId) {
        List<Map<String, String>> result = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> e : bookings.entrySet()) {
            if (e.getValue().get("guestId").equals(String.valueOf(guestId))) {
                Map<String, String> b = new HashMap<>(e.getValue());
                b.put("id", String.valueOf(e.getKey()));
                result.add(b);
            }
        }
        return result;
    }

    public List<Map<String, String>> getActiveBookings() {
        List<Map<String, String>> result = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> e : bookings.entrySet()) {
            if ("active".equals(e.getValue().get("status"))) {
                Map<String, String> b = new HashMap<>(e.getValue());
                b.put("id", String.valueOf(e.getKey()));
                result.add(b);
            }
        }
        return result;
    }

    public List<Map<String, String>> getAllBookings() {
        List<Map<String, String>> result = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> e : bookings.entrySet()) {
            Map<String, String> b = new HashMap<>(e.getValue());
            b.put("id", String.valueOf(e.getKey()));
            result.add(b);
        }
        return result;
    }

    private boolean save() {
        try (FileWriter fw = new FileWriter(FILE_PATH)) {
            for (int id : bookings.keySet()) {
                Map<String, String> b = bookings.get(id);
                fw.write(id
                    + "," + b.getOrDefault("guestId","")
                    + "," + b.getOrDefault("roomId","")
                    + "," + b.getOrDefault("checkIn","")
                    + "," + b.getOrDefault("checkOut","")
                    + "," + b.getOrDefault("status","")
                    + "," + b.getOrDefault("totalAmount","")
                    + "\n");
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error saving bookings.");
            return false;
        }
    }
}
