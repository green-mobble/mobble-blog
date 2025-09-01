package org.example.mobble.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public List<BoardResponse.BoardDTO> findAll() {
        return em.createQuery(
                        "select new org.example.mobble.board.BoardResponse$BoardDTO(" +
                                "   b.id, " +
                                "   b.title, " +
                                "   b.content, " +
                                "   u.id, " +
                                "   b.views, " +
                                "   c.category, " +
                                "   b.createdAt, " +
                                "   b.updatedAt, " +
                                "   (select count(m.id) from Bookmark m where m.boardId = b.id), " +
                                "   null) " +
                                "from Board b " +
                                "join b.user u " +
                                "left join b.category c " ,
                        BoardResponse.BoardDTO.class
                )
                .getResultList();

    }

    public Optional<BoardResponse.BoardDetailDTO> findByBoardIdandUserId(Integer boardId, Integer userId) {

            return Optional.ofNullable(em.createQuery(
                            "select new org.example.mobble.board.BoardResponse$BoardDetailDTO(" +
                                    "   b.id, " +
                                    "   u.id, " +
                                    "   u.username, " +
                                    "   null, " +
                                    "   b.title, " +
                                    "   b.content, " +
                                    "   b.views, " +
                                    "   (select count(m.id) from Bookmark m where m.boardId = b.id)," +   // bookmarkCount
                                    " (case when (select count(bm.id) from Bookmark bm " +
                                    "             where bm.boardId = b.id and bm.userId = :userId) > 0 " +
                                    "       then true else false end), " +   //  isBookmark
                                    "   c.category, " +
                                    "   b.createdAt, " +
                                    "   b.updatedAt) " +
                                    "from Board b " +
                                    "join b.user u " +
                                    "left join b.category c "+
                                    "where b.id = :boardId",
                            BoardResponse.BoardDetailDTO.class
                    )
                    .setParameter("userId", userId)
                    .setParameter("boardId", boardId)
                    .getSingleResult());
    }

    public void save(Board board) {
        em.persist(board);
    }
}
