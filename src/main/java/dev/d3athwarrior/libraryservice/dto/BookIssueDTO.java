package dev.d3athwarrior.libraryservice.dto;

public class BookIssueDTO {
    private Long bookId;
    private Long userId;
    private String message;
    private BookDTO bookDTO;
    private Boolean hasError = false;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BookDTO getBookDTO() {
        return bookDTO;
    }

    public void setBookDTO(BookDTO bookDTO) {
        this.bookDTO = bookDTO;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    @Override
    public String toString() {
        return "BookIssueDTO{" +
                "bookId=" + bookId +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", bookDTO=" + bookDTO +
                '}';
    }
}
