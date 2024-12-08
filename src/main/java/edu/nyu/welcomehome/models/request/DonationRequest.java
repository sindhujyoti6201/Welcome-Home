package edu.nyu.welcomehome.models.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.nyu.welcomehome.models.Piece;
import jakarta.annotation.Nullable;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableDonationRequest.class)
@JsonDeserialize(as = ImmutableDonationRequest.class)
public interface DonationRequest {
    String donorName();

    LocalDate donateDate();

    String itemDescription();

    String color();

    String material();

    String mainCategory();

    String subCategory();

    boolean isNew();

    boolean hasPieces();

    @Value.Default
    default List<Piece> pieces() {
        return new ArrayList<>();
    }

    @Nullable
    byte[] itemImage();
}
