package subject.store.supplement.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import subject.store.supplement.entities.User;
import subject.store.supplement.repository.UserRepository;

import java.util.Optional;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // JSP page path: register.jsp
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam String username,
                                 @RequestParam String password,
                                 Model model,
                                 HttpSession session) {

        // Input validation: ensure required fields
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Username and password are required.");
            return "register";
        }

        // Check if username already exists
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            model.addAttribute("error", "Username already exists.");
            return "register";
        }

        // Create new user, set fields explicitly
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password); // In production, hash the password!
        user.setRole("CUSTOMER");   // Default new registrations to CUSTOMER role

        // Save new user
        userRepository.save(user);

        // Optionally auto-login new user or redirect to login page
        // session.setAttribute("user", user);
        // session.setAttribute("username", user.getUsername());
        // session.setAttribute("role", user.getRole());
        // return "redirect:/customer/dashboard";

        // Recommended: redirect to login page with a success query param
        return "redirect:/login?registered";
    }
}
   	