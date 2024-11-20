package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Piece {
    Long itemID();

    Long pieceNum();

    String pieceDescription();

    Double length();

    Double width();

    Double height();

    String roomNum();

    String shelfNum();

    String pNotes();
}
