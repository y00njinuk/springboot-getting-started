package com.springboot.tutorial.repository;

import com.springboot.tutorial.repository.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// JpaRepository 상속 시 타입변수는 다음과 같다. <대상엔티티, 기본값타입>
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository에서 구현되어 있는 메소드들을 활용할 수 있다.
    // Question) Override 구현 시에는 어떻게 할까?

    /**
     *  쿼리 메서드에는 크게 동작을 결정하는 주제(Subject)와 서술어(Predicate)로 구분한다.
     *  'By' 는 서술어의 시작을 나타내는 구분자 역할을 한다. 서술어 부분은 검색 및 정렬 조건을 지정하는 영역이다.
     *
     *  (리턴타입) + {주제 + 서술어(속성)} 구조
     */

    // 1. find..by - 조회하는 기능을 수행한다.
    Optional<Product> findByNumber(Long number);
    List<Product> findAllByName(String name);
    Product queryByNumber(Long number);

    // 2. exists..by - 특정 데이터가 존재하는지 확인한다.
    boolean existsByNumber(Long number);

    // 3. count..by - 조회 쿼리를 수행한 후 쿼리 결과로 나온 레코드의 개수를 반환한다.
    long countByName(String name);

    // 4. delete..by, remove..by - 삭제 쿼리를 수행한다. 리턴 타입이 없거나 삭제한 횟수를 반환한다.
    void deleteByNumber(Long number);
    long removeByName(String name);

    // 5. ..First<number>..., ..Top<number>.. - 쿼리를 통해 조회된 결과값의 개수를 제한한다.
    List<Product> findFirst5ByName(String name);
    List<Product> findTop10ByName(String name);

    // 6. Is - 값의 일치를 조건으로 사용하는 조건자 키워드이다.
    Product findByNumberIs(Long number);        // findByNumber와 동일한 동작을 함
    Product findByNumberEquals(Long number);

    // 7. (Is)Not - 값의 불일치를 조건으로 사용하는 조건자 키워드이다.
    Product findByNumberIsNot(Long number);
    Product findByNumberNot(Long number);

    // 8. (Is)Null, (Is)NotNull - 값이 Null인지 검사한ㄷ,.
    List<Product> findByUpdatedAtNull();
    List<Product> findByUpdatedAtIsNull();
    List<Product> findByUpdatedAtNotNull();
    List<Product> findByUpdatedAtIsNotNull();

    // 9. (Is)True, (Is)False - boolean 타입으로 지정된 칼럼값을 확인한다. 
    // 현재 Product에는 관련 컬럼이 없기 때문에 구현은 생략
    
    // 10. And, Or - 여러 조건을 묶을 때 사용한다.
    Product findByNumberAndName(Long number, String name);
    Product findByNumberOrName(Long number, String name);

    // 11. (Is)GreaterThean, (Is)LessThan, (Is)Between
    // 숫자나 datetime 칼럼을 대상으로 한 비교 연산에 사용할 수 있다.
    // Is는 equals를 포함하였으며 경계값을 포함하는 지의 여부에 따라 사용하면 된다.
    List<Product> findByPriceIsGreaterThan(Long price);
    List<Product> findByPriceGreaterThan(Long price);
    List<Product> findByPriceGreaterThanEqual(Long pirce);
    List<Product> findByPriceIsLessThan(Long price);
    List<Product> findByPriceLessThan(Long price);
    List<Product> findByPriceLessThanEqual(Long price);
    List<Product> findByPriceIsBetween(Long lowPrice, Long highPrice);
    List<Product> findByPriceBetween(Long lowPrice, Long highPrice);

    // 12. (Is)StartingWith, (Is)EndingWith, (Is)Containing, (Is)Like
    // 칼럼값에서 일부 일치 여부를 확인하는 조건자 키워드로, SQL 쿼리문에서 '%' 연산자의 역할과 동일하다.
    List<Product> findByNameLike(String name);
    List<Product> findByNameIsLike(String name);

    List<Product> findByNameContains(String name);
    List<Product> findByNameContaining(String name);
    List<Product> findByNameIsContaining(String name);

    List<Product> findByNameStartsWith(String name);
    List<Product> findByNameStartingWith(String name);
    List<Product> findByNameIsStartingWith(String name);

    List<Product> findByNameEndsWith(String name);
    List<Product> findByNameEndingWith(String name);
    List<Product> findByNameIsEndingWith(String name);

    // 13. Order..by - 특정 컬럼을 기준으로 정렬한다.
    List<Product> findByNameOrderByNumberAsc(String name);
    List<Product> findByNameOrderByNumberDesc(String name);
    List<Product> findByNameOrderByPriceAscStockDesc(String name);

    // 14. 매개 변수를 활용하여 정렬을 할 수 있다.
    List<Product> findByName(String name, Sort sort);

    // 15. 페이징 처리
    Page<Product> findByName(String name, Pageable pageable);

    // 16. 특정 컬럼만 추출하는 쿼리
    @Query("SELECT p.name, p.price, p.stock FROM Product p WHERE p.name = :name")
    List<Object[]> findByNameParam2(@Param("name") String name);
}
