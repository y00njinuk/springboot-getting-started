package com.springboot.tutorial;

import com.springboot.tutorial.repository.ProductRepository;
import com.springboot.tutorial.repository.ProviderRepository;
import com.springboot.tutorial.repository.entity.Product;
import com.springboot.tutorial.repository.entity.Provider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    void tearDown() {
        productRepository.deleteAll();
        providerRepository.deleteAll();
    }

    @Test
    @DisplayName("연관관계를 가지는 엔티티들을 대상으로 테스트 데이터를 생성하여 데이터베이스에 저장한 후 결과를 확인한다.")
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
        productRepository.flush();

        assertThat(productRepository.findById(1L).orElseThrow(RuntimeException::new)).isNotNull();
        assertThat(productRepository.findById(1L).orElseThrow(RuntimeException::new).getProvider()).isNotNull();
    }
}
