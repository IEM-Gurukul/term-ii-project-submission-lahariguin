package services;

import models.Guest;
import java.util.*;

public class GuestService {
    private final Guest guestModel;

    public GuestService(Guest guestModel) {
        this.guestModel = guestModel;
    }

    public Map<Integer, Map<String, String>> getAllGuests() {
        return guestModel.getGuests();
    }

    public Map<String, String> getGuestById(int id) {
        return guestModel.getById(id);
    }

    public List<Map<String, String>> searchByName(String name) {
        return guestModel.searchByName(name);
    }

    public int registerGuest(String name, String phone) {
        int nextId = guestModel.getNextId();
        guestModel.addGuest(name, phone);
        return nextId;
    }

    public boolean updateGuest(int id, String name, String phone) {
        return guestModel.updateGuest(id, name, phone);
    }
}
