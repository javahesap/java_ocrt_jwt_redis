package com.sendika.bookstore.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private static final AntPathMatcher PATHS = new AntPathMatcher();
    public JwtAuthFilter(JwtUtil jwtUtil) { this.jwtUtil = jwtUtil; }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String uri = req.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) return true;
        if ("/".equals(uri) || "/index.html".equals(uri) ||
                uri.startsWith("/js/") || uri.startsWith("/css/")) return true;
        if ("/api/auth/login".equals(uri)) return true;
        if (PATHS.match("/api/snaps/*/image", uri)) return true; // <-- resim public
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) { chain.doFilter(request, response); return; }
        try {
            Claims c = jwtUtil.parse(header.substring(7));
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) c.get("roles");
            var auths = new ArrayList<SimpleGrantedAuthority>();
            if (roles != null) roles.forEach(r -> auths.add(new SimpleGrantedAuthority(r)));
            var auth = new UsernamePasswordAuthenticationToken(c.getSubject(), null, auths);
            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(request, response);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), Map.of("error", "Invalid token"));
        }
    }
}
