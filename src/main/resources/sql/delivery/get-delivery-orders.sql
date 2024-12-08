SELECT d.*, o.orderNotes, o.orderStatus,
       c.fname as clientFname, c.lname as clientLname,
       s.fname as supervisorFname, s.lname as supervisorLname
FROM Delivered d
         JOIN Ordered o ON d.orderID = o.orderID
         JOIN Person c ON o.client = c.userName
         JOIN Person s ON o.supervisor = s.userName
WHERE d.userName = {username}
  AND d.deliveredStatus = 'IN TRANSIT'
ORDER BY d.date DESC