package com.ecommerce.Domain.Domain;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    private Long id;

    private Integer dni;

    private String addres;

    private String phone;

    private User user;

    private Date createdAt;

    private Date updatedAt;

}
