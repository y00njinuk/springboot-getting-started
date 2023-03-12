package com.springboot.tutorial;


import com.springboot.tutorial.repository.ProducerRepository;
import com.springboot.tutorial.repository.ProductRepository;
import com.springboot.tutorial.repository.entity.Producer;
import com.springboot.tutorial.repository.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProducerRepositoryTest {
    @Autowired
    ProducerRepository producerRepository;

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    @DisplayName("테스트가 종료되면 데이터베이스에 저장된 데이터를 삭제한다.")
    void tearDown() {
        productRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("연관관계를 가진 엔티티를 조회하였을 때 Join 연산이 정상적으로 수행되어 결과를 반환한다.")
    void relationshipTest() {
        Product product1 = saveProduct("동글펜", 500, 1000);
        Product product2 = saveProduct("네모 공책", 100, 2000);
        Product product3 = saveProduct("지우개", 152, 1234);

        Producer producer1 = saveProducer("falture");
        Producer producer2 = saveProducer("wikibooks");

        producer1.addProduct(product1);
        producer2.addProduct(product2);

        producer2.addProduct(product2);
        producer2.addProduct(product3);

        producerRepository.saveAll(List.of(producer1, producer2));

        System.out.println(producerRepository.findById(1L).get().getProducts());
    }

    // 별도 메소드로 구현을 하여 레포지토리를 사용하게 되면 매번 트랜잭션이 끊기게 된다.
    // 이를 하나의 트랜잭션으로 만들기 위해 테스트 클래스에서 @Transactional 어노테이션을 추가한다.
    private Product saveProduct(String name, Integer price, Integer stock) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);

        return productRepository.save(product);
    }

    private Producer saveProducer(String name) {
        Producer producer = new Producer();
        producer.setName(name);

        return producerRepository.save(producer);
    }
}
