package org.example.mobble.board;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponse.BoardListDTO> 게시글목록() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponse.BoardListDTO> boardListDto = boards.stream()
                .map(board -> new BoardResponse.BoardListDTO(board))
                .collect(Collectors.toList());
        return boardListDto;
    }
}
