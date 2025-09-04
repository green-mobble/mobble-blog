package org.example.mobble.category;

import jakarta.persistence.EntityManager;
import org.example.mobble.category.domain.Category;
import org.example.mobble.category.domain.CategoryRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({CategoryRepository.class})
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    EntityManager em;

    @Test
    void save_and_findByUserIdAndCategory() {
        // given
        Category c = Category.builder().userId(1).category("Dev").build();

        // when
        categoryRepository.save(c);
        em.flush(); // DB 반영

        // then
        assertThat(c.getId()).isNotNull();
        assertThat(categoryRepository.findByUserIdAndCategory(1, "Dev")).isPresent();
    }

    @Test
    void existsByUserIdAndCategory() {
        // given
        Category c = Category.builder().userId(1).category("Dev").build();

        // when
        categoryRepository.save(c);
        em.flush();

        // then
        assertThat(categoryRepository.existsByUserIdAndCategory(1, "Dev")).isTrue();
        assertThat(categoryRepository.existsByUserIdAndCategory(1, "Ops")).isFalse();
    }

    @Test
    void uniqueConstraint_userId_category_enforced() {
        // given: 같은 userId + category 두 번 저장 → 유니크 위반
        categoryRepository.save(Category.builder().userId(1).category("Dev").build());

        // when & then: 두 번째 save()나 flush() 어느 시점에서든 제약 위반이 발생할 수 있음
        assertThatThrownBy(() -> {
            categoryRepository.save(Category.builder().userId(1).category("Dev").build());
            em.flush();
        }).isInstanceOf(org.hibernate.exception.ConstraintViolationException.class);

    }

    @Test
    void findById_returnsCategory_whenExists() {
        // given
        Category c = Category.builder().userId(1).category("Dev").build();
        categoryRepository.save(c);
        em.flush(); em.clear();

        // when
        Optional<Category> found = categoryRepository.findById(c.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getCategory()).isEqualTo("Dev");
    }

    @Test
    void findAllByUserIdOrderByIdDesc_returnsCategoriesSorted() {
        // given
        categoryRepository.save(Category.builder().userId(2).category("A").build());
        categoryRepository.save(Category.builder().userId(2).category("B").build());
        em.flush(); em.clear();

        // when
        List<Category> categories = categoryRepository.findAllByUserIdOrderByIdDesc(2);

        // then
        assertThat(categories).hasSize(2);
        assertThat(categories.get(0).getCategory()).isEqualTo("B"); // desc 정렬 확인
    }

    @Test
    void existsByUserIdAndCategory_returnsTrue_whenDuplicateExists() {
        // given
        categoryRepository.save(Category.builder().userId(1).category("Dev").build());
        em.flush(); em.clear();

        // when
        boolean exists = categoryRepository.existsByUserIdAndCategory(1, "Dev");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void deleteById_removesCategory() {
        // given
        Category c = Category.builder().userId(1).category("Dev").build();
        categoryRepository.save(c);
        em.flush(); em.clear();

        // when
        categoryRepository.deleteById(c.getId());
        em.flush(); em.clear();

        // then
        assertThat(categoryRepository.findById(c.getId())).isEmpty();
    }

}
