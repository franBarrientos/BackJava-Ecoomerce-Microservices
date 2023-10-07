package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PrivilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPrivilegeRepository extends JpaRepository<PrivilegeEntity, Long> {

    PrivilegeEntity findPrivilegeByName(String name);

}
