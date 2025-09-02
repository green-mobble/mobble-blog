package org.example.mobble.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;

public class BoardResponse {

    @Data
    @AllArgsConstructor
    public static class BoardDTO{
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private Integer views;
        private String category;
        private Timestamp createdAt;
        private Timestamp updatedAt;

        private Long bookmarkCount;
        private String image; // base64
    }

    @Data
    @AllArgsConstructor
    public static class BoardDetailDTO {

        private Integer id;
        private Integer userId;
        private String username;
        private String profileUrl;
        private String title;
        private String content;
        private Integer views;
        private Long  bookmarkCount;
        private Boolean isBookmark;
        private String category;
        private Timestamp createdAt;
        private Timestamp updatedAt;
    }

    @Data
    public static class DTO {
        private Integer id;
        private String title;
        private String content;
        private Integer userId;       // user 엔티티 대신 userId만
        private Integer views;
        private Integer categoryId;   // category 엔티티 대신 categoryId만
        private Timestamp createdAt;
        private Timestamp updatedAt;

        public DTO(Board board) {
            this.updatedAt = board.getUpdatedAt();
            this.createdAt = board.getCreatedAt();
            this.categoryId = board.getCategory().getId();
            this.views = board.getViews();
            this.userId = board.getUser().getId();
            this.content = board.getContent();
            this.title = board.getTitle();
            this.id = board.getId();
        }

    }



}
