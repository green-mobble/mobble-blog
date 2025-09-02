package org.example.mobble.board;


import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception403;
import org.example.mobble._util.error.ex.Exception404;
import org.example.mobble._util.error.ex.ExceptionApi403;
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

    //전체 조회
    public List<BoardResponse.BoardDTO> list() {
        //리스트 받기
        List<BoardResponse.BoardDTO> resDTO = boardRepository.findAll();
        return resDTO;
    }

    //상세 조회
    public BoardResponse.BoardDetailDTO detail(Integer boardId, Integer userId) {

        //팔로워 여부를 같이 값을 반환하기위해 userId도 같이 넣음
        BoardResponse.BoardDetailDTO resDTO = boardRepository.findByBoardIdandUserId(boardId,userId)
                .orElseThrow(() -> new Exception404("글을 찾을 수 없습니다."));
        return resDTO;
    }

    //저장
    @Transactional
    public BoardResponse.DTO save(BoardRequest.BoardSaveDTO reqDTO, Integer userId) {

        //유저 객체 조회
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404("유저를 찾을 수 없습니다."));
        //카테고리 저장
        Category categoryPS = categorySaveAndUpdate(reqDTO.getCategory(),userPS);

        //저장
        Board board = Board.builder()
                .title(reqDTO.getTitle())
                .content(reqDTO.getContent())
                .user(userPS)
                .views(0) // 초기 조회수
                .category(categoryPS)
                .build();

        boardRepository.save(board);

        //확인용 return
       return new BoardResponse.DTO(board);
    }



    @Transactional
    public BoardResponse.DTO update(BoardRequest.BoardUpdateDTO reqDTO, Integer userId,Integer boardId) {

        Board boardPS = boardRepository.findById(boardId)
               .orElseThrow(() -> new Exception404("글을 찾을 수 없습니다."));

        //유저 권한 체크
        checkBoardUser(boardPS, userId);

        //카테고리 저장
        //1.유저 객체 조회 (카테고리 저장용)
        //2.저장
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404("유저를 찾을 수 없습니다."));
        Category categoryPS = categorySaveAndUpdate(reqDTO.getCategory(),userPS);

        //변경(제목,내용만)
        boardPS.updateInfo(reqDTO,categoryPS);
        //확인용 return
        return new BoardResponse.DTO(boardPS);
    }


    @Transactional
    public void delete(Integer boardId,Integer userId) {
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new Exception404("글을 찾을 수 없습니다."));

        //유저 권한 체크
        checkBoardUser(boardPS, userId);
        //삭제
        boardRepository.delete(boardPS);
    }

    private void checkBoardUser(Board boardPS, Integer userId) {
        if (!boardPS.getUser().getId().equals(userId))
            throw new Exception403("권한이 없습니다");
    }

    private Category categorySaveAndUpdate(String category, User userPS) {
        //일단 컨벤션상 dto를 지키기 위해 dto 생성 후 삽입
        CategoryRequest.CategorySaveDTO categorySaveDTO = new CategoryRequest.CategorySaveDTO(category);
        return categoryService.save(categorySaveDTO,userPS);


    }
}
