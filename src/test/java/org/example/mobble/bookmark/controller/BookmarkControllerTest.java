package org.example.mobble.bookmark.controller;

import org.example.mobble.board.domain.Board;
import org.example.mobble.board.domain.BoardRepository;
import org.example.mobble.board.service.BoardService;
import org.example.mobble.bookmark.domain.Bookmark;
import org.example.mobble.bookmark.domain.BookmarkRepository;
import org.example.mobble.bookmark.dto.BookmarkResponse;
import org.example.mobble.bookmark.service.BookmarkService;
import org.example.mobble.category.Category;
import org.example.mobble.category.CategoryRepository;
import org.example.mobble.user.domain.User;
import org.example.mobble.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookmarkService bookmarkService;

    private User testUser;
    private Board testBoard;

    @BeforeEach
    void setUp() {
        // 1. 테스트용 유저 생성
        testUser = User.builder()
                .username("testuser")
                .password("1234")
                .email("test@test.com")
                .build();
        userRepository.save(testUser);
        System.out.println(">>> 생성된 테스트 유저: id=" + testUser.getId() + ", username=" + testUser.getUsername());

        // 2. 테스트용 Board 생성 및 User 연결
        testBoard = Board.builder()
                .title("테스트 게시글")
                .content("테스트 내용")
                .user(testUser)  // User 연결
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        boardRepository.save(testBoard);
        System.out.println(">>> 생성된 테스트 Board: id=" + testBoard.getId() + ", title=" + testBoard.getTitle());
    }

    @Test
    @DisplayName("북마크 저장 성공")
    void bookmarkSave_success() throws Exception {
        mockMvc.perform(post("/bookmark/{boardId}/save", testBoard.getId())
                        .sessionAttr("user", testUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/" + testBoard.getId()));

        Bookmark bookmark = bookmarkRepository.findByBoardIdAndUserId(testBoard.getId(), testUser.getId())
                .orElseThrow(() -> new IllegalStateException("북마크 저장 실패"));

        System.out.printf(">>> 저장된 북마크 확인: boardId=%d, boardTitle=%s, userId=%d, username=%s%n",
                bookmark.getBoard().getId(),
                bookmark.getBoard().getTitle(),
                bookmark.getUser().getId(),
                bookmark.getUser().getUsername());

        assertThat(bookmark.getBoard().getId()).isEqualTo(testBoard.getId());
        assertThat(bookmark.getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("북마크 삭제 성공")
    void bookmarkDelete_success() throws Exception {
        // 먼저 북마크 생성
        Bookmark bookmark = Bookmark.builder()
                .board(testBoard)
                .user(testUser)
                .build();
        bookmarkRepository.BookmarkSave(bookmark);

        System.out.printf(">>> 삭제 전 북마크: boardId=%d, boardTitle=%s, userId=%d, username=%s%n",
                bookmark.getBoard().getId(),
                bookmark.getBoard().getTitle(),
                bookmark.getUser().getId(),
                bookmark.getUser().getUsername());

        mockMvc.perform(post("/bookmark/{boardId}/delete", testBoard.getId())
                        .sessionAttr("user", testUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/" + testBoard.getId()));

        boolean exists = bookmarkRepository.findByBoardIdAndUserId(testBoard.getId(), testUser.getId()).isPresent();
        System.out.println(">>> 삭제 후 북마크 존재 여부: " + exists);
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("북마크 리스트 조회 - 더미 5개, Board 모든 값 포함")
    void bookmarkList_withBoardTitle_success() throws Exception {
        List<Board> boards = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Board board = Board.builder()
                    .title("테스트 제목 " + i)
                    .content("테스트 내용 " + i)
                    .views(10 + i)
                    .user(testUser) // 글 작성자
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            boardRepository.save(board);
            boards.add(board);

            Bookmark bm = Bookmark.builder()
                    .board(board)
                    .user(testUser)
                    .build();
            bookmarkRepository.BookmarkSave(bm);

            System.out.printf(">>> 저장된 Board & Bookmark: boardId=%d, boardTitle=%s, userId=%d, username=%s, bookmarkId=%d%n",
                    board.getId(), board.getTitle(), testUser.getId(), testUser.getUsername(), bm.getId());
        }

        mockMvc.perform(get("/bookmarks")
                        .sessionAttr("user", testUser))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage/main"));

        BookmarkResponse.BookmarkListDTO respDTO = bookmarkService.bookmarkList(testUser.getId());

        System.out.println(">>> 북마크 리스트 조회 확인:");
        System.out.println("isList = " + respDTO.isList());
        for (BookmarkResponse.BookmarkDTO dto : respDTO.getBookmarksList()) {
            Board board = dto.getBoard();
            System.out.printf("bookId=%d, boardId=%d, boardTitle=%s, boardContent=%s, boardViews=%d, userId=%d, username=%s%n",
                    dto.getBookId(),
                    board.getId(),
                    board.getTitle(),
                    board.getContent(),
                    board.getViews(),
                    board.getUser().getId(),
                    board.getUser().getUsername());
        }
    }

    @Test
    @DisplayName("북마크 리스트 조회 - 북마크 없음, isList=false")
    void bookmarkList_emptyList_isFalse() throws Exception {
        User newUser = User.builder()
                .username("nouser")
                .password("1234")
                .email("nouser@test.com")
                .build();
        userRepository.save(newUser);
        System.out.println(">>> 새 유저 생성: id=" + newUser.getId() + ", username=" + newUser.getUsername());

        mockMvc.perform(get("/bookmarks")
                        .sessionAttr("user", newUser))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage/main"));

        BookmarkResponse.BookmarkListDTO respDTO = bookmarkService.bookmarkList(newUser.getId());
        System.out.println(">>> 북마크 리스트 조회 확인 (빈 리스트): isList=" + respDTO.isList() +
                ", size=" + respDTO.getBookmarksList().size());
    }
}
