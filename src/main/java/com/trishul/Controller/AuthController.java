package com.trishul.Controller;

// src/main/java/com/licencemanagement/controller/AuthController.java

import com.trishul.DTO.LoginDTO;
import com.trishul.Model.User;
import com.trishul.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    public AuthController(AuthService authService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, HttpServletRequest request) {
       // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        /*if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            return "redirect:/dashboard";
        }*/


        model.addAttribute("pageTitle", "Login - LicencePro");
        return "login/loginSignup";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("pageTitle", "Sign Up - LicencePro");
        model.addAttribute("user", new User());
        return "auth/login";
    }

    @PostMapping("/submitlogin")
    public String register(@ModelAttribute("user") LoginDTO loginDTO, BindingResult result, HttpServletRequest request,HttpServletResponse reponce){

        authService.autoLogin(loginDTO.getEmailId(), loginDTO.getPassword(), request, reponce);


          return "redirect:/";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           @RequestParam("passwordConfirmation") String passwordConfirmation,
                           Model model,
                           HttpServletRequest request,HttpServletResponse reponce) {

        log.info("Registration attempt for email: {}", user.getEmail());

        if (result.hasErrors()) {
            model.addAttribute("message", "Please fix the errors");
            model.addAttribute("messageType", "error");
            return "auth/login";
        }

            if (!user.getPassword().equals(passwordConfirmation)) {
            model.addAttribute("message", "Passwords do not match");
            model.addAttribute("messageType", "error");
            model.addAttribute("user", user);
            return "auth/login";
        }

        try {
            authService.register(user);
            authService.autoLogin(user.getEmail(), user.getPassword(), request,reponce);
            return "redirect:/";

        } catch (Exception e) {
            log.error("Registration failed for email: {}", user.getEmail(), e);
            model.addAttribute("message", e.getMessage());
            model.addAttribute("messageType", "error");
            model.addAttribute("user", user);
            return "login/loginSignup";
        }
    }

    @GetMapping("/api/auth/check")
    @ResponseBody
    public AuthStatus checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String);

        AuthStatus status = new AuthStatus();
        status.setAuthenticated(authenticated);

        if (authenticated) {
            User user = (User) auth.getPrincipal();
            status.setUser(user);
            status.setRole(user.getRole().name());
        }

        return status;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            User user = (User) auth.getPrincipal();
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Dashboard - LicencePro");
            return "dashboard";
        }
        return "redirect:/login";
    }

    public static class AuthStatus {
        private boolean authenticated;
        private User user;
        private String role;

        public boolean isAuthenticated() { return authenticated; }
        public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }
        public User getUser() { return user; }
        public void setUser(User user) { this.user = user; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}