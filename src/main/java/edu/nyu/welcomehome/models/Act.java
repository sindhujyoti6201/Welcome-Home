package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Act {
    String userName();

    Long roleID();
}
