package com.museum.booking.web;

import com.museum.booking.model.Museum;
import com.museum.booking.repo.MuseumRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/museums")
public class MuseumController {
    private final MuseumRepository museums;

    public MuseumController(MuseumRepository museums) {
        this.museums = museums;
    }

    /** GET /api/museums?q=kelkar  (the "Search" relationship) */
    @GetMapping
    public List<Museum> search(@RequestParam(defaultValue = "") String q) {
        return museums.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q, q);
    }

    @GetMapping("/{id}")
    public Museum one(@PathVariable Long id) {
        return museums.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Museum not found"));
    }
}
