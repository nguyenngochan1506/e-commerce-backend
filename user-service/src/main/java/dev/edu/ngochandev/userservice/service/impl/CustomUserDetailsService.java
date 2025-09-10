package dev.edu.ngochandev.userservice.service.impl;

import dev.edu.ngochandev.sharedkernel.exception.ResourceNotFoundException;
import dev.edu.ngochandev.userservice.entity.UserEntity;
import dev.edu.ngochandev.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByIdentifier(username)
                .orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));
    }
}
