package com.example.museumticketshop.entities;
public class Exhibition {
    private String id;
    private String name;
    String openFrom;
    private String description;
    private int descriptionLength;
    private int imageResource;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Exhibition() {
    }

    public Exhibition(String id, String name, String openFrom, String description,
                      int descriptionLength, int imageResource) {
        this.id = id;
        this.name = name;
        this.openFrom = openFrom;
        this.description = description;
        this.descriptionLength = descriptionLength;
        this.imageResource = imageResource;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getDescriptionLength() {
        return descriptionLength;
    }

    public void setDescriptionLength(int descriptionLength) {
        this.descriptionLength = descriptionLength;
    }

    public String getOpenFrom() {
        return openFrom;
    }

    public void setOpenFrom(String openFrom) {
        this.openFrom = openFrom;
    }
}
