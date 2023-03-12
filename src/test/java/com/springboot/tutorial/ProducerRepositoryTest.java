package com.springboot.tutorial;


import com.springboot.tutorial.repository.ProducerRepository;
import com.springboot.tutorial.repository.ProductRepository;
import com.springboot.tutorial.repository.entity.Producer;
import com.springboot.tutorial.repository.entity.Product;
import org.junit.jupiter.api.*;
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
    void tearDown(TestInfo info) {
        // TODO. 영속성 전이 및 데이터베이스 작업시 외래키 제약 조건을 해결할 수 있는 방안 마련
        if(info.getTags().contains("SkipAfter"))
            return;

        // 엔티티에 다른 설정 없이 중간 테이블 삭제 시에는 FK 제약 조건으로 인해 아래와 같은 오류가 발생하게 된다.
        // Cannot delete or update a parent row: a foreign key constraint fails
        productRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    @Tag("SkipAfter") // AfterEach 작업을 수행하지 않도록 태그를 추가한다.
    @DisplayName("N:M 단방향 연관관계를 가진 엔티티를 조회하였을 때 Join 연산이 정상적으로 수행되어 결과를 반환한다.")
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

    @Test
    @Tag("SkipAfter") // AfterEach 작업을 수행하지 않도록 태그를 추가한다.
    @DisplayName("N:M 양방향 연관관계를 가진 엔티티를 조회하였을 때 Join 연산이 정상적으로 수행되어 결과를 반환한다.")
    void relationshipTest2() {
        Product product1 = saveProduct("동글펜", 500, 1000);
        Product product2 = saveProduct("네모 공책", 100, 2000);
        Product product3 = saveProduct("지우개", 152, 1234);

        Producer producer1 = saveProducer("falture");
        Producer producer2 = saveProducer("wikibooks");

        producer1.addProduct(product1);
        producer1.addProduct(product2);
        producer2.addProduct(product2);
        producer2.addProduct(product3);

        product1.addProducer(producer1);
        product2.addProducer(producer1);
        product2.addProducer(producer2);
        product3.addProducer(producer2);

        producerRepository.saveAll(List.of(producer1, producer2));
        productRepository.saveAll(List.of(product1, product2, product3));

        System.out.println("products : " + producerRepository.findById(1L).get().getProducts());
        System.out.println("producers : " + productRepository.findById(1L).get().getProducers());
    }

    // 별도 메소드로 구현을 하여 레포지토리를 사용하게 되면 매번 트랜잭션이 끊기게 된다.
    // 이를 하나의 트랜잭션으로 만들기 위해 테스트 클래스에서 @Transactional 어노테이션을 추가하면 된다.
    // 또는 엔티티의 연관관계 칼럼에 대한 Fetch 방식을 eager로 수정하면 된다.
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
