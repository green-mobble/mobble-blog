package org.example.mobble.board;

import lombok.Data;

import java.security.Timestamp;

public class BoardResponse {

    @Data
    public static class BoardListDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer views;
        private Timestamp createdAt;
        public BoardListDTO(Board b) {
            this.id = b.getId();
            this.title = b.getTitle();
            this.content = b.getContent();
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
        public BoardDetailDTO(Board b) {
            this.id = b.getId();
            this.title = b.getTitle();
            this.content = b.getContent();
            this.views = b.getViews();
        }
    }
}
