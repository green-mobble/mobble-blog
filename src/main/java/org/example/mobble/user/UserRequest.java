package org.example.mobble.user;

import lombok.Data;

@Data
public class UserRequest {

    @Data
    public class UserJoinDTO {
        private String username;
        private String password;
        private String email;
    }

    @Data
    public class UserLoginDTO {
        private String username;
        private String password;
    }
}
