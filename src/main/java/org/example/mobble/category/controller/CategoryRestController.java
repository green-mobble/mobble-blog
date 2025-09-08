package org.example.mobble.category.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble.category.dto.CategoryResponse;
import org.example.mobble.category.service.CategoryService;
import org.example.mobble.user.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final CategoryService categoryService;
    private final HttpSession session;

    // 내 카테고리 목록
    @GetMapping
    public List<CategoryResponse.CategoryItemDTO> myCategories() {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) throw new Exception401("로그인이 필요합니다.");
        return categoryService.getMyCategoryItems(sessionUser.getId());
    }
}
