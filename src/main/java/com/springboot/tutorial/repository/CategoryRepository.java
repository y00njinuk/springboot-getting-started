package com.springboot.tutorial.repository;

import com.springboot.tutorial.repository.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
