package com.gmail.merikbest2015.ecommerce.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    private List<String> bookrs;
    private List<String> book_gender;
    private Integer price = 0;
    private String searchType;
    private String text;

}
