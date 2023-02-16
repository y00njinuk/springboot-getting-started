package com.springboot.tutorial;

import com.springboot.tutorial.repository.ProductRepository;
import com.springboot.tutorial.repository.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest // 스프링의 모든 설정을 가져오고 빈 객체도 전체를 스캔한다.
                // 다만, 테스트 속도가 느리므로 다른 방법으로 테스트할 수 있으면 고안하는 것도 나쁘진 않다.
public class ProductRepositoryTest2 {
    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("Repository 클래스의 기본적인 CRUD 기능을 테스트한다.")
    public void basicCRUDTest() {
        // given
        Product givenProduct = Product.builder()
                .name("노트")
                .price(1000)
                .stock(500)
                .build();

        /******************** create ********************/
        // when
        Product savedProduct = productRepository.save(givenProduct);

        // then
        assertThat(savedProduct.getNumber()).isEqualTo(givenProduct.getNumber());
        assertThat(savedProduct.getName()).isEqualTo(givenProduct.getName());
        assertThat(savedProduct.getPrice()).isEqualTo(givenProduct.getPrice());
        assertThat(savedProduct.getStock()).isEqualTo(givenProduct.getStock());

        /******************** read ********************/
        // when
        Product selectedProduct = productRepository.findById(savedProduct.getNumber())
                .orElseThrow(RuntimeException::new);

        // then
        assertThat(selectedProduct.getNumber()).isEqualTo(givenProduct.getNumber());
        assertThat(selectedProduct.getName()).isEqualTo(givenProduct.getName());
        assertThat(selectedProduct.getPrice()).isEqualTo(givenProduct.getPrice());
        assertThat(selectedProduct.getStock()).isEqualTo(givenProduct.getStock());

        /******************** update ********************/
        // when
        Product foundProduct = productRepository.findById(selectedProduct.getNumber())
                .orElseThrow(RuntimeException::new);

        foundProduct.setName("장난감");
        Product updatedProduct = productRepository.save(foundProduct);

        // then
        assertEquals(updatedProduct.getName(), "장난감");

        /******************** delete ********************/
        // when
        productRepository.delete(updatedProduct);

        // then
        assertFalse(productRepository.findById(selectedProduct.getNumber()).isPresent());
    }
}
