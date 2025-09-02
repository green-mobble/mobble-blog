package org.example.mobble.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/join-form")
    public String joinForm() {
        return "auth/join-page";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "auth/login-page";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) {
        userService.회원가입(requestDTO);
        return "redirect:/login-form";
    }

    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) {
        User user = userService.로그인(requestDTO);
        session.setAttribute("session", user);  // 세션에 저장
        return "redirect:/boards";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/boards";
    }
}
