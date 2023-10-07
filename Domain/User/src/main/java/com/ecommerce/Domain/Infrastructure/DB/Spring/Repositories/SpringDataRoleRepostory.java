package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRoleRepostory extends JpaRepository<RoleEntity, Long> {
    RoleEntity findRoleByName(String name);
}
