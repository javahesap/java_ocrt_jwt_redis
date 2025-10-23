package com.sendika.bookstore.web;

import com.sendika.bookstore.security.JwtUtil;
import com.sendika.bookstore.repo.AuthorityRepository;
import com.sendika.bookstore.security.LoginAttemptService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

record LoginRequest(String username, String password) {}
record LoginResponse(String token, String username, List<String> roles) {}
record ErrorResponse(String error, String detail) {}

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthorityRepository authorityRepository;
    private final LoginAttemptService attemptService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          AuthorityRepository authorityRepository,
                          LoginAttemptService attemptService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authorityRepository = authorityRepository;
        this.attemptService = attemptService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String username = req.username();

        // 1) Kilit kontrolü (önce)
        if (attemptService.isLocked(username)) {
            long ttl = attemptService.lockTtlSeconds(username);
            return ResponseEntity.status(429) // Too Many Requests
                    .header(HttpHeaders.RETRY_AFTER, String.valueOf(ttl > 0 ? ttl : 600))
                    .body(new ErrorResponse("locked",
                            "Hesap kilitli. Lütfen " + ttl + " saniye sonra tekrar deneyin."));
        }

        // 2) Doğrulama
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, req.password())
            );

            // 3) Başarılı -> sayaç/kilit temizle
            attemptService.onSuccess(username);

            var user = (UserDetails) auth.getPrincipal();
            var roles = authorityRepository.findByUsername(user.getUsername())
                    .stream().map(a -> a.getAuthority()).toList();

            String token = jwtUtil.generate(user.getUsername(), roles);
            return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), roles));
        } catch (BadCredentialsException ex) {
            // 4) Başarısız -> sayaç artır, gerekirse kilitle
            var state = attemptService.onFailure(username);
            if (state.locked()) {
                return ResponseEntity.status(429)
                        .header(HttpHeaders.RETRY_AFTER, String.valueOf(state.ttlSeconds()))
                        .body(new ErrorResponse("locked",
                                "Çok fazla hatalı deneme. Hesap " + state.ttlSeconds() + " sn kilitli."));
            } else {
                long kalan = Math.max(0, (5 - state.failCount())); // 5 => app.auth.max-fail
                return ResponseEntity.status(401)
                        .body(new ErrorResponse("bad_credentials",
                                "Kullanıcı adı/şifre hatalı. Kalan deneme: " + kalan));
            }
        }
    }
}
