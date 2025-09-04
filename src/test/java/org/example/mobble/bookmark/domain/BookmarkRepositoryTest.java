package org.example.mobble.bookmark.domain;

import org.example.mobble.board.domain.Board;
import org.example.mobble.board.domain.BoardRepository;
import org.example.mobble.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Import({BookmarkRepository.class, BoardRepository.class})
@DataJpaTest
public class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BoardRepository boardRepository;

    // 북마크 있는지 조회 (false일떄)
    @Test
    public void findByBoardIdAndUserIdFALSE_test() {
        Integer userId = 1;
        Integer boardId = 1;

        Board board = Board.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        boardRepository.save(board);

        bookmarkRepository.findByBoardIdAndUserId(boardId, userId)
                .ifPresentOrElse(
                        bookmark -> {
                            System.out.println(">>> 북마크 존재 여부: BoardId = " + bookmark.getBoard().getId());
                            System.out.println(">>> 북마크 존재 여부: UserId  = " + bookmark.getUserId());
                        },
                        () -> System.out.println(">>> 있는지 조회 했늗데 북마크 없음")
                );
    }

    // 북마크 있는지 조회 (true일떄)
    @Test
    public void findByBoardIdAndUserIdTRUE_test() {
        Integer userId = 1;

        Board board = Board.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        boardRepository.save(board); // save 후 ID가 생성됨

        Integer savedBoardId = board.getId(); // 실제 DB에서 할당된 ID 사용

        Bookmark bookmark = Bookmark.builder()
                .board(board)
                .userId(userId)
                .build();
        bookmarkRepository.BookmarkSave(bookmark);

// 존재 여부 확인
        bookmarkRepository.findByBoardIdAndUserId(savedBoardId, userId)
                .ifPresentOrElse(
                        b -> {
                            System.out.println(">>> 북마크 존재: BoardId = " + b.getBoard().getId());
                            System.out.println(">>> 북마크 존재: Board Title = " + b.getBoard().getTitle());
                            System.out.println(">>> 북마크 존재: UserId  = " + b.getUserId());
                        },
                        () -> System.out.println(">>> 북마크 없음")
                );
    }

    //북마크 저장
    @Test
    public void bookmarkSave_test(){
        Integer userId = 1;
        Integer boardId = 1;

        Board board = Board.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        boardRepository.save(board);

        Board boardPS = boardRepository.findById(board.getId()).orElse(null);
        Bookmark bookmark = Bookmark.builder()
                .board(boardPS)
                .userId(userId)
                .build();
        bookmarkRepository.BookmarkSave(bookmark);


        System.out.println(">>> 저장 후 Bookmark 값: " + bookmark);
    }

    // 북마크 삭제
    @Test
    public void bookmarkDelete_test() {
        Integer userId = 1;
        Integer boardId = 1;

        Board board = boardRepository.findById(boardId).orElse(null);
        // 1. 테스트용 북마크 먼저 저장
        Bookmark bookmark = Bookmark.builder()
                .board(board)
                .userId(userId)
                .build();
        bookmarkRepository.BookmarkSave(bookmark); // DB에 저장

        // 2. 삭제
        bookmarkRepository.BookmarkDelete(boardId, userId);

        // 3. 삭제 여부 확인
        bookmarkRepository.findByBoardIdAndUserId(boardId, userId)
                .ifPresentOrElse(
                        b -> System.out.println(">>> 삭제 후 북마크 존재: BoardId = " + b.getBoard().getId()),
                        () -> System.out.println(">>> 삭제 후 북마크 없음") // false 예상
                );
    }

    @Test
    public void bookmarkList_test() {
        Integer userId = 1;

        List<Board> savedBoards = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Board board = Board.builder()
                    .title("테스트 제목 " + i)
                    .content("테스트 내용 " + i)
                    .views(i)
                    .build();
            boardRepository.save(board); // DB에 저장
            savedBoards.add(board);
        }

        // 더미 북마크 생성
        for (Board board : savedBoards) {
            Bookmark bookmark = Bookmark.builder()
                    .board(board) // 실제 DB에 저장된 Board 객체
                    .userId(userId)
                    .build();
            bookmarkRepository.BookmarkSave(bookmark); // 또는 BookmarkSave(bookmark)
        }

        // 북마크 조회
        List<Bookmark> bookmarks = bookmarkRepository.bookmarkList(userId);
        System.out.println(">>> 저장된 북마크 리스트:");
        for (Bookmark b : bookmarks) {
            System.out.println("BoardId: " + b.getBoard().getId() +
                    ", Board Title: " + b.getBoard().getTitle() +
                    ", Board views: " + b.getBoard().getViews() +
                    ", UserId: " + b.getUserId());
        }
    }
}
