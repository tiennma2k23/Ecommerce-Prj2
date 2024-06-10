package com.gmail.merikbest2015.ecommerce.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BookRequest {
    private Long id;

    private String bookTitle;

    private String bookr;


    private Integer year;

    private String country;

    private String bookGender;
    private String description;



    private Integer price;

    private String authors;
    private String imageLink; // For image
    private String pdfLink;
    private String filename;
    private String type;
    private String isbn;
    private Integer quantity;

}
