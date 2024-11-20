package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Location {
    String roomNum();

    String shelfNum();

    String shelfDescription();
}
