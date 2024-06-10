package com.gmail.merikbest2015.ecommerce.dto.response;
import com.gmail.merikbest2015.ecommerce.domain.BookItem;
import lombok.Data;

import java.util.List;

@Data
public class GoogleBooksResponse {
    private int totalItems;
    private List<BookItem> items;

    // getters and setters
}

