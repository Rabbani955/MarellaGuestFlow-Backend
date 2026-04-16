package com.marella.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

	private final String ACCOUNT_SID = System.getenv("TWILIO_SID");
	private final String AUTH_TOKEN = System.getenv("TWILIO_TOKEN");

    public void sendMessage(String to, String messageText) {

        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            // ✅ Clean spaces
            if (to != null) {
                to = to.replaceAll("\\s+", "");
            }

            // ✅ Convert to +91 format
            if (to != null && to.matches("\\d{10}")) {
                to = "+91" + to;
            }

            if (!to.startsWith("+")) {
                throw new RuntimeException("Invalid phone number: " + to);
            }

            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber("whatsapp:" + to),
                    new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                    messageText
            ).create();

            System.out.println("✅ WhatsApp SENT: " + message.getSid());

        } catch (Exception e) {
            System.out.println("❌ WhatsApp FAILED");
            e.printStackTrace();
        }
    }
}