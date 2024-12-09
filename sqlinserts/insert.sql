USE welcomehomedb;

INSERT INTO Category (mainCategory, subCategory, catNotes)
VALUES

    ('Furniture', 'Sofa', 'Couches and sofas, including sectional and futons.'),
    ('Furniture', 'Table', 'Dining tables, coffee tables, and side tables.'),
    ('Furniture', 'Chair', 'Armchairs, office chairs, and recliners.'),
    ('Furniture', 'Bed', 'Beds, mattresses, and bed frames.'),
    ('Furniture', 'Cabinet', 'Storage cabinets, filing cabinets, and lockers.'),
    ('Furniture', 'Bookshelf', 'Bookshelves, display shelves, and storage racks.'),

    ('Electronics', 'TV', 'Flat-screen TVs, LED, and smart TVs.'),
    ('Electronics', 'Laptop', 'Laptops and notebooks.'),
    ('Electronics', 'Smartphone', 'Mobile phones and smartphones.'),
    ('Electronics', 'Microwave', 'Countertop and over-the-range microwaves.'),
    ('Electronics', 'Refrigerator', 'Fridges, freezers, and mini-fridges.'),
    ('Electronics', 'Washing Machine', 'Top and front-load washing machines.'),
    ('Electronics', 'Headphones', 'Wired and wireless headphones.'),
    ('Electronics', 'Camera', 'Digital cameras, action cameras, and camcorders.'),

    ('Clothing', 'Shirts', 'T-shirts, blouses, and button-down shirts.'),
    ('Clothing', 'Pants', 'Jeans, trousers, and leggings.'),
    ('Clothing', 'Shoes', 'Sneakers, boots, and formal shoes.'),

    ('Kitchen', 'Utensils', 'Cooking utensils, spoons, and spatulas.'),
    ('Kitchen', 'Cookware', 'Pots, pans, and baking trays.'),
    ('Kitchen', 'Appliances', 'Blenders, toasters, and coffee makers.'),

    ('Home Decor', 'Rugs', 'Area rugs and carpets.'),
    ('Home Decor', 'Lighting', 'Lamps and ceiling lights.'),

    ('Books and Media', 'Books', 'Novels, textbooks, and reference books.'),

    ('Toys and Games', 'Board Games', 'Monopoly, Scrabble, and other tabletop games.'),

    ('Sports', 'Bicycles', 'Mountain bikes, road bikes, and kids bikes.'),
    ('Sports', 'Balls', 'Soccer balls, basketballs, and footballs.'),
    ('Sports', 'Equipment', 'Tennis rackets, baseball bats, and golf clubs.'),

    ('Miscellaneous', 'Tools', 'Hand tools and power tools.');


