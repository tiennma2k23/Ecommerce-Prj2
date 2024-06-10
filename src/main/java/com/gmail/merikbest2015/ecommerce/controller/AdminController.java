package com.gmail.merikbest2015.ecommerce.controller;

import com.gmail.merikbest2015.ecommerce.constants.Pages;
import com.gmail.merikbest2015.ecommerce.constants.PathConstants;
import com.gmail.merikbest2015.ecommerce.dto.request.BookRequest;
import com.gmail.merikbest2015.ecommerce.dto.request.SearchRequest;
import com.gmail.merikbest2015.ecommerce.dto.response.UserInfoResponse;
import com.gmail.merikbest2015.ecommerce.service.AdminService;
import com.gmail.merikbest2015.ecommerce.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(PathConstants.ADMIN)
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ControllerUtils controllerUtils;

    @GetMapping("/books")
    public String getBooks(Pageable pageable, Model model) {
        controllerUtils.addPagination(model, adminService.getBooks(pageable));
        return Pages.ADMIN_BOOKS;
    }

    @GetMapping("/books/search")
    public String searchBooks(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchBooks(request, pageable));
        return Pages.ADMIN_BOOKS;
    }

    @GetMapping("/users")
    public String getUsers(Pageable pageable, Model model) {
        controllerUtils.addPagination(model, adminService.getUsers(pageable));
        return Pages.ADMIN_ALL_USERS;
    }

    @GetMapping("/users/search")
    public String searchUsers(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchUsers(request, pageable));
        return Pages.ADMIN_ALL_USERS;
    }

    @GetMapping("/order/{orderId}")
    public String getOrder(@PathVariable Long orderId, Model model) {
        model.addAttribute("order", adminService.getOrder(orderId));
        return Pages.ORDER;
    }

    @GetMapping("/orders")
    public String getOrders(Pageable pageable, Model model) {
        controllerUtils.addPagination(model, adminService.getOrders(pageable));
        return Pages.ORDERS;
    }

    @GetMapping("/orders/search")
    public String searchOrders(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchOrders(request, pageable));
        return Pages.ORDERS;
    }

    @GetMapping("/book/{bookId}")
    public String getBook(@PathVariable Long bookId, Model model) {
        model.addAttribute("book", adminService.getBookById(bookId));
        return Pages.ADMIN_EDIT_BOOK;
    }

    @PostMapping("/edit/book")
    public String editBook(@Valid BookRequest book, BindingResult bindingResult, Model model,
                              @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (controllerUtils.validateInputFields(bindingResult, model, "book", book)) {
            return Pages.ADMIN_EDIT_BOOK;
        }
        return controllerUtils.setAlertFlashMessage(attributes, "/admin/books", adminService.editBook(book, file));
    }

    @GetMapping("/add/book")
    public String addBook() {
        return Pages.ADMIN_ADD_BOOK;
    }

    @PostMapping("/add/book")
    public String addBook(@Valid BookRequest book, BindingResult bindingResult, Model model,
                             @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (controllerUtils.validateInputFields(bindingResult, model, "book", book)) {
            return Pages.ADMIN_ADD_BOOK;
        }
        return controllerUtils.setAlertFlashMessage(attributes, "/admin/books", adminService.addBook(book, file));
    }

    @GetMapping("/user/{userId}")
    public String getUserById(@PathVariable Long userId, Model model, Pageable pageable) {
        UserInfoResponse userResponse = adminService.getUserById(userId, pageable);
        model.addAttribute("user", userResponse.getUser());
        controllerUtils.addPagination(model, userResponse.getOrders());
        return Pages.ADMIN_USER_DETAIL;
    }
}
