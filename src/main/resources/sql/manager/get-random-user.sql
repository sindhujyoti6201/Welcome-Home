SELECT p.userName
FROM Person p
         JOIN Act a ON p.userName = a.userName
WHERE a.roleID = {role}
ORDER BY RAND()
    LIMIT 1