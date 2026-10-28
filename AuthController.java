package com.museum.booking.web;

import com.museum.booking.model.AppUser;
import com.museum.booking.repo.UserRepository;
import com.museum.booking.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public record SignupRequest(@NotBlank String name,
                                @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits") String phone,
                                @Email @NotBlank String email,
                                @Size(min = 6, message = "Password must be at least 6 characters") String password) { }
    public record LoginRequest(@NotBlank String email, @NotBlank String password) { }
    public record AuthResponse(String token, String name, String email, String userType) { }

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/signup")
    public AuthResponse signup(@Valid @RequestBody SignupRequest r) {
        String email = r.email().trim().toLowerCase();
        if (users.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        AppUser u = new AppUser();
        u.setName(r.name().trim());
        u.setPhone(r.phone());
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(r.password()));   // BCrypt hash, never plain text
        u.setUserType("Visitor");                          // Admin accounts are set directly in the database
        users.save(u);
        return new AuthResponse(jwt.create(email), u.getName(), email, u.getUserType());
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest r) {
        String email = r.email().trim().toLowerCase();
        AppUser u = users.findByEmail(email)
                .filter(x -> x.getPasswordHash() != null && encoder.matches(r.password(), x.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password"));
        return new AuthResponse(jwt.create(email), u.getName(), email, u.getUserType());
    }
}
