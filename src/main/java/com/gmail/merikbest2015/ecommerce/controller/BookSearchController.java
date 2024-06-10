package com.gmail.merikbest2015.ecommerce.controller;
import com.gmail.merikbest2015.ecommerce.GoogleBook.GoogleBookFeignClient;
import com.gmail.merikbest2015.ecommerce.domain.Book;
import com.gmail.merikbest2015.ecommerce.domain.BookItem;
import com.gmail.merikbest2015.ecommerce.dto.response.GoogleBooksResponse;
import com.gmail.merikbest2015.ecommerce.repository.BookRepository;
import com.gmail.merikbest2015.ecommerce.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController
@Slf4j
public class BookSearchController {
    @Value("${google.apikey}")
    public String apikey;

//    @Autowired
    GoogleBookFeignClient googleBookFeignClient;

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/book-search/book/v1")
    BookItem getBookByISBN(@RequestParam String q) {
        GoogleBooksResponse books = googleBookFeignClient.getAllBookByISBN(q, apikey);
        return books.getItems().get(0);
    }

    @GetMapping("/search/book")
    @ResponseBody
    public ResponseEntity<Book> searchBookByIsbn(@RequestParam("isbn") String isbn) {
        Book book = bookRepository.findBookByIsbn(isbn);
        if (book != null) {
            return ResponseEntity.ok().body(book);
        } else {
            return null;
        }
    }
}
