package com.springboot.tutorial;

import com.querydsl.core.types.Predicate;
import com.springboot.tutorial.repository.QProductRepository;
import com.springboot.tutorial.repository.entity.Product;
import com.springboot.tutorial.repository.entity.QProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class QProductRepositoryTest {
    @Autowired
    QProductRepository qProductRepository;

    @Test
    @DisplayName("QueryDSL의 Predicate 를 통해 쿼리를 생성하고 결과를 반환한다.")
    public void queryDSLTest1() {
        Predicate predicate = QProduct.product.name
                .containsIgnoreCase("펜")
                .and(QProduct.product.price.between(1000, 25000));

        Optional<Product> foundProduct = qProductRepository.findOne(predicate);

        if(foundProduct.isPresent()) {
            Product product = foundProduct.get();
            System.out.println(product.getName());
            System.out.println(product.getNumber());
            System.out.println(product.getPrice());
            System.out.println(product.getStock());
        }
    }
}
