package org.example.mobble.board;


import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.ExceptionApi404;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponse.BoardDTO> list() {
        //리스트 받기
        List<BoardResponse.BoardDTO> resDTO = boardRepository.findAll();
        return resDTO;
    }

    public BoardResponse.BoardDetailDTO detail(Integer boardId, Integer userId) {

        BoardResponse.BoardDetailDTO resDTO = boardRepository.findByBoardIdandUserId(boardId,userId)
                .orElseThrow(() -> new ExceptionApi404("글을 찾을 수 없습니다."));
        return resDTO;
    }


}
