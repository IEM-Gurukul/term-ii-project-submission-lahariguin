package models;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Guest {
    private Map<Integer, Map<String, String>> guests = new HashMap<>();
    private static final String[] guestMap = {"name", "phone"};
    private static final String FILE_PATH = "data/guests.csv";

    public Guest() {
        load();
    }

    private void load() {
        guests.clear();
        try (Scanner scan = new Scanner(new File(FILE_PATH))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();
                if (line.isEmpty()) continue;
                parseLine(line);
            }
        } catch (Exception e) {
            System.out.println("Cannot read guests.");
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
                else if (count - 1 < guestMap.length) details.put(guestMap[count - 1], val);
                count++;
            }
            guests.put(id, details);
        } catch (Exception e) {
            System.out.println("Error reading guest.");
        }
    }

    public Map<Integer, Map<String, String>> getGuests() { return guests; }

    public Map<String, String> getById(int id) {
        Map<String, String> g = guests.get(id);
        if (g != null) { Map<String, String> result = new HashMap<>(g); result.put("id", String.valueOf(id)); return result; }
        return null;
    }

    public List<Map<String, String>> searchByName(String name) {
        List<Map<String, String>> result = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> e : guests.entrySet()) {
            if (e.getValue().getOrDefault("name","").toLowerCase().contains(name.toLowerCase())) {
                Map<String, String> g = new HashMap<>(e.getValue());
                g.put("id", String.valueOf(e.getKey()));
                result.add(g);
            }
        }
        return result;
    }

    public int getNextId() {
        return guests.keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
    }

    public boolean addGuest(String name, String phone) {
        int id = getNextId();
        Map<String, String> g = new HashMap<>();
        g.put("name", name);
        g.put("phone", phone);
        guests.put(id, g);
        return save();
    }

    public boolean updateGuest(int id, String name, String phone) {
        if (!guests.containsKey(id)) return false;
        guests.get(id).put("name", name);
        guests.get(id).put("phone", phone);
        return save();
    }

    private boolean save() {
        try (FileWriter fw = new FileWriter(FILE_PATH)) {
            for (int id : guests.keySet()) {
                Map<String, String> g = guests.get(id);
                fw.write(id + "," + g.getOrDefault("name","") + "," + g.getOrDefault("phone","") + "\n");
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error saving guests.");
            return false;
        }
    }
}
