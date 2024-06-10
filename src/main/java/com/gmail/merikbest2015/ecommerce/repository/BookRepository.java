package com.gmail.merikbest2015.ecommerce.repository;

import com.gmail.merikbest2015.ecommerce.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByIdIn(List<Long> booksIds);

    Book findBookByIsbn(String ISBN);

    Book findByBookTitle(String bookTitle);


    Page<Book> findAllByOrderByPriceAsc(Pageable pageable);

    @Query("SELECT book FROM Book book WHERE " +
            "(CASE " +
            "   WHEN :searchType = 'bookTitle' THEN UPPER(book.bookTitle) " +
            "   WHEN :searchType = 'description' THEN UPPER(book.description) " +
            "   ELSE UPPER(book.bookr) " +
            "END) " +
            "LIKE UPPER(CONCAT('%',:text,'%')) " +
            "ORDER BY book.price ASC")
    Page<Book> searchBooks(String searchType, String text, Pageable pageable);

    @Query("SELECT book FROM Book book " +
            "WHERE (coalesce(:bookrs, null) IS NULL OR book.bookr IN :bookrs) " +
            "AND (coalesce(:priceStart, null) IS NULL OR book.price BETWEEN :priceStart AND :priceEnd) " +
            "ORDER BY book.price ASC")
    Page<Book> getBooksByFilterParams(
            List<String> bookrs,
            Integer priceStart,
            Integer priceEnd,
            Pageable pageable);

}
