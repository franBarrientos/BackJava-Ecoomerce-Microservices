package com.ecommerce.Domain.Application.Repositories;

import com.ecommerce.Domain.Domain.Role;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findRoleByName(String name);
    Role save(Role product);
}
