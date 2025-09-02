package org.example.mobble.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class UserRequest {

    @Data
    public class LoginDTO{

        private String username;
        private String password;

    }

    @Data
    public class JoinDTO{

        private String username;
        private String password;
        private String email;

    }
    @Data
    public class UserPasswordUpdateDTO {
       // private Integer userId;
        private String password;

    }
    @Data
    public class UserProfileUpdateDTO {
      //  private Integer userId;
        private MultipartFile profileImage;

    }


}
