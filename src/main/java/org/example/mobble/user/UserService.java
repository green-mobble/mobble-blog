package org.example.mobble.user;

import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception400;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble._util.error.ex.ExceptionApi404;
import org.springframework.stereotype.Service;

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
        User userPS = userRepository.findByUsername(reqDTO.getUsername())
                .orElseThrow(() -> new Exception400("중복된 유저입니다"));
        //save
        User user = User.builder()
                .username(reqDTO.getUsername())
                .password(reqDTO.getPassword())
                .email(reqDTO.getEmail())
                .build();

        User newUser = userRepository.save(user);
        return new UserResponse.DTO(newUser);
    }
}
