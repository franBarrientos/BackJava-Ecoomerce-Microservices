package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Application.Repositories.RoleRepository;
import com.ecommerce.Domain.Domain.Role;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.RoleEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.RoleEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleDboRepository implements RoleRepository {

    private final SpringDataRoleRepostory dataRoleRepository;
    public final RoleEntityMapper roleEntityMapper;

    @Override
    public Optional<Role> findRoleByName(String name) {
        RoleEntity role = this.dataRoleRepository.findRoleByName(name);
        return role != null?
                Optional.of(this.roleEntityMapper.toDomain(role))
                :
                Optional.empty();
    }

    @Override
    public Role save(Role product) {
        return this.roleEntityMapper
                .toDomain(this.dataRoleRepository
                        .save(this.roleEntityMapper
                                .toEntity(product)));
    }
}
