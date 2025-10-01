package com.rajat.quickpick.service;

import com.rajat.quickpick.model.User;
import com.rajat.quickpick.model.Vendor;
import com.rajat.quickpick.repository.UserRepository;
import com.rajat.quickpick.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User u = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(u.getEmail())
                    .password(u.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name())))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(!u.isEmailVerified())
                    .build();
        }

        var vendor = vendorRepository.findByEmail(email);
        if (vendor.isPresent()) {
            Vendor v = vendor.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(v.getEmail())
                    .password(v.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + v.getRole().name())))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(!v.isEmailVerified())
                    .build();
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}