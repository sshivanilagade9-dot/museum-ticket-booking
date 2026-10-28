package com.museum.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** ER entity: Payment (Payment ID, Payment Method, Amount, Transaction, Payment Status) */
@Entity
@Getter @Setter
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @OneToOne(optional = false) @JoinColumn(name = "ticket_id", unique = true)
    private TicketBooking ticket;
    private String paymentMethod;   // UPI, Card, Net banking, Pay at counter
    private int amount;
    private String transaction;     // transaction number
    private String paymentStatus;   // Success or Pending
}
