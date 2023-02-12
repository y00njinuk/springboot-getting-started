package com.springboot.tutorial.dao.impl;

import com.springboot.tutorial.dao.ProductDAO;
import com.springboot.tutorial.repository.ProductRepository;
import com.springboot.tutorial.repository.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ProductDAOImpl implements ProductDAO {

    private final ProductRepository productRepository;

    @Autowired
    public ProductDAOImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product insertProduct(Product product) {
        Product savedProduct = productRepository.save(product);

        return savedProduct;
    }

    /** getById?
     * - 내부적으로 EntityManager 클래스의 getReference() 메서드 호출하며 프록시 객체를 리턴된다.
     * - 실제 쿼리는 프록시 객체를 통해 최초로 데이터에 접근하는 시점에 실행된다.
     * - 데이터가 존재하지 않는 경우 EntityNotFoundException 예외가 발생된다.
     */
    @Override
    public Product selectProduct(Long number) {
        Product selectedProduct = productRepository.getById(number);

        return selectedProduct;
    }

    /** JPA에서 데이터 값을 변경할 때 다른 메서드와 다른 점은?
     * - JPA에서 데이터 값을 변경할 때는 update 키워드를 사용하지 않는다.
     * - 영속성 컨텍스트를 활용하여 값을 갱신한다.
     * - 영속성 컨텍스트를 활용하면 얻을 수 있는 이점은 다음과 같다.
     *  - 동일성(identity) 보장
     *  - 더티 체크(Dirty check)
     *  - 트랜잭션을 지원하는 쓰기 지연(transactional write-behind)
     *  - 지연 로딩(lazy loading)
     */
    @Override
    public Product updateProductName(Long number, String name) throws Exception {
        /** findById 호출 시 다음과 같은 작업을 진행한다.
         * 1. 영속성 컨텍스트에 해당 객체 정보가 있는지 조회
         * 2. 조회 후 없기에 실제 데이터베이스를 조회
         * 3. 조회 결과를 영속성 컨텍스트에 저장
         * 4. 영속성 컨텍스트에 저장된 객체 정보 반환
         */
        Optional<Product> selectedProduct = productRepository.findById(number);
        Product updatedProduct;

        if(selectedProduct.isPresent()) {
            Product product = selectedProduct.get();
            product.setName(name);
            product.setUpdatedAt(LocalDateTime.now());

            /** save 호출 시 다음과 같은 작업을 진행한다.
             * 1. 영속성 컨텍스트에 해당 객체 정보를 조회환다.
             * 2. 객체가 존재한다면 dirty check(변경 감지)를 한다.
             * 3. 변경 감지가 되었다면 변경된 객체에 대하여 flush를 호출한다.
             * 4. 영속성 컨텍스트에 있는 객체의 스냅샷과 변경된 데이터를 비교한다.
             * 5. 변경된 내역이 있으면 해당 객체에 대한 Update 쿼리를 생성한다.
             * 4. 데이터베이스에 변경 내용이 Update 쿼리를 실행함으로써 반영된다.
             */
            updatedProduct = productRepository.save(product);
        } else {
            throw new Exception();
        }

        return updatedProduct;
    }

    @Override
    public void deleteProduct(Long number) throws Exception {
        Optional<Product> selectedProduct = productRepository.findById(number);

        if(selectedProduct.isPresent()) {
            Product product = selectedProduct.get();
            productRepository.delete(product);
        } else {
            throw new Exception();
        }
    }
}