INSERT INTO Location (roomNum, shelfNum, shelf, shelfDescription)
VALUES

    (1, 1, 'Sofa Shelf', 'Section for couches, sofas, and futons.'),
    (1, 2, 'Table Shelf', 'Shelf for dining tables, coffee tables, and side tables.'),
    (1, 3, 'Chair Shelf', 'Storage for armchairs, office chairs, and recliners.'),
    (1, 4, 'Bed Shelf', 'Space for beds, mattresses, and bed frames.'),
    (1, 5, 'Cabinet Shelf', 'Cabinets for storage, filing, and organization.'),
    (1, 6, 'Bookshelf Shelf', 'Bookshelves and storage racks for books and decor.'),


    (2, 1, 'TV Shelf', 'Storage for flat-screen TVs and entertainment systems.'),
    (2, 2, 'Laptop Shelf', 'Shelf for laptops, notebooks, and accessories.'),
    (2, 3, 'Smartphone Shelf', 'Space for mobile phones and chargers.'),
    (2, 4, 'Microwave Shelf', 'Storage for microwaves and small kitchen electronics.'),
    (2, 5, 'Refrigerator Shelf', 'Space for refrigerators and mini-fridges.'),
    (2, 6, 'Washing Machine', 'Shelf for washing machines and laundry-related items.'),
    (2, 7, 'Air Conditioner', 'Storage for portable air conditioning units.'),
    (2, 8, 'Headphones Shelf', 'Area for headphones and audio equipment.'),
    (2, 9, 'Camera Shelf', 'Shelf for cameras, camcorders, and photography accessories.'),
    (2, 10, 'Game Console Shelf', 'Space for game consoles and related gaming accessories.'),


    (3, 1, 'Shirt Shelf', 'Shelf for t-shirts, blouses, and button-down shirts.'),
    (3, 2, 'Pants Shelf', 'Storage for jeans, trousers, and leggings.'),
    (3, 3, 'Shoe Shelf', 'Shelf for sneakers, boots, and formal shoes.'),
    (4, 1, 'Utensils Shelf', 'Storage for cooking utensils, spoons, and spatulas.'),
    (4, 2, 'Cookware Shelf', 'Shelf for pots, pans, and baking trays.'),
    (4, 3, 'Appliances Shelf', 'Space for blenders, toasters, and coffee makers.'),
    (5, 1, 'Rug Shelf', 'Shelf for area rugs and carpets.'),
    (5, 2, 'Lighting Shelf', 'Storage for lamps and ceiling lights.'),
    (6, 1, 'Book Shelf', 'Shelf for books, novels, and textbooks.'),
    (7, 1, 'Board Games Shelf', 'Shelf for board games and tabletop games.'),
    (7, 2, 'Toys Shelf', 'Storage for stuffed animals, dolls, and action figures.'),

    (8, 1, 'Bicycle Shelf', 'Space for bicycles, including mountain and road bikes.'),
    (8, 2, 'Balls Shelf', 'Shelf for soccer balls, basketballs, and footballs.'),
    (8, 3, 'Equipment Shelf', 'Storage for tennis rackets, baseball bats, and sports gear.'),
    (9, 1, 'Tool Shelf', 'Shelf for hand tools and power tools.');


INSERT INTO Role (roleID, rDescription)
VALUES
    ('DELIVERY AGENT', 'Responsible for delivering items to donors or recipients.'),
    ('DONOR', 'Person who donates goods to the organization or service.'),
    ('STAFF', 'Employees handling day-to-day operations, assisting donors and borrowers.'),
    ('BORROWER', 'Person who borrows items from the organization.'),
    ('MANAGER', 'Oversees the operations, manages staff, and ensures processes are followed.'),
    ('SUPERVISOR', 'Supervises the work of staff, ensuring efficiency and quality control.');


INSERT INTO Person (userName, password, fname, lname, email)
VALUES
    ('Amit Kumar', 'password1', 'Amit', 'Kumar', 'amit@example.com'),
    ('David Lee', 'password2', 'David', 'Lee', 'david@example.com'),
    ('Emily Johnson', 'password3', 'Emily', 'Johnson', 'emily@example.com'),
    ('Harshitha Jonnagaddala', 'password4', 'Harshitha', 'Jonnagaddala', 'harshi@example.com'),
    ('John Smith', 'password5', 'John', 'Smith', 'john@example.com'),
    ('Michael Williams', 'password6', 'Michael', 'Williams', 'michael@example.com'),
    ('Priya Patel', 'password7', 'Priya', 'Patel', 'priya@example.com'),
    ('Rahul Mallidi', 'password8', 'Rahul', 'Mallidi', 'rahul@example.com'),
    ('Sindhujyothi Dutta', 'password9', 'Sindhujyothi', 'Dutta', 'sindhu@example.com'),
    ('Sophia Brown', 'password10', 'Sophia', 'Brown', 'sophia@example.com'),
    ('Anjali Reddy', 'password11', 'Anjali', 'Reddy', 'anjali@example.com'),
    ('Manoj Kumar', 'password12', 'Manoj', 'Kumar', 'manoj@example.com');




