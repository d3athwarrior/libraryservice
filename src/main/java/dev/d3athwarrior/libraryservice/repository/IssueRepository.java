package dev.d3athwarrior.libraryservice.repository;

import dev.d3athwarrior.libraryservice.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

//    Issue findIssuesByBook_IdAndUser_Id(Long book_id, Long userId);

    int countByUser_Id(Long userId);

    Optional<Integer> countByBook_Id(Long bookId);
}
