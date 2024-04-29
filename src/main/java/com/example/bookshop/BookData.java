package com.example.bookshop;

import java.io.Serializable;

public class BookData implements BookInterface, Serializable {
    private static final long serialVersionUID = 1L;

    private Integer bookID;
    private String title;
    private String author;
    private String genre;
    private double price;

    public BookData(Integer bookID, String title, String author, String genre, double price) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.price = price;
    }

    public Integer getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public double getPrice() {
        return price;
    }
}
