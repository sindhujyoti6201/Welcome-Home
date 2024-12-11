package edu.nyu.welcomehome.models.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.annotation.Nullable;
import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(as = ImmutableSearchOrderResponse.class)
@JsonDeserialize(as = ImmutableSearchOrderResponse.class)
@Value.Immutable
public interface SearchOrderResponse {

    Integer orderId();

    @Nullable
    String orderDate();

    @Nullable
    String orderNotes();

    @Nullable
    String supervisor();

    String orderedBy();

    @Nullable
    String orderStatus();

    @Nullable
    Integer itemId();

    @Nullable
    String iDescription();

    Boolean hasPieces();

    @Value.Default
    default List<PieceResponse> pieceNum() {
        return new ArrayList<>();
    }
}
