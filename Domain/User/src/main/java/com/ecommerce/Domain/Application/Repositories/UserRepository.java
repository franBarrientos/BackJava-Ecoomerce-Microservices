package com.ecommerce.Domain.Application.Repositories;

import com.ecommerce.Domain.Domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository {
    Page<User> findAll(Pageable pageable);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    User save (User user);

    Optional<User> findUserIsActive(Long id);

    boolean deleteById(Long id);
    Optional<User> updateById(Long id, User user);


}
