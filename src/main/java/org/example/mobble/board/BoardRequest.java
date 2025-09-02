package org.example.mobble.board;

import lombok.Data;
import org.example.mobble.user.User;

import java.sql.Timestamp;

public class BoardRequest {

    @Data
    public static class BoardSaveDTO {
        private String title;
        private String content;
        private User user;


        public Board toEntity(User user) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .build();
        }
    }

    @Data
    public static class BoardUpdateDTO {
        private String title;
        private String content;
    }
}
