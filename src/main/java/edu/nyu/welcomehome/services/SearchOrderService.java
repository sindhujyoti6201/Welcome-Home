package edu.nyu.welcomehome.services;

import edu.nyu.welcomehome.models.response.ImmutablePieceResponse;
import edu.nyu.welcomehome.models.response.ImmutableSearchOrderResponse;
import edu.nyu.welcomehome.models.response.PieceResponse;
import edu.nyu.welcomehome.models.response.SearchOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static edu.nyu.welcomehome.utils.SqlFileLoader.loadSqlFromFile;

@Service
public class SearchOrderService {
    private static final Logger logger = Logger.getLogger(SearchOrderService.class.getName());
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SearchOrderService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SearchOrderResponse> searchOrders(String clientName, String orderId) {
        String sqlFilePath;
        Map<String, String> params = new HashMap<>();
        if (clientName != null && !clientName.isEmpty() && orderId != null && !orderId.isEmpty()) {
            params.put("username", clientName);
            params.put("orderID", orderId);
            sqlFilePath = "sql/item-search/search-orders-by-orderId-and-username.sql";
        } else if (clientName != null && !clientName.isEmpty()) {
            params.put("username", clientName);
            sqlFilePath = "sql/item-search/search-orders-by-username.sql";
        } else if (orderId != null && !orderId.isEmpty()) {
            params.put("orderID", orderId);
            sqlFilePath = "sql/item-search/search-orders-by-orderId.sql";
        } else {
            throw new IllegalArgumentException("clientName or orderId is null");
        }

        String query = loadSqlFromFile(sqlFilePath, params);
        logger.info("The query parsed for searching the orders is: " + query);


        RowMapper<SearchOrderResponse> rowMapper = (rs, rowNum) ->
                ImmutableSearchOrderResponse.builder()
                        .orderId(rs.getInt("orderID"))
                        .orderDate(rs.getDate("orderDate").toString()) // Converting SQL Date to LocalDate
                        .orderNotes(rs.getString("orderNotes"))
                        .supervisor(rs.getString("supervisor"))
                        .orderedBy(rs.getString("client"))
                        .orderStatus(rs.getString("orderStatus")) // Assuming it's stored as a String in DB
                        .deliveredBy(rs.getString("username"))
                        .itemId(rs.getInt("itemID"))
                        .iDescription(rs.getString("iDescription"))
                        .hasPieces(rs.getBoolean("hasPieces"))
                        .build();


        // Fetch order details
        List<SearchOrderResponse> orders = jdbcTemplate.query(query, rowMapper);

        orders.replaceAll(curr -> curr.hasPieces() ?
                ImmutableSearchOrderResponse.builder()
                        .from(curr)
                        .pieceNum(fetchPieces(curr.itemId()))
                        .build()
                : curr);

        return orders;
    }

    private List<PieceResponse> fetchPieces(Integer itemId) {
        Map<String, String> params = Collections.singletonMap("ItemID", String.valueOf(itemId));

        String query = loadSqlFromFile("sql/item-search/fetch-pieces.sql", params);
        logger.info("The query parsed for searching the pieces is: " + query);

        RowMapper<PieceResponse> rowMapper = (rs, rowNum) ->
                ImmutablePieceResponse.builder()
                        .pieceNum(rs.getInt("pieceNum"))
                        .pieceDescription(rs.getString("pDescription"))
                        .roomNum(rs.getInt("roomNum"))
                        .shelfNum(rs.getInt("shelfNum"))
                        .build();

        return jdbcTemplate.query(query, rowMapper);
    }
}
