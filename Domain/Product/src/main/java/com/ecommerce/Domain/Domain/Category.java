package com.ecommerce.Domain.Domain;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Category {
    private Long id;

    private String name;

    private String img;

    private Date createdAt;

    private Date updatedAt;

    private Boolean state;
}
