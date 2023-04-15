package com.springboot.tutorial.service;

import com.springboot.tutorial.dto.SignInResultDto;
import com.springboot.tutorial.dto.SignUpResultDto;

public interface SignService {

    SignUpResultDto signUp(String id, String password, String name, String role);
    SignInResultDto signin(String id, String password) throws RuntimeException;
}
