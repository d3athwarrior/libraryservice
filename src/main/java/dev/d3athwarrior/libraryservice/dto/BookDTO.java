package dev.d3athwarrior.libraryservice.dto;

/**
 * The {@link BookDTO} class provides a minimal representation of the book data to be sent to the client
 */
public class BookDTO {

    private long id;
    private String name;
    private String authorName;
    private int numOfCopies;
    private int numCopiesAvailable;

    public BookDTO() {
    }

    /**
     * @param id          The id of the book
     * @param name        The name of the book
     * @param authorName  The name of the author of the book
     * @param numOfCopies The number of copies owned
     */
    public BookDTO(long id, String name, String authorName, int numOfCopies, int numCopiesAvailable) {
        this.id = id;
        this.name = name;
        this.authorName = authorName;
        this.numOfCopies = numOfCopies;
        this.numCopiesAvailable = numCopiesAvailable;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getNumOfCopies() {
        return numOfCopies;
    }

    public int getNumCopiesAvailable() {
        return numCopiesAvailable;
    }
}
