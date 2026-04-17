package com.marella.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class WhatsAppService {

    private final String INSTANCE_ID = System.getenv("ULTRAMSG_INSTANCE_ID");
    private final String TOKEN = System.getenv("ULTRAMSG_TOKEN");

    public void sendMessage(String to, String messageText) {

        try {
            // 🔹 Step 1: Clean spaces
            if (to != null) {
                to = to.replaceAll("\\s+", "");
            }

            // 🔹 Step 2: Remove + if exists
            if (to.startsWith("+")) {
                to = to.substring(1);
            }

            // 🔹 Step 3: If 10-digit → add 91
            if (to.matches("\\d{10}")) {
                to = "91" + to;
            }

            // 🔹 Step 4: Final validation
            if (!to.startsWith("91")) {
                throw new RuntimeException("Invalid phone number: " + to);
            }

            // 🔹 Step 5: Add + back
            to = "+" + to;

            System.out.println("📞 Sending to: " + to);

            // 🔹 Step 6: API URL
            String url = "https://api.ultramsg.com/" + INSTANCE_ID + "/messages/chat";

            RestTemplate restTemplate = new RestTemplate();

            // 🔹 Step 7: Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // 🔹 Step 8: Body
            String body = "token=" + TOKEN +
                    "&to=" + to +
                    "&body=" + URLEncoder.encode(messageText, StandardCharsets.UTF_8);

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            // 🔹 Step 9: Send request
            String response = restTemplate.postForObject(url, request, String.class);

            System.out.println("✅ WhatsApp SENT via UltraMsg");
            System.out.println("📩 Response: " + response);

        } catch (Exception e) {
            System.out.println("❌ WhatsApp FAILED");
            e.printStackTrace();
        }
    }
}