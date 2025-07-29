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
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Your login.jsp
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (!optionalUser.isPresent() || !optionalUser.get().getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        User user = optionalUser.get();

        session.setAttribute("user", user);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin/dashboard";
        } else if ("CUSTOMER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/customer/dashboard";
        } else {
            model.addAttribute("error", "Invalid user role");
            session.invalidate();
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
