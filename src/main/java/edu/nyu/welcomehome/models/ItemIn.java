package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface ItemIn {
    Long itemID();

    Long orderID();

    Boolean found();
}
