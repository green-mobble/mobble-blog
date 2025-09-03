package org.example.mobble.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.example.mobble.bookmark.domain.Bookmark;
import org.example.mobble.bookmark.domain.BookmarkRepository;
import org.example.mobble.bookmark.dto.BookmarkResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public void findBookmark(Integer boardId, Integer userId) {
        boolean isBookmarked = bookmarkRepository.findByBoardIdAndUserId(boardId, userId);
        if (isBookmarked) {
            bookmarkRepository.BookmarkDelete(boardId,userId);

        }else {
            Bookmark bookmark = Bookmark.builder()
                    .boardId(boardId)
                    .userId(userId)
                    .build();
            bookmarkRepository.BookmarkSave(bookmark);
        }
    }
}
