package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Application.Repositories.CustomerRepository;
import com.ecommerce.Domain.Domain.Customer;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.CustomerEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.CustomerEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerDboRepository implements CustomerRepository {

    private final SpringDataCustomerRepository customerRepository;
    private final CustomerEntityMapper customerEntityMapper;

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return this.customerRepository
                .findAll(pageable)
                .map(customerEntityMapper::toDomain);
    }

    @Override
    public Page<Customer> findAllActiveCustomers(Pageable pageable) {
        return this.customerRepository
                .findAllActiveCustomers(pageable)
                .map(customerEntityMapper::toDomain);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        Optional<CustomerEntity> customerEntity = this.customerRepository.findById(id);
        return customerEntity.isPresent()?
                Optional.of(
                        this.customerEntityMapper.toDomain(customerEntity.get()))
                :
            Optional.empty();
    }
    public Optional<Customer> findByUserId(Long id) {
        Optional<CustomerEntity> customerEntity = this.customerRepository.findActiveCustomerByUserId(id);
        return customerEntity.isPresent()?
                Optional.of(
                        this.customerEntityMapper.toDomain(customerEntity.get()))
                :
            Optional.empty();
    }

    @Override
    public List<Customer> searchCustomers(Integer dni, String firstName, String lastName) {
        return this.customerRepository
                .searchCustomers(dni, firstName, lastName)
                .stream().map(this.customerEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Customer> findActiveCustomer(Long id) {
        Optional<CustomerEntity> customerEntity = this.customerRepository.findActiveCustomer(id);
        return customerEntity.isPresent()?
                Optional.of(
                        this.customerEntityMapper.toDomain(customerEntity.get()))
                :
                Optional.empty();
    }

    @Override
    public Customer save(Customer customer) {
        return this.customerEntityMapper.toDomain(
                this.customerRepository.save(this.customerEntityMapper.toEntity(customer)));
    }

    
}
