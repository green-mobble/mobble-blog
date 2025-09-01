package org.example.mobble.board.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble.board.dto.BoardRequest;
import org.example.mobble.board.dto.BoardResponse;
import org.example.mobble.board.service.BoardService;
import org.example.mobble.user.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;
    private final HttpSession session;

    @GetMapping("/save-form")
    public String boardSaveForm() {
        User user = getLoginUser();
        return "board/save-form";
    }

    @GetMapping
    public String getBoardsList(HttpServletRequest request) {
        User user = getLoginUser();
        List<BoardResponse.DTO> resDTO = boardService.getList(user.getId());
        request.setAttribute("boardsList", resDTO);
        return "board/list-page";
    }

    @PostMapping()
    public String boardSave(BoardRequest.BoardSaveDTO reqDTO) {
        User user = getLoginUser();
        boardService.save(reqDTO, user);
        return "board/save-page";
    }

    private User getLoginUser() {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new Exception401("로그인 후 이용 부탁드립니다.");
        }
        return user;
    }
}
