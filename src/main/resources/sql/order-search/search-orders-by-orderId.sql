SELECT Ordered.orderID, Ordered.orderDate, Ordered.orderNotes, Ordered.supervisor, Ordered.client, Ordered.orderStatus, ItemIn.itemID, Item.iDescription, Item.hasPieces FROM Ordered JOIN ItemIn using (orderID) JOIN Item using (ItemID) WHERE Ordered.orderID = {orderID}