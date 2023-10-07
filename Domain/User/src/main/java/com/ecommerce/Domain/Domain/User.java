package com.ecommerce.Domain.Domain;

import lombok.*;

import java.util.Date;
import java.util.Set;
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String city;

    private Integer age;

    private Set<Role> roles;

    private String province;

    private Boolean state;

    private Date createdAt;

    private Date updatedAt;

    private Customer customer;


}
