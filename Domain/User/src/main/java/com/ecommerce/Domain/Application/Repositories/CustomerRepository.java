package com.ecommerce.Domain.Application.Repositories;

import com.ecommerce.Domain.Domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Page<Customer> findAll(Pageable pageable);
    Page<Customer> findAllActiveCustomers(Pageable pageable);

    Optional<Customer> findById(Long id);

    Optional<Customer> findActiveCustomer(Long id);

    Customer save(Customer customer);

    Optional<Customer> findByUserId (Long id);


    List<Customer> searchCustomers(Integer dni, String firstName, String lastName);
}
