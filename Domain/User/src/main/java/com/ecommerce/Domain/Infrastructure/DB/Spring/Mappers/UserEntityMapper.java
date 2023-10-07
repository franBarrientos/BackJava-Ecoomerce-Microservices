package com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers;


import com.ecommerce.Domain.Domain.Privilege;
import com.ecommerce.Domain.Domain.Role;
import com.ecommerce.Domain.Domain.User;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PrivilegeEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.RoleEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    @Mapping(target = "customer.user", ignore = true)
    User toDomain(UserEntity userEntity);

    @Mapping(target = "users", ignore = true)
    Role toRole(RoleEntity roleEntity);

    @Mapping(target = "roles", ignore = true)
    Privilege toPrivilege(PrivilegeEntity privilegeEntity);


    UserEntity toEntity(User user);


}
