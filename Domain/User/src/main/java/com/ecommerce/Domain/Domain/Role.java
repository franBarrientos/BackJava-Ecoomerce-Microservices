package com.ecommerce.Domain.Domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    private Long id;

    private String name;

    private List<User> users;

    private List<Privilege> privileges;

    public Role( String name, List<Privilege> privileges) {
        this.setName(name);
        this.setPrivileges(privileges);
    }

}
