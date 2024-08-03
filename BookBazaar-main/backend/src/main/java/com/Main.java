package com;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.List;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static String userRole;
    private static int userId;

    public static void main(String[] args) {
        System.out.println("Connecting to database...");

        // Create an ExecutorService to run the application
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try (Connection connection = DatabaseConnection.getConnection()) {
                if (connection != null) {
                    System.out.println("Connection established successfully.");
                    Scanner scanner = new Scanner(System.in);

                    try {
                        while (true) {
                            System.out.println("Select an option:");
                            System.out.println("1. Create an account");
                            System.out.println("2. Sign in");
                            System.out.println("3. Exit");
                            int choice = scanner.nextInt();
                            scanner.nextLine();  // Consume newline

                            switch (choice) {
                                case 1:
                                    createUser(connection, scanner);
                                    break;
                                case 2:
                                    if (signIn(connection, scanner)) {
                                        showMenu(connection, scanner);
                                    }
                                    break;
                                case 3:
                                    System.out.println("Closing connection and exiting...");
                                    return;
                                default:
                                    System.out.println("Invalid choice.");
                            }
                        }
                    } finally {
                        scanner.close();
                    }
                } else {
                    System.out.println("Connection failed.");
                }
            } catch (SQLException e) {
                System.err.println("Connection failed: " + e.getMessage());
                e.printStackTrace();
            } finally {
                cleanupMySQLThread();
                System.out.println("Application shutdown complete.");
                System.exit(0);
            }
        });

        // Add a shutdown hook to clean up resources
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                executorService.shutdownNow();
                cleanupMySQLThread();
                System.out.println("Shutdown hook executed. Application shutdown complete.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    private static void cleanupMySQLThread() {
        try {
            // Get the MySQL cleaner thread
            Class<?> cleanerClass = Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread");
            cleanerClass.getMethod("shutdown").invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void createUser(Connection connection, Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter role (customer/admin):");
        String role = scanner.nextLine();

        if ("customer".equalsIgnoreCase(role)) {
            System.out.println("Enter name:");
            String name = scanner.nextLine();
            System.out.println("Enter address:");
            String address = scanner.nextLine();
            System.out.println("Enter phone:");
            String phone = scanner.nextLine();
            Customer customer = new Customer(username, password, email, name, address, phone);

            try {
                System.out.println("Creating customer...");
                UserDAO.createUser(connection, customer);
                System.out.println("Customer created.");
            } catch (SQLException e) {
                System.err.println("Error creating customer: " + e.getMessage());
            }
        } else if ("admin".equalsIgnoreCase(role)) {
            Admin admin = new Admin(username, password, email, role);

            try {
                System.out.println("Creating admin...");
                UserDAO.createUser(connection, admin);
                System.out.println("Admin created.");
            } catch (SQLException e) {
                System.err.println("Error creating admin: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid role.");
        }
    }

    private static boolean signIn(Connection connection, Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        try {
            User user = UserDAO.getUserByUsernameAndPassword(connection, username, password);
            if (user != null) {
                userRole = user.getRole();
                userId = user.getUserId();
                System.out.println("Logged in as: " + userRole);
                return true;
            } else {
                System.out.println("Invalid username or password.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
            return false;
        }
    }

    private static void showMenu(Connection connection, Scanner scanner) {
        while (true) {
            System.out.println("Select an option:");
            if ("admin".equalsIgnoreCase(userRole) || "superadmin".equalsIgnoreCase(userRole)) {
                System.out.println("1. Add book to inventory");
                System.out.println("2. View all books");
                System.out.println("3. Delete book");
                System.out.println("4. View all orders");
                System.out.println("5. Delete user");
                System.out.println("6. Exit");
            } else if ("customer".equalsIgnoreCase(userRole)) {
                System.out.println("1. View all books");
                System.out.println("2. Add book to cart");
                System.out.println("3. Return book");
                System.out.println("4. View my orders");
                System.out.println("5. Exit");
            }

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if ("admin".equalsIgnoreCase(userRole) || "superadmin".equalsIgnoreCase(userRole)) {
                switch (choice) {
                    case 1:
                        addBook(connection, scanner);
                        break;
                    case 2:
                        viewAllBooks(connection);
                        break;
                    case 3:
                        deleteBook(connection, scanner);
                        break;
                    case 4:
                        viewAllOrders(connection);
                        break;
                    case 5:
                        deleteUser(connection, scanner);
                        break;
                    case 6:
                        System.out.println("Exiting to main menu...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } else if ("customer".equalsIgnoreCase(userRole)) {
                switch (choice) {
                    case 1:
                        viewAllBooks(connection);
                        break;
                    case 2:
                        addBookToCart(connection, scanner);
                        break;
                    case 3:
                        returnBook(connection, scanner);
                        break;
                    case 4:
                        viewMyOrders(connection);
                        break;
                    case 5:
                        System.out.println("Exiting to main menu...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }
    }

    private static void addBook(Connection connection, Scanner scanner) {
        System.out.println("Enter book title:");
        String title = scanner.nextLine();
        System.out.println("Enter book author:");
        String author = scanner.nextLine();
        System.out.println("Enter book price:");
        double price = scanner.nextDouble();
        System.out.println("Enter book quantity in stock:");
        int qtyStocked = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        Book book = new Book(title, author, price, qtyStocked);

        try {
            System.out.println("Adding book...");
            BookDAO.addBook(connection, book);
            System.out.println("Book added.");
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }

    private static void deleteBook(Connection connection, Scanner scanner) {
        System.out.println("Enter book ID to delete:");
        int bookId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        try {
            BookDAO.deleteBook(connection, bookId);
            System.out.println("Book deleted.");
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
        }
    }

    private static void createOrder(Connection connection, Scanner scanner) {
        System.out.println("Enter book ID to add to order:");
        int bookId = scanner.nextInt();
        System.out.println("Enter quantity:");
        int quantity = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        try {
            Book book = BookDAO.getBook(connection, bookId);
            if (book == null) {
                System.out.println("Book not found.");
                return;
            }

            double totalCost = book.getPrice() * quantity;

            Order order = new Order(new Date(), totalCost, userId, 1, "Pending", "Address", "Card", "Paid");
            int orderId = OrderDAO.addOrder(connection, order);

            OrderDetails orderDetails = new OrderDetails(orderId, bookId, quantity, book.getPrice());
            OrderDAO.addOrderDetails(connection, orderDetails);

            System.out.println("Order created.");
        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
        }
    }

    private static void addBookToCart(Connection connection, Scanner scanner) {
        try {
            List<Book> books = BookDAO.getAllBooks(connection);
            if (books.isEmpty()) {
                System.out.println("No books available.");
                return;
            }

            System.out.println("Available books:");
            for (Book book : books) {
                System.out.println("Book ID: " + book.getBookId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.println("Price: " + book.getPrice());
                System.out.println("Quantity in Stock: " + book.getQtyStocked());
                System.out.println();
            }

            System.out.println("Enter book ID to add to cart:");
            int bookId = scanner.nextInt();
            System.out.println("Enter quantity:");
            int quantity = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            Book book = BookDAO.getBook(connection, bookId);
            if (book == null || book.getQtyStocked() < quantity) {
                System.out.println("Invalid book ID or insufficient stock.");
                return;
            }

            double totalCost = book.getPrice() * quantity;

            Order order = new Order(new Date(), totalCost, userId, 1, "Pending", "Address", "Card", "Paid");
            int orderId = OrderDAO.addOrder(connection, order);

            OrderDetails orderDetails = new OrderDetails(orderId, bookId, quantity, book.getPrice());
            OrderDAO.addOrderDetails(connection, orderDetails);

            // Update book stock
            book.setQtyStocked(book.getQtyStocked() - quantity);
            BookDAO.updateBook(connection, book);

            System.out.println("Book added to cart and order created.");
        } catch (SQLException e) {
            System.err.println("Error adding book to cart: " + e.getMessage());
        }
    }

    private static void returnBook(Connection connection, Scanner scanner) {
        System.out.println("Enter order ID to return book:");
        int orderId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        try {
            // Check if the order belongs to the logged-in customer
            Order order = OrderDAO.getOrderByIdAndCustomerId(connection, orderId, userId);
            if (order == null) {
                System.out.println("Order not found or does not belong to you.");
                return;
            }

            // Remove the order details
            OrderDAO.deleteOrderDetailsByOrderId(connection, orderId);

            // Remove the order (or update it if partially returned)
            OrderDAO.deleteOrder(connection, orderId);
            System.out.println("Book returned and order deleted.");
        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
        }
    }

    private static void deleteUser(Connection connection, Scanner scanner) {
        System.out.println("Enter user ID to delete:");
        int userId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        try {
            UserDAO.deleteUser(connection, userId, userRole);
            System.out.println("User deleted.");
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void viewAllBooks(Connection connection) {
        try {
            List<Book> books = BookDAO.getAllBooks(connection);
            for (Book book : books) {
                System.out.println("Book ID: " + book.getBookId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.println("Price: " + book.getPrice());
                System.out.println("Quantity in Stock: " + book.getQtyStocked());
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving books: " + e.getMessage());
        }
    }

    private static void viewAllOrders(Connection connection) {
        try {
            List<Order> orders = OrderDAO.getAllOrders(connection);
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Order Date: " + order.getOrderDate());
                System.out.println("Total Cost: " + order.getTotalCost());
                System.out.println("Customer ID: " + order.getCustomerId());
                System.out.println("Admin ID: " + order.getAdminId());
                System.out.println("Status: " + order.getStatus());
                System.out.println("Shipping Address: " + order.getShippingAddress());
                System.out.println("Payment Method: " + order.getPaymentMethod());
                System.out.println("Payment Status: " + order.getPaymentStatus());
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving orders: " + e.getMessage());
        }
    }

    private static void viewMyOrders(Connection connection) {
        try {
            List<Order> orders = OrderDAO.getOrdersByCustomerId(connection, userId);
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Order Date: " + order.getOrderDate());
                System.out.println("Total Cost: " + order.getTotalCost());
                System.out.println("Customer ID: " + order.getCustomerId());
                System.out.println("Admin ID: " + order.getAdminId());
                System.out.println("Status: " + order.getStatus());
                System.out.println("Shipping Address: " + order.getShippingAddress());
                System.out.println("Payment Method: " + order.getPaymentMethod());
                System.out.println("Payment Status: " + order.getPaymentStatus());
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving orders: " + e.getMessage());
        }
    }
}
