package com.gmail.merikbest2015.ecommerce.service.impl;

import com.gmail.merikbest2015.ecommerce.repository.BookRepository;
import com.gmail.merikbest2015.ecommerce.service.UserService;
import com.gmail.merikbest2015.ecommerce.domain.Book;
import com.gmail.merikbest2015.ecommerce.domain.User;
import com.gmail.merikbest2015.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserService userService;
    private final BookRepository bookRepository;

    @Override
    public List<Book> getBooksInCart() {
        User user = userService.getAuthenticatedUser();
        return user.getBookList();
    }

    @Override
    @Transactional
    public void addBookToCart(Long bookId) {
        User user = userService.getAuthenticatedUser();
        Book book = bookRepository.getOne(bookId);
        user.getBookList().add(book);
    }

    @Override
    @Transactional
    public void removeBookFromCart(Long bookId) {
        User user = userService.getAuthenticatedUser();
        Book book = bookRepository.getOne(bookId);
        user.getBookList().remove(book);
    }
}
