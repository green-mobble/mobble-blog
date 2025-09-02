package org.example.mobble.board;

import lombok.Data;

import java.security.Timestamp;

public class BoardResponse {

    @Data
    public static class BoardDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer views;

        public BoardDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }

    @Data
    public static class BoardDetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer views;
        private Timestamp createdAt;
        private Timestamp updatedAt;
        private Integer userId;
        private String username;

        public BoardDetailDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.views = board.getViews();
            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();
        }
    }
}
