package com.springboot.tutorial.config;

import com.springboot.tutorial.config.security.CustomAccessDeniedHandler;
import com.springboot.tutorial.config.security.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * HttpSecurity 클래스의 configure 메소드는 다음과 같은 기능을 한다.
     * - 리소스 접근 권한 설정
     * - 인증 실패 시 발생하는 예외 처리
     * - 인증 로직 커스터마이징
     * - csrf, cors 등의 스프링 시큐리티 설정
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()                                                               // csrf 토큰을 발급하여 클라이언트으로부터 요청을 받을 때마다 토큰을 검증한다. (여기선 비활성화)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)     // 세션을 관리하는 방식을 설정. 세션은 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .authorizeRequests()                                                            // 애플리케이션에 들어오는 요청에 대한 사용권한 체크 antMatchers를 참고한다.
                .antMatchers(
                        "/sign-api/sign-in",                                                    // 해당 경로들에 대해서는
                        "/sign-up/sign-up",
                        "/sign-api/exception"
                ).permitAll()                                                                   // 모두 허용하도록 설정하고..
                .antMatchers(
                        HttpMethod.GET, "/product/**"                                           // 해당 경로로 GET 요청을 보내는 경우에 대해서는
                ).permitAll()                                                                   // 모두 허용하도록 설정하고..
                .antMatchers(
                        "**exception**"                                                         // 해당 경로들에 대해서는
                ).permitAll()                                                                   // 모두 허용하도록 설정한다.
                .anyRequest()                                                                   // 그 외의 모든 요청은 ADMIN 권한을 가져야 한다.
                .hasRole("ADMIN")
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())       // 권한을 확인하는 과정에서 통과하지 못하는 예외가 발생 시 예외를 전달한다.
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 과정에서 예외가 발생하는 경우 예외를 전달한다.
                .and()
                .addFilterBefore(                                                               // 필터체인의 호출 과정들을 나열하였다.
                        new JwtAuthenticationFilter(jwtTokenProvider),                          // JwtAuthenticationFilter에서 작업이 끝나고,
                        UsernamePasswordAuthenticationFilter.class                              // UsernamePasswordAuthenticationFilter 작업이 끝나면,
                );                                                                              // 정상적으로 인증이 처리된다.
    }

    /**
     * WebSecurity 클래스의 configure 메소드는 다음과 같은 기능을 한다.
     * - HttpSecurity 앞단에 적용되며 전체적으로 스프링 시큐리티의 영향권 밖에 이?ㅆ다.
     * - 인증과 인가가 모두 적용되기 전에 설정한다.
     * - 아래의 구현은 Swagger에 적용되는 인증과 인가를 모두 ignore 처리하기 위해 정의하였다.
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger/**",
                        "/sign-api/exception");
    }
}
