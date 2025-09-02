package org.example.mobble.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception400;
import org.example.mobble._util.error.ex.Exception401;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponse.userDTO userSave(UserRequest.UserJoinDTO userJoinDTO) {
        //1. 회원이 존재하는지 확인
        // user 객체명은 함수명과 동일하게 하지 말 것 : user/userPS
        if (userRepository.findUsername(userJoinDTO.getUsername()).isPresent()) {
            throw new Exception401("이미 존재하는 유저네임 입니다.");
        }
        // 2. 없으면 insert
        User user = User.builder()
                .username(userJoinDTO.getUsername())
                .password(userJoinDTO.getPassword())
                .email(userJoinDTO.getEmail())
                .build();
        User saveUser =  userRepository.userSave(user);
        return new UserResponse.userDTO(saveUser);

    }

    public User userFindUsername(UserRequest.UserLoginDTO userLoginDTO) {
        // user 객체 이름 함수명과 동일하게 하지 않기
        User userPS = userRepository.findUsername(userLoginDTO.getUsername())
                .orElseThrow(() ->new Exception400("유저네임을 찾을수없습니다"));
        if(!userPS.getPassword().equals(userLoginDTO.getPassword())){
            throw new Exception400("password가 틀렸습니다. :  " + userLoginDTO.getPassword()); // TODO : 에러 컨벤션 정하기
        }
        return userPS;
    }
}
