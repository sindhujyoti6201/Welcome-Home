package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Delivered {
    String userName();

    Long orderID();

    String status();

    String date();
}
