package org.example.mobble.user;

import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception400;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble._util.error.ex.ExceptionApi403;
import org.example.mobble._util.error.ex.ExceptionApi404;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    //login
    public UserResponse.LoginDTO login(UserRequest.LoginDTO reqDTO) {

        //유저 조회
        User userPS = userRepository.findByUsername(reqDTO.getUsername())
                .orElseThrow(() -> new ExceptionApi404("유저를 찾을 수 없습니다."));
        if (!userPS.getPassword().equals(reqDTO.getPassword())) throw new Exception401("password가 틀렸습니다.");

        //dto 변환
        return  new UserResponse.LoginDTO (userPS);
    }

    public UserResponse.DTO join(UserRequest.JoinDTO reqDTO) {

        //중복체크
       Optional<User> userPS = userRepository.findByUsername(reqDTO.getUsername());

        if(userPS.isPresent() ) { throw new Exception400("이미 있는 유저입니다");}
        //save
        User user = User.builder()
                .username(reqDTO.getUsername())
                .password(reqDTO.getPassword())
                .email(reqDTO.getEmail())
                .build();

        User newUser = userRepository.save(user);
        return new UserResponse.DTO(newUser);
    }

    public UserResponse.UserDetailDTO detail(Integer userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new Exception401("인증 실패 유저 정보가 없습니다"));

        return new UserResponse.UserDetailDTO(userPS);
    }

    //패스워드 변경
    public UserResponse.DTO updatePassword(Integer sessionUserId, Integer userId, UserRequest.UserPasswordUpdateDTO reqDTO) {
        User userPS = checkUserPermission(sessionUserId,userId);
        userPS.updatePassword(reqDTO);
        return new UserResponse.DTO(userPS);
    }

    public UserResponse.DTO updateProfile(Integer sessionUserId, Integer userId, UserRequest.UserProfileUpdateDTO reqDTO) {
        User userPS = checkUserPermission(sessionUserId,userId);
        userPS.updateProfile(reqDTO);
        return new UserResponse.DTO(userPS);
    }

    private User checkUserPermission(Integer sessionUserId, Integer userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new Exception401("인증 실패 유저 정보가 없습니다"));
        if(userPS.getId().equals(sessionUserId)) { throw new ExceptionApi403("권한이 없습니다");}
        return userPS;
    }


}
