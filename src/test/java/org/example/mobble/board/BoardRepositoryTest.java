package org.example.mobble.board;

import jakarta.persistence.EntityManager;
import org.example.mobble.category.Category;
import org.example.mobble.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.sql.Timestamp;
import java.util.List;

@Import(BoardRepository.class)
@DataJpaTest
public class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    EntityManager em;

    @Test
    public void findAll(){
        List<Board> boards = boardRepository.findAll();

        boards.forEach(board -> {
            System.out.println("ID: " + board.getId());
            System.out.println("UserId: " + board.getUser().getId());
            System.out.println("Username: " + board.getUser().getUsername());
            System.out.println("Title: " + board.getTitle());
            System.out.println("Content: " + board.getContent());
            System.out.println("CreatedAt: " + board.getCreatedAt());
            System.out.println("Views: " + board.getViews());
            System.out.println("Bookmark: " + board.getBookmark());
            System.out.println("CategoryId: " + board.getCategoryId());
            System.out.println("-------------------------");
        });
    }

    @Test
    public void findById_test(){
        int id = 2;

        Board board = boardRepository.findById(id);

        System.out.println("ID: " + board.getId());
        System.out.println("UserId: " + board.getUser().getId());
        System.out.println("Username: " + board.getUser().getUsername());
        System.out.println("Title: " + board.getTitle());
        System.out.println("Content: " + board.getContent());
        System.out.println("CreatedAt: " + board.getCreatedAt());
        System.out.println("Views: " + board.getViews());
        System.out.println("Bookmark: " + board.getBookmark());
        System.out.println("CategoryId: " + board.getCategoryId());
    }

    @Test
    public void boardSave_test(){
        User user = new User(1, "ssar", "1234", "ssar@nate.com");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String title = "제목입니다.";
        String content = "내용입니다.";
        Integer category = 1;
        Board board = Board.builder()
                .id(null)
                .title(title)
                .content(content)
                .user(user)
                .views(0)
                .bookmark(0)
                .createdAt(now)
                .updatedAt(now)
                .categoryId(category)
                .build();
        boardRepository.boardSave(board);
        Board savedBoard = em.find(Board.class, board.getId());
        System.out.println("저장된 게시글 ID: " + savedBoard.getId());
        System.out.println("제목: " + savedBoard.getTitle());
        System.out.println("내용: " + savedBoard.getContent());
        System.out.println("작성자: " + savedBoard.getUser().getUsername());
        System.out.println("카테고리: " + savedBoard.getCategoryId());
        System.out.println("조회수: " + savedBoard.getViews());
        System.out.println("북마크: " + savedBoard.getBookmark());
        System.out.println("생성일: " + savedBoard.getCreatedAt());
        System.out.println("수정일: " + savedBoard.getUpdatedAt());

    }

}
