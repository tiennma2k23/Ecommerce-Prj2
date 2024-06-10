package com.gmail.merikbest2015.ecommerce.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class VolumeInfo {
    private String title;
    private String subtitle;
    private List<String> authors;
    private String publisher;
    private Date publishedDate;
    private String description;
    private ImageLinks imageLinks; // For image

    // getters and setters
}

