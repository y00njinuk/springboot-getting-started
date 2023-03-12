package com.springboot.tutorial;

import com.springboot.tutorial.repository.ProductRepository;
import com.springboot.tutorial.repository.ProviderRepository;
import com.springboot.tutorial.repository.entity.Product;
import com.springboot.tutorial.repository.entity.Provider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProviderRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProviderRepository providerRepository;

    @AfterEach
    @DisplayName("테스트가 종료되면 데이터베이스에 저장된 데이터를 삭제한다.")
    void tearDown(TestInfo info) {
        // TODO. 영속성 전이 및 데이터베이스 작업시 외래키 제약 조건을 해결할 수 있는 방안 마련
        if(info.getTags().contains("SkipAfter"))
            return;

        // 엔티티에 다른 설정 없이 중간 테이블 삭제 시에는 FK 제약 조건으로 인해 아래와 같은 오류가 발생하게 된다.
        // Cannot delete or update a parent row: a foreign key constraint fails
        productRepository.deleteAll();
        providerRepository.deleteAll();
    }

    @Test
    @Tag("SkipAfter") // AfterEach 작업을 수행하지 않도록 태그를 추가한다.
    @DisplayName("1:N 연관관계를 가진 엔티티를 조회하였을 때 Join 연산이 정상적으로 수행되어 결과를 반환한다.")
    void relationshipTest() {
        Provider provider = new Provider();
        provider.setName("ㅇㅇ물산");

        Product product = new Product();
        product.setName("가위");
        product.setPrice(5000);
        product.setStock(500);

        // 두 작업 중에 어느 것이 실제로 데이터베이스에서 작업이 수행되는 걸까?
        // 외래키를 가지고 있는 엔티티에게 작업을 할당해야만 작업이 실제로 수행이 된다.
        provider.getProduct().add(product);     // 실제로 데이터베이스 적용 X
        product.setProvider(provider);          // 실제로 데이터베이스 적용 O

        providerRepository.save(provider);
        productRepository.save(product);

        assertThat(productRepository.findById(1L).orElseThrow(RuntimeException::new)).isNotNull();
        assertThat(providerRepository.findById(1L).orElseThrow(RuntimeException::new).getProduct()).isNotNull();
    }

    @Test
    @Tag("SkipAfter") // AfterEach 작업을 수행하지 않도록 태그를 추가한다.
    @DisplayName("영속성 전이를 적용하여 1:N 연관관계를 가진 다른 주인이 아닌 엔티티에 대해서도 CRUD 작업이 정상적으로 수행되어야 한다.")
    void cascadeTest() {
        Provider provider = savedProvider("새로운 공급업체");

        Product product1 = savedProduct("상품1", 1000, 1000);
        Product product2 = savedProduct("상품2", 500, 1500);
        Product product3 = savedProduct("상품3", 750, 500);

        product1.setProvider(provider);
        product2.setProvider(provider);
        product3.setProvider(provider);

        provider.getProduct().addAll(List.of(product1, product2, product3));

        // Provider 엔티티를 대상으로 Insert 작업을 수행하였지만 Product 엔티티에도 Insert 작업이 수행된다.
        // Provider 엔티티는 1:N 연관관계에서 주인이 아니지만 영속성 전이로 인해 실제 데이터베이스 작업이 수행된다.
        providerRepository.save(provider);
    }

    private Provider savedProvider(String name) {
        Provider provider = new Provider();
        provider.setName(name);

        return provider;
    }

    @Test
    @Tag("SkipAfter")
    @Transactional
    @DisplayName("고아 객체가 발생하였을 때 연관관계를 가진 다른 엔티티에서도 해당 객체를 삭제할 수 있어야 한다.")
    void orphanRemovalRest() {
        Provider provider = savedProvider("새로운 공급업체");

        Product product1 = savedProduct("상품1", 1000, 1000);
        Product product2 = savedProduct("상품2", 500, 1500);
        Product product3 = savedProduct("상품3", 750, 500);

        product1.setProvider(provider);
        product2.setProvider(provider);
        product3.setProvider(provider);

        provider.getProduct().addAll(List.of(product1, product2, product3));

        providerRepository.saveAndFlush(provider);

        providerRepository.findAll().forEach(System.out::println);
        productRepository.findAll().forEach(System.out::println);

        // Provider 엔티티를 조회하고 해당 엔티티에 대한 Product 정보를 모두 초기화 시킨다.
        Provider foundProvider = providerRepository.findById(1L).get();
        foundProvider.getProduct().clear();

        assertThat(providerRepository.findById(1L).get().getProduct()).isEmpty();

        // Provider 엔티티와 연관관계인 Product 엔티티를 조회하였을 때도 결과가 존재하지 않게 된다.
        // Provider 엔티티에 orphanRemoval = true 설정이 적용되어있기에 가능하다.
        assertThat(productRepository.findAll()).isEmpty();
    }

    private Product savedProduct(String name, Integer price, Integer stock) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);

        return product;
    }
}
