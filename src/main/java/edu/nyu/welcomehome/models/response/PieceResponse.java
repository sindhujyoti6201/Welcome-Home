package edu.nyu.welcomehome.models.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutablePieceResponse.class)
@JsonDeserialize(as = ImmutablePieceResponse.class)
@Value.Immutable
public interface PieceResponse {
    Integer pieceNum();

    String pieceDescription();
}
