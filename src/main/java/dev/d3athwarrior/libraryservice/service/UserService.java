package dev.d3athwarrior.libraryservice.service;

import dev.d3athwarrior.libraryservice.entity.Issue;
import dev.d3athwarrior.libraryservice.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private IssueRepository issueRepository;

    @Autowired
    public UserService(final IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    /**
     * This method will accept bookId and userId as input to identify the book being returned and by whom and process it
     * to return the result set containing data for the client
     *
     * @param bookId the id of the book being returned
     * @param userId the id of the user returning the book
     * @return In case the user was issued the book: the result containing 'userId', 'returnedBookId', 'message', 'userBookList'
     * In case the user was not issued the book: the result containing 'hasError' and 'message'
     */
    /*
     * A point of improvement is to use bean classes as return type
     */
    @Transactional
    public Map<String, Object> issuedBookReturned(Long bookId, Long userId) {
        Map<String, Object> result = new HashMap<>();
        if (issueRepository.deleteByBook_IdAndUser_Id(bookId, userId) == 1) {
            List<Issue> userBookIssues = issueRepository.findIssuesByUser_Id(userId);
            result.put("userId", userId);
            result.put("returnedBookId", bookId);
            result.put("message", "Book returned successfully");
            result.put("userBookList", userBookIssues);
        } else {
            result.put("message", "You were not issued this book");
            result.put("hasError", true);
        }
        return result;
    }
}