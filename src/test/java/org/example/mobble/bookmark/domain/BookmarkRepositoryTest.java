package org.example.mobble.bookmark.domain;

import org.example.mobble.board.domain.Board;
import org.example.mobble.board.domain.BoardRepository;
import org.example.mobble.user.domain.User;
import org.example.mobble.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Import({BookmarkRepository.class, BoardRepository.class, UserRepository.class})
@DataJpaTest
public class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    // 북마크 있는지 조회 (false일떄)
    @Test
    public void findByBoardIdAndUserIdFALSE_test() {
        // 1. 테스트용 User 생성 및 저장
        User testUser = User.builder()
                .username("tester")
                .build();
        userRepository.save(testUser);

        // 2. 테스트용 Board 생성 및 User 연결 후 저장
        Board testBoard = Board.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .user(testUser)
                .build();
        boardRepository.save(testBoard);

        // 3. 존재하지 않는 북마크 조회 (아직 생성 안 함)
        bookmarkRepository.findByBoardIdAndUserId(testBoard.getId(), testUser.getId())
                .ifPresentOrElse(
                        bookmark -> {
                            System.out.println(">>> 북마크 존재: BoardId = " + bookmark.getBoard().getId());
                            System.out.println(">>> 북마크 존재: UserId = " + bookmark.getUser().getId());
                        },
                        () -> System.out.println(">>> 있는지 조회 했는데 북마크 없음") // 예상: 이 출력
                );
    }
    // 북마크 있는지 조회 (true일떄)
    @Test
    public void findByBoardIdAndUserIdTRUE_test() {
        User testUser = User.builder()
                .username("tester")
                .build();  // ID 지정하지 않음
        userRepository.save(testUser);

        Board testboard = Board.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .user(testUser)   // 여기서 user 지정
                .build();
        boardRepository.save(testboard);

        Bookmark bookmark = Bookmark.builder()
                .board(testboard)
                .user(testUser)   // 여기에도 같은 user 지정
                .build();
        bookmarkRepository.BookmarkSave(bookmark);

// 존재 여부 확인
        bookmarkRepository.findByBoardIdAndUserId(testboard.getId(), testUser.getId())
                .ifPresentOrElse(
                        b -> {
                            System.out.println(">>> 북마크 존재: BoardId = " + b.getBoard().getId());
                            System.out.println(">>> 북마크 존재: Board Title = " + b.getBoard().getTitle());
                            System.out.println(">>> 북마크 존재: UserId  = " + b.getUser().getId());
                        },
                        () -> System.out.println(">>> 북마크 없음")
                );
    }

    //북마크 저장
    @Test
    public void bookmarkSave_test() {
        // 1. 테스트용 User 생성 및 저장
        User testUser = User.builder()
                .username("tester")
                .build(); // ID는 지정하지 않음
        userRepository.save(testUser);

        // 2. 테스트용 Board 생성 및 User 연결 후 저장
        Board testBoard = Board.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .user(testUser) // 영속 상태 User 연결
                .build();
        boardRepository.save(testBoard);

        // 3. DB에서 Board 조회
        Board boardPS = boardRepository.findById(testBoard.getId()).orElseThrow();

        // 4. Bookmark 생성 및 저장
        Bookmark bookmark = Bookmark.builder()
                .board(boardPS)
                .user(testUser) // 같은 User 연결
                .build();
        bookmarkRepository.BookmarkSave(bookmark);

        System.out.println(">>> 저장 후 Bookmark 값: " + bookmark);
    }

    // 북마크 삭제
    @Test
    public void bookmarkDelete_test() {
        // 1. 테스트용 User 생성 및 저장
        User testUser = User.builder()
                .username("tester")
                .build();
        userRepository.save(testUser);

        // 2. 테스트용 Board 생성 및 User 연결 후 저장
        Board testBoard = Board.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .user(testUser)
                .build();
        boardRepository.save(testBoard);

        // 3. Bookmark 생성 및 저장
        Bookmark bookmark = Bookmark.builder()
                .board(testBoard)
                .user(testUser)
                .build();
        bookmarkRepository.BookmarkSave(bookmark);

        // 4. 삭제
        bookmarkRepository.BookmarkDelete(testBoard.getId(), testUser.getId());

        // 5. 삭제 여부 확인
        bookmarkRepository.findByBoardIdAndUserId(testBoard.getId(), testUser.getId())
                .ifPresentOrElse(
                        b -> System.out.println(">>> 삭제 후 북마크 존재: BoardId = " + b.getBoard().getId()),
                        () -> System.out.println(">>> 삭제 후 북마크 없음") // false 예상
                );
    }

    @Test
    public void bookmarkList_test() {
        // 1. 테스트용 User 생성 및 저장
        User testUser = User.builder()
                .username("tester")
                .build();
        userRepository.save(testUser);

        // 2. 테스트용 Boards 생성 및 User 연결 후 저장
        List<Board> savedBoards = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Board board = Board.builder()
                    .title("테스트 제목 " + i)
                    .content("테스트 내용 " + i)
                    .views(i)
                    .user(testUser) // User 연결
                    .build();
            boardRepository.save(board);
            savedBoards.add(board);
        }

        // 3. 테스트용 Bookmark 생성 및 저장
        for (Board board : savedBoards) {
            Bookmark bookmark = Bookmark.builder()
                    .board(board)
                    .user(testUser) // 항상 같은 User
                    .build();
            bookmarkRepository.BookmarkSave(bookmark);
        }

        // 4. 북마크 조회
        List<Bookmark> bookmarks = bookmarkRepository.bookmarkList(testUser.getId());
        System.out.println(">>> 저장된 북마크 리스트:");
        for (Bookmark b : bookmarks) {
            System.out.println("BoardId: " + b.getBoard().getId() +
                    ", Board Title: " + b.getBoard().getTitle() +
                    ", Board views: " + b.getBoard().getViews() +
                    ", UserId: " + b.getUser().getId());
        }
    }
}
