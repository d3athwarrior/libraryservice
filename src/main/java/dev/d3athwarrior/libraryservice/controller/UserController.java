package dev.d3athwarrior.libraryservice.controller;

import dev.d3athwarrior.libraryservice.dto.BookDTO;
import dev.d3athwarrior.libraryservice.dto.UserBookResponseDTO;
import dev.d3athwarrior.libraryservice.dto.UserReturnBookResponseDTO;
import dev.d3athwarrior.libraryservice.entity.Issue;
import dev.d3athwarrior.libraryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("{userId}/returnbook/{bookId}")
    public UserReturnBookResponseDTO issuedBookReturn(@PathVariable Long userId, @PathVariable Long bookId) {
        Map<String, Object> bookReturnResult = this.userService.issuedBookReturned(bookId, userId);
        List<BookDTO> bookDTOList = new ArrayList<>();
        ((List<Issue>) bookReturnResult.get("userBookList")).forEach(issue -> {
            bookDTOList.add(
                    new BookDTO(
                            issue.getBook().getId(),
                            issue.getBook().getName(),
                            issue.getBook().getAuthorName(),
                            issue.getBook().getNumOfCopies(),
                            null
                    )
            );
        });
        return new UserReturnBookResponseDTO(
                bookReturnResult.get("message").toString(),
                bookDTOList,
                (Long) bookReturnResult.get("returnedBookId"),
                (Long) bookReturnResult.get("userId"),
                (Boolean) bookReturnResult.get("hasError"));
    }

    @GetMapping("{userId}/issuedBooks")
    public UserBookResponseDTO getAllIssuedBooks(@PathVariable Long userId) {
        UserBookResponseDTO userBookResponseDTO = new UserBookResponseDTO();
        List<BookDTO> bookDTOList = new ArrayList<>();
        this.userService.getUserBooks(userId).forEach(issue -> {
            bookDTOList.add(
                    new BookDTO(
                            issue.getBook().getId(),
                            issue.getBook().getName(),
                            issue.getBook().getAuthorName(),
                            issue.getBook().getNumOfCopies(),
                            null
                    )
            );
        });
        userBookResponseDTO.setUserId(userId);
        userBookResponseDTO.setIssuedBooks(bookDTOList);
        return userBookResponseDTO;
    }
}
