SELECT o.*, d.deliveredStatus as currentStatus
FROM Ordered o
         LEFT JOIN Delivered d ON o.orderID = d.orderID
WHERE o.orderStatus = 'IN PROGRESS'
  AND (d.deliveredStatus = 'NOT YET DELIVERED')
ORDER BY o.orderDate DESC