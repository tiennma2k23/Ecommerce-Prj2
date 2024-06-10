package com.gmail.merikbest2015.ecommerce.GoogleBook;


import com.gmail.merikbest2015.ecommerce.dto.response.GoogleBooksResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@RestController
@FeignClient(
        name="googleBookFeignClient",
        url = "https://www.googleapis.com"
)
public interface GoogleBookFeignClient {
    @GetMapping(value = "books/v1/volumes")
    GoogleBooksResponse getAllBookByISBN(@RequestParam String q, @RequestParam String key);
}
