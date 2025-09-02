package org.example.mobble.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble._util.error.ex.Exception404;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void 회원가입(UserRequest.JoinDTO requestDTO) {
        // 중복 체크
        if (userRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            throw new Exception401("이미 존재하는 유저네임 입니다.");
        }
        // 회원 가입
        User user = requestDTO.toEntity();
        userRepository.join(user);
    }

    public User 로그인(UserRequest.LoginDTO requestDTO) {
        // 유저 조회
        User user = userRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> new Exception404("유저네임을 찾을 수 없습니다"));
        // 비밀번호 확인
        if(!user.getPassword().equals(requestDTO.getPassword())) {
            throw new Exception401("비밀번호가 일치하지 않습니다."); // TODO: 에러 처리는 400 / 401으로 하면 됨
        }
        return user;
    }
}
