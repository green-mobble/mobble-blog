package org.example.mobble.board;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardResponse.BoardListDTO boardList() {
        List<Board> boardList = boardRepository.findAll();
        return new BoardResponse.BoardListDTO(boardList);
    }

    public BoardResponse.BoardDetailDTO boardfindById(Integer id) {
        Board findById = boardRepository.findById(id);
        return new BoardResponse.BoardDetailDTO(findById);
    }
}
