package org.example.mobble.category;

import lombok.Data;

@Data
public class CategoryRequest {

    @Data
    public static class CategorySaveDTO {
        private String category;

        public CategorySaveDTO(String category) {
            this.category = category;
        }
    }
}
