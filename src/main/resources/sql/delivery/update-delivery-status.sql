UPDATE Delivered
SET deliveredStatus = 'DELIVERED',
    date = CURDATE()
WHERE orderID = {orderID}
  AND userName = {username}
  AND deliveredStatus = 'IN TRANSIT'