package com.marella.service;

import com.marella.model.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GuestService {

    @Autowired
    private GoogleSheetsService sheetsService;

    @Autowired
    private WhatsAppService whatsappService;

    public String checkInGuest(Guest guest) {

        if (sheetsService.isRoomOccupied(guest.getRoomNumber())) {
            return "❌ Room already occupied!";
        }

        guest.setCheckInTime(LocalDateTime.now().toString());
        guest.setStatus("Checked-In");

        sheetsService.saveGuest(guest);

        // ✅ Custom WhatsApp Message
        String message =
                "🏨 Welcome To Hotel Marella Royal Inn\n" +
                "📞 Reception: 7795951743\n\n" +

                "Dear " + guest.getName() + ",\n\n" +

                "Welcome to Marella Royal Inn 🌟\n" +
                "We are delighted to have you with us 😊\n\n" +

                "📶 WiFi Details\n" +
                "Network: Marella_Floor_Name\n" +
                "Password: 7795951743\n\n" +

                "🛏 Room No: " + guest.getRoomNumber() + "\n" +
                "🕒 Check-out Time: 11:00 AM\n\n" +

                "✨ Have a pleasant stay!";

        whatsappService.sendMessage(guest.getPhone(), message);

        return "✅ Check-in successful!";
    }

    public String checkOutGuest(Guest guest) {

        Guest existing = sheetsService.findGuestByRoom(guest.getRoomNumber());

        if (existing == null) {
            return "❌ No active guest found!";
        }

        guest.setCheckOutTime(LocalDateTime.now().toString());
        guest.setStatus("Checked-Out");

        sheetsService.updateCheckout(guest);

        // ✅ Custom WhatsApp Message
        String message =
        		"🏨 Hotel Marella Royal Inn\n" +
        				"📞 Reception: 7795951743\n\n" +

        				"Dear " + existing.getName() + ",\n\n" +

        				"Thank you for choosing Marella Royal Inn 🙏\n" +
        				"We hope you had a pleasant and comfortable stay with us 😊\n\n" +

        				"⭐ We would love to hear your feedback:\n" +
        				"https://maps.app.goo.gl/GydQ1ogJ28W9V3Hr8\n\n" +

        				"If you enjoyed your stay, we would truly appreciate a 5⭐ rating from you 🙏\n" +
        				"Your feedback helps us improve and serve you better.\n\n" +

        				"✨ We look forward to welcoming you again!\n" +
        				"Warm regards,\n" +
        				"Marella Royal Inn Team";

        whatsappService.sendMessage(existing.getPhone(), message);

        return "✅ Check-out successful!";
    }

    public List<List<Object>> getAllGuests() {
        return sheetsService.getAllGuests();
    }

    public Guest findByRoom(String room) {
        return sheetsService.findGuestByRoom(room);
    }
}