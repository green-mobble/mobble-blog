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

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;
    private final HttpSession  session;

    // 글 목록
    @GetMapping("/boards")
    public String boardList(HttpServletRequest request) {

        BoardResponse.BoardListDTO respDTO = boardService.boardList();
        request.setAttribute("models", respDTO.getBoardList());

        return "board/list-page";
    }

    // 글 상세보기
    @GetMapping("/boards/{id}")
    public String boardDetail(@PathVariable("id")Integer id, HttpServletRequest request) {

        BoardResponse.BoardResponseDTO respDTO = boardService.boardfindById(id);
        request.setAttribute("model", respDTO);

        return "board/detail-page";
    }

    // 글 쓰기 폼
    @GetMapping("/boards/save-form")
    public String saveForm(){
        return "board/save-form";
    }

    // 글쓰기
    @PostMapping("/boards")
    public String boardSave(BoardRequest.BoardSaveANDUpdateDTO boardSaveDTO){

        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            throw new Exception401("로그인 하세요");
        }
        BoardResponse.DTO respDTO = boardService.boardSave(boardSaveDTO,sessionUser);
        return "redirect:/boards";
    }
    // 글 삭제
    @PostMapping("/boards/{id}/delete")
    public String boardDelete(@PathVariable("id")Integer id){
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            throw new Exception401("로그인 하세요");
        }
        boardService.boardDelete(id,sessionUser);
        return "redirect:/boards";
    }

    // 글 수정 폼
    @PostMapping("/boards/{id}/update-form")
    public String updateForm(@PathVariable("id")Integer id,HttpServletRequest request){

        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            throw new Exception401("로그인 하세요");
        }

        BoardResponse.BoardResponseDTO respDTO = boardService.boardfindById(id);
        request.setAttribute("model", respDTO);

        return "board/update-page";
    }

    // 글 수정
    @PostMapping("/boards/{id}/update")
    public String boardUpdate(@PathVariable("id") Integer id,BoardRequest.BoardSaveANDUpdateDTO boardSaveDTO){
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DTO respDTO = boardService.boardUpdate(id,boardSaveDTO,sessionUser);
        return "redirect:/boards/" + id;
    }

}
