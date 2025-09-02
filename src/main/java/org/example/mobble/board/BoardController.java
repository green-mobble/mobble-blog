package org.example.mobble.board;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble.user.User;
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
        User sessionUser = (User) session.getAttribute("session");
        if(sessionUser == null){
            throw new Exception401("로그인이 필요합니다.");
        }
        return "board/save-page";
    }

    @GetMapping("/boards/update-form/{id}")
    public String updateForm(HttpServletRequest request, @PathVariable Integer id) {
        User sessionUser = (User)session.getAttribute("session");
        if(sessionUser == null){
            throw new Exception401("로그인이 필요합니다.");
        }
        BoardResponse.BoardDTO board = boardService.게시글수정폼(id, sessionUser.getId());
        request.setAttribute("model", board);
        return "board/update-page";
    }

    @PostMapping("/boards/save")
    public String save(BoardRequest.BoardSaveDTO requestDTO) {
        User sessionUser = (User)session.getAttribute("session");
        if(sessionUser == null){
            throw new Exception401("로그인이 필요합니다.");
        }
        boardService.게시글추가(requestDTO, sessionUser);
        return "redirect:/boards";
    }

    @PostMapping("/boards/{id}/delete") // update, delete -> put, delete
    public String deleteById(@PathVariable Integer id) {
        User sessionUser = (User) session.getAttribute("session");
        if(sessionUser == null){
            throw new Exception401("로그인이 필요합니다.");
        }
        boardService.게시글삭제(id, sessionUser.getId());
        return "redirect:/boards";
    }

    @PostMapping("/boards/{id}")
    public String updateById(@PathVariable Integer id, BoardRequest.BoardUpdateDTO requestDTO) {
        User sessionUser = (User)session.getAttribute("session");
        if(sessionUser == null){
            throw new Exception401("로그인이 필요합니다.");
        }
        boardService.게시글수정(id, requestDTO, sessionUser.getId());
        return "redirect:/boards";
    }

}
