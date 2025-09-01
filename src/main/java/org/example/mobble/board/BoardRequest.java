package org.example.mobble.board;

import lombok.Data;

import java.sql.Timestamp;

public class BoardRequest {

    @Data
    public static class BoardSaveDTO {
        private String title;
        private String content;

        public Board toEntity() {
            return Board.builder().title(title).content(content).build();
        }
    }
}
