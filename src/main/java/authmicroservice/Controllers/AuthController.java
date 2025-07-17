package authmicroservice.Controllers;


import authmicroservice.Entities.User;
import authmicroservice.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Controller
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already taken");
            return "register";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping({"/", "/login"})
    public String showLoginPage() {
        return "index";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String redirect_uri,
            HttpServletResponse response,
            Model model
    ) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            String code = UUID.randomUUID().toString();

            CodeStore.saveCode(code, username);

            String redirect = (redirect_uri != null && !redirect_uri.isBlank()) ?
                    redirect_uri : "http://localhost:1337/oauth/callback";
//            String token = JwtUtil.generateToken(username);
//            Cookie cookie = new Cookie("JWT_TOKEN", token);
//            cookie.setPath("/");
//            cookie.setHttpOnly(true);
//            response.addCookie(cookie);
//            return "redirect:http://localhost:1337/";
            return "redirect:" + redirect + "?code=" + code;
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "index";
        }
    }
}
