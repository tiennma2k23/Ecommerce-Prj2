package com.gmail.merikbest2015.ecommerce.service;

import com.gmail.merikbest2015.ecommerce.domain.Book;

import java.util.List;

public interface CartService {

    List<Book> getBooksInCart();

    void addBookToCart(Long bookId);

    void removeBookFromCart(Long bookId);
}
