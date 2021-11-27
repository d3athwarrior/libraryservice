package dev.d3athwarrior.libraryservice.controller;

import dev.d3athwarrior.libraryservice.dto.UserBookDTO;
import dev.d3athwarrior.libraryservice.entity.Issue;
import dev.d3athwarrior.libraryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("{userId}/returnbook/{bookId}")
    public UserBookDTO issuedBookReturn(@PathVariable Long userId, @PathVariable Long bookId) {
        Map<String, Object> bookReturnResult = this.userService.issuedBookReturned(bookId, userId);
        return new UserBookDTO(
                bookReturnResult.get("message").toString(),
                (List<Issue>) bookReturnResult.get("userBookList"),
                (Long) bookReturnResult.get("returnedBookId"),
                (Long) bookReturnResult.get("userId"),
                (Boolean) bookReturnResult.get("hasError"));
    }
}
