package edu.nyu.welcomehome.services;

import edu.nyu.welcomehome.models.ImmutablePiece;
import edu.nyu.welcomehome.models.Piece;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemSearchService {

    private final JdbcTemplate jdbcTemplate;

    public ItemSearchService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Fetches the details of the pieces for the given item ID.
     */
    public List<Piece> getItemPieces(int itemID) {
        String query = """
            SELECT p.pieceNum,
                   p.pDescription,
                   p.length,
                   p.width,
                   p.height,
                   s.roomNum,
                   s.shelfNum,
                   s.shelf,
                   s.shelfDescription
            FROM Piece p
            INNER JOIN Location s ON p.roomNum = s.roomNum AND p.shelfNum = s.shelfNum
            WHERE p.itemID = ?
            """;

        // Map the result set to Piece model
        return jdbcTemplate.query(query, new Object[]{itemID}, (rs, rowNum) -> {
            return ImmutablePiece.builder()
                    .pieceNum(rs.getLong("pieceNum")) // Mapping pieceNum to Long
                    .pieceDescription(rs.getString("pDescription"))
                    .length(rs.getDouble("length")) // Mapping to Double
                    .width(rs.getDouble("width"))   // Mapping to Double
                    .height(rs.getDouble("height")) // Mapping to Double
                    .roomNum(rs.getString("roomNum"))
                    .shelfNum(rs.getString("shelfNum"))
                    .pNotes(rs.getString("shelfDescription")) // Assuming pNotes is shelfDescription in the query
                    .build();
        });
    }
}
