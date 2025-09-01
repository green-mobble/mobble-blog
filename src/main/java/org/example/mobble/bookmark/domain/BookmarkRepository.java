package org.example.mobble.bookmark.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BookmarkRepository {
    private final EntityManager em;

    public List<Bookmark> findByUserId(Integer userId) {
        return em.createQuery("select b from Bookmark b where b.userId = :userId", Bookmark.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
