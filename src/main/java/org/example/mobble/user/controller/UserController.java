package org.example.mobble.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.mobble.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


}
