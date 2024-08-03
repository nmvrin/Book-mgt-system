package com;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private double price;
    private int qtyStocked;

    public Book(String title, String author, double price, int qtyStocked) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.qtyStocked = qtyStocked;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQtyStocked() {
        return qtyStocked;
    }

    public void setQtyStocked(int qtyStocked) {
        this.qtyStocked = qtyStocked;
    }
}
