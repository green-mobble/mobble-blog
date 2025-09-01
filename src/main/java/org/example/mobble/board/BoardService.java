package org.example.mobble.board;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mobble.user.User;
import org.springframework.stereotype.Service;

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
        Board findById = boardRepository.findById(id);

        // 조회수 증가
        boardRepository.viewsIncrease(id);

        return new BoardResponse.BoardDetailDTO(findById);
    }
}
