-- Data for Books table
INSERT INTO Books (Title, Author, Price, QtyStocked) VALUES
('To Kill a Mockingbird', 'Harper Lee', 10.99, 100),
('1984', 'George Orwell', 8.99, 150),
('The Great Gatsby', 'F. Scott Fitzgerald', 12.99, 200);

-- Data for Customers table
INSERT INTO Customers (Username, Password, Name, Email, Address, Phone) VALUES
('john_doe', 'password_hash1', 'John Doe', 'john@example.com', '123 Main St, Anytown, USA', '555-1234'),
('jane_smith', 'password_hash2', 'Jane Smith', 'jane@example.com', '456 Oak St, Anytown, USA', '555-5678');

-- Data for Admin table
INSERT INTO Admin (Username, Password, Email, Role) VALUES
('admin1', 'admin_password_hash1', 'admin1@example.com', 'superadmin'),
('admin2', 'admin_password_hash2', 'admin2@example.com', 'manager');

-- Data for Orders table
INSERT INTO Orders (OrderDate, TotalCost, CustomerID, AdminID, Status, ShippingAddress, PaymentMethod, PaymentStatus) VALUES
('2023-06-01', 29.97, 1, 1, 'Shipped', '123 Main St, Anytown, USA', 'Credit Card', 'Paid'),
('2023-06-02', 8.99, 2, 2, 'Processing', '456 Oak St, Anytown, USA', 'PayPal', 'Pending');

-- Data for OrderDetails table
INSERT INTO OrderDetails (OrderID, BookID, Quantity, Price) VALUES
(1, 1, 1, 10.99),
(1, 2, 1, 8.99),
(1, 3, 1, 12.99),
(2, 2, 1, 8.99);
