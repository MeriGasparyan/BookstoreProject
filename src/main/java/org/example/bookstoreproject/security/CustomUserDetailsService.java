package org.example.bookstoreproject.security;


import lombok.RequiredArgsConstructor;

import org.example.bookstoreproject.persistance.entity.User;
import org.example.bookstoreproject.persistance.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("Bad Credentials"));

        if (!user.isEnabled()) {
            throw new LockedException("User is locked");
        }

        return new CustomUserDetails(user);
    }
}