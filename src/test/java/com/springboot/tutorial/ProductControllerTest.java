package com.springboot.tutorial;

import com.google.gson.Gson;
import com.springboot.tutorial.dto.ProductDto;
import com.springboot.tutorial.dto.ProductResponseDto;
import com.springboot.tutorial.dto.SignInResultDto;
import com.springboot.tutorial.service.SignService;
import com.springboot.tutorial.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(controllers = ProductController.class)?
//      웹에서 사용되는 요청과 응답에 대한 테스트를 수행할 때 사용하는 어노테이션
//      대상 클래스만 로드해 테스트를 수행하하며, 만약 대상 클래스를 추가하지 않으면 컨트롤러 관련 빈 객체들이 모두 로드됨
//      @SpringBootTest보다 가볍게 테스트하기 위한 용도로 사용이 된다.

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductControllerTest {
    @Autowired
    private SignService signService;

    @Autowired
    private WebApplicationContext context;

    // FIXME. Could not autowire. No beans of 'MockMvc' type found.
    // 컴파일 오류가 발생하지만 런타임 시에 실행은 잘된다.
    private MockMvc mockMvc;

    @MockBean // 실제 빈 객체가 아닌 Mock 객체를 생성해서 주입
              // 실제 객체가 아니기 때문에 행위를 수행하지 않으며 given으로 동작을 정의한다.
    ProductServiceImpl productService;

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        signService.signUp("id", "password", "name", "ADMIN");
    }

    @Test
    @DisplayName("MockMvc를 통하여 Product 데이터를 정상적으로 받을 수 있는지 확인한다.")
    void getProductTest() throws Exception {
        SignInResultDto signInResultDto = signService.signin("id", "password");
        String token = signInResultDto.getToken();

        given(productService.getProduct(123L))
                .willReturn(new ProductResponseDto(123L, "pen", 5000, 2000));

        String productId = "123";

        // perform 메서드를 이용하면 서버로 URL 요청을 보내는 것처럼 통신 테스트 코드를 작성하여 컨트롤러를 테스트할 수 있다.
        // andExpect 메서드를 활용하여 결과값을 검증할 수 있다.
        // andDo 메서드를 활용하여 전체 내용을 확인할 수 있다.
        mockMvc.perform(get("/product?number=" + productId)
                        .header("X-AUTH-TOKEN", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andDo(print());

        // 지정된 메서드가 정상적으로 실행되었는지 확인한다.
        verify(productService).getProduct(123L);
    }

    @Test
    @DisplayName("MockMvc를 통하여 Product 데이터가 정상적으로 생성되었는지 확인한다.")
    void createProduct() throws Exception {
        SignInResultDto signInResultDto = signService.signin("id", "password");
        String token = signInResultDto.getToken();

        given(productService.saveProduct(new ProductDto("pen", 5000, 2000)))
                .willReturn(new ProductResponseDto(12315L, "pen", 5000, 2000));

        ProductDto productDto = ProductDto.builder()
                .name("pen")
                .price(5000)
                .stock(2000)
                .build();

        Gson gson = new Gson();
        String content = gson.toJson(productDto);

        mockMvc.perform(
                        post("/product")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-AUTH-TOKEN", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andDo(print());

        verify(productService).saveProduct(new ProductDto("pen", 5000, 2000));
    }
}
