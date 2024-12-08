SELECT o.*, d.deliveredStatus as currentStatus
FROM Ordered o
         LEFT JOIN Delivered d ON o.orderID = d.orderID
WHERE o.orderID = {orderID}