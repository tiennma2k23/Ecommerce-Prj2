package com.gmail.merikbest2015.ecommerce.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq")
    @SequenceGenerator(name = "book_id_seq", sequenceName = "book_id_seq", initialValue = 109, allocationSize = 1)
    private Long id;
    private String isbn;
    @Column(name = "book_title", nullable = true)
    private String bookTitle;

    @Column(name = "bookr", nullable = true)
    private String bookr;

    @Column(name = "year", nullable = true)
    private Integer year;

    @Column(name = "country", nullable = true)
    private String country;

    @Column(name = "book_gender", nullable = true)
    private String bookGender;

    @Column(name = "description")
    private String description;

    @Column(name = "filename")
    private String filename;

    @Column(name = "price", nullable = true)
    private Integer price;
    @Column(name = "authors")
    private String authors;
    private String imageLink; // For image
    private String pdfLink;

    @Column(name = "type", nullable = true)
    private String type;

    @Column(name = "quantity", nullable = true)
    private Integer quantity;
}
