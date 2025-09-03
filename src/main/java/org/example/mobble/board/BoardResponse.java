package org.example.mobble.board;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class BoardResponse {

    // TODO : 통일하거나, 데이터 갯수를 줄여서 차이 만들기 (헷갈림)
    @Data
    public static class BoardResponseDTO{
        private Integer id;
        private Integer userId;
        private String username;
        private String title;
        private String content;
        private Timestamp createAt;
        private Integer views;
        private Integer bookmark;
        private String categoryName;
        private Timestamp updatedAt;

        public BoardResponseDTO(Board board) {
            this.id = board.getId();
            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.createAt =board.getCreatedAt();
            this.views = board.getViews();
            this.bookmark = board.getBookmark();
            this.categoryName = board.getCategory().getCategory();
            this.updatedAt = board.getUpdatedAt();
        }
    }

    @Data
    public static class BoardListDTO {
        private List<BoardResponseDTO> boardList;

        public BoardListDTO(List<Board> boardList) {
            this.boardList = boardList.stream().map(BoardResponseDTO::new).toList();
        }
    }

    @Data
    public static class BoardDetailDTO {
        private Integer id;
        private Integer userId;
        private String username;
        private String title;
        private String content;
        private Timestamp createAt;
        private Integer views;
        private Integer bookmark;
        private Integer categoryId;
        private String categoryName;
        private Timestamp updatedAt;

        public BoardDetailDTO(Board board) {
            this.id = board.getId();
            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.createAt =board.getCreatedAt();
            this.views = board.getViews();
            this.bookmark = board.getBookmark();
            this.categoryName = board.getCategory().getCategory();
            this.updatedAt = board.getUpdatedAt();
        }
    }

    // 생성, 수정용
    @Data
    public static class DTO {
        private Integer id;
        private String title;
        private String content;
        private String categoryName;

        public DTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.categoryName = board.getCategory().getCategory();
        }
    }
}
