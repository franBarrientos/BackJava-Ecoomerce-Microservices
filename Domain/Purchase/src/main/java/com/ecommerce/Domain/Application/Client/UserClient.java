package com.ecommerce.Domain.Application.Client;

import com.ecommerce.Domain.Application.Dtos.CustomerDTO;

import java.util.List;

public interface UserClient {

    CustomerDTO getCustomerDTO(Long customerId);

    List<CustomerDTO> searchCustomers(Integer dni, String firstName, String lastName);

}
