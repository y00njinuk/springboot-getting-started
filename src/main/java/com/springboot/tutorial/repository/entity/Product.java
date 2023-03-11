package com.springboot.tutorial.repository.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity // 해당 클래스가 엔티티임을 명시하며 테이블과의 관게는 1:1이다.
        // 해당 클래스의 인스턴스는 매핑되는 테이블에서 하나의 레코드를 의미한다.
        // 엔티티 클래스의 필드는 테이블의 칼럼과 매핑된다.
@Table(name="product")  // 클래스의 이름과 테이블의 이름이 다른 경우 해당 어노테이션을 사용하여 테이블을 지정한다.
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // 상위 클래스의 필드도 포함할 수 있도록 한다.
@ToString(callSuper = true)
public class Product extends BaseEntity {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 필드의 값을 어떤 방식으로 자동으로 생성할지 결정할 때 사용
                                                        // IDENTITY: 기본값 생성을 데이터베이스에 위임하며 AUTO_INCREMENT를 사용해 자동 생성
                                                        // AUTO: 기본값을 사용하는 데이터베이스에 맞게 자동 생성 (default)
                                                        // SEQUENCE: 식별자 생성기(@SequenceGenerator)를 설정하고 이를 통해 값을 자동 생성
                                                        // TABLE: 식별자로 사용할 숫자의 테이블을 별도로 생성해서 엔티티 생성 시 값을 갱신하며 사용
    private Long number;

    @Column(nullable = false) // 일반적으로는 자동 매핑이 되는데 설정을 추가하고 싶을 때 사용
                              // name: 데이터베이스의 칼럼명을 설정하는 속성
                              // nullable: 레코드를 생성할 때 칼럼 값에 null 처리가 가능한지 명시
                              // length: 데이터베이스에 저장하는 데이터의 최대 길이 설정
                              // unique: 해당 컬럼을 유니크로 설정
    @ToString.Exclude
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @OneToOne(mappedBy = "product") // 외래키의 주체를 설정 (즉, ProductDetail 엔티티의 외래키가 실제 외래키)
    @ToString.Exclude   // ProductDetail 엔티티의 toString 호출 ->
                        // Product 엔티티의 toString 호출 ->
                        // ProductDetail 엔티티의 toString 호출 ->
                        // 무한 반복.. 방지
    private ProductDetail productDetail;

    public Product(Long number, String name, Integer price, Integer stock) {
        this.number = number;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}
