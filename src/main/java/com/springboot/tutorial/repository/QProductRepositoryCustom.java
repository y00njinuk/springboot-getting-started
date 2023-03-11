package com.springboot.tutorial.repository;

import com.springboot.tutorial.repository.entity.Product;

import java.util.List;

// QueryDSL을 활용하여 직접 쿼리를 구현하기 위해서 JpaRepository 인터페이스를 상속받지 않는다.
public interface QProductRepositoryCustom {
    List<Product> findByName(String name);
}
