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
public interface RoleEntityMapper {


    Role toDomain(RoleEntity entity);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "customer", ignore = true)
    User toUser(UserEntity userEntity);

    @Mapping(target = "roles", ignore = true)
    Privilege toPrivilege(PrivilegeEntity privilegeEntity);


    RoleEntity toEntity(Role domain);

}
