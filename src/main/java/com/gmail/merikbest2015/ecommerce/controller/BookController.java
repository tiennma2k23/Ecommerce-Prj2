package com.gmail.merikbest2015.ecommerce.controller;

import com.gmail.merikbest2015.ecommerce.GoogleBook.GoogleBookFeignClient;
import com.gmail.merikbest2015.ecommerce.constants.Pages;
import com.gmail.merikbest2015.ecommerce.constants.PathConstants;
import com.gmail.merikbest2015.ecommerce.domain.Book;
import com.gmail.merikbest2015.ecommerce.domain.BookItem;
import com.gmail.merikbest2015.ecommerce.dto.request.SearchRequest;
import com.gmail.merikbest2015.ecommerce.dto.response.GoogleBooksResponse;
import com.gmail.merikbest2015.ecommerce.service.BookService;
import com.gmail.merikbest2015.ecommerce.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.BOOK)
public class BookController {


    private final BookService bookService;
    private final ControllerUtils controllerUtils;

    @GetMapping("/{bookId}")
    public String getBookById(@PathVariable Long bookId, Model model) {
        model.addAttribute("book", bookService.getBookById(bookId));
        return Pages.BOOK;
    }

    @GetMapping
    public String getBooksByFilterParams(SearchRequest request, Model model, Pageable pageable) {
        controllerUtils.addPagination(request, model, bookService.getBooksByFilterParams(request, pageable));
        return Pages.BOOKS;
    }

    @GetMapping("/search")
    public String searchBooks(SearchRequest request, Model model, Pageable pageable) {
        controllerUtils.addPagination(request, model, bookService.searchBooks(request, pageable));
        return Pages.BOOKS;
    }

}
