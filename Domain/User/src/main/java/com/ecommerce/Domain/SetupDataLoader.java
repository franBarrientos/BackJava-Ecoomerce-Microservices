package com.ecommerce.Domain;

import com.ecommerce.Domain.Application.Repositories.PrivilegeRepository;
import com.ecommerce.Domain.Application.Repositories.RoleRepository;
import com.ecommerce.Domain.Domain.Privilege;
import com.ecommerce.Domain.Domain.Role;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PrivilegeEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.RoleEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.PrivilegeEntityMapper;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.RoleEntityMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final RoleEntityMapper roleEntityMapper;
    private final PrivilegeEntityMapper privilegeEntityMapper;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (this.alreadySetup){
            return;
        }

                    //admin
        PrivilegeEntity readPrivilegeAdminEntity
                = this.createPrivilegeIfNotFound("ADMIN:READ");
        PrivilegeEntity writePrivilegeAdminEntity
                = this.createPrivilegeIfNotFound("ADMIN:WRITE");
        PrivilegeEntity updatePrivilegeAdminEntity
                = this.createPrivilegeIfNotFound("ADMIN:UPDATE");
        PrivilegeEntity deletePrivilegeAdminEntity
                = this.createPrivilegeIfNotFound("ADMIN:DELETE");


        PrivilegeEntity readPrivilegeUserEntity
                = this.createPrivilegeIfNotFound("USER:READ");
        PrivilegeEntity writePrivilegeUserEntity
                = this.createPrivilegeIfNotFound("USER:WRITE");
        PrivilegeEntity updatePrivilegeUserEntity
                = this.createPrivilegeIfNotFound("USER:UPDATE");
        PrivilegeEntity deletePrivilegeUserEntity
                = this.createPrivilegeIfNotFound("USER:DELETE");

        PrivilegeEntity readPrivilegeCustomerEntity
                = this.createPrivilegeIfNotFound("CUSTOMER:READ");
        PrivilegeEntity writePrivilegeCustomerEntity
                = this.createPrivilegeIfNotFound("CUSTOMER:WRITE");
        PrivilegeEntity updatePrivilegeCustomerEntity
                = this.createPrivilegeIfNotFound("CUSTOMER:UPDATE");
        PrivilegeEntity deletePrivilegeCustomerEntity
                = this.createPrivilegeIfNotFound("CUSTOMER:DELETE");


        List<PrivilegeEntity> adminPrivilegeEntities = List.of(
                readPrivilegeAdminEntity,
                writePrivilegeAdminEntity,
                updatePrivilegeAdminEntity,
                deletePrivilegeAdminEntity);

        List<PrivilegeEntity> userPrivilegeEntities = List.of(
                readPrivilegeUserEntity,
                writePrivilegeUserEntity,
                updatePrivilegeUserEntity,
                deletePrivilegeUserEntity);

        List<PrivilegeEntity> customerPrivilegeEntities = List.of(
                readPrivilegeCustomerEntity,
                writePrivilegeCustomerEntity,
                updatePrivilegeCustomerEntity,
                deletePrivilegeCustomerEntity);

        this.createRoleIfNotFound("ROLE_ADMIN", adminPrivilegeEntities);
        this.createRoleIfNotFound("ROLE_USER", userPrivilegeEntities);
        this.createRoleIfNotFound("ROLE_CUSTOMER", customerPrivilegeEntities);

        this.alreadySetup = true;
    }


    @Transactional
    public PrivilegeEntity createPrivilegeIfNotFound(String name) {

        Optional<Privilege> privilegeEntity = this.privilegeRepository
                .findPrivilegeByName(name);

        if (privilegeEntity.isEmpty()) {
            Privilege privilegeNew = new Privilege(name);
            return this.privilegeEntityMapper
                    .toEntity(this.privilegeRepository.save(privilegeNew));
        } else {
            return this.privilegeEntityMapper
                    .toEntity(privilegeEntity.get());
        }

    }


    @Transactional
    public RoleEntity createRoleIfNotFound(
            String name, Collection<PrivilegeEntity> privilegeEntities) {

        Optional<Role> role = this.roleRepository.findRoleByName(name);

        if (role.isEmpty()) {
            Role roleNew = new Role(name, privilegeEntities
                    .stream()
                    .map(this.privilegeEntityMapper::toDomain)
                    .collect(Collectors.toList())
            );
            return this.roleEntityMapper
                    .toEntity(this.roleRepository.save(roleNew));
        } else {
            return this.roleEntityMapper.toEntity(role.get());
        }
    }
}



