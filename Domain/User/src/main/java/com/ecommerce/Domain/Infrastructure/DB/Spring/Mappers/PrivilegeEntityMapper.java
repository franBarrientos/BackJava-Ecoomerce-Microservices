package com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers;

import com.ecommerce.Domain.Domain.Privilege;
import com.ecommerce.Domain.Domain.Role;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PrivilegeEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrivilegeEntityMapper {
    Privilege toDomain(PrivilegeEntity privilegeEntity);

    @Mapping(target = "privileges", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role toRole(RoleEntity roleEntity);


    PrivilegeEntity toEntity(Privilege privilege);


    RoleEntity roleToRoleEntity(Role role);


    }
