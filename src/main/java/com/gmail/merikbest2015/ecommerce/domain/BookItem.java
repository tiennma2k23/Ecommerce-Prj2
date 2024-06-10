package com.gmail.merikbest2015.ecommerce.domain;

import lombok.Data;

@Data
public class BookItem {
    private VolumeInfo volumeInfo;
    private AccessInfo accessInfo; //For readerLink
}