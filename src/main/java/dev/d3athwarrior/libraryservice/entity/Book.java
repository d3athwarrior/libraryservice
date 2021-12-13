package dev.d3athwarrior.libraryservice.entity;

import javax.persistence.*;

/**
 * Book Class represents the book entity in the database
 */
@Table(name = "book")
@Entity
public class Book {

    @Id
    @Column(name = "book_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "num_of_copies", nullable = false)
    private Integer numOfCopies;

    public Book() {
    }

    public Book(Long id, String name, String authorName, Integer numOfCopies) {
        this.id = id;
        this.name = name;
        this.authorName = authorName;
        this.numOfCopies = numOfCopies;
    }

    public Book(String name, String authorName, Integer numOfCopies) {
        this.name = name;
        this.authorName = authorName;
        this.numOfCopies = numOfCopies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getNumOfCopies() {
        return numOfCopies;
    }

    public void setNumOfCopies(Integer numOfCopies) {
        this.numOfCopies = numOfCopies;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorName='" + authorName + '\'' +
                ", numOfCopies=" + numOfCopies +
                '}';
    }
}
