package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Application.Repositories.PrivilegeRepository;
import com.ecommerce.Domain.Domain.Privilege;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PrivilegeEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.PrivilegeEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PrivilegeDboRepository implements PrivilegeRepository {

    private final SpringDataPrivilegeRepository dataPrivilegeRepository;
    private final PrivilegeEntityMapper privilegeEntityMapper;

    @Override
    public Optional<Privilege> findPrivilegeByName(String name) {
        PrivilegeEntity privilege = this.dataPrivilegeRepository.findPrivilegeByName(name);
        return privilege != null?
                Optional.of(this.privilegeEntityMapper
                        .toDomain(privilege))
                :
                Optional.empty();
    }

    @Override
    public Privilege save(Privilege privilege) {
        return this.privilegeEntityMapper
                .toDomain(this.dataPrivilegeRepository
                        .save(this.privilegeEntityMapper.toEntity(privilege)));
    }
}
