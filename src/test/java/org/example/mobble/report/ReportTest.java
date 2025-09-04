package org.example.mobble.report;


import org.example.mobble.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ReportTest {

    @Autowired
    private MockMvc mvc;

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
            session.setAttribute("user", user);
    }

        @Test
        public void update_form_test() throws Exception {
                //given
                Integer reportId = 1;

                //when
                ResultActions actions = mvc.perform(
                        MockMvcRequestBuilders
                                .get("/reports/{id}/update-form", reportId)
                                .contentType(MediaType.APPLICATION_JSON)
                );

                //eye
                String responseBody = actions.andReturn().getResponse().getContentAsString();
                System.out.println("✅응답바디 : " + responseBody);

                // then

        }

        @Test
        void get_report_list_test() throws Exception {
                // given
                // when
                ResultActions actions = mvc.perform(
                        get("/reports")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .session(session)
                );
                // then
                MvcResult result = actions.andExpect(status().isOk())
                        .andReturn();

                Map<String, Object> modelMap = result.getModelAndView().getModel();
                Object resDTO = modelMap.get("resDTO");
                System.out.println("=== resDTO ===");
                System.out.println(resDTO);

        }

        @Test
        void get_report_test() throws Exception {
                // given
                Integer reportId = 1;
                // when
                ResultActions actions = mvc.perform(
                        get("/reports/{id}",reportId)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .session(session)
                );
                // then
                MvcResult result = actions.andExpect(status().isOk())
                        .andReturn();

                Map<String, Object> modelMap = result.getModelAndView().getModel();
                Object resDTO = modelMap.get("resDTO");
                System.out.println("=== resDTO ===");
                System.out.println(resDTO);

        }



        @Test
        void update_test() throws Exception {
                // given
                // when
                ResultActions actions = mvc.perform(
                        post("/reports/4/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .session(session)
                                .param("result","ADVERTISING_BOARD_CONTENT")
                                .param("content","바보로 수정")
                );
                // then
                actions.andExpect(status().is3xxRedirection()) // redirect
                .andExpect(redirectedUrlPattern("/reports*")) // 저장 후 리다이렉트
                        .andDo(print());
        }

        @Test
        void delete_test() throws Exception {
                // given
                // when
                ResultActions actions = mvc.perform(
                        post("/reports/1/delete")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .session(session)
                );
                // then
                actions.andExpect(status().is3xxRedirection()) // redirect
                        .andExpect(redirectedUrlPattern("/reports*")) // 저장 후 리다이렉트
                        .andDo(print());
        }

    @Test
    void report_save_test() throws Exception {

        // given
        Integer boardId = 1;
        // when
        ResultActions actions = mvc.perform(
                post("/boards/{id}/report",boardId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .session(session)
                        .param("result","ADVERTISING_BOARD_CONTENT")
                        .param("content","추가 신고")

        );
        // then
        actions.andExpect(status().is3xxRedirection()) // redirect
                .andExpect(redirectedUrlPattern("/boards/*")) // 저장 후 리다이렉트
                .andDo(print());
    }


}