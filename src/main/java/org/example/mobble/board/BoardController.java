package org.example.mobble.board;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;

    //리스트 전체보기
    @GetMapping("/boards")
    public String listBoards(Model model ){

        //기본 전체 리스트
        boardService.findAll();

        // 클릭시 키워드별 리스트
        //리스트별로 sorting기능

        return "redirect:/";
    }

    //글 상세보기

    //글 쓰기

    //글 수정하기

    //글 신고하기

    //신고화면, 삭제화면,신고하기화면


}
