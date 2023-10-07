package com.ecommerce.Domain.Domain;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Privilege {
    private Long id;

    private String name;

    private Collection<Role> roles;


    public Privilege(String name) {
        this.name = name;
    }

}
