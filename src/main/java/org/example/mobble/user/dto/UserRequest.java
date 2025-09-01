package org.example.mobble.user.dto;

import lombok.Builder;
import lombok.Data;

public class UserRequest {
    @Data
    public static class LoginDTO {
        private String username;
        private String password;

        @Builder
        public LoginDTO(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Data
    public static class JoinDTO {
        private String email;
        private String username;
        private String password;

        @Builder
        public JoinDTO(String email, String username, String password) {
            this.email = email;
            this.username = username;
            this.password = password;
        }
    }
}
