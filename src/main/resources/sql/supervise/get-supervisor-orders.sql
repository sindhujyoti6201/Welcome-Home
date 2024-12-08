SELECT o.*, d.deliveredStatus as currentStatus
FROM Ordered o
         LEFT JOIN Delivered d ON o.orderID = d.orderID
WHERE o.supervisor = :username
  AND o.orderStatus = 'IN PROGRESS'
  AND (d.deliveredStatus = 'NOT YET DELIVERED' OR d.deliveredStatus IS NULL)
ORDER BY o.orderDate DESC;