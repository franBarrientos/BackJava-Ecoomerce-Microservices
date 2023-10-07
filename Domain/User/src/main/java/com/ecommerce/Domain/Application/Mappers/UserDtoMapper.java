package com.ecommerce.Domain.Application.Mappers;


import com.ecommerce.Domain.Application.Dtos.UserDTO;
import com.ecommerce.Domain.Domain.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserDTO toDto(User user);

    User toDomain(UserDTO userDto);
}
