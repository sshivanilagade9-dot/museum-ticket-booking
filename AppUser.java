package com.museum.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** ER entity: User (User ID, Name, Phone No., Email ID, Password, User Type) */
@Entity
@Table(name = "app_user")
@Getter @Setter
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String phone;
    @Column(unique = true, nullable = false)
    private String email;
    private String passwordHash;          // empty for Google users
    private String userType = "Visitor";  // Visitor or Admin
    private String provider = "LOCAL";    // LOCAL or GOOGLE
}
