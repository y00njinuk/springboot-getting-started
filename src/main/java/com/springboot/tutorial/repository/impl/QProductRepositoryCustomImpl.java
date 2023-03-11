package com.springboot.tutorial.repository.impl;

import com.springboot.tutorial.repository.QProductRepositoryCustom;
import com.springboot.tutorial.repository.entity.Product;
import com.springboot.tutorial.repository.entity.QProduct;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.List;

// QuerydslRepositorySupport 추상 클래스로서 상속을 받게 되면 QueryDSL의 다양한 기능을 제공한다.
// QuerydslRepositorySupport 상속받으면 생성자를 통해 도메인 클래스를 상위 클래스에서 전달해야 한다.
// 대표적인 사용방법은 아래와 같이 Custom한 Repository의 인터페이스를 직접 구현한다.
@Component
public class QProductRepositoryCustomImpl
        extends QuerydslRepositorySupport implements QProductRepositoryCustom {
    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     */
    public QProductRepositoryCustomImpl() {
        super(QProduct.class);
    }

    /**
     * Q도메인과 QuerydslRepositorySupport에서 제공하는 기능을 사용하여 쿼리 생성 후 결과를 반환한다.
     *
     * @param name
     * @return
     */
    @Override
    public List<Product> findByName(String name) {
        QProduct product = QProduct.product;

        // from 메서드에서 JPAQuery를 반환하여 쿼리를 생성할 수 있게 된다.
        List<Product> productList = from(product)
                .where(product.name.eq(name))
                .select(product)
                .fetch();

        return productList;
    }
}
