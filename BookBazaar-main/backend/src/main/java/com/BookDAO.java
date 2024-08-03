package com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public static void addBook(Connection connection, Book book) throws SQLException {
        String query = "INSERT INTO Books (Title, Author, Price, QtyStocked) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setDouble(3, book.getPrice());
            preparedStatement.setInt(4, book.getQtyStocked());
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteBook(Connection connection, int bookId) throws SQLException {
        String query = "DELETE FROM Books WHERE BookID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.executeUpdate();
        }
    }

    public static Book getBook(Connection connection, int bookId) throws SQLException {
        String query = "SELECT * FROM Books WHERE BookID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book(
                    resultSet.getString("Title"),
                    resultSet.getString("Author"),
                    resultSet.getDouble("Price"),
                    resultSet.getInt("QtyStocked")
                );
                book.setBookId(resultSet.getInt("BookID"));
                return book;
            }
        }
        return null;
    }

    public static List<Book> getAllBooks(Connection connection) throws SQLException {
        String query = "SELECT * FROM Books";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book(
                    resultSet.getString("Title"),
                    resultSet.getString("Author"),
                    resultSet.getDouble("Price"),
                    resultSet.getInt("QtyStocked")
                );
                book.setBookId(resultSet.getInt("BookID"));
                books.add(book);
            }
        }
        return books;
    }

    public static void updateBook(Connection connection, Book book) throws SQLException {
        String query = "UPDATE Books SET Title = ?, Author = ?, Price = ?, QtyStocked = ? WHERE BookID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setDouble(3, book.getPrice());
            preparedStatement.setInt(4, book.getQtyStocked());
            preparedStatement.setInt(5, book.getBookId());
            preparedStatement.executeUpdate();
        }
    }
}
