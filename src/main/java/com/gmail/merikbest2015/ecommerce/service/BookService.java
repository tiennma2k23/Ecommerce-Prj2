package com.gmail.merikbest2015.ecommerce.service;

import com.gmail.merikbest2015.ecommerce.domain.Book;
import com.gmail.merikbest2015.ecommerce.dto.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    Book getBookById(Long bookId);

    Book getBookByISBNWithGoogle(String isbn);

    List<Book> getPopularBooks();

    Page<Book> getBooksByFilterParams(SearchRequest searchRequest, Pageable pageable);

    Page<Book> searchBooks(SearchRequest searchRequest, Pageable pageable);
}
