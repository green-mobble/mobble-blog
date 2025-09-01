package org.example.mobble.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.example.mobble.bookmark.service.BookmarkService;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class BookmarkController {
    private final BookmarkService bookmarkService;

}
