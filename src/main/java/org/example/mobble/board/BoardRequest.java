package org.example.mobble.board;

import lombok.Data;

public class BoardRequest {

    @Data
    public class BoardSaveDTO {
    String title;
    String content;
    String category;
    }
    @Data
    public class BoardUpdateDTO{
        Integer id;
        String title;
        String content;
        Integer categoryId;
    }

}
