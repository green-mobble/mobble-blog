package org.example.mobble.category.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble.category.service.CategoryService;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class CategoryController {
    private final CategoryService categoryService;
    private final HttpSession session;
}
