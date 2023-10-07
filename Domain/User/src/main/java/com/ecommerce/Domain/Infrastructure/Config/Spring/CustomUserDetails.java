package com.ecommerce.Domain.Infrastructure.Config.Spring;

import com.ecommerce.Domain.Domain.Privilege;
import com.ecommerce.Domain.Domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> rolesAndPrivileges =
                this.user.getRoles().stream().flatMap(role -> Stream.concat(
                                Stream.of(role.getName()),
                                role.getPrivileges().stream().map(Privilege::getName)))
                        .collect(Collectors.toList());

        return rolesAndPrivileges.stream().map(
                role -> new SimpleGrantedAuthority(role)
        ).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getState();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.getState();
    }

}
