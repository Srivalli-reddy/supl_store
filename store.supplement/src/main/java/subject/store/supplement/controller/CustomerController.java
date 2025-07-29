package subject.store.supplement.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import subject.store.supplement.entities.Product;
import subject.store.supplement.entities.User;
import subject.store.supplement.entities.CartItem;
import subject.store.supplement.repository.ProductRepository;
import subject.store.supplement.repository.UserRepository;
import subject.store.supplement.repository.CartItemRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/dashboard")
    public String customerDashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"CUSTOMER".equalsIgnoreCase(role)) {
            return "redirect:/login";
        }

        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("username", username);

        return "cust_dashboard";
    }

    @PostMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable Long productId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"CUSTOMER".equalsIgnoreCase(role)) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            return "redirect:/login";
        }
        User user = userOpt.get();

        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            List<CartItem> existingItems = cartItemRepository.findByUser(user);
            CartItem cartItem = existingItems.stream()
                    .filter(ci -> ci.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItemRepository.save(cartItem);
            } else {
                cartItemRepository.save(new CartItem(user, product, 1));
            }
        }

        return "redirect:/customer/cart";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"CUSTOMER".equalsIgnoreCase(role)) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            return "redirect:/login";
        }
        User user = userOpt.get();

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        double total = cartItems.stream()
                .mapToDouble(ci -> ci.getProduct().getPrice() * ci.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/cart/delete/{cartItemId}")
    public String deleteCartItem(@PathVariable Long cartItemId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"CUSTOMER".equalsIgnoreCase(role)) {
            return "redirect:/login";
        }

        cartItemRepository.deleteById(cartItemId);
        return "redirect:/customer/cart";
    }
}
