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
        List<BoardResponse.BoardListDTO> boardListDTO = boards.stream()
                .map(board -> new BoardResponse.BoardListDTO(board))
                .collect(Collectors.toList());
        return boardListDTO;
    }

    public BoardResponse.BoardDetailDTO 게시글상세(Integer id) {
        Board board = boardRepository.findById(id).get();
        BoardResponse.BoardDetailDTO boardDetailDTO = new BoardResponse.BoardDetailDTO(board);
        return boardDetailDTO;
    }
}
