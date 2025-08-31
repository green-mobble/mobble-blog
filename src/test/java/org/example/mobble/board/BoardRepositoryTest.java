package org.example.mobble.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(BoardRepository.class)
@DataJpaTest
public class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;

    @Test
    public void findAll(){
        List<Board> boards = boardRepository.findAll();

        boards.forEach(board -> {
            System.out.println("ID: " + board.getId());
            System.out.println("Title: " + board.getTitle());
            System.out.println("UserID: " + board.getUser().getId());
            System.out.println("Content: " + board.getContent());
            System.out.println("CreatedAt: " + board.getCreatedAt());
            System.out.println("-------------------------");
        });
    }

    @Test
    public void findById_test(){
        int id = 1;

        Board board = boardRepository.findById(id);

        System.out.println("lazy start");
        System.out.println("ID: " + board.getId());
        System.out.println("Title: " + board.getTitle());
        System.out.println("Username: " + board.getUser().getUsername());
    }


}
