package com.gmail.merikbest2015.ecommerce.dto.request;

import com.gmail.merikbest2015.ecommerce.constants.ErrorMessage;
import com.gmail.merikbest2015.ecommerce.domain.Book;
import lombok.Data;

import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderRequest {
    private Long id;
    private Double totalPrice;
    private LocalDateTime date = LocalDateTime.now();

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String firstName;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String lastName;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String city;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String address;

    @Email(message = ErrorMessage.INCORRECT_EMAIL)
    @NotBlank(message = ErrorMessage.EMAIL_CANNOT_BE_EMPTY)
    private String email;

    @NotBlank(message = ErrorMessage.EMPTY_PHONE_NUMBER)
    private String phoneNumber;

    @ManyToMany
    private List<Book> books = new ArrayList<>();


}
