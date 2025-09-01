package org.example.mobble.user;



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

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    private MockHttpSession session;

    @BeforeEach
    public void setup() {

        // SecurityContext 에 ADMIN 사용자 인증 심어주기
    }

    @Test
    void login_success_test() throws Exception {
        // given
        //testAdminLogin  = 로그인이 되는지 확인을 하는 유저

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
}