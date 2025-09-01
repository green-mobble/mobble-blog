package org.example.mobble.user.service;

import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception400;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble.user.domain.User;
import org.example.mobble.user.domain.UserRepository;
import org.example.mobble.user.dto.UserRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findByUser(UserRequest.LoginDTO reqDTO) {
        User user = userRepository.findByUsername(reqDTO.getUsername())
                .orElseThrow(() -> new Exception400("존재하지 않는 사용자 아이디입니다."));
        if (!user.getPassword().equals(reqDTO.getPassword())) throw new Exception401("비밀번호가 일치하지 않습니다.");
        return user;
    }
}
