package com.example.museumticketshop.entities;
public class Exhibition {
    private String id;
    private String name;
    private String description;
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

    public Exhibition(String id, String name, String description, int imageResource) {
        this.id = id;
        this.name = name;
        this.description = description;
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
}
