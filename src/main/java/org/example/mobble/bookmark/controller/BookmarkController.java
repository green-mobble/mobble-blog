package org.example.mobble.bookmark.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble.bookmark.service.BookmarkService;
import org.example.mobble.user.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final HttpSession session;

    @PostMapping("/bookmark/{boardId}")
    public String bookmarkInsert(@PathVariable Integer boardId) {
        User user = getLoginUser();
        bookmarkService.findBookmark(boardId,user.getId());
        return "redirect:/boards/{boardId}";
    }

    private User getLoginUser() {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new Exception401("로그인 후 이용 부탁드립니다."); // 에러 코드, 에러 메시지 컨벤션
        }
        return user;
    }
}
