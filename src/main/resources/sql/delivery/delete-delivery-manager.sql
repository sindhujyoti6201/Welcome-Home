DELETE FROM Delivered
WHERE orderID = {orderID}
  AND deliveredStatus = 'IN TRANSIT'