UPDATE Ordered
SET orderStatus = 'CANCELLED',
    supervisor = NULL
WHERE orderID = {orderID}
  AND orderStatus IN ('INITIATED', 'IN PROGRESS')