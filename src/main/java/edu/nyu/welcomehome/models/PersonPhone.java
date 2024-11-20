package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface PersonPhone {
    String userName();

    String phone();
}
