package org.example.mobble.board;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class BoardResponse {

    @Data
    public static class BoardResponseDTO{
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private Integer views;
        private Integer categoryId;
        private Timestamp createdAt;
        private Timestamp updatedAt;

        public BoardResponseDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.userId = board.getUser().getId();
            this.views = board.getViews();
            this.categoryId = board.getCategoryId();
            this.createdAt = board.getCreatedAt();
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
        private Integer userId;
        private String title;
        private String content;
        private Timestamp createAt;
        private Integer views;
        private Integer categoryId;

        public BoardDetailDTO(Board board) {
            this.userId = board.getUser().getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.createAt =board.getCreatedAt();
            this.views = board.getViews();
            this.categoryId = board.getCategoryId();
        }
    }

}
