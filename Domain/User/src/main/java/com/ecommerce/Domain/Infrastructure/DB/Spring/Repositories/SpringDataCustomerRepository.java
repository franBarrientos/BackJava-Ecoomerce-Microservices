package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataCustomerRepository extends JpaRepository<CustomerEntity, Long> {
    @Query("SELECT c FROM Customer c WHERE c.user.state = true")
    Page<CustomerEntity> findAllActiveCustomers(Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.user.state = true AND c.id = :id")
    Optional<CustomerEntity> findActiveCustomer(@Param("id") Long id);

    @Query("SELECT c FROM Customer c WHERE c.user.state = true AND c.user.id = :id")
    Optional<CustomerEntity> findActiveCustomerByUserId(@Param("id") Long id);

    @Query("SELECT c FROM Customer c " +
            "WHERE c.dni = :dni " +
            "OR c.user.firstName LIKE %:firstName% " +
            "OR c.user.lastName LIKE %:lastName%")
    List<CustomerEntity> searchCustomers(@Param("dni") Integer dni,@Param("firstName") String firstName,@Param("lastName") String lastName);


}
