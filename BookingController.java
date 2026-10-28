package com.museum.booking.web;

import com.museum.booking.model.*;
import com.museum.booking.repo.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    public record BookingRequest(@NotNull Long museumId, @NotNull LocalDate visitDate,
                                 @NotBlank String ticketType, @Min(1) @Max(10) int quantity,
                                 Long transportId, @NotBlank String paymentMethod) { }

    public record BookingView(Long ticketId, String museum, LocalDate visitDate, String ticketType,
                              int quantity, String transport, Long paymentId, String paymentMethod,
                              String transaction, String paymentStatus, int amount) { }

    private final BookingRepository bookings;
    private final UserRepository users;
    private final MuseumRepository museums;
    private final TransportRepository transports;

    public BookingController(BookingRepository bookings, UserRepository users,
                             MuseumRepository museums, TransportRepository transports) {
        this.bookings = bookings;
        this.users = users;
        this.museums = museums;
        this.transports = transports;
    }

    /** Museum issues a Ticket Booking, Transport is included, Payment confirms it. */
    @PostMapping
    @Transactional
    public BookingView create(@Valid @RequestBody BookingRequest r, Authentication auth) {
        AppUser user = users.findByEmail(auth.getName()).orElseThrow();
        Museum m = museums.findById(r.museumId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Museum not found"));
        if (r.visitDate().isBefore(LocalDate.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pick today or a future date");

        int price = switch (r.ticketType()) {
            case "Adult" -> m.getAdultPrice();
            case "Child" -> m.getChildPrice();
            case "Student" -> m.getStudentPrice();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown ticket type");
        };

        Transport t = null;
        int transportPrice = 0;
        if (r.transportId() != null) {
            t = transports.findById(r.transportId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transport not found"));
            if (!t.isAvailable())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This transport is not available");
            transportPrice = t.getPrice();
        }

        TicketBooking b = new TicketBooking();
        b.setUser(user);
        b.setMuseum(m);
        b.setTransport(t);
        b.setPrice(price);
        b.setQuantity(r.quantity());
        b.setTotal((price + transportPrice) * r.quantity());
        b.setBookingDate(LocalDate.now());
        b.setVisitDate(r.visitDate());
        b.setTicketType(r.ticketType());

        boolean atCounter = r.paymentMethod().equals("Pay at counter");
        Payment p = new Payment();
        p.setTicket(b);
        p.setPaymentMethod(r.paymentMethod());
        p.setAmount(b.getTotal());
        p.setTransaction(atCounter ? "-" : "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        p.setPaymentStatus(atCounter ? "Pending" : "Success");   // a real gateway would confirm this
        b.setPayment(p);

        return view(bookings.save(b));
    }

    /** Visitors see their own bookings, Admins see all bookings. */
    @GetMapping
    public List<BookingView> mine(Authentication auth) {
        AppUser user = users.findByEmail(auth.getName()).orElseThrow();
        List<TicketBooking> list = isAdmin(auth) ? bookings.findAll() : bookings.findByUserOrderByTicketIdDesc(user);
        return list.stream().map(BookingController::view).toList();
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id, Authentication auth) {
        TicketBooking b = bookings.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (!isAdmin(auth) && !b.getUser().getEmail().equals(auth.getName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your booking");
        bookings.delete(b);   // the payment row is removed too (cascade)
    }

    private static boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private static BookingView view(TicketBooking b) {
        Payment p = b.getPayment();
        return new BookingView(b.getTicketId(), b.getMuseum().getName(), b.getVisitDate(), b.getTicketType(),
                b.getQuantity(), b.getTransport() == null ? "No transport" : b.getTransport().getTransportType(),
                p.getPaymentId(), p.getPaymentMethod(), p.getTransaction(), p.getPaymentStatus(), p.getAmount());
    }
}
