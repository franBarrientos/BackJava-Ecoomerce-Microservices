package com.ecommerce.Domain.Application.Services;

import com.ecommerce.Domain.Application.Dtos.CustomerDTO;
import com.ecommerce.Domain.Application.Exceptions.NotFoundException;
import com.ecommerce.Domain.Application.Exceptions.RelationshipAlreadyExist;
import com.ecommerce.Domain.Application.Mappers.CustomerDtoMapper;
import com.ecommerce.Domain.Application.Repositories.CustomerRepository;
import com.ecommerce.Domain.Application.Repositories.UserRepository;
import com.ecommerce.Domain.Domain.Customer;
import com.ecommerce.Domain.Domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerDtoMapper customerDtoMapper;




    public Page<CustomerDTO> getAll(Pageable pageable){
        return this.customerRepository.findAllActiveCustomers(pageable)
                .map(this.customerDtoMapper::toDto);
    }

    public CustomerDTO getById(Long id){
        return this.customerDtoMapper.toDto
                (this.customerRepository
                .findActiveCustomer(id)
                .orElseThrow(() -> new NotFoundException(" customer " + id + " not found")));
    }

    public CustomerDTO createOne(CustomerDTO customer){
        User user = this.userRepository.findUserIsActive(customer.getUser().getId())
                .orElseThrow(() -> new NotFoundException(" user " + customer.getUser() + " not found"));

        if (user.getCustomer() != null) {
            throw new RelationshipAlreadyExist("can not create a relationship one to one already exist!.");
        }

        return this.customerDtoMapper.toDto
                (this.customerRepository.save(this.customerDtoMapper.toDomain(customer)));
    }

    public CustomerDTO updateById(Long id, CustomerDTO customer){
        Customer customerToUpdate = this.customerRepository.findActiveCustomer(id)
                .orElseThrow(() -> new NotFoundException("customer " + id + " not found "));

        if (customer.getDni() != null) {
            customerToUpdate.setDni(customer.getDni());
        }


        if (customer.getAddres() != null) {
            customerToUpdate.setAddres(customer.getAddres());
        }


        if (customer.getPhone() != null) {
            customerToUpdate.setPhone(customer.getPhone());
        }

        return this.customerDtoMapper.toDto
                (this.customerRepository.save(customerToUpdate));

    }


    public CustomerDTO getByUserId(long id) {
        return this.customerDtoMapper.toDto
                (this.customerRepository
                        .findByUserId(id)
                        .orElse(null));
    }
}
