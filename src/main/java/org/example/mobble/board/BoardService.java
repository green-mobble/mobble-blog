package org.example.mobble.board;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception403;
import org.example.mobble._util.error.ex.Exception404;
import org.example.mobble.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponse.BoardDTO> 게시글목록() {

        List<Board> boards = boardRepository.findAll();
        List<BoardResponse.BoardDTO> responseDTO = boards.stream()
                .map(board -> new BoardResponse.BoardDTO(board))
                .collect(Collectors.toList());
        return responseDTO;
    }

    public BoardResponse.BoardDetailDTO 게시글상세(Integer id) {
        Board board = boardRepository.findByIdJoinUser(id).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        BoardResponse.BoardDetailDTO responseDTO = new BoardResponse.BoardDetailDTO(board);
        return responseDTO;
    }

    @Transactional
    public void 게시글추가(BoardRequest.BoardSaveDTO requestDTO, User sessionUser) {
        Board board = requestDTO.toEntity(sessionUser);
        boardRepository.save(board);
    }

    @Transactional
    public void 게시글삭제(Integer boardId, Integer sessionUserId) {
        Board  board = boardRepository.findByIdJoinUser(boardId).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        if(board.getUser().getId() != sessionUserId){
            throw new Exception403("게시글을 삭제할 권한이 없습니다.");
        }
        boardRepository.deleteById(boardId);
    }

    public BoardResponse.BoardDTO 게시글수정폼(Integer boardId, Integer sessionUserId) {
        Board board = boardRepository.findByIdJoinUser(boardId).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        if(board.getUser().getId() != sessionUserId){
            throw new Exception403("게시글을 수정할 권한이 없습니다.");
        }
        BoardResponse.BoardDTO boardDTO = new BoardResponse.BoardDTO(board);
        return boardDTO;
    }

    @Transactional
    public void 게시글수정(Integer boardId, BoardRequest.BoardUpdateDTO requestDTO, Integer sessionUserId) {
        Board board = boardRepository.findByIdJoinUser(boardId).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        if(board.getUser().getId() != sessionUserId){
            throw new Exception403("게시글을 수정할 권한이 없습니다.");
        }
        // 더티 체킹
        board.setTitle(requestDTO.getTitle());
        board.setContent(requestDTO.getContent());
    }
}
