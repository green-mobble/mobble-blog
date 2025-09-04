package org.example.mobble.bookmark.controller;

import org.example.mobble.board.domain.Board;
import org.example.mobble.board.domain.BoardRepository;
import org.example.mobble.bookmark.domain.Bookmark;
import org.example.mobble.bookmark.domain.BookmarkRepository;
import org.example.mobble.bookmark.dto.BookmarkResponse;
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

        // 2. 테스트용 Board 생성
        testBoard = Board.builder()
                .title("테스트 게시글")
                .content("테스트 내용")
                .build();
        boardRepository.save(testBoard);
    }

    @Test
    @DisplayName("북마크 저장 성공")
    void bookmarkSave_success() throws Exception {
        // when
        mockMvc.perform(post("/bookmark/{boardId}/save", testBoard.getId())
                        .sessionAttr("user", testUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/" + testBoard.getId()));

        // then
        Bookmark bookmark = bookmarkRepository.findByBoardIdAndUserId(testBoard.getId(), testUser.getId())
                .orElseThrow(() -> new IllegalStateException("북마크 저장 실패"));

        System.out.printf("==== 북마크 저장 확인: boardId=%d, userId=%d, boardTitle=%s ====%n",
                bookmark.getBoard().getId(),
                bookmark.getUserId(),
                bookmark.getBoard().getTitle());

        assertThat(bookmark.getBoard().getId()).isEqualTo(testBoard.getId());
        assertThat(bookmark.getUserId()).isEqualTo(testUser.getId());
        assertThat(bookmark.getBoard().getTitle()).isEqualTo("테스트 게시글");
    }


    @Test
    @DisplayName("북마크 삭제 성공")
    void bookmarkDelete_success() throws Exception {
        // given
        Integer boardId = testBoard.getId();

        Bookmark bookmark = Bookmark.builder()
                .board(testBoard)
                .userId(testUser.getId())
                .build();
        bookmarkRepository.BookmarkSave(bookmark);

        System.out.printf("==== 저장된 북마크: boardId=%d, userId=%d ====%n",
                bookmark.getBoard().getId(), bookmark.getUserId());

        // when
        mockMvc.perform(post("/bookmark/{boardId}/delete", boardId)
                        .sessionAttr("user", testUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/" + boardId));

        // then
        boolean exists = bookmarkRepository.findByBoardIdAndUserId(boardId, testUser.getId()).isPresent();

        System.out.println("==== 삭제 후 확인 ====");
        System.out.println("남아있나? -> " + exists);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("북마크 리스트 조회 - 더미 5개, Board 모든 값 포함")
    void bookmarkList_withBoardTitle_success() throws Exception {
        // given: 테스트 유저, Board 5개 생성
        List<Board> boards = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Board board = Board.builder()
                    .title("테스트 제목 " + i)
                    .content("테스트 내용 " + i)
                    .views(10 + i)              // 조회수 다르게 설정
                    .userId(testUser.getId())    // 글 작성자
                    .categoryId(i)               // 카테고리 id
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            boardRepository.save(board);
            boards.add(board);

            // 북마크 저장
            bookmarkRepository.BookmarkSave(Bookmark.builder()
                    .board(board)
                    .userId(testUser.getId())
                    .build());
        }

        // when
        mockMvc.perform(get("/bookmarks")
                        .sessionAttr("user", testUser))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage/main"));

        // then: DB에서 직접 조회해서 DTO 변환 후 검증
        List<BookmarkResponse.BookmarkDTO> bookmarkDTOList = bookmarkRepository.bookmarkList(testUser.getId())
                .stream()
                .map(BookmarkResponse.BookmarkDTO::new)
                .toList();

        System.out.println("==== 북마크 리스트 조회 확인 ====");
        for (BookmarkResponse.BookmarkDTO dto : bookmarkDTOList) {
            Board board = dto.getBoard(); // fetch join 했으면 바로 접근 가능
            System.out.printf("bookId=%d, boardId=%d, boardTitle=%s, boardContent=%s, boardViews=%d, userId=%d, categoryId=%d, createAt=%s, updateAt=%s%n",
                    dto.getBookId(),
                    board.getId(),
                    board.getTitle(),
                    board.getContent(),
                    board.getViews(),
                    board.getUserId(),
                    board.getCategoryId(),
                    board.getCreatedAt(),
                    board.getUpdatedAt());
        }

        assertThat(bookmarkDTOList).hasSize(5);
    }
}
