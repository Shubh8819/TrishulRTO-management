package com.trishul.ServiceImpl;
// src/main/java/com/licencemanagement/service/impl/AuthServiceImpl.java

import com.trishul.Model.User;
import com.trishul.repository.UserRepository;
import com.trishul.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public void register(User user) {
        log.info("Processing registration for user: {}", user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User already exists with this email");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailVerifiedAt(LocalDateTime.now());
        user.setActive(true);
        user.setRole(User.Role.USER);

        userRepository.save(user);
        log.info("Registration successful for user: {}", user.getEmail());
    }

    @Override
    public void autoLogin(String email, String password,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        try {

            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(email, password);

            Authentication authentication = authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSessionSecurityContextRepository securityRepository =
                    new HttpSessionSecurityContextRepository();

            securityRepository.saveContext(
                    SecurityContextHolder.getContext(),
                    request,
                    response
            );

        } catch (Exception e) {
            throw new RuntimeException("Auto-login failed: " + e.getMessage(), e);
        }
    }
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal() instanceof String) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }
}