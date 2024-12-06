DROP DATABASE IF EXISTS welcomehomedb;

-- Create the database
CREATE DATABASE IF NOT EXISTS welcomehomedb;

-- Switch to the new database
USE welcomehomedb;

-- Drop tables if they exist
DROP TABLE IF EXISTS Delivered;
DROP TABLE IF EXISTS ItemIn;
DROP TABLE IF EXISTS Ordered;
DROP TABLE IF EXISTS Piece;
DROP TABLE IF EXISTS Location;
DROP TABLE IF EXISTS Act;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS DonatedBy;
DROP TABLE IF EXISTS PersonPhone;
DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Item;
DROP TABLE IF EXISTS Category;

CREATE TABLE Category (
                          mainCategory VARCHAR(50) NOT NULL,
                          subCategory VARCHAR(50) NOT NULL,
                          catNotes TEXT,
                          PRIMARY KEY (mainCategory, subCategory)
) ENGINE=InnoDB;

CREATE TABLE Item (
                      ItemID INT NOT NULL AUTO_INCREMENT,
                      iDescription TEXT,
                      photo LONGBLOB,  -- stored in LONGBLOB
                      color VARCHAR(20),
                      isNew BOOLEAN DEFAULT TRUE,
                      hasPieces BOOLEAN,
                      material VARCHAR(50),
                      mainCategory VARCHAR(50) NOT NULL,
                      subCategory VARCHAR(50) NOT NULL,
                      PRIMARY KEY (ItemID),
                      FOREIGN KEY (mainCategory, subCategory) REFERENCES Category(mainCategory, subCategory)
) ENGINE=InnoDB;

CREATE TABLE Person (
                        userName VARCHAR(50) NOT NULL,
                        password VARCHAR(100) NOT NULL,
                        fname VARCHAR(50) NOT NULL,
                        lname VARCHAR(50) NOT NULL,
                        email VARCHAR(100) NOT NULL,
                        PRIMARY KEY (userName)
) ENGINE=InnoDB;

CREATE TABLE PersonPhone (
                             userName VARCHAR(50) NOT NULL,
                             phone VARCHAR(20) NOT NULL,
                             PRIMARY KEY (userName, phone),
                             FOREIGN KEY (userName) REFERENCES Person(userName) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE DonatedBy (
                           ItemID INT NOT NULL,
                           userName VARCHAR(50) NOT NULL,
                           donateDate DATE NOT NULL,
                           PRIMARY KEY (ItemID, userName),
                           FOREIGN KEY (ItemID) REFERENCES Item(ItemID) ON DELETE CASCADE,
                           FOREIGN KEY (userName) REFERENCES Person(userName) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Role (
                      roleID ENUM('DELIVERY AGENT', 'DONOR', 'STAFF', 'BORROWER', 'MANAGER', 'SUPERVISOR') NOT NULL,
                      rDescription VARCHAR(100),
                      PRIMARY KEY (roleID)
) ENGINE=InnoDB;

CREATE TABLE Act (
                     userName VARCHAR(50) NOT NULL,
                     roleID ENUM('DELIVERY AGENT', 'DONOR', 'STAFF', 'BORROWER', 'MANAGER', 'SUPERVISOR') NOT NULL,  -- Matching the ENUM type of the Role table
                     PRIMARY KEY (userName, roleID),
                     FOREIGN KEY (userName) REFERENCES Person(userName) ON DELETE CASCADE,
                     FOREIGN KEY (roleID) REFERENCES Role(roleID) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Location (
                          roomNum INT NOT NULL,
                          shelfNum INT NOT NULL,
                          shelf VARCHAR(20),
                          shelfDescription VARCHAR(200),
                          PRIMARY KEY (roomNum, shelfNum)
) ENGINE=InnoDB;

CREATE TABLE Piece (
                       ItemID INT NOT NULL,
                       pieceNum INT NOT NULL,
                       pDescription VARCHAR(200),
                       length INT NOT NULL,
                       width INT NOT NULL,
                       height INT NOT NULL,
                       roomNum INT NOT NULL,
                       shelfNum INT NOT NULL,
                       pNotes TEXT,
                       PRIMARY KEY (ItemID, pieceNum),
                       FOREIGN KEY (ItemID) REFERENCES Item(ItemID) ON DELETE CASCADE,
                       FOREIGN KEY (roomNum, shelfNum) REFERENCES Location(roomNum, shelfNum) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Ordered (
                         orderID INT NOT NULL AUTO_INCREMENT,
                         orderDate DATE NOT NULL,
                         orderNotes VARCHAR(200),
                         supervisor VARCHAR(50) NULL,
                         client VARCHAR(50) NOT NULL,
                         orderStatus ENUM('INITIATED', 'IN PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'INITIATED',
                         PRIMARY KEY (orderID),
                         FOREIGN KEY (supervisor) REFERENCES Person(userName) ON DELETE SET NULL,
                         FOREIGN KEY (client) REFERENCES Person(userName) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE ItemIn (
                        ItemID INT NOT NULL,
                        orderID INT NOT NULL,
                        found BOOLEAN DEFAULT FALSE,
                        PRIMARY KEY (ItemID, orderID),
                        FOREIGN KEY (ItemID) REFERENCES Item(ItemID) ON DELETE CASCADE,
                        FOREIGN KEY (orderID) REFERENCES Ordered(orderID) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Delivered (
                           userName VARCHAR(50) NOT NULL,
                           orderID INT NOT NULL,
                           deliveredStatus ENUM('NOT YET DELIVERED', 'IN TRANSIT', 'DELIVERED') NOT NULL DEFAULT 'NOT YET DELIVERED',
                           date DATE NOT NULL,
                           PRIMARY KEY (userName, orderID),
                           FOREIGN KEY (userName) REFERENCES Person(userName) ON DELETE CASCADE,
                           FOREIGN KEY (orderID) REFERENCES Ordered(orderID) ON DELETE CASCADE
) ENGINE=InnoDB;