package edu.nyu.welcomehome.models;

import org.immutables.value.Value;

@Value.Immutable
public interface OrderDTO {
    Integer orderID();
    String orderDate();
    String status();
    String supervisor();
    String client();
    Integer itemCount();
    Boolean canUpdate();
}