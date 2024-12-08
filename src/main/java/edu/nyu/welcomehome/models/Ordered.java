package edu.nyu.welcomehome.models;

public class Ordered {
    private Long orderID;
    private String orderDate;
    private String orderNotes;
    private String supervisor;
    private String client;
    private String orderStatus;

    // Getters and Setters
    public Long getOrderID() { return orderID; }
    public void setOrderID(Long orderID) { this.orderID = orderID; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getOrderNotes() { return orderNotes; }
    public void setOrderNotes(String orderNotes) { this.orderNotes = orderNotes; }

    public String getSupervisor() { return supervisor; }
    public void setSupervisor(String supervisor) { this.supervisor = supervisor; }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
}
