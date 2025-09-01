package org.example.mobble.board;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;
    private final HttpSession session;

    @GetMapping("/boards")
    public String list(HttpServletRequest request) {
        List<BoardResponse.BoardDTO> boards = boardService.게시글목록();
        request.setAttribute("models", boards);
        return "board/list-page";
    }

    @GetMapping("/boards/{id}")
    public String detail(HttpServletRequest request, @PathVariable Integer id) {
        BoardResponse.BoardDetailDTO board = boardService.게시글상세(id);
        request.setAttribute("model", board);
        return "board/detail-page";
    }

    @GetMapping("/boards/save-form")
    public String saveForm() {
        return "board/save-page";
    }

    @GetMapping("/boards/update-form/{id}")
    public String updateForm(HttpServletRequest request, @PathVariable Integer id) {
        BoardResponse.BoardDTO board = boardService.게시글수정폼(id);
        request.setAttribute("model", board);
        return "board/update-page";
    }

    @PostMapping("/boards/save")
    public String save(BoardRequest.BoardSaveDTO requestDTO) {
        boardService.게시글추가(requestDTO);
        return "redirect:/boards";
    }

    @PostMapping("/boards/{id}/delete")
    public String deleteById(@PathVariable Integer id){
        boardService.게시글삭제(id);
        return "redirect:/boards";
    }

    @PostMapping("/boards/{id}")
    public String updateById(@PathVariable Integer id, BoardRequest.BoardUpdateDTO requestDTO) {
        boardService.게시글수정(id, requestDTO);
        return "redirect:/boards";
    }

}
