package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Category {
    String mainCategory();

    String subCategory();

    String catNotes();
}

