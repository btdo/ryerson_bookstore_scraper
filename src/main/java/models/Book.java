package models;

public class Book {
    String courseName;
    String title;
    String author;
    String isbn;
    String price;
    boolean isRequired;

    public Book(String courseName, String title, String author, String isbn, String price, boolean isRequired) {
        this.courseName =  courseName;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.isRequired = isRequired;
    }

    @Override
    public String toString() {
        return courseName + "," + title + "," +  author + "," + isbn + "," + price + "," + isRequired;
    }
}
