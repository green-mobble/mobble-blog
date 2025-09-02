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
    private BoardRepository boardRepository;

    @Test
    public void findAll_test(){
        //given

        //when
        List<Board> boards = boardRepository.findAll();
        //eye
        System.out.println("=======================");
        System.out.println("Board Count : " + boards.size());
        System.out.println("Board 1 title :" +boards.get(0).getTitle());
        System.out.println("Board 2 title :" +boards.get(1).getTitle());
        System.out.println("Board 2 title :" +boards.get(2).getTitle());
        System.out.println("Board 2 title :" +boards.get(3).getTitle());
        System.out.println("Board 2 title :" +boards.get(4).getTitle());
    }

    @Test
    public void findById_test() {
        // given
        int id = 1;
        // when
        Board board = boardRepository.findById(id).get();
        // eye
        System.out.println("=======================");
        System.out.println("Board Title : " + board.getTitle());
        System.out.println("Board Content : " + board.getContent());
    }

    @Test
    public void save_test(){
        //given
        Board board = Board.builder()
                .title("제목6")
                .content("내용6")
                .build();
        //when
        boardRepository.save(board);
        //eye
        List<Board> boards = boardRepository.findAll();
        System.out.println("=======================");
        System.out.println("Board Count : " + boards.size());
        System.out.println("Board ID: " + boards.get(5).getId());
        System.out.println("Board Title: " + boards.get(5).getTitle());
        System.out.println("Board Content: " + boards.get(5).getContent());
    }

    @Test
    public void deleteById_test(){
        //given
        int id = 5;
        //when
        boardRepository.deleteById(id);
        //eye
        List<Board> boards = boardRepository.findAll();
        System.out.println("=======================");
        System.out.println("Board count : " + boards.size());
    }

    @Test
    public void findByIdJoinUser_test(){
        //given
        int id = 1;
        //when
        Board board = boardRepository.findByIdJoinUser(id).get();
        //eye
        System.out.println("=======================");
        System.out.println("Board ID : " + board.getId());
        System.out.println("username : " + board.getUser().getUsername());
    }

}