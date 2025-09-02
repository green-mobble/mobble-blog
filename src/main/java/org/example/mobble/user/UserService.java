package org.example.mobble.user;

import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private String uploadDir = "src/main/resources/db/image/";
    //login
    public UserResponse.LoginDTO login(UserRequest.LoginDTO reqDTO) {

        //유저 조회
        User userPS = userRepository.findByUsername(reqDTO.getUsername())
                .orElseThrow(() -> new Exception404("유저를 찾을 수 없습니다."));
        if (!userPS.getPassword().equals(reqDTO.getPassword())) throw new Exception401("password가 틀렸습니다.");

        //dto 변환
        return  new UserResponse.LoginDTO (userPS);
    }

    //회원가입
    @Transactional
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

    //유저 정보 상세보기
    public UserResponse.UserDetailDTO detail(Integer userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new Exception401("인증 실패 유저 정보가 없습니다"));

        return new UserResponse.UserDetailDTO(userPS);
    }

    //패스워드 변경
    @Transactional
    public UserResponse.DTO updatePassword(Integer sessionUserId, Integer userId, UserRequest.UserPasswordUpdateDTO reqDTO) {
        User userPS = checkUserPermission(sessionUserId,userId);
        userPS.updatePassword(reqDTO);
        return new UserResponse.DTO(userPS);
    }

    //프로필 변경
    @Transactional
    public UserResponse.DTO updateProfile(Integer sessionUserId, Integer userId, UserRequest.UserProfileUpdateDTO reqDTO) {
        User userPS = checkUserPermission(sessionUserId,userId);
        fileSave(reqDTO,userPS);
        return new UserResponse.DTO(userPS);
    }



    //유저 탈퇴
    @Transactional
    public void delete(Integer userId, Integer sessionUserId) {
        User userPS = checkUserPermission(sessionUserId,userId);
        userRepository.delete(userPS);
    }

    //수정 화면 이동하기
    public UserResponse.DTO updateForm(Integer sessionUserId, Integer userId) {
        User userPS = checkUserPermission(sessionUserId,userId);
        return  new UserResponse.DTO(userPS);
    }

    //유저 검증
    private User checkUserPermission(Integer sessionUserId, Integer userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new Exception401("인증 실패 유저 정보가 없습니다"));
        if(!userPS.getId().equals(sessionUserId)) { throw new Exception403("권한이 없습니다");}
        return userPS;
    }

    //파일 저장 - 팀장 지시에 따라 변경
    private void fileSave(UserRequest.UserProfileUpdateDTO reqDTO,User user) {
        MultipartFile target = reqDTO.getProfileImage();
        if (!target.getName().contains("jpg") && target.getName().contains("png")) {
            throw new Exception400("파일 형식이 잘못되었습니다.");
        }
        String fileName = UUID.randomUUID() + "_" + target.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        try{
            Files.copy(target.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            throw new Exception500("시스템 내부에서 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
        user.updateProfileUrl(filePath.toString());
    }


}
