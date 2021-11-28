package dev.d3athwarrior.libraryservice.dto;

import java.util.List;

public class UserReturnBookResponseDTO extends UserCommonResponseDTO {

    private Long returnedBookId;

    public UserReturnBookResponseDTO(String message, List<BookDTO> userBookList, Long returnedBookId, Long userId, Boolean hasError) {
        this.setMessage(message);
        this.returnedBookId = returnedBookId;
        this.setIssuedBooks(userBookList);
        this.setUserId(userId);
        this.setHasError(hasError);
    }

    public Long getReturnedBookId() {
        return returnedBookId;
    }

    public void setReturnedBookId(Long returnedBookId) {
        this.returnedBookId = returnedBookId;
    }
}
