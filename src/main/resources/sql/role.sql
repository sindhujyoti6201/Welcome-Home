INSERT INTO Act (username, roleID)
VALUES ({username}, {roleID}) AS new ON DUPLICATE KEY UPDATE roleID = new.roleID