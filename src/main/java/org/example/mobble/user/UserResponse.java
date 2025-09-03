package org.example.mobble.user;

import lombok.Data;

@Data
public class UserResponse {

    @Data
    public static class userDTO {
        private String username;
        private String email;

        public userDTO(User user) {
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }
}
