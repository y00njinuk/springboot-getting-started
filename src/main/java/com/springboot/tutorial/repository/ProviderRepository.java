package com.springboot.tutorial.repository;

import com.springboot.tutorial.repository.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

}
