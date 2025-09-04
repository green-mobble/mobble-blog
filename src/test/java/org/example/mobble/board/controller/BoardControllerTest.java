package org.example.mobble.board.controller;

import org.example.mobble.board.domain.SearchOrderCase;
import org.example.mobble.board.dto.BoardRequest;
import org.example.mobble.board.dto.BoardResponse;
import org.example.mobble.board.service.BoardService;
import org.example.mobble.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BoardControllerStandaloneTest {

    private MockMvc mockMvc;
    private BoardService boardService;     // @MockBean 대신 Mockito.mock 사용
    private MockHttpSession session;
    private BoardController controller;

    private static List<BoardResponse.DTO> dummies(int n) {
        return IntStream.range(0, n)
                .mapToObj(i -> Mockito.mock(BoardResponse.DTO.class))
                .toList();
    }

    @BeforeEach
    void setup() {
        boardService = Mockito.mock(BoardService.class);
        session = new MockHttpSession();
        session.setAttribute("user", Mockito.mock(User.class)); // 로그인 세션

        controller = new BoardController(boardService, session);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /boards - 1페이지(오버페치 발생) → isFirst=true, isLast=false")
    void list_firstPage_hasNext() throws Exception {
        int perPage = BoardController.PER_PAGE; // 10
        when(boardService.getList(0, perPage + 1))
                .thenReturn(dummies(perPage + 1)); // 11개 → 다음 페이지 존재

        MvcResult mvcResult = mockMvc.perform(get("/boards").param("page", "1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("board/list-page"))
                // request attribute로 넣었으므로 request()로 검증
                .andExpect(request().attribute("isFirst", true))
                .andExpect(request().attribute("isLast", false))
                .andExpect(request().attribute("model", notNullValue()))
                .andReturn();

        // see
        MockHttpServletRequest request = mvcResult.getRequest();
        List<BoardResponse.DTO> model = (List<BoardResponse.DTO>) request.getAttribute("model");
        System.out.println("👉List model = " + model.toString());
    }

    @Test
    @DisplayName("GET /boards - 마지막 페이지(오버페치 없음) → isFirst=false, isLast=true")
    void list_lastPage_noNext() throws Exception {
        int perPage = BoardController.PER_PAGE; // 10
        int page = 2;
        int firstIndex = (page - 1) * perPage; // 10
        when(boardService.getList(firstIndex, perPage + 1))
                .thenReturn(dummies(7)); // 7개 → 오버페치 없음 → 마지막

        MvcResult mvcResult = mockMvc.perform(get("/boards").param("page", String.valueOf(page)).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("board/list-page"))
                .andExpect(request().attribute("isFirst", false))
                .andExpect(request().attribute("isLast", true))
                .andExpect(request().attribute("model", notNullValue()))
                .andReturn();

        // see
        MockHttpServletRequest request = mvcResult.getRequest();
        List<BoardResponse.DTO> model = (List<BoardResponse.DTO>) request.getAttribute("model");
        System.out.println("👉List model = " + model.toString());
    }

    @Test
    @DisplayName("GET /boards/search - 오버페치 발생 → isFirst=true, isLast=false")
    void search_hasNext() throws Exception {
        int perPage = BoardController.PER_PAGE;
        when(boardService.findBy(anyString(), anyString(), any(SearchOrderCase.class),
                eq(0), eq(perPage + 1)))
                .thenReturn(dummies(perPage + 1));

        MvcResult mvcResult = mockMvc.perform(get("/boards/search")
                        .param("key", "title")
                        .param("keyword", "hello")
                        .param("order", "CREATED_AT_ASC")
                        .param("page", "1")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("board/list-page"))
                .andExpect(request().attribute("isFirst", true))
                .andExpect(request().attribute("isLast", false))
                .andExpect(request().attribute("model", notNullValue()))
                .andReturn();

        // see
        MockHttpServletRequest request = mvcResult.getRequest();
        List<BoardResponse.DTO> model = (List<BoardResponse.DTO>) request.getAttribute("model");
        System.out.println("👉SEARCH model = " + model.toString());
    }

    @Test
    @DisplayName("GET /boards/{id} - 상세 뷰 렌더")
    void detail_ok() throws Exception {
        when(boardService.getUpdateBoardDetail(1))
                .thenReturn(Mockito.mock(BoardResponse.DetailDTO.class));

        MvcResult mvcResult = mockMvc.perform(get("/boards/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("board/detail-page"))
                .andExpect(request().attribute("model", notNullValue()))
                .andReturn();

        // see
        MockHttpServletRequest request = mvcResult.getRequest();
        BoardResponse.DetailDTO model = (BoardResponse.DetailDTO) request.getAttribute("model");
        System.out.println("👉 Detail model = " + model.toString());
    }

    @Test
    @DisplayName("POST /boards/{id}/update - 서비스 호출 및 리다이렉트")
    void update_ok() throws Exception {
        // given
        var board = Mockito.mock(org.example.mobble.board.domain.Board.class);
        when(board.getId()).thenReturn(1);
        when(boardService.update(eq(1), any(BoardRequest.BoardUpdateDTO.class), any(User.class)))
                .thenReturn(board); // ✅ NPE 방지: 컨트롤러가 getId() 써도 안전

        // when
        mockMvc.perform(post("/boards/1/update")
                        .param("title", "new title")      // 필요시 DTO 필드 채우기
                        .param("content", "new content")  // (검증/바인딩 실패 방지)
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/1"));
        verify(boardService).update(eq(1), any(BoardRequest.BoardUpdateDTO.class), any(User.class));

        //redirect 라서 확인 불가
    }

    @Test
    @DisplayName("POST /boards - 저장 후 리다이렉트")
    void save_ok() throws Exception {
        var board = Mockito.mock(org.example.mobble.board.domain.Board.class);
        when(board.getId()).thenReturn(99);
        when(boardService.save(any(BoardRequest.BoardSaveDTO.class), any(User.class)))
                .thenReturn(board);

        mockMvc.perform(post("/boards").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/99"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("POST /boards/{id}/delete - 삭제 후 목록 리다이렉트")
    void delete_ok() throws Exception {
        mockMvc.perform(post("/boards/1/delete").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards"));

        verify(boardService).delete(eq(1), any(User.class));
    }

    @Test
    @DisplayName("POST /boards/{id}/report - 신고 후 상세 리다이렉트")
    void report_ok() throws Exception {

        mockMvc.perform(post("/boards/1/report").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/1"));

        verify(boardService).report(any(User.class), eq(1), any(BoardRequest.BoardReportDTO.class));


        //redirect 라서 확인 불가
    }
}
