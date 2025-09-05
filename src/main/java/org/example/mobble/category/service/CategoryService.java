package org.example.mobble.category.service;

import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception400;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble._util.error.ex.Exception404;
import org.example.mobble.category.domain.Category;
import org.example.mobble.category.domain.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


     // 특정 유저의 카테고리 목록 조회
    @Transactional(readOnly = true)
    public List<Category> getCategoriesByUser(Integer userId) {
        if (userId == null) throw new Exception400("userId가 없습니다.");
        return categoryRepository.findAllByUserIdOrderByIdDesc(userId);
    }

    // 카테고리 추가
    @Transactional
    public Category addCategory(Integer userId, String categoryName) {
        if (userId == null) throw new Exception400("userId가 없습니다.");
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new Exception400("카테고리명이 비어 있습니다.");
        }
        if (categoryRepository.existsByUserIdAndCategory(userId, categoryName.trim())) {
            throw new Exception400("이미 존재하는 카테고리명입니다.");
        }
        Category category = Category.builder()
                .userId(userId)
                .category(categoryName.trim())
                .build();
        return categoryRepository.save(category);
    }

    // 카테고리 이름 변경
    @Transactional
    public Category renameCategory(Integer userId, Integer categoryId, String newName) {
        if (userId == null) throw new Exception400("userId가 없습니다.");
        if (categoryId == null) throw new Exception400("categoryId가 없습니다.");
        if (newName == null || newName.trim().isEmpty()) {
            throw new Exception400("새 카테고리명이 비어 있습니다.");
        }
        String normalized = newName.trim();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exception404("카테고리를 찾을 수 없습니다."));

        // 소유자 검증
        if (!category.getUserId().equals(userId)) {
            throw new Exception401("본인 카테고리만 수정할 수 있습니다.");
        }

        // 동일 유저 내 중복 방지
        if (categoryRepository.existsByUserIdAndCategory(userId, normalized)) {
            throw new Exception400("이미 존재하는 카테고리명입니다.");
        }

        category.rename(normalized); // 더티체킹 반영
        return category;
    }

    // 카테고리 삭제
    @Transactional
    public void deleteCategory(Integer userId, Integer categoryId) {
        if (userId == null) throw new Exception400("userId가 없습니다.");
        if (categoryId == null) throw new Exception400("categoryId가 없습니다.");

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exception404("카테고리를 찾을 수 없습니다."));

        // 소유자 검증
        if (!category.getUserId().equals(userId)) {
            throw new Exception401("본인 카테고리만 삭제할 수 있습니다.");
        }

        categoryRepository.deleteById(category.getId());
    }
}
