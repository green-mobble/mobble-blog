package org.example.mobble.user;

import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/join")
    public String join(UserRequest.UserJoinDTO userJoinDTO){
        userService.userSave(userJoinDTO);
        return "redirect:/login-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "auth/login-page";
    }

    @PostMapping("/login")
    public String login(UserRequest.UserLoginDTO userLoginDTO){
        User sessionUser = userService.userFindUsername(userLoginDTO);
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/boards";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/boards";
    }



}
