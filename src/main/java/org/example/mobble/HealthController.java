package org.example.mobble;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble.user.UserRequest;
import org.example.mobble.user.UserResponse;
import org.example.mobble.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@RequiredArgsConstructor
@Controller
public class HealthController {

    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/")
    public String health() {
        return  "redirect:/login-form";
    }


}
