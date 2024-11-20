package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Item {
    Long itemID();

    String description();

    String photo();

    String color();

    Boolean isNew();

    Boolean hasPieces();

    String material();

    String mainCategory();

    String subCategory();
}
