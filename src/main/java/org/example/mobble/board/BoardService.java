package org.example.mobble.board;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble._util.error.ex.Exception404;
import org.example.mobble.category.Category;
import org.example.mobble.category.CategoryRepository;
import org.example.mobble.category.CategoryRequest;
import org.example.mobble.category.CategoryService;
import org.example.mobble.user.User;
import org.example.mobble.user.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public BoardResponse.BoardListDTO boardList() {
        // 전체리스트
        List<Board> boardList = boardRepository.findAll();
        return new BoardResponse.BoardListDTO(boardList);
    }

    @Transactional
    public BoardResponse.BoardResponseDTO boardfindById(Integer id) {
        // 상세보기
        Board findBoard = boardRepository.findById(id);

        // 조회수 증가
        findBoard.viewUP(findBoard.getViews());

        return new BoardResponse.BoardResponseDTO(findBoard);
    }


    @Transactional
    public BoardResponse.DTO boardSave(BoardRequest.BoardSaveDTO boardSaveDTO, User sessionUser) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // 카테고리 있는지 확인
        Category category = categoryRepository.findByUserIdAndCategory( sessionUser.getId(),boardSaveDTO.getCategory()).orElse(null);

        //없으면 null,있으면 카테고리 저장
        if (category == null) {
            category = categoryRepository.save(
                    Category.builder()
                            .userId(sessionUser.getId())
                            .category(boardSaveDTO.getCategory())
                            .build()
            );
        }
        // 저장한 카테고리 객체를 board에 연결
        Board board = Board.builder()
                .title(boardSaveDTO.getTitle())
                .content(boardSaveDTO.getContent())
                .user(sessionUser)
                .views(0)
                .bookmark(0)
                .createdAt(now)
                .updatedAt(now)
                .category(category) // TODO : category 객체로 연결
                .build();

        Board boardsave = boardRepository.boardSave(board);
        return new BoardResponse.DTO(boardsave);
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
        // 변경한 카테고리를 객체로 변경해서 받아오기
        Category category = categoryRepository.findByUserIdAndCategory( sessionUser.getId(),boardSaveDTO.getCategory()).orElse(null);

        //없으면 null,있으면 카테고리 저장
        if (category == null) {
            category = categoryRepository.save(
                    Category.builder()
                            .userId(sessionUser.getId())
                            .category(boardSaveDTO.getCategory())
                            .build()
            );
        }

        // 더티체킹은 update 함수 만들어서
        findBoard.update(boardSaveDTO.getTitle(),boardSaveDTO.getContent(),category);
        return new BoardResponse.DTO(findBoard) ;
    }
}
