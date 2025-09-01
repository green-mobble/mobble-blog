package org.example.mobble.board;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;
    private final HttpSession session;

    @GetMapping("/boards")
    public String list(HttpServletRequest request) {
        List<BoardResponse.BoardListDTO> boards = boardService.게시글목록();
        request.setAttribute("models", boards);
        return "board/list-page";
    }

    @GetMapping("/boards/{boardId}")
    public String detail(HttpServletRequest request, @PathVariable Integer boardId) {
        BoardResponse.BoardDetailDTO board = boardService.게시글상세(boardId);
        request.setAttribute("model", board);
        return "board/detail-page";
    }

    @GetMapping("/boards/save-form")
    public String saveForm() {
        return "board/save-page";
    }

    @GetMapping("/boards/1/update-form")
    public String updateForm() {
        return "board/update-page";
    }

}
