package org.example.mobble.user;

import lombok.Data;

public class UserResponse {

    @Data
    public static class LoginDTO{
        private String username;
        private String password;

        public LoginDTO(User user) {
            this.password = user.getPassword();
            this.username = user.getUsername();
        }
    }

    @Data
    public static class DTO{
        private Integer id;
        private String username;
        private String password;
        private String email;

        public DTO(User user) {
            this.id = user.getId();
            this.password = user.getPassword();
            this.username = user.getUsername();
            this.email = user.getEmail();

        }
    }
}
