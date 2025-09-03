package org.example.mobble.category.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CategoryRepository {
    private final EntityManager em;

    public Category save(Category category) {
        em.persist(category);
        return category;
    }

    public Optional<Category> findById(Integer id) {
        return Optional.ofNullable(em.find(Category.class, id));
    }

    public List<Category> findAllByUserIdOrderByIdDesc(Integer userId) {
        return em.createQuery(
                "select c from Category c where c.userId = :userId order by c.id desc",
                Category.class
        ).setParameter("userId", userId).getResultList();
    }

    public Optional<Category> findByUserIdAndCategory(Integer userId, String category) {
        // 예외 대신 리스트로 안전 조회
        List<Category> rows = em.createQuery(
                        "select c from Category c where c.userId = :userId and c.category = :category",
                        Category.class
                ).setParameter("userId", userId)
                .setParameter("category", category)
                .setMaxResults(1)
                .getResultList();
        return rows.stream().findFirst();
    }

    public boolean existsByUserIdAndCategory(Integer userId, String category) {
        // 가장 가벼운 존재 확인: 1건만 조회
        List<Integer> rows = em.createQuery(
                        "select 1 from Category c where c.userId = :userId and c.category = :category",
                        Integer.class
                ).setParameter("userId", userId)
                .setParameter("category", category)
                .setMaxResults(1)
                .getResultList();
        return !rows.isEmpty();
    }

    public void deleteById(Integer id) {
        Category found = em.find(Category.class, id);
        if (found != null) em.remove(found);
    }
}
