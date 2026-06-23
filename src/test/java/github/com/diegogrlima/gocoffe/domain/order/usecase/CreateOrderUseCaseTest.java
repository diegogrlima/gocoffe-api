package github.com.diegogrlima.gocoffe.domain.order.usecase;

import github.com.diegogrlima.gocoffe.application.dto.order.CreateOrderInput;
import github.com.diegogrlima.gocoffe.application.dto.order.CreateOrderOutput;
import github.com.diegogrlima.gocoffe.domain.order.entity.Order;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderItem;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderItemRepository;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @Test
    void shouldCreateOrderWithSingleItem() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CreateOrderInput input = new CreateOrderInput(
                "12345678901",
                List.of(new CreateOrderInput.OrderItemInput(productId, 2, new BigDecimal("25.00")))
        );

        Order savedOrder = Order.builder()
                .id(orderId)
                .orderCode("Pedido-7E9X2P")
                .customerCpf("12345678901")
                .status(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("50.00"))
                .createdAt(now)
                .build();

        OrderItem savedItem = OrderItem.builder()
                .id(itemId)
                .orderId(orderId)
                .productId(productId)
                .quantity(2)
                .priceUnit(new BigDecimal("25.00"))
                .subtotal(new BigDecimal("50.00"))
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.saveAll(any())).thenReturn(List.of(savedItem));

        CreateOrderOutput result = createOrderUseCase.execute(input);

        assertNotNull(result);
        assertEquals(orderId, result.id());
        assertEquals("12345678901", result.customerCpf());
        assertEquals(OrderStatus.PENDING, result.status());
        assertEquals(new BigDecimal("50.00"), result.totalPrice());
        assertEquals(1, result.items().size());
        assertEquals(productId, result.items().get(0).productId());
        assertEquals(2, result.items().get(0).quantity());
        assertEquals(new BigDecimal("25.00"), result.items().get(0).priceUnit());
        assertEquals(new BigDecimal("50.00"), result.items().get(0).subtotal());

        verify(orderRepository, org.mockito.Mockito.times(2)).save(any(Order.class));
        verify(orderItemRepository).saveAll(any());
    }

    @Test
    void shouldCreateOrderWithMultipleItems() {
        UUID orderId = UUID.randomUUID();
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        UUID itemId1 = UUID.randomUUID();
        UUID itemId2 = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CreateOrderInput input = new CreateOrderInput(
                "12345678901",
                List.of(
                        new CreateOrderInput.OrderItemInput(productId1, 2, new BigDecimal("25.00")),
                        new CreateOrderInput.OrderItemInput(productId2, 1, new BigDecimal("15.00"))
                )
        );

        Order savedOrder = Order.builder()
                .id(orderId)
                .orderCode("Pedido-7E9X2P")
                .customerCpf("12345678901")
                .status(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("65.00"))
                .createdAt(now)
                .build();

        List<OrderItem> savedItems = List.of(
                OrderItem.builder()
                        .id(itemId1)
                        .orderId(orderId)
                        .productId(productId1)
                        .quantity(2)
                        .priceUnit(new BigDecimal("25.00"))
                        .subtotal(new BigDecimal("50.00"))
                        .build(),
                OrderItem.builder()
                        .id(itemId2)
                        .orderId(orderId)
                        .productId(productId2)
                        .quantity(1)
                        .priceUnit(new BigDecimal("15.00"))
                        .subtotal(new BigDecimal("15.00"))
                        .build()
        );

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.saveAll(any())).thenReturn(savedItems);

        CreateOrderOutput result = createOrderUseCase.execute(input);

        assertNotNull(result);
        assertEquals(orderId, result.id());
        assertEquals(new BigDecimal("65.00"), result.totalPrice());
        assertEquals(2, result.items().size());

        verify(orderRepository, org.mockito.Mockito.times(2)).save(any(Order.class));
        verify(orderItemRepository).saveAll(any());
    }

    @Test
    void shouldGenerateOrderCodeWithCorrectFormat() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        CreateOrderInput input = new CreateOrderInput(
                "12345678901",
                List.of(new CreateOrderInput.OrderItemInput(productId, 1, new BigDecimal("10.00")))
        );

        Order savedOrder = Order.builder()
                .id(orderId)
                .orderCode("Pedido-ABC123")
                .customerCpf("12345678901")
                .status(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("10.00"))
                .build();

        OrderItem savedItem = OrderItem.builder()
                .id(itemId)
                .orderId(orderId)
                .productId(productId)
                .quantity(1)
                .priceUnit(new BigDecimal("10.00"))
                .subtotal(new BigDecimal("10.00"))
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.saveAll(any())).thenReturn(List.of(savedItem));

        CreateOrderOutput result = createOrderUseCase.execute(input);

        assertNotNull(result);
        assertTrue(result.orderCode().startsWith("Pedido-"));
        assertEquals(13, result.orderCode().length());
    }
}
