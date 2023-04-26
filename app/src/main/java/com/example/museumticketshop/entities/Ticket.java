package com.example.museumticketshop.entities;

public class Ticket {
    private String id;
    private Long price;
    private String ticketType;
    private String date; // can't store LocalDate, don't want to store Date ==> String
    private String userEmail;
    private String exhibitionId;

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public void setDate(String date) {
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

    public String getTicketType() {
        return ticketType;
    }

    public String getDate() {
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

    public Ticket(String id, Long price, String ticketType, String date, String userEmail, String exhibitionId) {
        this.id = id;
        this.price = price;
        this.ticketType = ticketType;
        this.date = date;
        this.userEmail = userEmail;
        this.exhibitionId = exhibitionId;
    }
}
