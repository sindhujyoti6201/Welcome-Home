package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface DonatedBy {
    Long itemID();

    String userName();

    String donateDate();
}
