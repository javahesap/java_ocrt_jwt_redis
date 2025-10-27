package com.sendika.bookstore.service;

import com.sendika.bookstore.aspect.LogExecution;
import com.sendika.bookstore.model.Authority;
import com.sendika.bookstore.model.User;
import com.sendika.bookstore.repo.AuthorityRepository;
import com.sendika.bookstore.repo.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public DbUserDetailsService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    @LogExecution("loadUserByUsername")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Authority> authorities = authorityRepository.findByUsername(username);
        var roles = authorities.stream().map(a -> new SimpleGrantedAuthority(a.getAuthority())).toList();
        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getPassword())
                .authorities(roles)
                .disabled(!u.isEnabled())
                .build();
    }
}
