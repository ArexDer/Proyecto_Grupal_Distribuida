package com.app.app_customers.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
public class LineItemDto {
    private Integer id;
    private Integer quantity;
    private String isbn;
    private String title;
    private BigDecimal price;
    private List<String> authors;
}
