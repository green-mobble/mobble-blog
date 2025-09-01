package org.example.mobble.board;


import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.ExceptionApi404;
import org.example.mobble.category.Category;
import org.example.mobble.category.CategoryRequest;
import org.example.mobble.category.CategoryService;
import org.example.mobble.user.User;
import org.example.mobble.user.UserRepository;
import org.example.mobble.user.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;

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


    @Transactional
    public BoardResponse.DTO save(BoardRequest.BoardSaveDTO reqDTO, Integer userId) {

        //유저 객체
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404("유저를 찾을 수 없습니다."));

        //카테고리 저장
        //추가 저장 로직이 있을 수 있어서 일단 컨벤션 대로  DTO로 넘김
        CategoryRequest.CategorySaveDTO categorySaveDTO = new CategoryRequest.CategorySaveDTO(reqDTO.getCategory());
        Category categoryPS = categoryService.save(categorySaveDTO,userPS);

        //저장
        Board board = Board.builder()
                .title(reqDTO.getTitle())
                .content(reqDTO.getContent())
                .user(userPS)
                .views(0) // 초기 조회수
                .category(categoryPS)
                .build();

        boardRepository.save(board);

       return new BoardResponse.DTO(board);
    }
}
