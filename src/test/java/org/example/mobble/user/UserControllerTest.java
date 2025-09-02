package org.example.mobble.user;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    private MockHttpSession session;

    @BeforeEach
    public void setup() {
        // 세션 객체 생성
        session = new MockHttpSession();
        // User 엔티티 가짜 객체 생성
        User user = User.builder()
                .id(2)
                .username("cos")
                .password("1234")
                .email("cos@nate.com")
                .build();
        // User → LoginDTO 변환
        UserResponse.LoginDTO sessionUser = new UserResponse.LoginDTO(user);
        // 세션에 사용자 심기
        session.setAttribute("sessionUser", sessionUser);

    }

    @Test
    void login_success_test() throws Exception {
        // given

        // when
        ResultActions actions = mvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "ssar")
                        .param("password", "1234")
        );


        // then
        actions.andExpect(status().isFound()) // "main.mustache" 뷰 리턴하면 200 OK
                .andExpect(request().sessionAttribute("sessionUser", notNullValue()))
                .andDo(print());
    }

    @Test
    void detail_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                get("/users")
                        .session(session)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(request().sessionAttribute("sessionUser", notNullValue()))
                .andDo(print());
    }

    @Test
    void updatePassword_test() throws Exception {
        // given
        //password 는 나중에 body로 변경

        // when
        ResultActions actions = mvc.perform(
                put("/users/2/password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .session(session)
                        .param("password", "2345")
        );

        // then
        actions.andExpect(status().is3xxRedirection()) // redirect:/users 로 이동
                .andExpect(redirectedUrl("/users"))
                .andDo(print());
    }

    @Test
    void updateProfile_test() throws Exception {

        // given - 목업 파일 생성
        MockMultipartFile mf = new MockMultipartFile(
                "profileImage",                        // 요청 파라미터 이름 (@RequestParam("profileImage")와 일치해야 함)
                "test.png",                            // 원본 파일명
                MediaType.IMAGE_PNG_VALUE,             // MIME 타입
                "dummy image content".getBytes()       // 파일 내용 (바이트 배열)
        );

        // when
        ResultActions actions = mvc.perform(
                multipart("/users/2/profile")          // multipart() 사용해야 파일 업로드 가능
                        .file(mf)                      // MockMultipartFile 전달
                        .session(session)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
        );

        // then
        actions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andDo(print());
    }

    @Test
    void delete_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                delete("/users/2")
                        .session(session)
        );

        // then
        actions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());
    }

    @Test
    void updateForm_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                get("/users/update-form/2")
                        .session(session)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(model().attributeExists("resDTO"))
                .andDo(print());
    }


}