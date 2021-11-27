package dev.d3athwarrior.libraryservice.dto;

import dev.d3athwarrior.libraryservice.entity.Issue;

import java.util.List;

public class UserBookDTO {

    private String message;
    private List<Issue> userBookList;
    private Long returnedBookId;
    private Long userId;
    private Boolean hasError;

    public UserBookDTO(String message, List<Issue> userBookList, Long returnedBookId, Long userId, Boolean hasError) {
        this.message = message;
        this.userBookList = userBookList;
        this.returnedBookId = returnedBookId;
        this.userId = userId;
        this.hasError = hasError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Issue> getUserBookList() {
        return userBookList;
    }

    public void setUserBookList(List<Issue> userBookList) {
        this.userBookList = userBookList;
    }

    public Long getReturnedBookId() {
        return returnedBookId;
    }

    public void setReturnedBookId(Long returnedBookId) {
        this.returnedBookId = returnedBookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }
}
