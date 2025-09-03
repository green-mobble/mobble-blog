package org.example.mobble.category.service;

import lombok.RequiredArgsConstructor;
import org.example.mobble.category.domain.CategoryRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
}
