package com.ecommerce.Domain.Application.Client;

import com.ecommerce.Domain.Application.Dtos.CustomerDTO;

public interface UserClient {

    CustomerDTO getCustomerDTO(Long customerId);

}
