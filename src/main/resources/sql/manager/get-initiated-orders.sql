SELECT o.*, p.fname, p.lname, p2.fname as supervisorFname, p2.lname as supervisorLname
FROM Ordered o
         JOIN Person p ON o.client = p.userName
         LEFT JOIN Person p2 ON o.supervisor = p2.userName
WHERE o.orderStatus IN ('INITIATED', 'IN PROGRESS')
ORDER BY
    CASE o.orderStatus
        WHEN 'INITIATED' THEN 1
        WHEN 'IN PROGRESS' THEN 2
        END,
    o.orderDate DESC