INSERT INTO PersonPhone (userName, phone)
VALUES
    ('Rahul Mallidi', '9876543210'),
    ('Sindhujyothi Dutta', '9887654321'),
    ('Harshitha Jonnagaddala', '9876123456'),
    ('Amit Kumar', '9876987654'),
    ('Priya Patel', '9887765432'),
    ('John Smith', '9876001234'),
    ('Emily Johnson', '9887112234'),
    ('Michael Williams', '9876223345'),
    ('Sophia Brown', '9887334456'),
    ('David Lee', '9877445567'),
    ('Anjali Reddy', '9878998765'),
    ('Manoj Kumar', '9888877654');



INSERT INTO Act (userName, roleID)
VALUES
    ('Rahul Mallidi', 'MANAGER'),
    ('Sindhujyothi Dutta', 'MANAGER'),
    ('Harshitha Jonnagaddala', 'MANAGER'),
    ('Amit Kumar', 'SUPERVISOR'),
    ('Priya Patel', 'SUPERVISOR'),
    ('John Smith', 'DONOR'),
    ('John Smith', 'SUPERVISOR'),
    ('Emily Johnson', 'DONOR'),
    ('Emily Johnson', 'DELIVERY AGENT'),
    ('Michael Williams', 'DONOR'),
    ('Michael Williams', 'SUPERVISOR'),
    ('Sophia Brown', 'DONOR'),
    ('David Lee', 'DONOR'),
    ('Rahul Mallidi', 'DONOR'),
    ('Sindhujyothi Dutta', 'DONOR'),
    ('Harshitha Jonnagaddala', 'DONOR'),
    ('Anjali Reddy', 'BORROWER'),
    ('Manoj Kumar', 'BORROWER');


