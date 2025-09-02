package org.example.mobble.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.*;
import org.example.mobble.user.domain.User;
import org.example.mobble.user.domain.UserRepository;
import org.example.mobble.user.dto.UserRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final String uploadDir = "src/main/resources/db/images";

    public User findByUser(UserRequest.LoginDTO reqDTO) {
        User user = userRepository.findByUsername(reqDTO.getUsername())
                .orElseThrow(() -> new Exception400("존재하지 않는 사용자 아이디입니다."));
        if (!user.getPassword().equals(reqDTO.getPassword())) throw new Exception401("비밀번호가 일치하지 않습니다.");
        return user;
    }

    @Transactional
    public void save(UserRequest.JoinDTO reqDTO) {
        User user = userRepository.findByUsername(reqDTO.getUsername()).orElse(null);
        if (user != null) throw new Exception302("중복되는 닉네임입니다.");
        String pw = bCryptPasswordEncoder.encode(reqDTO.getPassword());
        userRepository.save(new User(null, reqDTO.getUsername(), pw, reqDTO.getEmail(), null));
    }

    public boolean isNicknameDuplicate(String username) {
        return userRepository.existsByNickname(username);
    }

    @Transactional
    public void changeProfile(User user, Integer userId, MultipartFile profileImage) {
        checkPermissions(user, userId);
        User userPS = getUser(user.getId());
        if (profileImage == null || profileImage.isEmpty()) {
            throw new Exception400("업로드된 파일이 없습니다.");
        }
        String contentType = profileImage.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image")) {
            throw new Exception400("이미지 파일만 업로드 가능합니다.");
        }
        Path dir = Paths.get(uploadDir, userPS.getId().toString(), "profile");
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new Exception500("업로드 디렉토리를 생성할 수 없습니다.");
        }
        String original = StringUtils.cleanPath(
                profileImage.getOriginalFilename() == null ? "" : profileImage.getOriginalFilename()
        );
        String ext = ".jpg";
        int dot = original.lastIndexOf('.');
        if (dot != -1 && dot < original.length() - 1) {
            ext = original.substring(dot); // .png, .jpeg 등
        }
        String newFilename = userPS.getId() + "_" + UUID.randomUUID() + ext;
        Path targetPath = dir.resolve(newFilename).normalize();

        // 4) 기존 파일 삭제 (있다면)
        String oldPathStr = userPS.getProfileImageUrl();
        if (oldPathStr != null && !oldPathStr.isBlank()) {
            try {
                Files.deleteIfExists(Paths.get(oldPathStr));
            } catch (IOException ignore) {
                // 로그 정도 남기고 계속 진행 (권한/존재 문제로 실패할 수 있음)
                System.out.println("기존 프로필 이미지 삭제 실패: " + oldPathStr);
            }
        }

        // 5) 새 파일 저장
        try (InputStream in = profileImage.getInputStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new Exception500("프로필 이미지를 저장하는 중 오류가 발생했습니다.");
        }

        // 6) 유저 엔티티에 새 경로 반영
        userPS.setProfileImageUrl(targetPath.toString());
    }

    @Transactional
    public void changePassword(User user, Integer userId, String password) {
        if (password == null || password.isEmpty()) throw new Exception400("비밀번호가 입력되지 않았습니다.");
        checkPermissions(user, userId);
        User userPS = getUser(user.getId());
        userPS.setPassword(password);
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new Exception403("존재하지 않는 사용자 입니다.")
        );
    }

    @Transactional
    public void delete(User user, Integer userId) {
        checkPermissions(user, userId);
        userRepository.delete(user);
    }

    private void checkPermissions(User user, Integer userId) {
        if (!user.getId().equals(userId)) throw new Exception403("해당 계정의 권한이 없습니다.");
    }
}
