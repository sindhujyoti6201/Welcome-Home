package edu.nyu.welcomehome.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutablePiece.class)
@JsonDeserialize(as = ImmutablePiece.class)
public interface Piece {

    Long pieceNum();

    String pieceDescription();

    Double length();

    Double width();

    Double height();

    String roomNum();

    String shelfNum();

    String pNotes();
}
