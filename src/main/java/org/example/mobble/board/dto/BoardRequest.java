package org.example.mobble.board.dto;

import lombok.Builder;
import lombok.Data;

public class BoardRequest {
    @Data
    public class BoardSaveDTO {
        String category;
        String title;
        String content;

        @Builder
        public BoardSaveDTO(String category, String title, String content) {
            this.category = category;
            this.title = title;
            this.content = content;
        }
    }
}
