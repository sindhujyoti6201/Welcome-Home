USE welcomehomedb;

-- Insert categories into the Category table
INSERT INTO Category (mainCategory, subCategory, catNotes) VALUES
                                                               ('Electronics', 'Smartphones', 'Mobile devices like phones'),
                                                               ('Electronics', 'Laptops', 'Portable computing devices'),
                                                               ('Furniture', 'Tables', 'Different types of tables for homes and offices'),
                                                               ('Furniture', 'Chairs', 'Varieties of chairs for different purposes'),
                                                               ('Furniture', 'Sofas', 'Comfortable sofas for homes'),
                                                               ('Home Decor', 'Wall Art', 'Decorations for the wall'),
                                                               ('Home Decor', 'Rugs', 'Decorative rugs for floors'),
                                                               ('Toys', 'Educational', 'Toys that help kids learn'),
                                                               ('Toys', 'Action Figures', 'Collectible action figures'),
                                                               ('Clothing', 'Men', 'Clothing for men'),
                                                               ('Clothing', 'Women', 'Clothing for women'),
                                                               ('Sports', 'Equipment', 'Sports equipment for various sports'),
                                                               ('Sports', 'Apparel', 'Clothing for athletes'),
                                                               ('Garden', 'Tools', 'Tools for gardening'),
                                                               ('Garden', 'Plants', 'Plants and flowers for gardening');

-- Insert people into the Person table
INSERT INTO Person (userName, password, fname, lname, email) VALUES
                                                                 ('john_doe', 'password123', 'John', 'Doe', 'john@example.com'),
                                                                 ('jane_smith', 'password123', 'Jane', 'Smith', 'jane@example.com'),
                                                                 ('bob_brown', 'password123', 'Bob', 'Brown', 'bob@example.com'),
                                                                 ('emma_watson', 'password123', 'Emma', 'Watson', 'emma@example.com'),
                                                                 ('liam_jones', 'password123', 'Liam', 'Jones', 'liam@example.com'),
                                                                 ('noah_smith', 'password123', 'Noah', 'Smith', 'noah@example.com'),
                                                                 ('admin', 'adminpassword', 'Admin', 'User', 'admin@example.com');

-- Insert phone numbers for persons into the PersonPhone table
INSERT INTO PersonPhone (userName, phone) VALUES
                                              ('john_doe', '555-0101'),
                                              ('jane_smith', '555-0102'),
                                              ('bob_brown', '555-0103'),
                                              ('emma_watson', '555-0104'),
                                              ('liam_jones', '555-0105'),
                                              ('noah_smith', '555-0106'),
                                              ('admin', '555-0000');

-- Insert roles into the Role table
INSERT INTO Role (roleID, rDescription) VALUES
                                            ('admin', 'Administrator'),
                                            ('client', 'Client User'),
                                            ('supervisor', 'Order Supervisor');

-- Insert actions for each person into the Act table
INSERT INTO Act (userName, roleID) VALUES
                                       ('john_doe', 'client'),
                                       ('jane_smith', 'client'),
                                       ('bob_brown', 'client'),
                                       ('emma_watson', 'client'),
                                       ('liam_jones', 'client'),
                                       ('noah_smith', 'client'),
                                       ('admin', 'admin'),
                                       ('admin', 'supervisor');

