package com.springboot.tutorial;

import com.springboot.tutorial.repository.CategoryRepository;
import com.springboot.tutorial.repository.ProductRepository;
import com.springboot.tutorial.repository.entity.Category;
import com.springboot.tutorial.repository.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CategoryRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    /**
     * spring.jpa.hibernate.ddl-auto=create 되어있어서 실행할 때마다
     * 데이터베이스를 초기화하기 때문에 해당 작업은 굳이 하지 않아도 되지만 앞으로 테스트할 때는 필요하다.
     * 테스트 클래스에 @Transactional 사용 시 영속성 컨텍스트를 사용하기 때문에 실제 데이터베이스에 접근하지 않는다.
     * 따라서, 실제 데이터베이스에 접근하고 아래와 같이 테스트 끝나면 데이터를 초기화 해주는 작업을 추가한다.
     * 해당 작업은 Transactional 어노테이션으로 대체가 가능하기 때문에 지금은 Disabled 처리를 한다.
     */
    @AfterEach
    @Disabled
    @DisplayName("테스트가 종료되면 데이터베이스에 저장된 데이터를 삭제한다.")
    void tearDown() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("1:N 연관관계를 가진 엔티티를 조회하였을 때 Join 연산이 정상적으로 수행되어 결과를 반환한다.")
    void relationshipTest() {
        Product product = new Product();
        product.setName("펜");
        product.setPrice(200);
        product.setStock(100);

        productRepository.save(product);

        Category category = new Category();
        category.setCode("S1");
        category.setName("도서");
        category.getProducts().add(product);

        categoryRepository.save(category);

        List<Product> products = categoryRepository
                .findById(1L)
                .get()
                .getProducts();

        assertThat(products).isNotNull();

        for (Product foundProduct : products) {
            System.out.println(foundProduct);
        }
    }
}
