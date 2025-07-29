package subject.store.supplement.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import subject.store.supplement.entities.Product;
import subject.store.supplement.entities.User;
import subject.store.supplement.repository.CartItemRepository;
import subject.store.supplement.repository.ProductRepository;
import subject.store.supplement.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (!"ADMIN".equalsIgnoreCase((String) session.getAttribute("role"))) {
            return "access-denied";
        }
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "admin_dashboard";
    }

    @GetMapping("/addProduct")
    public String showAddProductForm(HttpSession session) {
        if (!"ADMIN".equalsIgnoreCase((String) session.getAttribute("role"))) {
            return "access-denied";
        }
        return "add_product";
    }

    @PostMapping("/addProduct")
    public String handleAddProduct(@RequestParam String name,
                                   @RequestParam String category,
                                   @RequestParam double price,
                                   @RequestParam("imageFile") MultipartFile imageFile,
                                   HttpSession session) {

        if (!"ADMIN".equalsIgnoreCase((String) session.getAttribute("role"))) {
            return "access-denied";
        }

        Product product = new Product();
        product.setName(name.trim());
        product.setCategory(category.trim());
        product.setPrice(price);

        if (!imageFile.isEmpty()) {
            try {
                String uploadDir = new File("src/main/webapp/images/").getAbsolutePath();
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = imageFile.getOriginalFilename();
                File destFile = new File(dir, fileName);
                imageFile.transferTo(destFile);

                product.setImageUrl("/images/" + fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productRepository.save(product);
        return "redirect:/admin/dashboard";
    }

    @Transactional
    @PostMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session) {
        if (!"ADMIN".equalsIgnoreCase((String) session.getAttribute("role"))) {
            return "access-denied";
        }

        cartItemRepository.deleteByProductId(id);
        productRepository.deleteById(id);

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (!"ADMIN".equalsIgnoreCase((String) session.getAttribute("role"))) {
            return "access-denied";
        }

        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/cleanup-users")
    public String cleanupUsers(HttpSession session) {
        if (!"ADMIN".equalsIgnoreCase((String) session.getAttribute("role"))) {
            return "access-denied";
        }

        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            String username = user.getUsername();
            String role = user.getRole();

            if (username == null || username.trim().isEmpty() ||
                role == null || role.trim().isEmpty()) {
                userRepository.deleteById(user.getId());
            }
        }

        return "redirect:/admin/dashboard";
    }

    // Remove duplicate users by username
    @PostMapping("/cleanup-duplicate-users")
    @Transactional
    public String cleanupDuplicateUsers(HttpSession session) {
        if (!"ADMIN".equalsIgnoreCase((String) session.getAttribute("role"))) {
            return "access-denied";
        }

        List<User> allUsers = userRepository.findAll();
        Map<String, Long> seenUsernames = new HashMap<>();

        for (User user : allUsers) {
            String username = user.getUsername();
            if (username == null || username.trim().isEmpty()) continue;

            if (seenUsernames.containsKey(username)) {
                userRepository.delete(user);
            } else {
                seenUsernames.put(username, user.getId());
            }
        }

        return "redirect:/admin/dashboard";
    }
}
