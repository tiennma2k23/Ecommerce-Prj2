package com.gmail.merikbest2015.ecommerce.controller;

import com.gmail.merikbest2015.ecommerce.GoogleBook.GoogleBookFeignClient;
import com.gmail.merikbest2015.ecommerce.constants.Pages;
import com.gmail.merikbest2015.ecommerce.domain.BookItem;
import com.gmail.merikbest2015.ecommerce.dto.response.GoogleBooksResponse;
import com.gmail.merikbest2015.ecommerce.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @Value("${google.apikey}")
    public String apikey;

    //    @Autowired
    GoogleBookFeignClient googleBookFeignClient;
    private final BookService bookService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("books", bookService.getPopularBooks());
        return Pages.HOME;
    }

}
