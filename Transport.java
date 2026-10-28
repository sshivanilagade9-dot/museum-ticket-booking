package com.museum.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** ER entity: Transport (Transport ID, Price, Transport Type, Availability Status) */
@Entity
@Getter @Setter
public class Transport {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transportId;
    private String transportType;
    private int price;
    private boolean available;   // Availability Status
}
