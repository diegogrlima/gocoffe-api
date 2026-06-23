package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.PageOutput;
import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductInput;
import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductOutput;
import github.com.diegogrlima.gocoffe.application.dto.product.GetAllProductOutput;
import github.com.diegogrlima.gocoffe.application.dto.product.GetProductByIdOutput;
import github.com.diegogrlima.gocoffe.application.dto.product.UpdateProductInput;
import github.com.diegogrlima.gocoffe.config.GlobalExceptionHandler;
import github.com.diegogrlima.gocoffe.domain.product.usecase.CreateProductUseCase;
import github.com.diegogrlima.gocoffe.domain.product.usecase.GetAllProductUseCase;
import github.com.diegogrlima.gocoffe.domain.product.usecase.GetProductByIdUseCase;
import github.com.diegogrlima.gocoffe.domain.product.usecase.UpdateProductByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private CreateProductUseCase createProductUseCase;

    @Mock
    private GetAllProductUseCase getAllProductUseCase;

    @Mock
    private GetProductByIdUseCase getProductByIdUseCase;

    @Mock
    private UpdateProductByIdUseCase updateProductByIdUseCase;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturn201WhenCreateProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        CreateProductOutput output = new CreateProductOutput(
                productId,
                "Cafe Especial",
                "Cafe torrado",
                new BigDecimal("25.00"),
                true,
                categoryId
        );

        when(createProductUseCase.execute(any(CreateProductInput.class))).thenReturn(output);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Cafe Especial\",\"description\":\"Cafe torrado\",\"price\":25.00,\"categoryId\":\"" + categoryId + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Cafe Especial"))
                .andExpect(jsonPath("$.description").value("Cafe torrado"))
                .andExpect(jsonPath("$.price").value(25.00))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()));

        verify(createProductUseCase).execute(any(CreateProductInput.class));
    }

    @Test
    void shouldReturn200WhenFindAllProducts() throws Exception {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        GetAllProductOutput product1 = new GetAllProductOutput(
                productId1,
                "Cafe Especial",
                "Cafe torrado",
                new BigDecimal("25.00"),
                true,
                categoryId,
                "Bebidas",
                now
        );

        GetAllProductOutput product2 = new GetAllProductOutput(
                productId2,
                "Cha Verde",
                "Cha organico",
                new BigDecimal("15.00"),
                true,
                categoryId,
                "Bebidas",
                now
        );

        PageOutput<GetAllProductOutput> pageOutput = new PageOutput<>(
                List.of(product1, product2),
                0,
                10,
                2,
                1
        );

        when(getAllProductUseCase.execute(0, 10)).thenReturn(pageOutput);

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(productId1.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Cafe Especial"))
                .andExpect(jsonPath("$.content[0].categoryName").value("Bebidas"))
                .andExpect(jsonPath("$.content[1].id").value(productId2.toString()))
                .andExpect(jsonPath("$.content[1].name").value("Cha Verde"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(getAllProductUseCase).execute(0, 10);
    }

    @Test
    void shouldReturn200WithDefaultPagination() throws Exception {
        PageOutput<GetAllProductOutput> pageOutput = new PageOutput<>(
                List.of(),
                0,
                10,
                0,
                0
        );

        when(getAllProductUseCase.execute(0, 10)).thenReturn(pageOutput);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(0));

        verify(getAllProductUseCase).execute(0, 10);
    }

    @Test
    void shouldReturn200WithCustomPagination() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        GetAllProductOutput product = new GetAllProductOutput(
                productId,
                "Cafe Especial",
                "Cafe torrado",
                new BigDecimal("25.00"),
                true,
                categoryId,
                "Bebidas",
                LocalDateTime.now()
        );

        PageOutput<GetAllProductOutput> pageOutput = new PageOutput<>(
                List.of(product),
                2,
                5,
                15,
                3
        );

        when(getAllProductUseCase.execute(2, 5)).thenReturn(pageOutput);

        mockMvc.perform(get("/products")
                        .param("page", "2")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.page").value(2))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.totalPages").value(3));

        verify(getAllProductUseCase).execute(2, 5);
    }

    @Test
    void shouldReturn200WhenFindProductById() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        GetProductByIdOutput output = new GetProductByIdOutput(
                productId,
                "Cafe Especial",
                "Cafe torrado",
                new BigDecimal("25.00"),
                true,
                categoryId,
                "Bebidas",
                now,
                now
        );

        when(getProductByIdUseCase.execute(productId)).thenReturn(output);

        mockMvc.perform(get("/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Cafe Especial"))
                .andExpect(jsonPath("$.description").value("Cafe torrado"))
                .andExpect(jsonPath("$.price").value(25.00))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()))
                .andExpect(jsonPath("$.categoryName").value("Bebidas"));

        verify(getProductByIdUseCase).execute(productId);
    }

    @Test
    void shouldReturn400WhenProductNotFound() throws Exception {
        UUID productId = UUID.randomUUID();

        when(getProductByIdUseCase.execute(productId))
                .thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(get("/products/" + productId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Product not found"));

        verify(getProductByIdUseCase).execute(productId);
    }

    @Test
    void shouldReturn204WhenUpdateProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        mockMvc.perform(put("/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Cafe Atualizado\",\"description\":\"Cafe premium\",\"price\":35.00,\"categoryId\":\"" + categoryId + "\"}"))
                .andExpect(status().isNoContent());

        verify(updateProductByIdUseCase).execute(any(UpdateProductInput.class));
    }

    @Test
    void shouldReturn400WhenUpdateProductWithInvalidData() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(put("/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"price\":-10}"))
                .andExpect(status().isBadRequest());
    }
}
