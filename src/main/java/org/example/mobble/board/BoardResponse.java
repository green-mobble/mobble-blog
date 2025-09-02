package org.example.mobble.board;

import lombok.Data;

import java.sql.Timestamp;


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
            // TODO: 조회수 추가
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
        // TODO: 카테고리 추가

        public BoardDetailDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.views = board.getViews();
            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();
            // TODO: 카테고리 추가
        }
    }
}
