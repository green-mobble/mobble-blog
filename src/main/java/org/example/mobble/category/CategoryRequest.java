package org.example.mobble.category;

import lombok.Data;

public class CategoryRequest {
    @Data
    public static class CategorySaveDTO {
        public String category;

        public CategorySaveDTO(String category) {
            this.category = category;
        }
    }
}
