package com.app.app_customers.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerDto {
    private Integer id;
    private String name;
    private String email;
    private Integer version;
}
