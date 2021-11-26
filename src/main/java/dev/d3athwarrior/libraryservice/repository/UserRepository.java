package dev.d3athwarrior.libraryservice.repository;

import dev.d3athwarrior.libraryservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
