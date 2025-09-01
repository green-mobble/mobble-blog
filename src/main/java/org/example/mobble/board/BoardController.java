package org.example.mobble.board;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble.user.UserResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;
    private final HttpSession session;

    //내 글 리스트 전체보기
    @GetMapping("/boards")
    public String list(Model model ){

        //기본 전체 리스트
        List<BoardResponse.BoardDTO> resDTO = boardService.list();
        model.addAttribute("resDTO", resDTO);
        return "board/list-page";
    }

    //글 상세보기
    @GetMapping("/boards/{id}")
    public String detail(@PathVariable("id")  Integer boardId , Model model ){

        //세션 유저 (북마크 여부를 위해)
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //상세페이지 조회
        BoardResponse.BoardDetailDTO resDTO = boardService.detail(boardId,sessionUser.getId());

        model.addAttribute("resDTO", resDTO);
        return "board/detail-page";
    }

    //글 쓰기
    @PostMapping ("/boards")
    public String save(BoardRequest.BoardSaveDTO reqDTO, Model model){

        //세션 유저 (북마크 여부를 위해)
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //저장 (상태확인을 위해 값을 리턴 받음)
        BoardResponse.DTO resDTO = boardService.save(reqDTO,sessionUser.getId());

        //model.addAttribute("resDTO", resDTO);
        return "board/detail-page";
    }

    //글 수정하기
    @PutMapping  ("/boards/{id}")
    public String update(BoardRequest.BoardUpdateDTO reqDTO, Model model){

        //세션 유저 (북마크 여부를 위해)
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //저장 (상태확인을 위해 값을 리턴 받음)
        BoardResponse.DTO resDTO = boardService.save(reqDTO,sessionUser.getId());

        model.addAttribute("resDTO", resDTO);
        return "board/detail-page";
    }

    //글 신고하기

    //신고화면, 삭제화면,신고하기화면





}
