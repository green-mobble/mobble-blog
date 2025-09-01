package org.example.mobble.category;

import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.ExceptionApi404;
import org.example.mobble.board.BoardRequest;
import org.example.mobble.user.User;
import org.example.mobble.user.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category save( CategoryRequest.CategorySaveDTO reqDTO,User user) {

        //조회
        Optional<Category> categoryPS = categoryRepository.findByCategoryAndUserId(reqDTO.getCategory(),user.getId());

        // 있으면 그 값 리턴
        if (categoryPS.isPresent()) { return categoryPS.get(); }

        //없으면 저장 후 리턴
        else{
            Category newCategory = Category.builder()
                    .category(reqDTO.getCategory())
                    .userId(user.getId())
                    .build();

            return categoryRepository.save(newCategory);
        }


    }
}