INSERT INTO Item (iDescription, photo, color, isNew, hasPieces, material, mainCategory, subCategory)
VALUES
    ('Wooden Chair', 'https://mypropboutique.com/wp-content/uploads/2017/09/BasicChair-162B3824.jpg', 'Brown', TRUE, TRUE, 'Wood', 'Furniture', 'Chair'),
    ('Glass Table', 'https://tse2.mm.bing.net/th?id=OIP.cuJF8q0Du4ejHjef4zkIXAHaHa&pid=Api&P=0&h=180', 'Transparent', TRUE, TRUE, 'Glass', 'Furniture', 'Table'),
    ('King-sized Bed', 'https://i5.walmartimages.com/asr/dbdc41ca-ac8c-4dc3-98dd-ccbf27b92166.60c608f2893872816c0af11449d3533a.jpeg', 'White', TRUE, TRUE, 'Wood', 'Furniture', 'Bed'),
    ('Sofa Set', 'https://images-na.ssl-images-amazon.com/images/I/611C8z-xBfL.SL1000.jpg', 'Grey', TRUE, TRUE, 'Fabric', 'Furniture', 'Sofa'),
    ('Recliner Chair', 'https://images.furnituredealer.net/img/products/palliser/color/theo%2042002_42002-33-b.jpg', 'Black', TRUE, TRUE, 'Leather', 'Furniture', 'Chair'),
    ('Dining Table', 'https://m.media-amazon.com/images/I/81yypSRNAsL.jpg', 'Wooden', TRUE, TRUE, 'Wood', 'Furniture', 'Table'),
    ('Cabinet', 'https://foter.com/photos/300/tall-white-wood-pantry-linen-cabinet-kitchen-bathroom-cupboard-storage.jpg', 'Brown', TRUE, TRUE, 'Wood', 'Furniture', 'Cabinet'),
    ('Coffee Table', 'https://menterarchitects.com/wp-content/uploads/2017/10/steel-frame-coffee-table-uk-coffee-addicts-with-large-contemporary-coffee-tables.jpg', 'Glass', TRUE, TRUE, 'Glass', 'Furniture', 'Table'),
    ('Wooden Shelf', 'https://i5.walmartimages.com/asr/525cfac0-3a59-4631-8702-7b0057b44366_3.5c367ffd2a48b14c871b58a8a5924b99.jpeg', 'Brown', TRUE, TRUE, 'Wood', 'Furniture', 'Bookshelf'),
    ('Wardrobe', 'https://tse1.mm.bing.net/th?id=OIP.Autjjv7fT_11CFPcCzOEWAHaGa&pid=Api&P=0&h=180', 'White', TRUE, TRUE, 'Wood', 'Furniture', 'Cabinet'),
    ('Washing Machine', 'https://tse2.mm.bing.net/th?id=OIP.zauObjkIVGgHuCr2yMzDLgHaHh&pid=Api&P=0&h=180', 'White', TRUE, TRUE, 'Metal', 'Electronics', 'Washing Machine'),
    ('Microwave Oven', 'https://tse3.mm.bing.net/th?id=OIP.xYgsfzQBbZZKrapUWntQegHaHt&pid=Api&P=0&h=180', 'Silver', TRUE, TRUE, 'Metal', 'Electronics', 'Microwave'),
    ('LED TV', 'https://tse3.mm.bing.net/th?id=OIP.WjvWIH87MWhg0aO42W_CzQHaFh&pid=Api&P=0&h=180', 'Black', TRUE, TRUE, 'Plastic', 'Electronics', 'TV'),
    ('Refrigerator', 'https://tse3.mm.bing.net/th?id=OIP.oT0ikxN6fROrKaQCU42tMgHaHt&pid=Api&P=0&h=180', 'Silver', TRUE, TRUE, 'Metal', 'Electronics', 'Refrigerator'),
    ('Mobile Phone', 'https://tse4.mm.bing.net/th?id=OIP.W3zdXGwcL-V79rH3H7eCrAHaFI&pid=Api&P=0&h=180', 'Black', TRUE, TRUE, 'Plastic', 'Electronics', 'Smartphone'),
    ('Laptop', 'https://tse3.mm.bing.net/th?id=OIP.AkrTzvpr4i429azZwn1K_gHaEp&pid=Api&P=0&h=180', 'Silver', TRUE, TRUE, 'Metal', 'Electronics', 'Laptop'),
    ('Headphones', 'https://tse3.mm.bing.net/th?id=OIP.lsgjTNpi7xsBn1XpRNMvxQHaHa&pid=Api&P=0&h=180', 'Black', TRUE, TRUE, 'Plastic', 'Electronics', 'Headphones'),
    ('Football', 'https://tse2.mm.bing.net/th?id=OIP.TthytAg4OxbLQkZT0tRrfQHaHa&pid=Api&P=0&h=180', 'White and Black', TRUE, FALSE, 'Leather', 'Sports', 'Balls'),
    ('Basketball', 'https://tse4.mm.bing.net/th?id=OIP.2S7vfMQQmEKnPNRMPNNTNAHaHP&pid=Api&P=0&h=180', 'Orange', TRUE, FALSE, 'Rubber', 'Sports', 'Balls');


