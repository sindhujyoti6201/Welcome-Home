package edu.nyu.welcomehome.models;

import com.mysql.cj.jdbc.Blob;
import org.immutables.value.Value;

@Value.Immutable
public interface Item {
    Long itemID();

    String description();

    Blob photo();

    String color();

    Boolean isNew();

    Boolean hasPieces();

    String material();

    String mainCategory();

    String subCategory();
}