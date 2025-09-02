package org.example.mobble.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final HttpSession session;


    //정보 조회
    @GetMapping("")
    public String detail( Model model){
        //세션 유저 정보
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //조회
        UserResponse.UserDetailDTO  resDTO =  userService.detail(sessionUser.getId());
        model.addAttribute("resDTO", resDTO);
        return "mypage/main";
    }

    // 비밀번호 변경
    @PutMapping("/{id}/password")
    public String updatePassword( @PathVariable("id")  Integer userId , UserRequest.UserPasswordUpdateDTO reqDTO){

        //세션 유저 정보
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //변경(확인용으로 반환)
        UserResponse.DTO  resDTO = userService.updatePassword(sessionUser.getId(),userId,reqDTO);
        return "redirect:/users";
    }

    //프로필 변경
    @PutMapping("/{id}/profile")
    public String updateProfile( @PathVariable("id")  Integer userId, UserRequest.UserProfileUpdateDTO reqDTO){

        //세션 유저 정보
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //변경(확인용으로 반환)
        UserResponse.DTO  resDTO = userService.updateProfile(sessionUser.getId(),userId,reqDTO);
        return "redirect:/users";
    }

    //유저 탈퇴
    @DeleteMapping("/{id}")
    public String delete( @PathVariable("id")  Integer userId){

        //세션 유저 정보
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //삭제
        userService.delete(userId,sessionUser.getId());

        return "redirect:/";
    }

    //수정 화면 이동하기
    @GetMapping("/update-form/{id}")
    public String updateForm(@PathVariable("id")  Integer userId , Model model){
        //세션 유저 정보
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //조회
        UserResponse.DTO  resDTO = userService.updateForm(sessionUser.getId(),userId);

        model.addAttribute("resDTO", resDTO);
        return "mypage/update-form";
    }




}
