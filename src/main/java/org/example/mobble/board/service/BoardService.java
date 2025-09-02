package org.example.mobble.board.service;


import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception400;
import org.example.mobble._util.error.ex.Exception403;
import org.example.mobble._util.error.ex.Exception404;
import org.example.mobble.board.domain.Board;
import org.example.mobble.board.domain.BoardRepository;
import org.example.mobble.board.dto.BoardRequest;
import org.example.mobble.board.dto.BoardResponse;
import org.example.mobble.category.Category;
import org.example.mobble.category.CategoryRepository;
import org.example.mobble.report.domain.Report;
import org.example.mobble.report.domain.ReportRepository;
import org.example.mobble.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final ReportRepository reportRepository;

    public List<BoardResponse.DTO> getList() {
        return boardRepository.findAllCreatedAtWithBookmarkCount();
    }

    @Transactional
    public Board save(BoardRequest.BoardSaveDTO reqDTO, User user) {
        Category category = categoryRepository.findByUserIdAndCategory(user.getId(), reqDTO.getCategory()).orElse(null);
        if (category == null) {
            category = categoryRepository.save(new Category(null, user.getId(), reqDTO.getCategory()));
        }
        return boardRepository.save(new Board(reqDTO, user.getId(), category.getId()));
    }

    public Board getBoard(Integer boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new Exception404("해당 게시글이 존재하지 않습니다.")
        );
    }

    @Transactional
    public void update(Integer boardId, BoardRequest.BoardUpdateDTO reqDTO, User user) {
        if (boardId == null || boardId.equals(reqDTO.getId()))
            throw new Exception400("잘못된 요청입니다. 게시글 목록으로 돌아간 뒤 다시시도해주세요.");
        Board boardPS = getBoard(boardId);
        checkPermissions(boardPS, user);
        boardPS.update(reqDTO);
    }

    public void delete(Integer boardId, User user) {
        if (boardId == null) throw new Exception400("잘못된 요청입니다. 게시글 목록으로 돌아간 뒤 다시시도해주세요.");
        Board boardPS = getBoard(boardId);
        checkPermissions(boardPS, user);
        boardRepository.delete(boardPS);
    }

    // 권한 확인 로직
    private void checkPermissions(Board board, User user) {
        if (!board.getUserId().equals(user.getId())) throw new Exception403("해당 게시물을 등록한 사용자가 아닙니다.");
    }

    public void report(User user, Integer boardId, BoardRequest.BoardReportDTO reqDTO) {
        Board boardPS = getBoard(boardId);
        reportRepository.save(new Report(user, boardId, reqDTO));
    }
}
