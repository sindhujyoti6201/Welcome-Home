SELECT i.mainCategory, i.subCategory, COUNT(*) as itemCount FROM Item i JOIN Category c ON i.mainCategory = c.mainCategory AND i.subCategory = c.subCategory GROUP BY i.mainCategory, i.subCategory
