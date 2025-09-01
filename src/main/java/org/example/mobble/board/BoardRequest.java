package org.example.mobble.board;

import lombok.Data;

public class BoardRequest {

    @Data
    public class BoardSaveDTO {
    private String title;
    private String content;
    private String category;
    }

    @Data
    public class BoardUpdateDTO{
        private String title;
        private String content;
        private Integer categoryId;
    }

}
