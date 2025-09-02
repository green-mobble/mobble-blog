package org.example.mobble.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception400;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void userSave(UserRequest.UserJoinDTO userJoinDTO) {
        //1. 회원이 존재하는지 확인
        // user 객체명은 함수명과 동일하게 하지 말 것 : user/userPS
        User findUsername = userRepository.findUsername(userJoinDTO.getUsername());
        if (findUsername != null) {
            throw new Exception400("username이 이미 존재합니다 : " + userJoinDTO.getUsername());
        }

        // 2. 없으면 insert
        User user = User.builder()
                .username(userJoinDTO.getUsername())
                .password(userJoinDTO.getPassword())
                .email(userJoinDTO.getEmail())
                .build();
        userRepository.userSave(user);
    }

    public User userFindUsername(UserRequest.UserLoginDTO userLoginDTO) {
        // user 객체 이름 함수명과 동일하게 하지 않기
        User findUser = userRepository.findUsername(userLoginDTO.getUsername());
        if (findUser.getUsername() == null) {
            throw new Exception400("username이 존재하지 않습니다 : " +  userLoginDTO.getUsername());
        }
        if(!findUser.getPassword().equals(userLoginDTO.getPassword())){
            throw new Exception400("password가 틀렸습니다. :  " + userLoginDTO.getPassword()); // TODO : 에러 컨벤션 정하기
        }
        return findUser;
    }
}
