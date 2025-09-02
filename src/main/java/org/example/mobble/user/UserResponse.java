package org.example.mobble.user;

import lombok.Data;

public class UserResponse {

    @Data
    public static class LoginDTO{
        private String username;
        private Integer id;

        public LoginDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }

    @Data
    public static class DTO{
        private Integer id;
        private String username;
        // pw 제외
        private String password;
        private String email;

        public DTO(User user) {
            this.id = user.getId();
            // pw 제외
            this.password = user.getPassword();
            this.username = user.getUsername();
            this.email = user.getEmail();

        }
    }

    @Data
    public static class UserDetailDTO{
        private Integer id;
        private String  username;
        private byte[] profileImage;

        public UserDetailDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }
}
