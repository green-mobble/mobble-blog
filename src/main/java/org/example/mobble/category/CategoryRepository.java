package org.example.mobble.category;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.example.mobble.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CategoryRepository {
    private final EntityManager em;

    public Optional<Category> findByCategoryAndUserId(String category, Integer userId) {
        try {
            Category result = em.createQuery(
                            "select c from Category c where c.category=:category and c.user.id=:userId",
                            Category.class)
                    .setParameter("category", category)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.of(result);
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public Category save(Category category) {
        em.persist(category);
        return category;
    }
}
