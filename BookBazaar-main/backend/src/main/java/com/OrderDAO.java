package com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public static int addOrder(Connection connection, Order order) throws SQLException {
        String query = "INSERT INTO Orders (OrderDate, TotalCost, CustomerID, AdminID, Status, ShippingAddress, PaymentMethod, PaymentStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
            preparedStatement.setDouble(2, order.getTotalCost());
            preparedStatement.setInt(3, order.getCustomerId());
            preparedStatement.setInt(4, order.getAdminId());
            preparedStatement.setString(5, order.getStatus());
            preparedStatement.setString(6, order.getShippingAddress());
            preparedStatement.setString(7, order.getPaymentMethod());
            preparedStatement.setString(8, order.getPaymentStatus());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }

    public static void addOrderDetails(Connection connection, OrderDetails orderDetails) throws SQLException {
        String query = "INSERT INTO OrderDetails (OrderID, BookID, Quantity, Price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderDetails.getOrderId());
            preparedStatement.setInt(2, orderDetails.getBookId());
            preparedStatement.setInt(3, orderDetails.getQuantity());
            preparedStatement.setDouble(4, orderDetails.getPrice());
            preparedStatement.executeUpdate();
        }
    }

    public static List<Order> getAllOrders(Connection connection) throws SQLException {
        String query = "SELECT * FROM Orders";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order(
                    resultSet.getDate("OrderDate"),
                    resultSet.getDouble("TotalCost"),
                    resultSet.getInt("CustomerID"),
                    resultSet.getInt("AdminID"),
                    resultSet.getString("Status"),
                    resultSet.getString("ShippingAddress"),
                    resultSet.getString("PaymentMethod"),
                    resultSet.getString("PaymentStatus")
                );
                order.setOrderId(resultSet.getInt("OrderID"));
                orders.add(order);
            }
        }
        return orders;
    }

    public static List<Order> getOrdersByCustomerId(Connection connection, int customerId) throws SQLException {
        String query = "SELECT * FROM Orders WHERE CustomerID = ?";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order(
                    resultSet.getDate("OrderDate"),
                    resultSet.getDouble("TotalCost"),
                    resultSet.getInt("CustomerID"),
                    resultSet.getInt("AdminID"),
                    resultSet.getString("Status"),
                    resultSet.getString("ShippingAddress"),
                    resultSet.getString("PaymentMethod"),
                    resultSet.getString("PaymentStatus")
                );
                order.setOrderId(resultSet.getInt("OrderID"));
                orders.add(order);
            }
        }
        return orders;
    }

    public static Order getOrderByIdAndCustomerId(Connection connection, int orderId, int customerId) throws SQLException {
        String query = "SELECT * FROM Orders WHERE OrderID = ? AND CustomerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Order order = new Order(
                    resultSet.getDate("OrderDate"),
                    resultSet.getDouble("TotalCost"),
                    resultSet.getInt("CustomerID"),
                    resultSet.getInt("AdminID"),
                    resultSet.getString("Status"),
                    resultSet.getString("ShippingAddress"),
                    resultSet.getString("PaymentMethod"),
                    resultSet.getString("PaymentStatus")
                );
                order.setOrderId(resultSet.getInt("OrderID"));
                return order;
            }
        }
        return null;
    }

    public static void deleteOrder(Connection connection, int orderId) throws SQLException {
        // First delete related order details
        deleteOrderDetailsByOrderId(connection, orderId);

        // Then delete the order
        String query = "DELETE FROM Orders WHERE OrderID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteOrderDetailsByOrderId(Connection connection, int orderId) throws SQLException {
        String query = "DELETE FROM OrderDetails WHERE OrderID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.executeUpdate();
        }
    }
}
