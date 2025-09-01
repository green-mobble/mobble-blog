package org.example.mobble.board;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble.user.UserResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String save(BoardRequest.BoardSaveDTO reqDTO){

        //세션 유저 (북마크 여부를 위해)
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //저장 (반환은 상태확인 / 리다이렉트 id 용)
        BoardResponse.DTO resDTO = boardService.save(reqDTO,sessionUser.getId());

        return "redirect:/boards/"+resDTO.getId();
    }

    //글 수정하기
    @PutMapping  ("/boards/{id}")
    public String update(@PathVariable("id")  Integer boardId, BoardRequest.BoardUpdateDTO reqDTO){

        //세션 유저 검증용
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //수정 (반환은 상태확인 / 리다이렉트 id 용)
        BoardResponse.DTO resDTO = boardService.update(reqDTO,sessionUser.getId(),boardId);

        return "redirect:/boards/"+resDTO.getId();
    }

    //글 신고하기
    @DeleteMapping("/boards/{id}")
    public String delete(@PathVariable("id")  Integer boardId){

        //세션 유저 검증용
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        //삭제
        boardService.delete(boardId,sessionUser.getId());

        return "redirect:/boards";
    }

    //게시판 쓰기 화면
    @GetMapping("/boards/save-form")
    public String saveForm(){

        //카테고리 리스트를 내려줄 필요가 있는지? 존재하는 카테고리를 빠르게 선택할 수 있게

        return "board/save-page";
    }

    //게시판수정화면
    @GetMapping("/boards/{id}/update-form")
    public String updateForm(@PathVariable("id")  Integer boardId, Model model ){

        //board 정보 내리기
//        boardService.updateForm();
        //model.addAttribute("resDTO", resDTO);
        return "board/list-page";
    }


    //신고화면
    @GetMapping("/boards/{id}/report-form")
    public String reportForm(@PathVariable("id")  Integer boardId,Model model ){

        //model.addAttribute("resDTO", resDTO);
        return "board/list-page";  //신고화면으로 연결
    }





}
