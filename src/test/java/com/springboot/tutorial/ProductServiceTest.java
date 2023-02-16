package com.springboot.tutorial;

import com.springboot.tutorial.dto.ProductResponseDto;
import com.springboot.tutorial.repository.ProductRepository;
import com.springboot.tutorial.repository.entity.Product;
import com.springboot.tutorial.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class ProductServiceTest {
    private ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUpTest() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
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
}
