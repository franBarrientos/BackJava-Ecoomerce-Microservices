package com.ecommerce.Domain.Infrastructure.DB.Spring.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity(name = "Role")
@Setter @Getter
@NoArgsConstructor
@Table(name = "`role`")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<UserEntity> users;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "roleId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilegeId", referencedColumnName = "id"))
    private Collection<PrivilegeEntity> privileges;

    public RoleEntity(String name, Collection<PrivilegeEntity> privilegeEntities) {
        this.setName(name);
        this.setPrivileges(privilegeEntities);
    }
}