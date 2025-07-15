package com.app.app_customers.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class PurchaseOrderDto {
    private String id;
    //private Integer total;
    private BigDecimal total;
    private String status;
    private LocalDateTime placedOn;
    private LocalDateTime deliveredOn;
    private CustomerDto customer;
    private List<LineItemDto> lineItems;
}
