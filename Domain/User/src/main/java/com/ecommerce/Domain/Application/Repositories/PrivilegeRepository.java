package com.ecommerce.Domain.Application.Repositories;

import com.ecommerce.Domain.Domain.Privilege;

import java.util.Optional;

public interface PrivilegeRepository {
    Optional<Privilege> findPrivilegeByName(String name);

    Privilege save(Privilege privilege);
}
