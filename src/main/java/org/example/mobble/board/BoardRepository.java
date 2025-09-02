package org.example.mobble.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;


    public List<Board> findAll() {
        Query query = em.createQuery("select b from Board b ORDER BY b.createdAt desc", Board.class);
        return query.getResultList();
    }

    public Board findById(Integer id) {
        return em.find(Board.class, id);
    }

    public void viewsIncrease(Integer id) {
        Board board = em.find(Board.class, id);
        // 세터를 이름 바꿔서 하면 좋을 것 같음
        board.setViews(board.getViews() + 1);
    }

    public Board boardSave(Board board) {
        em.persist(board);
        return board;
    }

    public void boardDelete(Integer id) {
        Query query = em.createQuery("delete from Board b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
