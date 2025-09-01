package org.example.mobble.board;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/boards")
    public String boardList(HttpServletRequest request) {
        BoardResponse.BoardListDTO respDTO = boardService.boardList();
        request.setAttribute("models", respDTO.getBoardList()); // 리스트만 전달
        return "board/list-page";
    }

    @GetMapping("/boards/{id}")
    public String boardDetail(@PathVariable("id")Integer id, HttpServletRequest request) {
        BoardResponse.BoardDetailDTO respDTO = boardService.boardfindById(id);
        request.setAttribute("model", respDTO);
        return "board/detail-page";
    }
}
