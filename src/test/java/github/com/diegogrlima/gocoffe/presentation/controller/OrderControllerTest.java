package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.order.CreateOrderInput;
import github.com.diegogrlima.gocoffe.application.dto.order.CreateOrderOutput;
import github.com.diegogrlima.gocoffe.config.GlobalExceptionHandler;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;
import github.com.diegogrlima.gocoffe.domain.order.usecase.CreateOrderUseCase;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturn201WhenCreateOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CreateOrderOutput output = new CreateOrderOutput(
                orderId,
                "Pedido-7E9X2P",
                "12345678901",
                OrderStatus.PENDING,
                new BigDecimal("50.00"),
                List.of(new CreateOrderOutput.OrderItemOutput(
                        itemId,
                        productId,
                        2,
                        new BigDecimal("25.00"),
                        new BigDecimal("50.00")
                )),
                now
        );

        when(createOrderUseCase.execute(any(CreateOrderInput.class))).thenReturn(output);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerCpf\":\"12345678901\",\"items\":[{\"productId\":\"" + productId + "\",\"quantity\":2,\"priceUnit\":25.00}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderId.toString()))
                .andExpect(jsonPath("$.orderCode").value("Pedido-7E9X2P"))
                .andExpect(jsonPath("$.customerCpf").value("12345678901"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalPrice").value(50.00))
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].productId").value(productId.toString()))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].priceUnit").value(25.00))
                .andExpect(jsonPath("$.items[0].subtotal").value(50.00));

        verify(createOrderUseCase).execute(any(CreateOrderInput.class));
    }

    @Test
    void shouldReturn201WhenCreateOrderWithMultipleItems() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        UUID itemId1 = UUID.randomUUID();
        UUID itemId2 = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CreateOrderOutput output = new CreateOrderOutput(
                orderId,
                "Pedido-ABC123",
                "12345678901",
                OrderStatus.PENDING,
                new BigDecimal("65.00"),
                List.of(
                        new CreateOrderOutput.OrderItemOutput(itemId1, productId1, 2, new BigDecimal("25.00"), new BigDecimal("50.00")),
                        new CreateOrderOutput.OrderItemOutput(itemId2, productId2, 1, new BigDecimal("15.00"), new BigDecimal("15.00"))
                ),
                now
        );

        when(createOrderUseCase.execute(any(CreateOrderInput.class))).thenReturn(output);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerCpf\":\"12345678901\",\"items\":[{\"productId\":\"" + productId1 + "\",\"quantity\":2,\"priceUnit\":25.00},{\"productId\":\"" + productId2 + "\",\"quantity\":1,\"priceUnit\":15.00}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value(65.00))
                .andExpect(jsonPath("$.items.length()").value(2));

        verify(createOrderUseCase).execute(any(CreateOrderInput.class));
    }

    @Test
    void shouldReturn400WhenCustomerCpfIsBlank() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerCpf\":\"\",\"items\":[{\"productId\":\"" + UUID.randomUUID() + "\",\"quantity\":1,\"priceUnit\":10.00}]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenCustomerCpfIsInvalid() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerCpf\":\"123\",\"items\":[{\"productId\":\"" + UUID.randomUUID() + "\",\"quantity\":1,\"priceUnit\":10.00}]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenItemsIsEmpty() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerCpf\":\"12345678901\",\"items\":[]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenItemsIsNull() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerCpf\":\"12345678901\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenQuantityIsZero() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerCpf\":\"12345678901\",\"items\":[{\"productId\":\"" + UUID.randomUUID() + "\",\"quantity\":0,\"priceUnit\":10.00}]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPriceUnitIsNegative() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerCpf\":\"12345678901\",\"items\":[{\"productId\":\"" + UUID.randomUUID() + "\",\"quantity\":1,\"priceUnit\":-10.00}]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenProductIdIsNull() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerCpf\":\"12345678901\",\"items\":[{\"quantity\":1,\"priceUnit\":10.00}]}"))
                .andExpect(status().isBadRequest());
    }
}
