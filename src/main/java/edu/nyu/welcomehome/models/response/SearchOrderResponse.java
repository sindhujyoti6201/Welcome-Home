package edu.nyu.welcomehome.models.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@JsonSerialize(as = ImmutableSearchOrderResponse.class)
@JsonDeserialize(as = ImmutableSearchOrderResponse.class)
@Value.Immutable
public interface SearchOrderResponse {

    Integer orderId();

    String orderDate();

    String orderNotes();

    String supervisor();

    String orderedBy();

    String orderStatus();

    String deliveredBy();

    Integer itemId();

    String iDescription();

    Boolean hasPieces();

    List<PieceResponse> pieceNum();
}
