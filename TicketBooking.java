package com.museum.booking.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/** ER entity: Ticket Booking (Ticket ID, Price, Booking Date, Visit Date, Ticket Type) */
@Entity
@Table(name = "ticket_booking")
@Getter @Setter
public class TicketBooking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne(optional = false) private AppUser user;
    @ManyToOne(optional = false) private Museum museum;       // relationship: Issues
    @ManyToOne private Transport transport;                   // relationship: Includes (optional)

    private int price;            // price of one ticket
    private int quantity;
    private int total;            // (ticket price + transport price) x quantity
    private LocalDate bookingDate;
    private LocalDate visitDate;
    private String ticketType;    // Adult, Child or Student

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;      // relationship: Confirms
}
