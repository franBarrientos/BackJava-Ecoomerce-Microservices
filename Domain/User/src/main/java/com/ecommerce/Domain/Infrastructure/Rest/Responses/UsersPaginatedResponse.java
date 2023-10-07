package com.ecommerce.Domain.Infrastructure.Rest.Responses;

import com.ecommerce.Domain.Application.Dtos.UserDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class UsersPaginatedResponse extends PaginatedResponseBase {
    private List<UserDTO> users;

}
