package com.marella.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.marella.model.Guest;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class GoogleSheetsService {

    private static final String APPLICATION_NAME = "MarellaGuestFlow";
    private static final String SPREADSHEET_ID = "1A-xFvS_xphZMi0ewj6OJ__8YlBdXTSLEEuBr6CSiyPk";

    private Sheets getSheetsService() throws Exception {

        InputStream in = getClass().getResourceAsStream("/credentials.json");

        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName(APPLICATION_NAME).build();
    }

    // ✅ SAFE METHOD
    private String getSafe(Object obj) {
        return obj == null ? "" : obj.toString().trim();
    }

    // ✅ CHECK-IN SAVE
    public void saveGuest(Guest guest) {
        try {
            Sheets service = getSheetsService();

            List<Object> row = Arrays.asList(
                    getSafe(guest.getName()),        // A
                    getSafe(guest.getPhone()),       // B
                    getSafe(guest.getRoomNumber()),  // C
                    getSafe(guest.getCheckInTime()), // D
                    getSafe(guest.getGuests()),      // E
                    getSafe(guest.getBookingType()), // F
                    "",                              // G (Checkout)
                    getSafe(guest.getStatus())       // H
            );

            ValueRange body = new ValueRange().setValues(List.of(row));

            service.spreadsheets().values()
                    .append(SPREADSHEET_ID, "Sheet1!A1", body)
                    .setValueInputOption("RAW")
                    .execute();

            System.out.println("✅ Saved correctly");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ CHECKOUT UPDATE (FIXED)
    public void updateCheckout(Guest guest) {
        try {
            Sheets service = getSheetsService();

            ValueRange response = service.spreadsheets().values()
                    .get(SPREADSHEET_ID, "Sheet1!A2:H")
                    .execute();

            List<List<Object>> rows = response.getValues();

            if (rows == null) return;

            for (int i = 0; i < rows.size(); i++) {

                List<Object> row = rows.get(i);

                if (row.size() >= 8) {

                    String room = getSafe(row.get(2));
                    String status = getSafe(row.get(7));

                    if (room.equals(guest.getRoomNumber()) && status.equals("Checked-In")) {

                        int rowNum = i + 2;

                        // ✅ Checkout time → Column G
                        service.spreadsheets().values()
                                .update(SPREADSHEET_ID, "Sheet1!G" + rowNum,
                                        new ValueRange().setValues(
                                                List.of(List.of(guest.getCheckOutTime()))))
                                .setValueInputOption("RAW")
                                .execute();

                        // ✅ Status → Column H
                        service.spreadsheets().values()
                                .update(SPREADSHEET_ID, "Sheet1!H" + rowNum,
                                        new ValueRange().setValues(
                                                List.of(List.of("Checked-Out"))))
                                .setValueInputOption("RAW")
                                .execute();

                        System.out.println("✅ Checkout updated correctly");
                        return;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ GET ALL GUESTS
    public List<List<Object>> getAllGuests() {
        try {
            Sheets service = getSheetsService();

            ValueRange res = service.spreadsheets().values()
                    .get(SPREADSHEET_ID, "Sheet1!A2:H")
                    .execute();

            return res.getValues();

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // ✅ CHECK ROOM OCCUPANCY
    public boolean isRoomOccupied(String room) {
        try {
            List<List<Object>> rows = getAllGuests();

            if (rows == null) return false;

            for (List<Object> r : rows) {
                if (r.size() >= 8) {
                    if (getSafe(r.get(2)).equals(room) &&
                        getSafe(r.get(7)).equals("Checked-In")) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ✅ FIND GUEST BY ROOM
    public Guest findGuestByRoom(String room) {
        try {

            List<List<Object>> rows = getAllGuests();

            if (rows == null) return null;

            for (List<Object> r : rows) {

                if (r.size() >= 8) {

                    String name = getSafe(r.get(0));
                    String phone = getSafe(r.get(1));
                    String roomNo = getSafe(r.get(2));
                    String status = getSafe(r.get(7));

                    if (roomNo.equals(room.trim()) && status.equalsIgnoreCase("Checked-In")) {

                        Guest g = new Guest();
                        g.setName(name);
                        g.setPhone(phone);
                        g.setRoomNumber(roomNo);

                        return g;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}