package org.example.mobble.user;

import lombok.Data;

public class UserRequest {

    @Data
    public static class JoinDTO {
        private String username;
        private String password;

        // 객체 생성
        public User toEntity(){
            return User.builder()
                    .username(username)
                    .password(password)
                    .build();
        }
    }

    @Data
    public static class LoginDTO {
        private String username;
        private String password;
    }
}
