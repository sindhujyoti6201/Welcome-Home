UPDATE Delivered
SET deliveredStatus = 'DELIVERED',
    date = CURRENT_DATE
WHERE orderID = {orderID}
  AND deliveredStatus = 'IN TRANSIT'