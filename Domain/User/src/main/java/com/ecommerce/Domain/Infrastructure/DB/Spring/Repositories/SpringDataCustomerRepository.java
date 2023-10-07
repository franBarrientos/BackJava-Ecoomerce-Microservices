package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpringDataCustomerRepository extends JpaRepository<CustomerEntity, Long> {
    @Query("SELECT c FROM Customer c WHERE c.user.state = true")
    Page<CustomerEntity> findAllActiveCustomers(Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.user.state = true AND c.id = :id")
    Optional<CustomerEntity> findActiveCustomer(Long id);
    @Query("SELECT c FROM Customer c WHERE c.user.state = true AND c.user.id = :id")
    Optional<CustomerEntity> findActiveCustomerByUserId(Long id);
}
