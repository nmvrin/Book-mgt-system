package com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static void createUser(Connection connection, Customer customer) throws SQLException {
        String query = "INSERT INTO Customers (Username, Password, Name, Email, Address, Phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customer.getUsername());
            preparedStatement.setString(2, customer.getPassword());
            preparedStatement.setString(3, customer.getName());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setString(5, customer.getAddress());
            preparedStatement.setString(6, customer.getPhone());
            preparedStatement.executeUpdate();
        }
    }

    public static void createUser(Connection connection, Admin admin) throws SQLException {
        String query = "INSERT INTO Admin (Username, Password, Email, Role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, admin.getUsername());
            preparedStatement.setString(2, admin.getPassword());
            preparedStatement.setString(3, admin.getEmail());
            preparedStatement.setString(4, admin.getRole());
            preparedStatement.executeUpdate();
        }
    }

    public static User getUserByUsernameAndPassword(Connection connection, String username, String password) throws SQLException {
        String query = "SELECT CustomerID as UserID, Username, Password, Email, 'customer' as Role FROM Customers WHERE Username = ? AND Password = ? " +
                       "UNION " +
                       "SELECT AdminID as UserID, Username, Password, Email, Role FROM Admin WHERE Username = ? AND Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String role = resultSet.getString("Role");
                    User user = new User();
                    user.setUserId(resultSet.getInt("UserID"));
                    user.setUsername(resultSet.getString("Username"));
                    user.setPassword(resultSet.getString("Password"));
                    user.setEmail(resultSet.getString("Email"));
                    user.setRole(role);
                    return user;
                }
            }
        }
        return null;
    }

    public static User getUser(Connection connection, int userId) throws SQLException {
        String customerQuery = "SELECT CustomerID as UserID, Username, Password, Email, 'customer' as Role FROM Customers WHERE CustomerID = ?";
        String adminQuery = "SELECT AdminID as UserID, Username, Password, Email, Role FROM Admin WHERE AdminID = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(customerQuery)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setUserId(resultSet.getInt("UserID"));
                    customer.setUsername(resultSet.getString("Username"));
                    customer.setPassword(resultSet.getString("Password"));
                    customer.setEmail(resultSet.getString("Email"));
                    customer.setName(resultSet.getString("Name"));
                    customer.setAddress(resultSet.getString("Address"));
                    customer.setPhone(resultSet.getString("Phone"));
                    customer.setRole("customer");
                    return customer;
                }
            }
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(adminQuery)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Admin admin = new Admin();
                    admin.setUserId(resultSet.getInt("UserID"));
                    admin.setUsername(resultSet.getString("Username"));
                    admin.setPassword(resultSet.getString("Password"));
                    admin.setEmail(resultSet.getString("Email"));
                    admin.setRole(resultSet.getString("Role"));
                    return admin;
                }
            }
        }

        return null;
    }

    public static void updateUser(Connection connection, User user) throws SQLException {
        String query = "UPDATE Customers SET Username = ?, Password = ?, Email = ?, Name = ?, Address = ?, Phone = ? WHERE CustomerID = ?";
        if ("admin".equalsIgnoreCase(user.getRole())) {
            query = "UPDATE Admin SET Username = ?, Password = ?, Email = ?, Role = ? WHERE AdminID = ?";
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            if ("customer".equalsIgnoreCase(user.getRole())) {
                preparedStatement.setString(4, ((Customer) user).getName());
                preparedStatement.setString(5, ((Customer) user).getAddress());
                preparedStatement.setString(6, ((Customer) user).getPhone());
                preparedStatement.setInt(7, user.getUserId());
            } else {
                preparedStatement.setString(4, ((Admin) user).getRole());
                preparedStatement.setInt(5, user.getUserId());
            }
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteUser(Connection connection, int userId, String userRole) throws SQLException {
        String query = "DELETE FROM Customers WHERE CustomerID = ?";
        if ("admin".equalsIgnoreCase(userRole)) {
            query = "DELETE FROM Admin WHERE AdminID = ?";
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        }
    }
}
