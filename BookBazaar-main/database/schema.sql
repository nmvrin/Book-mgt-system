CREATE DATABASE BookBazaar;
-- Books table

CREATE TABLE Books (

    BookID INT AUTO_INCREMENT PRIMARY KEY,

    Title VARCHAR(255) NOT NULL,

    Author VARCHAR(255) NOT NULL,

    Price DECIMAL(10, 2) NOT NULL,

    QtyStocked INT NOT NULL

);

 

-- Customers table

CREATE TABLE Customers (

    CustomerID INT AUTO_INCREMENT PRIMARY KEY,

    Username VARCHAR(50) NOT NULL UNIQUE,

    Password VARCHAR(255) NOT NULL,

    Name VARCHAR(255) NOT NULL,

    Email VARCHAR(255) NOT NULL UNIQUE,

    Address VARCHAR(255) NOT NULL,

    Phone VARCHAR(20) NOT NULL

);

 

-- Admin table

CREATE TABLE Admin (

    AdminID INT AUTO_INCREMENT PRIMARY KEY,

    Username VARCHAR(50) NOT NULL UNIQUE,

    Password VARCHAR(255) NOT NULL,

    Email VARCHAR(255) NOT NULL UNIQUE,

    Role VARCHAR(50) NOT NULL

);

 

-- Orders table

CREATE TABLE Orders (

    OrderID INT AUTO_INCREMENT PRIMARY KEY,

    OrderDate DATE NOT NULL,

    TotalCost DECIMAL(10, 2) NOT NULL,

    CustomerID INT,

    AdminID INT,

    Status VARCHAR(50) NOT NULL,

    ShippingAddress VARCHAR(255) NOT NULL,

    PaymentMethod VARCHAR(50) NOT NULL,

    PaymentStatus VARCHAR(50) NOT NULL,

    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID),

    FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)

);

 

-- Create the OrderDetails table

CREATE TABLE OrderDetails (

    OrderDetailID INT AUTO_INCREMENT PRIMARY KEY,

    OrderID INT,

    BookID INT,

    Quantity INT NOT NULL,

    Price DECIMAL(10, 2) NOT NULL,

    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),

    FOREIGN KEY (BookID) REFERENCES Books(BookID)

);