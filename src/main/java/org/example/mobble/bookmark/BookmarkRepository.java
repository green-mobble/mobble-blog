package org.example.mobble.bookmark;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookmarkRepository {
    private final EntityManager em;

    public Long countByBoardId(Integer boardId) {
        String jpql = "select count(b) from Bookmark b where b.boardId = :boardId";
        return em.createQuery(jpql, Long.class)
                .setParameter("boardId", boardId)
                .getSingleResult();

    }
}
