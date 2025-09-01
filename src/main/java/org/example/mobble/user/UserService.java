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
        User findUsername = userRepository.findUsername(userJoinDTO.getUsername());
        if (findUsername != null) {
            throw new Exception400("username이 이미 존재합니다" + userJoinDTO.getUsername());
        }

        // 2. 없으면 insert
        User user = User.builder()
                .username(userJoinDTO.getUsername())
                .password(userJoinDTO.getPassword())
                .email(userJoinDTO.getEmail())
                .build();
        userRepository.userSave(user);
    }
}
