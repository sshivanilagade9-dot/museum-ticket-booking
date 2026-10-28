package com.museum.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** ER entity: Museum (Museum ID, Name, Location, Opening Hours, Description) */
@Entity
@Getter @Setter
public class Museum {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long museumId;
    private String name;
    private String location;
    private String openDays;
    private String openTime;
    private String closeTime;
    private String closedOn;
    @Column(length = 500)
    private String description;
    private int adultPrice;
    private int childPrice;
    private int studentPrice;
}
