package com.gmail.merikbest2015.ecommerce.service.impl;

import com.gmail.merikbest2015.ecommerce.constants.SuccessMessage;
import com.gmail.merikbest2015.ecommerce.domain.Author;
import com.gmail.merikbest2015.ecommerce.repository.AuthorRepository;
import com.gmail.merikbest2015.ecommerce.repository.BookRepository;
import com.gmail.merikbest2015.ecommerce.repository.OrderRepository;
import com.gmail.merikbest2015.ecommerce.repository.UserRepository;
import com.gmail.merikbest2015.ecommerce.constants.ErrorMessage;
import com.gmail.merikbest2015.ecommerce.domain.Order;
import com.gmail.merikbest2015.ecommerce.domain.Book;
import com.gmail.merikbest2015.ecommerce.domain.User;
import com.gmail.merikbest2015.ecommerce.dto.request.BookRequest;
import com.gmail.merikbest2015.ecommerce.dto.request.SearchRequest;
import com.gmail.merikbest2015.ecommerce.dto.response.MessageResponse;
import com.gmail.merikbest2015.ecommerce.dto.response.UserInfoResponse;
import com.gmail.merikbest2015.ecommerce.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private final AuthorRepository authorRepository;

    @Override
    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAllByOrderByPriceAsc(pageable);
    }

    @Override
    public Page<Book> searchBooks(SearchRequest request, Pageable pageable) {
        return bookRepository.searchBooks(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUsers(SearchRequest request, Pageable pageable) {
        return userRepository.searchUsers(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.getById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND));
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);

    }

    @Override
    public Page<Order> searchOrders(SearchRequest request, Pageable pageable) {
        return orderRepository.searchOrders(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.BOOK_NOT_FOUND));
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse editBook(BookRequest bookRequest, MultipartFile file) {
        return saveBook(bookRequest, file, SuccessMessage.BOOK_EDITED);
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse addBook(BookRequest bookRequest, MultipartFile file) {
        return saveBook(bookRequest, file, SuccessMessage.BOOK_ADDED);
    }

    @Override
    public UserInfoResponse getUserById(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        Page<Order> orders = orderRepository.findOrderByUserId(userId, pageable);
        return new UserInfoResponse(user, orders);
    }

    private MessageResponse saveBook(BookRequest book, MultipartFile file, String message) throws IOException {
        Book existingBook = bookRepository.findBookByIsbn(book.getIsbn());
        if (message.equals(SuccessMessage.BOOK_ADDED) && existingBook !=null)
        {
            int newQuantity = existingBook.getQuantity() != null ? existingBook.getQuantity() + book.getQuantity() : book.getQuantity();
            existingBook.setQuantity(newQuantity);
            bookRepository.save(existingBook);
//            return new MessageResponse("alert-fail",SuccessMessage.BOOK_EXIST);
        }
        else if (existingBook == null){
            // Nếu chưa tồn tại, thêm mới
            Book newBook = new Book();

            newBook.setAuthors(book.getAuthors());
            newBook.setBookGender(book.getBookGender());
            newBook.setBookTitle(book.getBookTitle());
            newBook.setDescription(book.getDescription());
            newBook.setImageLink(book.getImageLink());
            newBook.setPdfLink(book.getPdfLink());
            newBook.setYear(book.getYear());
            newBook.setFilename(book.getFilename());
            newBook.setBookr(book.getBookr());
            newBook.setCountry(book.getCountry());
            newBook.setIsbn(book.getIsbn());
            newBook.setPrice(book.getPrice());


            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFilename));
                newBook.setFilename(resultFilename);
            }

            log.error(String.valueOf(book));
            bookRepository.save(newBook);
        }
        else {
            existingBook.setAuthors(book.getAuthors());
            existingBook.setBookGender(book.getBookGender());
            existingBook.setBookTitle(book.getBookTitle());
            existingBook.setDescription(book.getDescription());
            existingBook.setImageLink(book.getImageLink());
            existingBook.setPdfLink(book.getPdfLink());
            existingBook.setYear(book.getYear());
            existingBook.setFilename(book.getFilename());
            existingBook.setBookr(book.getBookr());
            existingBook.setCountry(book.getCountry());
            existingBook.setIsbn(book.getIsbn());
            existingBook.setPrice(book.getPrice());


            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFilename));
                existingBook.setFilename(resultFilename);
            }

            log.error(String.valueOf(book));
            bookRepository.save(existingBook);
        }
        String[] authorNames = book.getAuthors().split(", ");
        for (String authorName : authorNames) {
            Author author = authorRepository.findByName(authorName);
            if (author == null) {
                author = new Author();
                author.setName(authorName);
                authorRepository.save(author);
            }
        }
        // Gửi email cho tất cả người dùng quan tâm đến tác giả
        for (String authorName : authorNames) {
            Author author = authorRepository.findByName(authorName);
            if (author.getUsersId()==null) author.setUsersId("");
            if (author != null && author.getUsersId() != null && !author.getUsersId().isEmpty()) {
                String[] userIds=author.getUsersId().split(", ");
                for (String val:userIds)
                {
                    if (val.isEmpty()) continue;
                    Long userId= Long.parseLong(val);
                    Optional<User> user=userRepository.findById(userId);
                    mailService.sendAuthorNewBookEmail(user.get().getEmail(), book.getBookTitle(), authorName);
                }
            }
        }
        return new MessageResponse("alert-success", message);
    }
}
