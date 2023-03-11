package com.springboot.tutorial.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@MappedSuperclass // JPA의 엔티티 클래스가 상속받을 경우 하위 클래스에게 매핑 정보를 전달한다.
@EntityListeners(AuditingEntityListener.class)  // 엔티티를 데이터베이스에 적용하기 전후로 콜백을 요청할 수 있도록 한다.
                                                // AuditingEntityListener? 엔티티의 Auditing 정보를 주입하는 JPA 엔티티 리스너 클래스
public class BaseEntity {
    @CreatedDate // 생성 날짜를 자동으로 주입
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 날짜를 자동으로 주입
    private LocalDateTime updatedAt;
}
