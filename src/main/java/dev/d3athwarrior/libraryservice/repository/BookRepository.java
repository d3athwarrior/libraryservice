package dev.d3athwarrior.libraryservice.repository;

import dev.d3athwarrior.libraryservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
