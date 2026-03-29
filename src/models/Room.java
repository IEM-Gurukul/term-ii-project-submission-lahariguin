package models;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Room {
    private Map<Integer, Map<String, String>> rooms = new HashMap<>();
    private static final String[] roomMap = {"type", "price", "status"};
    private static final String FILE_PATH = "data/rooms.csv";

    public Room() {
        load();
    }

    private void load() {
        rooms.clear();
        try (Scanner scan = new Scanner(new File(FILE_PATH))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();
                if (line.isEmpty()) continue;
                parseLine(line);
            }
        } catch (Exception e) {
            System.out.println("Cannot read rooms.");
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
                else if (count - 1 < roomMap.length) details.put(roomMap[count - 1], val);
                count++;
            }
            rooms.put(id, details);
        } catch (Exception e) {
            System.out.println("Error reading room.");
        }
    }

    public Map<Integer, Map<String, String>> getRooms() { return rooms; }

    public Map<String, String> getById(int id) {
        Map<String, String> r = rooms.get(id);
        if (r != null) { Map<String, String> result = new HashMap<>(r); result.put("id", String.valueOf(id)); return result; }
        return null;
    }

    public int getNextId() {
        return rooms.keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
    }

    public boolean setStatus(int id, String status) {
        if (!rooms.containsKey(id)) return false;
        rooms.get(id).put("status", status);
        return save();
    }

    public boolean addRoom(String type, String price) {
        int id = getNextId();
        Map<String, String> r = new HashMap<>();
        r.put("type", type);
        r.put("price", price);
        r.put("status", "available");
        rooms.put(id, r);
        return save();
    }

    public boolean removeRoom(int id) {
        if (!rooms.containsKey(id)) return false;
        rooms.remove(id);
        return save();
    }

    private boolean save() {
        try (FileWriter fw = new FileWriter(FILE_PATH)) {
            for (int id : rooms.keySet()) {
                Map<String, String> r = rooms.get(id);
                fw.write(id + "," + r.getOrDefault("type","") + "," + r.getOrDefault("price","") + "," + r.getOrDefault("status","") + "\n");
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error saving rooms.");
            return false;
        }
    }
}
