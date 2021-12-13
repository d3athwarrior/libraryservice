package dev.d3athwarrior.libraryservice.dto;

public class BookToAddDTO {

    private String name;
    private String authorName;
    private Integer numOfCopies;

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


}
