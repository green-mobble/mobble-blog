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
        board.setViews(board.getViews() + 1);
    }
}
