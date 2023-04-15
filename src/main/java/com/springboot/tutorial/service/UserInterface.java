package com.springboot.tutorial.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserInterface {
    Collection<? extends GrantedAuthority> getAuthorities();

    String getUsername();

    String getPassword();

    boolean isAccountNonExpired();

    boolean isAccountNonLocked();

    boolean isCredentialsNonExpired();

    boolean isEnabled();
}
