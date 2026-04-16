package com.marella.controller;

import com.marella.model.Guest;
import com.marella.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @PostMapping("/checkin")
    public String checkIn(@RequestBody Guest guest) {
        return guestService.checkInGuest(guest);
    }

    @PostMapping("/checkout")
    public String checkOut(@RequestBody Guest guest) {
        return guestService.checkOutGuest(guest);
    }

    @GetMapping("/guests")
    public List<List<Object>> getGuests() {
        return guestService.getAllGuests();
    }

    
    @GetMapping("/guest-by-room/{room}")
    public Guest getByRoom(@PathVariable String room) {

        Guest g = guestService.findByRoom(room);

        if (g == null) {
            return null; // or throw exception later
        }

        return g;
    }
}