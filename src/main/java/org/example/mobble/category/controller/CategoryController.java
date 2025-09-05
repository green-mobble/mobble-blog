package org.example.mobble.category.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble.category.dto.CategoryRequest;
import org.example.mobble.category.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mypage/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final HttpSession session;

    // 마이페이지(내 피드) > 카테고리 관리 화면
    @GetMapping
    public String categoriesPage(HttpServletRequest request) {
        Integer userId = 1; // TODO: 세션 로그인으로 교체
        request.setAttribute("model", categoryService.getCategoriesByUser(userId));
        return "mypage/category-page";
    }

    // 카테고리 추가
    @PostMapping
    public String addCategory(CategoryRequest.CategorySaveDTO req) {
        Integer userId = 1;
        categoryService.addCategory(userId, req.getCategory()); // 빈값 금지(마이페이지 정책)
        return "redirect:/mypage/categories";
    }

    // 카테고리 이름 변경
    @PostMapping("/{categoryId}/rename")
    public String renameCategory(@PathVariable Integer categoryId,
                                 CategoryRequest.CategoryUpdateDTO req) {
        Integer userId = 1;
        categoryService.renameCategory(userId, categoryId, req.getCategory());
        return "redirect:/mypage/categories";
    }

    // 카테고리 삭제 (폼 전송)
    @PostMapping("/{categoryId}/delete")
    public String deleteCategory(@PathVariable Integer categoryId) {
        Integer userId = 1;
        categoryService.deleteCategory(userId, categoryId);
        return "redirect:/mypage/categories";
    }
}
