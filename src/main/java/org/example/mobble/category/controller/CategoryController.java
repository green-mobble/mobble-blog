package org.example.mobble.category.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble.category.dto.CategoryForm;
import org.example.mobble.category.service.CategoryService;
import org.example.mobble.user.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * SSR(템플릿) 전용 카테고리 컨트롤러
 * - 예외는 Exception400/401/404 등 "일반 예외"를 던져 HTML로 응답
 * - 성공 시에는 redirect
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/mypage/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final HttpSession session;

    // 마이페이지 - 카테고리 관리 화면
    @GetMapping
    public String categoriesPage(Model model) {
        // TODO(local-test): 로그인 우회
        // Integer userId = currentUserIdOrThrow();
        Integer userId = 1;  // 테스트용 하드 코딩
        model.addAttribute("categories", categoryService.getCategoriesByUser(userId));
        model.addAttribute("saveForm", new CategoryForm.Save());
        return "mypage/update-page"; // 필요시 "mypage/main"으로 변경
    }

    // 카테고리 추가 (폼 전송)
    @PostMapping
    public String addCategory(@Valid @ModelAttribute("saveForm") CategoryForm.Save form, Errors errors) {
        // TODO(local-test): 로그인 우회
        // Integer userId = currentUserIdOrThrow();
        Integer userId = 1;  // 테스트용 하드 코딩
        categoryService.addCategory(userId, form.getCategory());
        return "redirect:/mypage/categories";
    }

    // 카테고리 이름 변경 (폼 전송)
    @PostMapping("/{categoryId}/rename")
    public String renameCategory(@PathVariable Integer categoryId,
                                 @Valid @ModelAttribute CategoryForm.Rename form,
                                 Errors errors) {
        // TODO(local-test): 로그인 우회
        // currentUserIdOrThrow();  // 임시 주석
        categoryService.renameCategory(categoryId, form.getCategory());
        return "redirect:/mypage/categories";
    }

    // 카테고리 삭제 (폼 전송)
    @PostMapping("/{categoryId}/delete")
    public String deleteCategory(@PathVariable Integer categoryId) {
        // TODO(local-test): 로그인 우회
        // currentUserIdOrThrow();  // 임시 주석
        categoryService.deleteCategory(categoryId);
        return "redirect:/mypage/categories";
    }

    // ===== 공통 유틸 =====
    private Integer currentUserIdOrThrow() {
        Object principal = session.getAttribute("sessionUser");
        if (principal == null) {
            throw new Exception401("로그인이 필요합니다."); // GlobalExceptionHandler가 /login-form 으로 href
        }
        return ((User) principal).getId();
    }
}
