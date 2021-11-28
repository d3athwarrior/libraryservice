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

/**
 * The UserController deals with all the user actions from the UI
 */
/*
 * Arguably, issuedBookReturn could be moved to the BookController itself but since the books could possibly be returned
 * from a view where the user only sees the book issued to them, this endpoint has been kept in the UserController.
 */
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Rest endpoint to take care of book returns
     *
     * @param userId the user returning the book
     * @param bookId the book id of the book being returned
     * @return {@link UserReturnBookResponseDTO}
     */
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

    /**
     * Takes input as the user id and returns the books issued to them
     *
     * @param userId The user for which all issued books are being requested
     * @return the {@link UserBookResponseDTO}
     */
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