-- Insert items into the Item table
INSERT INTO Item (ItemID, iDescription, photo, color, isNew, hasPieces, material, mainCategory, subCategory) VALUES
                                                                                                                 (1, 'iPhone 14', NULL, 'Black', TRUE, FALSE, 'Aluminum', 'Electronics', 'Smartphones'),
                                                                                                                 (2, 'MacBook Pro', NULL, 'Silver', TRUE, FALSE, 'Aluminum', 'Electronics', 'Laptops'),
                                                                                                                 (3, 'Dining Table', NULL, 'Brown', TRUE, TRUE, 'Wood', 'Furniture', 'Tables'),
                                                                                                                 (4, 'Office Chair', NULL, 'Black', TRUE, FALSE, 'Plastic', 'Furniture', 'Chairs'),
                                                                                                                 (5, 'Modern Sofa', NULL, 'Grey', TRUE, FALSE, 'Fabric', 'Furniture', 'Sofas'),
                                                                                                                 (6, 'Canvas Wall Art', NULL, 'Multicolor', TRUE, FALSE, 'Canvas', 'Home Decor', 'Wall Art'),
                                                                                                                 (7, 'Area Rug', NULL, 'Beige', TRUE, FALSE, 'Fabric', 'Home Decor', 'Rugs'),
                                                                                                                 (8, 'Educational Toys', NULL, 'Multicolor', TRUE, FALSE, 'Plastic', 'Toys', 'Educational'),
                                                                                                                 (9, 'Action Figures Set', NULL, 'Various', TRUE, FALSE, 'Plastic', 'Toys', 'Action Figures'),
                                                                                                                 (10, 'Men\'s T-Shirt', NULL, 'Blue', TRUE, FALSE, 'Cotton', 'Clothing', 'Men'),
(11, 'Women\'s Dress', NULL, 'Red', TRUE, FALSE, 'Silk', 'Clothing', 'Women'),
                                                                                                                 (12, 'Football', NULL, 'Brown', TRUE, FALSE, 'Leather', 'Sports', 'Equipment'),
                                                                                                                 (13, 'Running Shoes', NULL, 'Black', TRUE, FALSE, 'Rubber', 'Sports', 'Apparel'),
                                                                                                                 (14, 'Garden Tools Set', NULL, 'Green', TRUE, FALSE, 'Metal', 'Garden', 'Tools'),
                                                                                                                 (15, 'Potted Plant', NULL, 'Green', TRUE, FALSE, 'Plastic', 'Garden', 'Plants');

-- Insert items into the DonatedBy table
INSERT INTO DonatedBy (ItemID, userName, donateDate) VALUES
                                                         (1, 'john_doe', '2024-11-01'),
                                                         (2, 'jane_smith', '2024-11-02'),
                                                         (3, 'bob_brown', '2024-11-03'),
                                                         (4, 'emma_watson', '2024-11-04'),
                                                         (5, 'liam_jones', '2024-11-05'),
                                                         (6, 'noah_smith', '2024-11-06'),
                                                         (7, 'john_doe', '2024-11-07'),
                                                         (8, 'jane_smith', '2024-11-08'),
                                                         (9, 'bob_brown', '2024-11-09'),
                                                         (10, 'emma_watson', '2024-11-10'),
                                                         (11, 'liam_jones', '2024-11-11'),
                                                         (12, 'noah_smith', '2024-11-12'),
                                                         (13, 'john_doe', '2024-11-13'),
                                                         (14, 'jane_smith', '2024-11-14'),
                                                         (15, 'bob_brown', '2024-11-15');

-- Insert orders into the Ordered table
INSERT INTO Ordered (orderID, orderDate, orderNotes, supervisor, client) VALUES
                                                                             (1001, '2024-11-01', 'Smartphone order for John', 'admin', 'john_doe'),
                                                                             (1002, '2024-11-02', 'Laptop order for John', 'admin', 'john_doe'),
                                                                             (1003, '2024-11-03', 'Table order for John', 'admin', 'john_doe'),
                                                                             (1004, '2024-11-04', 'Sofa order for John', 'admin', 'john_doe'),
                                                                             (1005, '2024-11-05', 'Smartphone order for Jane', 'admin', 'jane_smith'),
                                                                             (1006, '2024-11-06', 'Action Figures order for Jane', 'admin', 'jane_smith'),
                                                                             (1007, '2024-11-07', 'Wall Art order for Jane', 'admin', 'jane_smith'),
                                                                             (1008, '2024-11-08', 'T-shirt order for Jane', 'admin', 'jane_smith'),
                                                                             (1009, '2024-11-09', 'Table order for Bob', 'admin', 'bob_brown'),
                                                                             (1010, '2024-11-10', 'Chairs order for Bob', 'admin', 'bob_brown'),
                                                                             (1011, '2024-11-11', 'Rugs order for Bob', 'admin', 'bob_brown'),
                                                                             (1012, '2024-11-12', 'Men\'s clothing order for Bob', 'admin', 'bob_brown'),
