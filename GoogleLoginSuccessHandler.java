package com.museum.booking.security;

import com.museum.booking.model.AppUser;
import com.museum.booking.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/** After Google login: find or create the user, then send a JWT to the React app. */
@Component
public class GoogleLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository users;
    private final JwtService jwt;
    private final String frontendUrl;

    public GoogleLoginSuccessHandler(UserRepository users, JwtService jwt,
                                     @Value("${app.frontend.url}") String frontendUrl) {
        this.users = users;
        this.jwt = jwt;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res,
                                        Authentication auth) throws IOException {
        OAuth2User g = (OAuth2User) auth.getPrincipal();
        String email = g.getAttribute("email");
        String name = g.getAttribute("name");
        AppUser u = users.findByEmail(email).orElseGet(() -> {
            AppUser n = new AppUser();
            n.setEmail(email);
            n.setName(name);
            n.setProvider("GOOGLE");
            return users.save(n);
        });
        res.sendRedirect(frontendUrl + "/login-success?token=" + jwt.create(u.getEmail()));
    }
}
