package org.example.mobble.board;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponse.BoardDTO> 게시글목록() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponse.BoardDTO> BoardDTO = boards.stream()
                .map(board -> new BoardResponse.BoardDTO(board))
                .collect(Collectors.toList());
        return BoardDTO;
    }

    public BoardResponse.BoardDetailDTO 게시글상세(Integer id) {
        Board board = boardRepository.findById(id).get();
        BoardResponse.BoardDetailDTO boardDetailDTO = new BoardResponse.BoardDetailDTO(board);
        return boardDetailDTO;
    }

    @Transactional
    public void 게시글추가(BoardRequest.BoardSaveDTO requestDTO) {
        Board board = requestDTO.toEntity();
        boardRepository.save(board);
    }

    @Transactional
    public void 게시글삭제(Integer id){
        boardRepository.deleteById(id);
    }
}
