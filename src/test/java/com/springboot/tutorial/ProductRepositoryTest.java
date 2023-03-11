package com.springboot.tutorial;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.tutorial.repository.ProductRepository;
import com.springboot.tutorial.repository.entity.Product;
import com.springboot.tutorial.repository.entity.QProduct;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest // JPA와 관련된 설정만 로드해서 테스트를 진행한다.
             // @Transactional 어노테이션을 포함하고 있어서 테스트 코드 종료 시 데이터베이스가 롤백된다.
             // 기본값으로 임베디드 데이터베이스를 사용한다. 다른 데이터베이스는 별도의 설정을 거쳐서 사용 가능하다.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 해당 설정 적용 시 애플리케이션에서 실제로 사용하는 데이터베이스로 테스트로 변경 가능
@Transactional
public class ProductRepositoryTest {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        List<Product> products = Arrays.asList(
                new Product(1L, "펜", 1000, 100, LocalDateTime.now(), LocalDateTime.now()),
                new Product(2L, "펜", 5000, 300, LocalDateTime.now(), LocalDateTime.now()),
                new Product(3L, "펜", 500, 50, LocalDateTime.now(), LocalDateTime.now())

        );
        productRepository.saveAll(products);
    }

    @Test
    @Transactional
    void saveTest() {
        // given
        Product product = new Product();
        product.setName("TEST");
        product.setPrice(1000);
        product.setStock(1000);

        // when
        Product savedProduct = productRepository.save(product);

        // then
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
        assertEquals(product.getStock(), savedProduct.getStock());
    }

    @Test
    @Transactional
    void selectTest() {
        // given
        Product product = new Product();
        product.setName("TEST");
        product.setPrice(1000);
        product.setStock(1000);

        Product savedProduct = productRepository.saveAndFlush(product);

        // when
        Product foundProduct = productRepository.findById(savedProduct.getNumber()).get();

        // then
        assertEquals(foundProduct.getName(), savedProduct.getName());
        assertEquals(foundProduct.getPrice(), savedProduct.getPrice());
        assertEquals(foundProduct.getStock(), savedProduct.getStock());
    }

    @Test
    @Transactional
    @DisplayName("Sort 객체를 활용하여 매개변수로 받아들인 정렬 기준을 활용한다.")
    void sortingTest() {
        productRepository.findByName("펜", Sort.by((Order.asc("price"))));
        productRepository.findByName("펜", Sort.by(Order.asc("price"), Order.desc("stock")));
    }

    @Test
    @Transactional
    @DisplayName("페이징 처리를 하여 결과를 반환한다.")
    void pagingTest() {
        // of(int page, int size) - 페이지 번호(0부터), 페이지당 데이터 개수, 데이터 정렬 X
        Page<Product> productPage = productRepository.findByName("펜", PageRequest.of(0, 2));
        System.out.println(productPage.get().count()); // 2
    }

    @Test
    @Transactional
    @DisplayName("QueryDSL-JPAQuery을 활용하여 쿼리를 생성하고 결과를 확인한다.")
    void queryDslTest() {
        JPAQuery<Product> query = new JPAQuery<>(entityManager);
        QProduct qProduct = QProduct.product;

        List<Product> productList = query
                .from(qProduct)
                .where(qProduct.name.eq("펜"))
                .orderBy(qProduct.price.asc())
                .fetch();

        printProudctList(productList);
    }

    @Test
    @Transactional
    @DisplayName("QueryDSL-JPAQueryFactory을 활용하여 쿼리를 생성하고 결과를 확인한다.")
    void queryDslTest2() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QProduct qProduct = QProduct.product;

        List<Product> productList = jpaQueryFactory.selectFrom(qProduct)
                .where(qProduct.name.eq("펜"))
                .orderBy(qProduct.price.asc())
                .fetch();

        printProudctList(productList);

        // 전체 칼럼이 아닌 일부 칼럼만을 조회하는 것도 가능하다.

        List<String> productNameList = jpaQueryFactory
                .select(qProduct.name)
                .from(qProduct)
                .where(qProduct.name.eq("펜"))
                .orderBy(qProduct.price.asc())
                .fetch();

        for (String productName : productNameList) {
            System.out.println("----------------------------");
            System.out.println("Product Name : " + productName);
            System.out.println("----------------------------");
        }

        List<Tuple> productTupleList = jpaQueryFactory
                .select(qProduct.name, qProduct.price)
                .from(qProduct)
                .where(qProduct.name.eq("펜"))
                .orderBy(qProduct.price.asc())
                .fetch();

        for (Tuple productTuple : productTupleList) {
            System.out.println("----------------------------");
            System.out.println("Product Name : " + productTuple.get(qProduct.name));
            System.out.println("Product Price : " + productTuple.get(qProduct.price));
            System.out.println("----------------------------");
        }
    }

    private void printProudctList(List<Product> productList) {
        for (Product product : productList) {
            System.out.println("----------------------------");
            System.out.println("Product Number : " + product.getNumber());
            System.out.println("Product Name : " + product.getName());
            System.out.println("Product Price : " + product.getPrice());
            System.out.println("Product Stock : " + product.getStock());
            System.out.println("----------------------------");
        }
    }

}
