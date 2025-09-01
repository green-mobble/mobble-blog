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
        private String imageUrl;
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



}
