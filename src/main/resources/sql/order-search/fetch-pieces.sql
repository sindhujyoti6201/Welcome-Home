SELECT Piece.pieceNum, Piece.pDescription, Piece.roomNum, Piece.shelfNum FROM Piece JOIN Item using(ItemID) WHERE ItemID = {ItemID}