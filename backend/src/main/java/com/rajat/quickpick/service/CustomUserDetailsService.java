package com.rajat.quickpick.service;

import com.rajat.quickpick.model.User;
import com.rajat.quickpick.model.Vendor;
import com.rajat.quickpick.repository.UserRepository;
import com.rajat.quickpick.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);

        var user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User u = user.get();
            log.info("Found STUDENT: email={}, emailVerified={}, suspended={}, role={}",
                u.getEmail(), u.isEmailVerified(), u.isSuspended(), u.getRole());
            log.info("Password hash from DB starts with: {}", u.getPassword().substring(0, Math.min(20, u.getPassword().length())));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(u.getEmail())
                    .password(u.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name())))
                    .accountExpired(false)
                    .accountLocked(u.isSuspended())
                    .credentialsExpired(false)
                    .disabled(!u.isEmailVerified())
                    .build();
        }

        var vendor = vendorRepository.findByEmail(email);
        if (vendor.isPresent()) {
            Vendor v = vendor.get();
            log.info("Found VENDOR: email={}, emailVerified={}, suspended={}, role={}",
                v.getEmail(), v.isEmailVerified(), v.isSuspended(), v.getRole());

            return org.springframework.security.core.userdetails.User.builder()
                    .username(v.getEmail())
                    .password(v.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + v.getRole().name())))
                    .accountExpired(false)
                    .accountLocked(v.isSuspended())
                    .credentialsExpired(false)
                    .disabled(!v.isEmailVerified())
                    .build();
        }

        log.error("User not found with email: {}", email);
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}