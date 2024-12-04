SELECT o.orderID,
       o.orderDate,
       d.status,
       d.date AS deliveryDate,
       i.iDescription AS itemDescription
FROM Ordered o
         INNER JOIN Delivered d ON o.orderID = d.orderID
         INNER JOIN ItemIn ii ON o.orderID = ii.orderID
         INNER JOIN Item i ON ii.ItemID = i.ItemID
WHERE d.username = "john_doe"