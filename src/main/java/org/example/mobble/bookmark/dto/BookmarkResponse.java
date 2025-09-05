package org.example.mobble.bookmark.dto;

import lombok.Data;
import org.example.mobble.board.domain.Board;
import org.example.mobble.bookmark.domain.Bookmark;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BookmarkResponse {

    @Data
    public static class BookmarkSaveDTO {
        private Integer boardId;
        private Integer userId;

        public BookmarkSaveDTO(Bookmark bookmark) {
            this.boardId = bookmark.getBoard().getId();
            this.userId = bookmark.getUserId();
        }
    }

    @Data
    public static class BookmarkDTO {
        private Integer bookId;
        private Board board;
        private Integer userId;
        private Timestamp createAt;

        public BookmarkDTO(Bookmark  bookmark) {
            this.bookId = bookmark.getId();
            this.board = bookmark.getBoard();
            this.userId = bookmark.getUserId();
            this.createAt = bookmark.getCreatedAt();
        }
    }

    @Data
    public static class BookmarkListDTO {
        private boolean isList;
        private List<BookmarkDTO> bookmarksList;

        public BookmarkListDTO(List<Bookmark> bookmarks) {
            this.isList = bookmarks != null && !bookmarks.isEmpty();
            this.bookmarksList = bookmarks.stream().map(BookmarkDTO::new).collect(Collectors.toList());
        }
    }
}
