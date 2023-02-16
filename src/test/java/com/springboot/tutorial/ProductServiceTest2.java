package com.springboot.tutorial;

import com.springboot.tutorial.dto.ProductDto;
import com.springboot.tutorial.dto.ProductResponseDto;
import com.springboot.tutorial.repository.ProductRepository;
import com.springboot.tutorial.repository.entity.Product;
import com.springboot.tutorial.service.ProductService;
import com.springboot.tutorial.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class) // JUnit5의 테스트에서 스프링 테스트 컨텍스트를 사용하도록 설정
                                   // Jupiter 테스트에 스프링 테스트 컨텍스트 프레임워크를 통합
@Import({ProductServiceImpl.class})
@SpringBootTest
public class ProductServiceTest2 {

    @MockBean // 스프링에서 제공하는 테스트 어노테이션을 통해 Mock 객체를 생성하고 의존성을 주입한다.
              // @MockBean은 스프링에 Mock 객체를 등록해서 주입하는 형식이다.
    ProductRepository productRepository;

    @Autowired // Import 어노테이션으로 선언된 클래스와 매핑하여 productService에 의존성 주입
    ProductService productService;

    @Test
    public void getProductTest() {
        // given
        Product givenProduct = new Product();
        givenProduct.setNumber(123L);
        givenProduct.setName("펜");
        givenProduct.setPrice(1000);
        givenProduct.setStock(1234);

        Mockito.when(productRepository.findById(123L))
                .thenReturn(Optional.of(givenProduct));

        // when
        ProductResponseDto productResponseDto = productService.getProduct(123L);

        // then
        Assertions.assertEquals(productResponseDto.getNumber(), givenProduct.getNumber());
        Assertions.assertEquals(productResponseDto.getName(), givenProduct.getName());
        Assertions.assertEquals(productResponseDto.getPrice(), givenProduct.getPrice());
        Assertions.assertEquals(productResponseDto.getStock(), givenProduct.getStock());

        verify(productRepository).findById(123L);
    }

    @Test
    void saveProductTest() {
        // given
        Mockito.when(productRepository.save(any(Product.class)))
                .then(returnsFirstArg());

        // when
        ProductResponseDto productResponseDto = productService.saveProduct(
                new ProductDto("펜", 1000, 1234));

        // then
        Assertions.assertEquals(productResponseDto.getName(), "펜");
        Assertions.assertEquals(productResponseDto.getPrice(), 1000);
        Assertions.assertEquals(productResponseDto.getStock(), 1234);

        verify(productRepository).save(any());
    }

}