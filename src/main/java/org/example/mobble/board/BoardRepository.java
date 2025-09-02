package org.example.mobble.board;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public List<Board> findAll(){
        List<Board> boards = em.createQuery("select b from Board b", Board.class).getResultList();
        return boards;
    }

    public Optional<Board> findById(int id){
        Board board = em.find(Board.class, id);
        return Optional.ofNullable(board);
    }

    public void save(Board board){
        em.persist(board);
    }

    public void deleteById(int id) {
        em.createQuery("delete from Board b where b.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public Optional<Board> findByIdJoinUser(int id) {
        Optional<Board> board = em.createQuery("select b from Board b join fetch b.user where b.id = :id", Board.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
        return board;
    }
}