(1013, '2024-11-13', 'Laptops order for Emma', 'admin', 'emma_watson'),
(1014, '2024-11-14', 'Dining table order for Emma', 'admin', 'emma_watson'),
(1015, '2024-11-15', 'Smartphone order for Emma', 'admin', 'emma_watson'),
(1016, '2024-11-16', 'Chairs order for Emma', 'admin', 'emma_watson'),
(1017, '2024-11-17', 'Laptop order for Liam', 'admin', 'liam_jones'),
(1018, '2024-11-18', 'Action Figures order for Liam', 'admin', 'liam_jones'),
(1019, '2024-11-19', 'Wall Art order for Liam', 'admin', 'liam_jones'),
(1020, '2024-11-20', 'Chairs order for Liam', 'admin', 'liam_jones'),
(1021, '2024-11-21', 'Smartphone order for Noah', 'admin', 'noah_smith'),
(1022, '2024-11-22', 'Laptops order for Noah', 'admin', 'noah_smith'),
(1023, '2024-11-23', 'Clothing order for Noah', 'admin', 'noah_smith'),
(1024, '2024-11-24', 'Rugs order for Noah', 'admin', 'noah_smith');

-- Insert items ordered into ItemIn table
INSERT INTO ItemIn (ItemID, orderID, found) VALUES
(1, 1001, TRUE),
(2, 1002, TRUE),
(3, 1003, TRUE),
(4, 1004, TRUE),
(5, 1005, TRUE),
(6, 1006, TRUE),
(7, 1007, TRUE),
(8, 1008, TRUE),
(9, 1009, TRUE),
(10, 1010, TRUE),
(11, 1011, TRUE),
(12, 1012, TRUE),
(13, 1013, TRUE),
(14, 1014, TRUE),
(15, 1015, TRUE);

-- Insert delivery status into the Delivered table
INSERT INTO Delivered (userName, orderID, status, date) VALUES
('admin', 1001, 'Delivered', '2024-11-01'),
('admin', 1002, 'Delivered', '2024-11-02'),
('admin', 1003, 'Delivered', '2024-11-03'),
('admin', 1004, 'Delivered', '2024-11-04'),
('admin', 1005, 'Delivered', '2024-11-05'),
('admin', 1006, 'Delivered', '2024-11-06'),
('admin', 1007, 'Delivered', '2024-11-07'),
('admin', 1008, 'Delivered', '2024-11-08'),
('admin', 1009, 'Delivered', '2024-11-09'),
('admin', 1010, 'Delivered', '2024-11-10'),
('admin', 1011, 'Delivered', '2024-11-11'),
('admin', 1012, 'Delivered', '2024-11-12'),
('admin', 1013, 'Delivered', '2024-11-13'),
('admin', 1014, 'Delivered', '2024-11-14'),
('admin', 1015, 'Delivered', '2024-11-15'),
('admin', 1016, 'Delivered', '2024-11-16'),
('admin', 1017, 'Delivered', '2024-11-17'),
('admin', 1018, 'Delivered', '2024-11-18'),
('admin', 1019, 'Delivered', '2024-11-19'),
('admin', 1020, 'Delivered', '2024-11-20'),
('admin', 1021, 'Delivered', '2024-11-21'),
('admin', 1022, 'Delivered', '2024-11-22'),
('admin', 1023, 'Delivered', '2024-11-23'),
('admin', 1024, 'Delivered', '2024-11-24');
