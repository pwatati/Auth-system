package com.security.authentication.service;

import com.security.authentication.model.User;
import com.security.authentication.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String idNumberStr) throws UsernameNotFoundException {
        Long idNumber;
        try {
            idNumber = Long.parseLong(idNumberStr);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid ID Number format: " + idNumberStr);
        }

        User user = userRepository.findById(idNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID Number: " + idNumberStr));

        return UserDetailsImpl.build(user);
    }
}