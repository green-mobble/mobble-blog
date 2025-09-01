package org.example.mobble.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession session;


    @PostMapping("/login")
    public String login( UserRequest.LoginDTO reqDTO){
        //로그인 검증
        UserResponse.LoginDTO sessionUser = userService.login(reqDTO);
        //세션 생성
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/";
    }

    @PostMapping("/join")
    public String join( UserRequest.JoinDTO reqDTO){
        //회원가입 ( save 라도 값은 리턴 - 확인을 위해)
        UserResponse.DTO resDTO = userService.join(reqDTO);
        return "redirect:/";
    }

    @GetMapping("/login-form")
    public String loginForm( ){
        return "redirect:/";
    }

    @GetMapping("/join-form")
    public String joinForm( ){
        return "redirect:/";
    }


}
