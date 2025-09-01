package org.example.mobble.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/users")
    public String detail( ){
        //세션 유저 정보
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //조회
        UserResponse.UserDetailDTO  resDTO =  userService.detail(sessionUser.getId());
        return "redirect:/";
    }

    @PutMapping("/users/{id}/password")
    public String updatePassword( @PathVariable("id")  Integer userId , UserRequest.UserPasswordUpdateDTO reqDTO){

        //세션 유저 정보
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //조회
        UserResponse.DTO  resDTO = userService.updatePassword(sessionUser.getId(),userId,reqDTO);
        return "redirect:/";
    }
    @PutMapping("/users/{id}/profile")
    public String updateProfile( @PathVariable("id")  Integer userId, UserRequest.UserProfileUpdateDTO reqDTO){

        //세션 유저 정보
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //조회
        UserResponse.DTO  resDTO = userService.updateProfile(sessionUser.getId(),userId,reqDTO);
        return "redirect:/";
    }





}
