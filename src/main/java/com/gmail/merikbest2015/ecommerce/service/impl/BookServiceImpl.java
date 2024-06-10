package com.gmail.merikbest2015.ecommerce.service.impl;

import com.gmail.merikbest2015.ecommerce.GoogleBook.GoogleBookFeignClient;
import com.gmail.merikbest2015.ecommerce.constants.ErrorMessage;
import com.gmail.merikbest2015.ecommerce.domain.BookItem;
import com.gmail.merikbest2015.ecommerce.dto.response.GoogleBooksResponse;
import com.gmail.merikbest2015.ecommerce.repository.BookRepository;
import com.gmail.merikbest2015.ecommerce.service.BookService;
import com.gmail.merikbest2015.ecommerce.domain.Book;
import com.gmail.merikbest2015.ecommerce.dto.request.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    @Value("${google.apikey}")
    public String apikey;
    private GoogleBookFeignClient googleBookFeignClient;

    @Override
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.BOOK_NOT_FOUND));
    }

    @Override
    public Book getBookByISBNWithGoogle(String isbn) {
        GoogleBooksResponse googleBooksResponse=googleBookFeignClient.getAllBookByISBN(isbn,apikey);
        if (googleBooksResponse.getTotalItems()==0) return null;
        BookItem bookItem=googleBooksResponse.getItems().get(0);
        Book newBook=new Book();
        newBook.setBookTitle(bookItem.getVolumeInfo().getTitle());
        newBook.setIsbn(isbn);
        newBook.setDescription(bookItem.getVolumeInfo().getDescription());
        newBook.setYear(bookItem.getVolumeInfo().getPublishedDate().getYear());
        newBook.setImageLink(bookItem.getVolumeInfo().getImageLinks().getThumbnail());
        newBook.setPdfLink(bookItem.getAccessInfo().getWebReaderLink());
        return newBook;

//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.BOOK_NOT_FOUND));
    }

    @Override
    public List<Book> getPopularBooks() {
        List<Long> bookIds = Arrays.asList(109L,111L,110L,112L,113L);
        return bookRepository.findByIdIn(bookIds);
    }

    @Override
    public Page<Book> getBooksByFilterParams(SearchRequest request, Pageable pageable) {
        Integer startingPrice = request.getPrice();
        Integer endingPrice = startingPrice + (startingPrice == 0 ? 500 : 50);
        return bookRepository.getBooksByFilterParams(
                request.getBookrs(),
                startingPrice,
                endingPrice,
                pageable);
    }

    @Override
    public Page<Book> searchBooks(SearchRequest request, Pageable pageable) {
        return bookRepository.searchBooks(request.getSearchType(), request.getText(), pageable);
    }

//    @Override
//    public Page<Book> searchBookByTextInPdf(SearchRequest request,Pageable pageable)
//    {
//
//    }

}
