package edu.nyu.welcomehome.daos;

public record Delivered(String userName, Long orderID, String deliveredStatus, String date) {
}