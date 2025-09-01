package org.example.mobble.board.service;


import lombok.RequiredArgsConstructor;
import org.example.mobble.board.domain.Board;
import org.example.mobble.board.domain.BoardRepository;
import org.example.mobble.board.dto.BoardRequest;
import org.example.mobble.board.dto.BoardResponse;
import org.example.mobble.bookmark.domain.Bookmark;
import org.example.mobble.bookmark.domain.BookmarkRepository;
import org.example.mobble.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final BookmarkRepository bookmarkRepository;

    public List<BoardResponse.DTO> getList(Integer userId) {
        List<Board> boardList = boardRepository.findAllCreatedAt();
        List<Bookmark> bookmarkList = bookmarkRepository.findByUserId(userId);
        List<BoardResponse.DTO> result = boardList.stream().map(board -> new BoardResponse.DTO(board, null, null)).toList();
        for (BoardResponse.DTO board : result) {
            for (Bookmark bookmark : bookmarkList) {
                if (bookmark.getBoardId().equals(board.getId())) {
                    board.setIsBookmark(true);
                    break;
                }
            }
        }
        return result;
    }

    public void save(BoardRequest.BoardSaveDTO reqDTO, User user) {
        Board board = new Board(reqDTO, user, 1);
    }
}
