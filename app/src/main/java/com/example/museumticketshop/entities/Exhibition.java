package com.example.museumticketshop.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Exhibition {
    private Long id;
    private String name;
    private String description;
    private String picture;
}
