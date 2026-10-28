package com.museum.booking.web;

import com.museum.booking.model.Transport;
import com.museum.booking.repo.TransportRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transport")
public class TransportController {
    private final TransportRepository transports;

    public TransportController(TransportRepository transports) {
        this.transports = transports;
    }

    @GetMapping
    public List<Transport> all() {
        return transports.findAll();
    }
}
