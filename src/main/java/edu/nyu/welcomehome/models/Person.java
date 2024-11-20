package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Person {
    String userName();

    String password();

    String fname();

    String lname();

    String email();

}
