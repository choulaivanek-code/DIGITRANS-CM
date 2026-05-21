package cm.agrocam.digitrans.auth.service;

import cm.agrocam.digitrans.auth.config.JwtProvider;
import cm.agrocam.digitrans.auth.dto.AuthResponse;
import cm.agrocam.digitrans.auth.dto.LoginRequest;
import cm.agrocam.digitrans.auth.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, UserDetails> userStore = new HashMap<>();

    public AuthService(JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        initUsers();
    }

    private void initUsers() {
        userStore.put("admin", new UserDetails(passwordEncoder.encode("admin123"), List.of("ROLE_ADMIN")));
        userStore.put("rh_user", new UserDetails(passwordEncoder.encode("rh123"), List.of("ROLE_RH")));
        userStore.put("finance_user", new UserDetails(passwordEncoder.encode("fin123"), List.of("ROLE_FINANCE")));
        userStore.put("manager", new UserDetails(passwordEncoder.encode("mgr123"), List.of("ROLE_MANAGER")));
        userStore.put("agent", new UserDetails(passwordEncoder.encode("agt123"), List.of("ROLE_AGENT")));
        userStore.put("director", new UserDetails(passwordEncoder.encode("dir123"), List.of("ROLE_DIRECTOR")));
    }

    public AuthResponse authenticate(LoginRequest request) {
        log.info("Processing login request for user: {}", request.getUsername());
        UserDetails user = userStore.get(request.getUsername());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Authentication failed for user: {}", request.getUsername());
            throw new UnauthorizedException("Invalid username or password");
        }

        String token = jwtProvider.generateToken(request.getUsername(), user.getRoles());
        log.info("Token generated successfully for user: {}", request.getUsername());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .username(request.getUsername())
                .roles(user.getRoles())
                .expiresIn(jwtProvider.getExpirationMs())
                .build();
    }

    private static class UserDetails {
        private final String password;
        private final List<String> roles;

        public UserDetails(String password, List<String> roles) {
            this.password = password;
            this.roles = roles;
        }

        public String getPassword() { return password; }
        public List<String> getRoles() { return roles; }
    }
}
