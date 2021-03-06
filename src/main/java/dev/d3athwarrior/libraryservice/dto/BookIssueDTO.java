package dev.d3athwarrior.libraryservice.dto;

/**
 * The {@link BookIssueDTO} represents the data which will be sent to the client once a book issue
 * operation has been completed. This DTO will hold the message in case of success or failure.
 */
public class BookIssueDTO {
    private Long userId;
    private String message;
    private BookDTO bookDTO;
    private Boolean hasError = false;

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
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", bookDTO=" + bookDTO +
                '}';
    }
}
