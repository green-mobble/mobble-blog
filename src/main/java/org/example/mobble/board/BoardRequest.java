package org.example.mobble.board;

import lombok.Data;
import org.example.mobble.category.Category;

@Data
public class BoardRequest {

    // 생성, 수정 같아서 -> DTO
    @Data
    public class BoardSaveDTO {
        private String title;
        private String content;
        private Category category;
    }
}