INSERT INTO Piece (ItemID, pieceNum, pDescription, length, width, height, roomNum, shelfNum, pNotes)
VALUES

    (1, 1, 'Chair Seat', 18, 18, 4, 1, 3, 'Made of solid oak wood. Needs occasional polishing.'),
    (1, 2, 'Chair Backrest', 18, 2, 14, 1, 3, 'Ergonomic curve for better lumbar support.'),
    (1, 3, 'Chair Legs', 2, 2, 18, 1, 3, 'Set of four detachable legs for easy storage.'),
    (2, 1, 'Glass Tabletop', 60, 40, 1, 1, 2, 'Tempered glass for safety. Scratch-resistant.'),
    (2, 2, 'Metal Frame', 58, 38, 30, 1, 2, 'Steel frame with powder-coated finish.'),
    (3, 1, 'Headboard', 78, 4, 36, 1, 4, 'Padded headboard for extra comfort.'),
    (3, 2, 'Bed Frame', 78, 60, 10, 1, 4, 'Solid wood frame with reinforced joints.'),
    (3, 3, 'Mattress', 76, 58, 12, 1, 4, 'Memory foam mattress. Hypoallergenic.'),
    (4, 1, 'Left Section', 60, 36, 36, 1, 1, 'Fabric upholstery with stain resistance.'),
    (4, 2, 'Right Section', 60, 36, 36, 1, 1, 'Includes built-in storage compartment.'),
    (4, 3, 'Corner Section', 36, 36, 36, 1, 1, 'Can be rearranged to fit various layouts.'),
    (5, 1, 'Seat Base', 28, 28, 18, 1, 3, 'Swivels 360 degrees.'),
    (5, 2, 'Backrest', 28, 4, 32, 1, 3, 'Reclines up to 45 degrees for comfort.'),
    (5, 3, 'Footrest', 18, 10, 12, 1, 3, 'Folds neatly when not in use.'),
    (6, 1, 'Tabletop', 72, 36, 2, 1, 2, 'Features a smooth lacquer finish.'),
    (6, 2, 'Table Legs', 2, 2, 30, 1, 2, 'Four detachable legs for transport.'),
    (7, 1, 'Left Door', 30, 1, 60, 1, 5, 'Hinged door with soft-close mechanism.'),
    (7, 2, 'Right Door', 30, 1, 60, 1, 5, 'Mirror finish for decorative appeal.'),
    (7, 3, 'Shelving Unit', 28, 16, 58, 1, 5, 'Adjustable shelves for flexible storage.'),
    (8, 1, 'Glass Tabletop', 36, 36, 1, 1, 2, 'Tempered glass resistant to stains.'),
    (8, 2, 'Metal Frame', 34, 34, 18, 1, 2, 'Powder-coated steel with chrome finish.'),
    (9, 1, 'Top Shelf', 24, 12, 1, 1, 6, 'Can hold up to 50 lbs of weight.'),
    (9, 2, 'Bottom Shelf', 24, 12, 1, 1, 6, 'Reinforced with additional support beams.'),
    (11, 1, 'Outer Shell', 24, 24, 36, 2, 6, 'White enamel-coated metal.'),
    (11, 2, 'Drum', 20, 20, 20, 2, 6, 'Stainless steel with high spin capacity.'),
    (11, 3, 'Control Panel', 8, 2, 6, 2, 6, 'Features digital display and easy controls.');

INSERT INTO Ordered (orderDate, orderNotes, supervisor, client, orderStatus)
VALUES
    ('2024-12-01', 'Order for furniture items', NULL, 'Amit Kumar', 'INITIATED'),
    ('2024-12-02', 'Order for electronic gadgets', NULL, 'Sophia Brown', 'INITIATED'),
    ('2024-12-03', 'Donation order for electronics', NULL, 'David Lee', 'INITIATED'),
    ('2024-12-04', 'Furniture delivery request', NULL, 'Michael Williams', 'INITIATED');



INSERT INTO ItemIn (ItemID, orderID, found)
VALUES
    (1, 1, TRUE),
    (2, 1, FALSE),
    (6, 2, TRUE),
    (10, 2, TRUE),
    (11, 3, TRUE),
    (13, 3, TRUE),
    (19, 4, FALSE);


INSERT INTO DonatedBy (ItemID, userName, donateDate)
VALUES
    (1, 'John Smith', '2024-11-25'),
    (6, 'Emily Johnson', '2024-11-26'),
    (10, 'Michael Williams', '2024-11-27'),
    (11, 'Sophia Brown', '2024-11-28'),
    (13, 'David Lee', '2024-11-29'),
    (19, 'John Smith', '2024-11-30');