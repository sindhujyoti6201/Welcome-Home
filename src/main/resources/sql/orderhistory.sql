SELECT o.orderID,
       o.orderDate,
       o.orderStatus,
       i.iDescription AS itemDescription,
       i.mainCategory AS itemMainCategory,
       i.subCategory  AS itemSubCategory
FROM Ordered o
         INNER JOIN ItemIn ii ON o.orderID = ii.orderID
         INNER JOIN Item i ON ii.ItemID = i.ItemID
WHERE o.client = {username}