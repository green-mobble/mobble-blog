package org.example.mobble.board;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble._util.error.ex.Exception404;
import org.example.mobble.user.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardResponse.BoardListDTO boardList() {
        // 전체리스트
        List<Board> boardList = boardRepository.findAll();
        return new BoardResponse.BoardListDTO(boardList);
    }

    @Transactional
    public BoardResponse.BoardDetailDTO boardfindById(Integer id) {
        // 상세보기
        Board findBoard = boardRepository.findById(id);

        // 조회수 증가
        // TODO : 조회수 증가는 Board 객체에서 더티체킹 하면 됨
        boardRepository.viewsIncrease(id);

        return new BoardResponse.BoardDetailDTO(findBoard);
    }

    @Transactional
    public BoardResponse.DTO boardSave(BoardRequest.BoardSaveDTO boardSaveDTO, User sessionUser) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Board board = Board.builder()
                .title(boardSaveDTO.getTitle())
                .content(boardSaveDTO.getContent())
                .user(sessionUser)
                .views(0)
                .bookmark(0)
                .createdAt(now)
                .updatedAt(now)
                .categoryId(boardSaveDTO.getCategoryId()) // TODO : category name으로 변경
                .build();
        Board boardsave = boardRepository.boardSave(board);
        return new  BoardResponse.DTO(boardsave) ;
    }

    @Transactional
    public void boardDelete(Integer id, User sessionUser) {

        Board findBoard = boardRepository.findById(id);

        if (findBoard == null) {
            throw new Exception404("게시물을 찾을 수 없습니다 : " + id); // TODO : 에러 메시지도 컨벤션 정하기
        }
        if(findBoard.getUser().getId() != sessionUser.getId()) {
            throw new Exception401("권한 없습니다.");
        }

        boardRepository.boardDelete(id);
    }

    @Transactional
    public BoardResponse.DTO boardUpdate(Integer id, BoardRequest.BoardSaveDTO boardSaveDTO, User sessionUser) {

        Board findBoard = boardRepository.findById(id);
        if (findBoard == null) {
            throw new Exception404("게시물 찾을수 없어요");
        }
        if(findBoard.getUser().getId() != sessionUser.getId()) {
            throw new Exception401("권한 없습니다.");
        }

        // 더티체킹은 update 함수 만들어서
        findBoard.setTitle(boardSaveDTO.getTitle());
        findBoard.setContent(boardSaveDTO.getContent());
        findBoard.setCategoryId(boardSaveDTO.getCategoryId());
        return new BoardResponse.DTO(findBoard) ;
    }
}
