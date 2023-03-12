package com.springboot.tutorial.repository;

import com.springboot.tutorial.repository.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer,Long> {

}
