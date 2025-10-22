package com.sendika.bookstore.web;

import com.sendika.bookstore.security.JwtUtil;
import com.sendika.bookstore.repo.AuthorityRepository;
// import com.sendika.bookstore.repo.UserRepository; // kullanmıyorsan sil
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

record LoginRequest(String username, String password) {}
        record LoginResponse(String token, String username, List<String> roles) {}

@RestController
@RequestMapping("/api/auth")
public class AuthController {   //  <-- <LoginResponse> kaldırıldı

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthorityRepository authorityRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          AuthorityRepository authorityRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authorityRepository = authorityRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        var user = (UserDetails) auth.getPrincipal();
        var roles = authorityRepository.findByUsername(user.getUsername())
                .stream()
                .map(a -> a.getAuthority())
                .toList(); // Java 17'de sorun yok

        String token = jwtUtil.generate(user.getUsername(), roles);
        return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), roles));
    }
}
