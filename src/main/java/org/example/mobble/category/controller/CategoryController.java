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
public class CategoryController {

    private final CategoryService categoryService;
    private final HttpSession session;

    // 마이페이지(내 피드) > 카테고리 관리 화면
    @GetMapping("/categories")
    public String categoriesPage(HttpServletRequest request) {
        // TODO(local-test): 로그인 우회
        // Integer userId = currentUserIdOrThrow();
        Integer userId = 1;  // 테스트용 하드 코딩
        request.setAttribute("model", categoryService.getCategoriesByUser(userId));
        request.setAttribute("saveForm", CategoryRequest.CategorySaveDTO.builder().build());
        return "mypage/category-page";
    }

    // 카테고리 추가 (폼 전송)
    @PostMapping("/category")
    public String addCategory(@ModelAttribute("saveForm") CategoryRequest.CategorySaveDTO reqDTO) {
        // TODO(local-test): 로그인 우회
        Integer userId = 1;  // 테스트용 하드 코딩
        categoryService.addCategory(userId, reqDTO.getCategory());
        return "redirect:/categories";
    }

    // 카테고리 이름 변경 (빈값이면 서비스에서 예외)
    @PostMapping("/category/{categoryId}")
    public String renameCategory(@PathVariable Integer categoryId, CategoryRequest.CategoryUpdateDTO reqDTO) {
        // TODO(local-test): 로그인 우회
        Integer userId = 1;

        reqDTO.setId(categoryId); // PathVariable 우선
        categoryService.renameCategory(userId, reqDTO.getId(), reqDTO.getCategory());
        return "redirect:/categories";
    }

    // 카테고리 삭제 (폼 전송)
    @PostMapping("/category/{categoryId}/delete")
    public String deleteCategory(@PathVariable Integer categoryId) {
        // TODO(local-test): 로그인 우회
        Integer userId = 1;

        categoryService.deleteCategory(userId, categoryId);
        return "redirect:/categories";
    }
}
