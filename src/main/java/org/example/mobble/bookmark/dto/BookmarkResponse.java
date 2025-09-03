package org.example.mobble.bookmark.dto;

import lombok.Data;
import org.example.mobble.bookmark.domain.Bookmark;

@Data
public class BookmarkResponse {

    @Data
    public class BookmarkSaveDTO {
        private Integer boardId;
        private Integer userId;

        public BookmarkSaveDTO(Bookmark bookmark) {
            this.boardId = bookmark.getBoardId();
            this.userId = bookmark.getUserId();
        }
    }
}
