package services;

import models.Booking;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BookingService {
    private final Booking bookingModel;
    private final RoomService roomService;
    private final GuestService guestService;

    public BookingService(Booking bookingModel, RoomService roomService, GuestService guestService) {
        this.bookingModel = bookingModel;
        this.roomService = roomService;
        this.guestService = guestService;
    }

    public String book(int guestId, int roomId, String checkIn, String checkOut) {
        if (guestService.getGuestById(guestId) == null)
            return "Guest not found.";
        if (!roomService.isAvailable(roomId))
            return "Room not available.";

        LocalDate in, out;
        try {
            in  = LocalDate.parse(checkIn,  DateTimeFormatter.ISO_DATE);
            out = LocalDate.parse(checkOut, DateTimeFormatter.ISO_DATE);
        } catch (Exception e) {
            return "Invalid date format. Use YYYY-MM-DD.";
        }
        if (!out.isAfter(in))
            return "Check-out must be after check-in.";

        long nights = ChronoUnit.DAYS.between(in, out);
        int pricePerNight = roomService.getPricePerNight(roomId);
        int total = (int)(nights * pricePerNight);

        int id = bookingModel.createBooking(
            String.valueOf(guestId), String.valueOf(roomId),
            checkIn, checkOut, String.valueOf(total)
        );
        roomService.bookRoom(roomId);
        return "Booked. ID: " + id + " | Nights: " + nights + " | Total: Rs." + total;
    }

    public String checkout(int bookingId) {
        Map<String, String> b = bookingModel.getById(bookingId);
        if (b == null) return "Booking not found.";
        if (!"active".equals(b.get("status"))) return "Booking is not active.";
        int roomId = Integer.parseInt(b.get("roomId"));
        bookingModel.checkout(bookingId);
        roomService.freeRoom(roomId);
        return "Checked out. Total was Rs." + b.getOrDefault("totalAmount","0");
    }

    public String cancel(int bookingId) {
        Map<String, String> b = bookingModel.getById(bookingId);
        if (b == null) return "Booking not found.";
        if (!"active".equals(b.get("status"))) return "Booking is not active.";
        int roomId = Integer.parseInt(b.get("roomId"));
        bookingModel.cancelBooking(bookingId);
        roomService.freeRoom(roomId);
        return "Booking cancelled.";
    }

    public Map<String, String> getBookingById(int id) {
        return bookingModel.getById(id);
    }

    public List<Map<String, String>> getActiveBookings() {
        return bookingModel.getActiveBookings();
    }

    public List<Map<String, String>> getAllBookings() {
        return bookingModel.getAllBookings();
    }

    public List<Map<String, String>> getBookingsByGuest(int guestId) {
        return bookingModel.getByGuest(guestId);
    }
}
