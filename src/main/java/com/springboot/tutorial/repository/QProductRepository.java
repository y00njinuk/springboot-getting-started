package com.springboot.tutorial.repository;

import com.springboot.tutorial.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface QProductRepository extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product> {
    // 대부분의 메서드는 Predicate 매개변수를 받아서 쿼리 결과를 반환해준다.
    // Predicate? 표현식을 작성할 수 있게 QueryDSL에서 제공하는 인터페이스이다.
    // Bean 생성을 위해서는 JpaRepository 인터페이스를 상속받아야 한다. (이유는..?)
    // 단, join이나 fetch 기능은 사용할 수 없다.
}
