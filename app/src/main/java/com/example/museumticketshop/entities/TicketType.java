package com.example.museumticketshop.entities;
public enum TicketType {
    FULL_PRICE("full price"),
    HALF_PRICE("half_price");
    private final String type;
    TicketType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
