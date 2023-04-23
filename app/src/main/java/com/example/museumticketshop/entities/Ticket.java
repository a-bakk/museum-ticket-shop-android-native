package com.example.museumticketshop.entities;

import java.util.Date;

public class Ticket {
    private String id;
    private Long price;
    private TicketType ticketType;
    private Date date;
    private String userEmail;
    private String exhibitionId;

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setExhibitionId(String exhibitionId) {
        this.exhibitionId = exhibitionId;
    }

    public String getId() {
        return id;
    }

    public Long getPrice() {
        return price;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public Date getDate() {
        return date;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getExhibitionId() {
        return exhibitionId;
    }

    public Ticket() {
    }

    public Ticket(String id, Long price, TicketType ticketType, Date date, String userEmail, String exhibitionId) {
        this.id = id;
        this.price = price;
        this.ticketType = ticketType;
        this.date = date;
        this.userEmail = userEmail;
        this.exhibitionId = exhibitionId;
    }
}
