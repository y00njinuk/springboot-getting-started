package com.springboot.tutorial.repository;

import com.springboot.tutorial.repository.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

}
