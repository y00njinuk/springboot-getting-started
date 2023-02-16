package com.springboot.tutorial;

import com.springboot.tutorial.dto.ProductDto;
import com.springboot.tutorial.dto.ProductResponseDto;
import com.springboot.tutorial.repository.ProductRepository;
import com.springboot.tutorial.repository.entity.Product;
import com.springboot.tutorial.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class ProductServiceTest {
    // Mockito.mock()을 사용하는 방식은 스프링 빈에 등록하지 않고 직접 객체를 초기화해서 사용
    private ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUpTest() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    @DisplayName("Mockito를 통하여 ProductRepository 객체를 mock 하여 데이터를 정상적으로 받을 수 있는지 확인한다.")
    void getProductTest() {
        Product givenProduct = new Product();
        givenProduct.setNumber(123L);
        givenProduct.setName("펜");
        givenProduct.setPrice(1000);
        givenProduct.setStock(1234);

        Mockito.when(productRepository.findById(123L))
                .thenReturn(Optional.of(givenProduct));

        ProductResponseDto productResponseDto = productService.getProduct(123L);

        assertEquals(productResponseDto.getNumber(), givenProduct.getNumber());
        assertEquals(productResponseDto.getName(), givenProduct.getName());
        assertEquals(productResponseDto.getPrice(), givenProduct.getPrice());
        assertEquals(productResponseDto.getStock(), givenProduct.getStock());

        verify(productRepository).findById(123L);
    }

    @Test
    @DisplayName("Mockito를 통하여 ProductRepository 객체를 mock 하여 데이터를 정상적으로 저장할 수 있는지 확인한다.")
    void saveProductTest() {
        // any는 Mockito의 ArgumentMatchers에서 제공하는 메서드이다.
        // Mock 객체의 동작을 정의하거나 검증하는 단계에서 조건으로 특정 매개변수의 전달을 설정하지 않고
        // 메서드의 실행만을 확인하거나 좀 더 큰 범위의 클래스 객체를 매개변수로 전달받는 등의 상황에 사용된다.
        Mockito.when(productRepository.save(any(Product.class)))
                .then(returnsFirstArg());

        ProductResponseDto productResponseDto = productService.saveProduct(
                new ProductDto("펜", 1000, 1234));

        assertEquals(productResponseDto.getName(), "펜");
        assertEquals(productResponseDto.getPrice(), 1000);
        assertEquals(productResponseDto.getStock(), 1234);
    }
}
