package com.springboot.tutorial.repository;

import com.springboot.tutorial.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository 상속 시 타입변수는 다음과 같다. <대상엔티티, 기본값타입>
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository에서 구현되어 있는 메소드들을 활용할 수 있다.
    // Question) Override 구현 시에는 어떻게 할까?
}
