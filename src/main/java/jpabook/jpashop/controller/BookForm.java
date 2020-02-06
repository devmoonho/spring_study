package jpabook.jpashop.controller;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * BookForm
 */

@Getter
@Setter
public class BookForm {

    private Long id;

    @NotEmpty(message = "Not Allowed Empty")
    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}