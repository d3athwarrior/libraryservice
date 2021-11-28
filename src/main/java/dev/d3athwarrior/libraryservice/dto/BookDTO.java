package dev.d3athwarrior.libraryservice.dto;

/**
 * The {@link BookDTO} class provides a minimal representation of the book data to be sent to the client
 */
public class BookDTO {

    private Long id;
    private String name;
    private String authorName;
    private Integer numOfCopies;
    private Integer numCopiesAvailable;

    public BookDTO() {
    }

    /**
     * @param id          The id of the book
     * @param name        The name of the book
     * @param authorName  The name of the author of the book
     * @param numOfCopies The number of copies owned
     */
    public BookDTO(Long id, String name, String authorName, Integer numOfCopies, Integer numCopiesAvailable) {
        this.id = id;
        this.name = name;
        this.authorName = authorName;
        this.numOfCopies = numOfCopies;
        this.numCopiesAvailable = numCopiesAvailable;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Integer getNumOfCopies() {
        return numOfCopies;
    }

    public Integer getNumCopiesAvailable() {
        return numCopiesAvailable;
    }
}
