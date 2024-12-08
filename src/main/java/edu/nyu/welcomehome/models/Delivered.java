package edu.nyu.welcomehome.models;

public class Delivered {
    private String userName;
    private Long orderID;
    private String deliveredStatus;
    private String date;

    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Long getOrderID() { return orderID; }
    public void setOrderID(Long orderID) { this.orderID = orderID; }

    public String getDeliveredStatus() { return deliveredStatus; }
    public void setDeliveredStatus(String deliveredStatus) { this.deliveredStatus = deliveredStatus; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}