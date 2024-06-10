package com.gmail.merikbest2015.ecommerce.service.impl;

import com.gmail.merikbest2015.ecommerce.domain.Author;
import com.gmail.merikbest2015.ecommerce.repository.AuthorRepository;
import com.gmail.merikbest2015.ecommerce.repository.BookRepository;
import com.gmail.merikbest2015.ecommerce.repository.OrderRepository;
import com.gmail.merikbest2015.ecommerce.constants.ErrorMessage;
import com.gmail.merikbest2015.ecommerce.domain.Order;
import com.gmail.merikbest2015.ecommerce.domain.Book;
import com.gmail.merikbest2015.ecommerce.domain.User;
import com.gmail.merikbest2015.ecommerce.dto.request.OrderRequest;
import com.gmail.merikbest2015.ecommerce.service.OrderService;
import com.gmail.merikbest2015.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;


    @Override
    public Order getOrder(Long orderId) {
        User user = userService.getAuthenticatedUser();
        return orderRepository.getByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND));
    }

    @Override
    public List<Book> getOrdering() {
        User user = userService.getAuthenticatedUser();
        return user.getBookList();
    }

    @Override
    public Page<Order> getUserOrdersList(Pageable pageable) {
        User user = userService.getAuthenticatedUser();
        return orderRepository.findOrderByUserId(user.getId(), pageable);
    }

    @Override
    @Transactional
    public Long postOrder(User user, OrderRequest orderRequest) {
        Order order = modelMapper.map(orderRequest, Order.class);
        order.setUser(user);

        order.getBooks().addAll(user.getBookList());
        orderRepository.save(order);

        order.getBooks().forEach(book -> {
            log.info(book.getBookTitle());
            if (book.getQuantity() <= 0) {
                throw new IllegalArgumentException("Not enough quantity available for book with id: " + book.getId());
            }
            Integer newQuantity= book.getQuantity()-1;
            book.setQuantity(newQuantity);
            bookRepository.save(book);

        });
        order.getBooks().forEach(book -> {
            // Tách tên tác giả từ chuỗi authors
            String[] authorNames = book.getAuthors().split(", ");
            for (String authorName : authorNames) {
                // Tìm hoặc tạo tác giả mới
                Author author = authorRepository.findByName(authorName);
                if (author == null) {
                    author = new Author();
                    author.setName(authorName);
                    authorRepository.save(author);
                }
                // Liên kết tác giả với người dùng
                String currentUsers=author.getUsersId();
                if (currentUsers==null) author.setUsersId(String.valueOf(user.getId()));
                else author.setUsersId(currentUsers+", "+ String.valueOf(user.getId()));
            }
        });
        user.getBookList().clear();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("order", order);
        mailService.sendMessageHtml(order.getEmail(), "Order #" + order.getId(), "order-template", attributes);
        return order.getId();
    }
}
