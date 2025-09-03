package org.example.mobble.bookmark.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.example.mobble.bookmark.dto.BookmarkResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookmarkRepository {
    private final EntityManager em;

    public boolean findByBoardIdAndUserId(int boardId, int userId) {
        Long count = em.createQuery(
                        "select count(b) from Bookmark b where b.boardId = :boardId and b.userId = :userId",
                        Long.class)
                .setParameter("boardId", boardId)
                .setParameter("userId", userId)
                .getSingleResult();

        return count > 0;
    }

    public BookmarkResponse.BookmarkSaveDTO BookmarkSave(Bookmark bookmark) {
        em.persist(bookmark);
        return null;
    }

    public void BookmarkDelete(Integer boardId, Integer userId) {
        Query query =  em.createQuery("delete from Bookmark b where b.boardId = :boardId and b.userId = :userId");
        query.setParameter("boardId", boardId);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }
}
