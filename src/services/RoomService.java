package services;

import models.Room;
import java.util.*;

public class RoomService {
    private final Room roomModel;

    public RoomService(Room roomModel) {
        this.roomModel = roomModel;
    }

    public Map<Integer, Map<String, String>> getAllRooms() {
        return roomModel.getRooms();
    }

    public Map<String, String> getRoomById(int id) {
        return roomModel.getById(id);
    }

    public List<Map<String, String>> getAvailableRooms() {
        List<Map<String, String>> result = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> e : roomModel.getRooms().entrySet()) {
            if ("available".equals(e.getValue().get("status"))) {
                Map<String, String> r = new HashMap<>(e.getValue());
                r.put("id", String.valueOf(e.getKey()));
                result.add(r);
            }
        }
        return result;
    }

    public boolean isAvailable(int id) {
        Map<String, String> r = roomModel.getById(id);
        return r != null && "available".equals(r.get("status"));
    }

    public boolean bookRoom(int id) {
        return roomModel.setStatus(id, "booked");
    }

    public boolean freeRoom(int id) {
        return roomModel.setStatus(id, "available");
    }

    public boolean addRoom(String type, String price) {
        return roomModel.addRoom(type, price);
    }

    public boolean removeRoom(int id) {
        return roomModel.removeRoom(id);
    }

    public int getPricePerNight(int id) {
        Map<String, String> r = roomModel.getById(id);
        if (r == null) return 0;
        try { return Integer.parseInt(r.getOrDefault("price","0")); }
        catch (NumberFormatException e) { return 0; }
    }
}
