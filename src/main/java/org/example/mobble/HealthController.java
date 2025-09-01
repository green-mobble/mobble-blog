package org.example.mobble;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble.user.domain.User;
import org.example.mobble.user.dto.UserRequest;
import org.example.mobble.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class HealthController {

    private final HttpSession session;
    private final UserService userService;

    @GetMapping("/")
    public String health() {
        return "main";
    }

    @PostMapping("/login-form")
    public String loginForm() {
        return "auth/login-page";
    }

    @PostMapping("/join-form")
    public String joinForm() {
        return "auth/join-page";
    }

    @PostMapping("/login")
    public String login(@RequestBody UserRequest.LoginDTO reqDTO) {
        User user = userService.findByUser(reqDTO);
        session.setAttribute("resDTO", user);
        return "redirect:/";
    }

    @PutMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/login-form";
    }
}
