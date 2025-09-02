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
        return "auth/login-page";
    }

    // TODO : 아래는 유저로 옮기기
    @PostMapping("/login")
    public String login( UserRequest.LoginDTO reqDTO){
        //로그인 검증
        UserResponse.LoginDTO sessionUser = userService.login(reqDTO);
        //세션 생성
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/"; // TODO: redirect:/boards
    }

    @PostMapping("/join")
    public String join( UserRequest.JoinDTO reqDTO){
        //회원가입 ( save 라도 값은 리턴 - 확인을 위해)
        UserResponse.DTO resDTO = userService.join(reqDTO);
        return "redirect:/"; // TODO : redirect:/login-form
    }

    @GetMapping("/login-form")
    public String loginForm( ){
        return "redirect:/";
    } // TODO : return mustache

    @GetMapping("/join-form")
    public String joinForm( ){
        return "redirect:/";
    } // TODO : return mustache
}
