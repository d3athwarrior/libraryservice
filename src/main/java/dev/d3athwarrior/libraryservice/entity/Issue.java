package dev.d3athwarrior.libraryservice.entity;

import javax.persistence.*;

/**
 * Issue class represents the mapping of a user to a book in the database
 */
/*
 * Possibly, this table could have been avoided and one to many relationships could have been provided in the Book.java entity
 * and User.java entity for now but in the future if we wanted more details along with the person who borrowed a book,
 * we would have to refactor the DB to accommodate items like issue date, return date etc
 */
@Table(name = "issue", indexes = {
        @Index(name = "user_borrowed_book_pk", columnList = "user_id, book_id", unique = true)
})
@Entity
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Issue() {
    }

    public Issue(Long id, User user, Book book) {
        this.id = id;
        this.user = user;
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", user=" + user +
                ", book=" + book +
                '}';
    }
}
