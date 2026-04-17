package com.marella.controller;

import com.marella.model.Guest;

import com.marella.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @PostMapping("/checkin")
    public ResponseEntity<String> checkIn(@Valid @RequestBody Guest guest) {

        String result = guestService.checkInGuest(guest);

        if (result.contains("❌")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkOut(@RequestBody Guest guest){
        return ResponseEntity.ok(guestService.checkOutGuest(guest));
    }

    @GetMapping("/guests")
    public List<List<Object>> getGuests() {
        return guestService.getAllGuests();
    }

    
    @GetMapping("/guests/room/{room}")
    public ResponseEntity<Guest> getByRoom(@PathVariable String room) {

        Guest g = guestService.findByRoom(room);

        if (g == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(g);
    }
}