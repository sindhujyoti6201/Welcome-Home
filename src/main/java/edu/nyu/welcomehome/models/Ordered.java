package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Ordered {
    Long orderID();

    String orderDate();

    String orderNotes();

    String supervisor();

    String client();
}
