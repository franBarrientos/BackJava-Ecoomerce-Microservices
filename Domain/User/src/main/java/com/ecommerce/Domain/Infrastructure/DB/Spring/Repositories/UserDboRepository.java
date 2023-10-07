package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Application.Repositories.UserRepository;
import com.ecommerce.Domain.Domain.User;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.UserEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.UserEntityMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserDboRepository implements UserRepository {

    private final SpringDataUserRepository userRepository;
    private final UserEntityMapper userEntityMapper;




    @Override
    public Page<User> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserEntity> user = this.userRepository.findById(id);
        return user.isPresent() ?
                Optional.of(userEntityMapper.toDomain(user.get()))
                :
                Optional.empty();

    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> user = this.userRepository.findByEmail(email);
        return user.isPresent() ?
                Optional.of(userEntityMapper.toDomain(user.get()))
                :
                Optional.empty();
    }

    @Override
    public User save(User user) {

        return this.userEntityMapper.toDomain(
                this.userRepository.save(this.userEntityMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findUserIsActive(Long id) {
        Optional<UserEntity> user = this.userRepository.findByIdAndStateIsTrue(id);
        return user.isPresent() ?
                Optional.of(userEntityMapper.toDomain(user.get()))
                :
                Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<UserEntity> userEntity = this.userRepository.findByIdAndStateIsTrue(id);
        if (userEntity.isPresent()) {
            User user = this.userEntityMapper.toDomain(userEntity.get());
            user.setState(false);
            this.userRepository.save(this.userEntityMapper.toEntity(user));
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Optional<User> updateById(Long id, User userToUpdate) {
        Optional<UserEntity> user = this.userRepository.findByIdAndStateIsTrue(id);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        if (userToUpdate.getAge() != null) {
            user.get().setAge(userToUpdate.getAge());
        }
        if (userToUpdate.getCity() != null) {
            user.get().setCity(userToUpdate.getCity());
        }
        if (userToUpdate.getProvince() != null) {
            user.get().setProvince(userToUpdate.getProvince());
        }
        if (userToUpdate.getFirstName() != null) {
            user.get().setFirstName(userToUpdate.getFirstName());
        }
        if (userToUpdate.getLastName() != null) {
            user.get().setLastName(userToUpdate.getLastName());
        }
        return Optional.of(
                this.userEntityMapper.toDomain(
                        this.userRepository.save(user.get())));

    }
}


