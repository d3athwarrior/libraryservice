package dev.d3athwarrior.libraryservice.dto;

import java.util.List;

public abstract class UserCommonResponseDTO {
    private Long userId;
    private String message;
    private Boolean hasError;
    private List<BookDTO> issuedBooks;

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

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public List<BookDTO> getIssuedBooks() {
        return issuedBooks;
    }

    public void setIssuedBooks(List<BookDTO> issuedBooks) {
        this.issuedBooks = issuedBooks;
    }

    @Override
    public String toString() {
        return "UserCommonResponseDTO{" +
                "userId=" + userId +
                ", message='" + message + '\'' +
                ", hasError=" + hasError +
                ", issuedBooks=" + issuedBooks +
                '}';
    }
}
