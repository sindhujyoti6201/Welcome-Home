UPDATE Ordered
SET orderStatus = 'IN PROGRESS',
    supervisor = {supervisor}
WHERE orderID = {orderID} AND orderStatus = 'INITIATED'