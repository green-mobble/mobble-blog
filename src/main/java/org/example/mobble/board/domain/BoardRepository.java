package org.example.mobble.board.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public List<Board> findAllCreatedAt() {
        return em.createQuery("select b from Board b order by b.createdAt desc", Board.class)
                .getResultList();
    }
}
