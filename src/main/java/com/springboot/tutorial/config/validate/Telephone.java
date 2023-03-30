package com.springboot.tutorial.config.validate;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 해당 어노테이션을 어디에서 선언할 수 있는지 정의
@Retention(RetentionPolicy.RUNTIME) // 어노테이션이 실제로 적용되고 유지되는 범위
                                    // RUNTIME? 컴파일 이후에도 JVM에 의해 계속 참조. 리플렉션이나 로깅에 많이 사용
@Constraint(validatedBy = TelephoneValidator.class) // validatedBy에 지정된 클래스와 매핑되는 작업을 수행
public @interface Telephone {
    /**
     * 유효성 검사가 실패하는 경우 반환되는 메세지를 정의
     * @return 유효성 검사 실패 시 반환되는 메시지
     */
    String message() default "전화번호 형식이 일치하지 않습니다.";

    /**
     * 유효성 검사를 사용하는 그룹을 정의
     * @return 유효성 검사를 사용하는 그룹
     */
    Class[] groups() default {};

    /**
     * 사용자가 추가 정보를 위해 전달하는 값을 정의
     * @return 사용자가 추가할 정보
     */
    Class[] payload() default {};
}
