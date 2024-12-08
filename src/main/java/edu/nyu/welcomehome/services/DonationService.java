package edu.nyu.welcomehome.services;

import edu.nyu.welcomehome.models.Piece;
import edu.nyu.welcomehome.models.request.DonationRequest;
import jakarta.annotation.Nullable;
import org.immutables.value.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

import static edu.nyu.welcomehome.utils.SqlFileLoader.loadSqlFromFile;

@Service
public class DonationService {
    private static final Logger logger = Logger.getLogger(DonationService.class.getName());
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DonationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addPersonDonated(String donorName, int itemId, LocalDate donateDate) {
        Map<String, String> params = new HashMap<>();
        params.put("donorName", donorName);
        params.put("itemId", String.valueOf(itemId));
        params.put("donateDate", String.valueOf(donateDate));
        String sqlForAddingDonatedBy = loadSqlFromFile("sql/donated-by.sql", params);
        logger.info("The query parsed for storing the donatedBy info is: " +sqlForAddingDonatedBy);
        jdbcTemplate.update(sqlForAddingDonatedBy);
    }

    public void submitDonation(DonationRequest donationRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("itemDescription", donationRequest.itemDescription());
        params.put("color", donationRequest.color());
        params.put("material", donationRequest.material());
        params.put("mainCategory", donationRequest.mainCategory());
        params.put("subCategory", donationRequest.subCategory());
        params.put("isNew", donationRequest.isNew() ? "1" : "0"); // Convert boolean to String
        params.put("hasPieces", donationRequest.hasPieces() ? "1" : "0"); // Convert boolean to String

        // Handle itemImage separately, as it is a byte array
        byte[] itemImage = donationRequest.itemImage();
        if (itemImage != null) {
            params.put("itemImage", Base64.getEncoder().encodeToString(itemImage)); // Encode to Base64 if needed
        }

        String sqlQuery = loadSqlFromFile("sql/submit-item-donation.sql", params);
        logger.info("The parsed query for submitting a item is: " + sqlQuery);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS), keyHolder);

        // Retrieve the auto-generated ItemID
        Number generatedKey = keyHolder.getKey();
        if (generatedKey == null) {
            throw new RuntimeException("Failed to retrieve the generated ItemID");
        }
        int itemId = generatedKey.intValue();

        addPersonDonated(donationRequest.donorName(), itemId, donationRequest.donateDate());
        logger.info("The returned itemId is: " + itemId);
        if (!donationRequest.pieces().isEmpty()) {
            Map<String, String> pieceParams = new HashMap<>();
            for (Piece piece : donationRequest.pieces()) {
                pieceParams.put("itemID", String.valueOf(itemId));
                pieceParams.put("pieceNum", String.valueOf(piece.pieceNum()));
                pieceParams.put("pieceDescription", piece.pieceDescription());
                pieceParams.put("length", String.valueOf(piece.length()));
                pieceParams.put("width", String.valueOf(piece.width()));
                pieceParams.put("height", String.valueOf(piece.height()));
                pieceParams.put("roomNum", piece.roomNum());
                pieceParams.put("shelfNum", piece.shelfNum());
                pieceParams.put("pNotes", piece.pNotes());

                String sqlQueryForPieces = loadSqlFromFile("sql/submit-piece-donation.sql", pieceParams);
                logger.info("The parsed query for submitting a piece with pieceNum "+piece.pieceNum()+" is: " + sqlQueryForPieces);
                jdbcTemplate.update(sqlQueryForPieces);
            }
        }
    }
}
