package org.example.mobble.board;


import org.example.mobble.user.User;
import org.example.mobble.user.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BoardService boardService;

    private MockHttpSession session;

    @BeforeEach
    public void setup() {
        // 세션 객체 생성
        session = new MockHttpSession();
        // User 엔티티 가짜 객체 생성
        User user = User.builder()
                .id(1)
                .username("ssar")
                .password("1234")
                .email("ssar@nate.com")
                .build();
        // User → LoginDTO 변환
        UserResponse.LoginDTO sessionUser = new UserResponse.LoginDTO(user);
        // 세션에 사용자 심기
        session.setAttribute("sessionUser", sessionUser);

    }

    @Test
    void list_test() throws Exception {
        // given
        //testAdminLogin  = 로그인이 되는지 확인을 하는 유저

        // when
        ResultActions actions = mvc.perform(
                get("/boards")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .session(session)
        );


        // then
        actions.andExpect(status().isOk()) // "main.mustache" 뷰 리턴하면 200 OK
                .andExpect(request().sessionAttribute("sessionUser", notNullValue()))
                .andDo(print());

    }

    @Test
    void findAll_success() {
        // when
        List<BoardResponse.BoardDTO> result = boardService.list();
        System.out.println(result.get(1).getCategory());

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo("첫 번째 글");
        assertThat(result.get(0).getUserId()).isEqualTo(1);
        assertThat(result.get(0).getViews()).isEqualTo(15);

    }

    @Test
    void detail_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                get("/boards/" + 1)
                        .session(session)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(model().attributeExists("resDTO")) // 모델에 resDTO 있는지 확인
                .andDo(print());
    }

    @Test
    void save_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                post("/boards")
                        .session(session)
                        .param("title", "새 제목")
                        .param("content", "새 내용")
                        .param("category", "java")
        );

        // then
        actions.andExpect(status().is3xxRedirection()) // redirect
                .andExpect(redirectedUrlPattern("/boards/*")) // 저장 후 리다이렉트
                .andDo(print());
    }

    @Test
    void update_test() throws Exception {
        //when
        ResultActions actions = mvc.perform(
                put("/boards/" + 1)
                        .session(session)
                        .param("title", "수정된 제목")
                        .param("content", "수정된 내용")
                        .param("category", "공지사항")
        );

        //then
        actions.andExpect(status().is3xxRedirection()) // redirect
                .andExpect(redirectedUrlPattern("/boards/*")) // 저장 후 리다이렉트
                .andDo(print());
    }

    @Test
    void delete_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                delete("/boards/" + 2)
                        .session(session)
        );

        // then
        actions.andExpect(status().is3xxRedirection()) // 삭제 후 리다이렉트
                .andExpect(redirectedUrl("/boards"))
                .andDo(print());
    }
}