package org.example.mobble.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class CategoryForm {

    @Data
    public static class Save {
        @NotBlank(message = "카테고리명을 입력해주세요.")
        private String category;
    }

    @Data
    public static class Rename {
        @NotBlank(message = "새 카테고리명을 입력해주세요.")
        private String category;
    }
}
