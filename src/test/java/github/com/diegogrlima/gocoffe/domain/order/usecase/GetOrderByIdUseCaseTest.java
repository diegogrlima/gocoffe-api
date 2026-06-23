package github.com.diegogrlima.gocoffe.domain.order.usecase;

import github.com.diegogrlima.gocoffe.application.dto.order.GetOrderByIdOutput;
import github.com.diegogrlima.gocoffe.domain.order.entity.Order;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderByIdUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetOrderByIdUseCase getOrderByIdUseCase;

    @Test
    void shouldReturnOrderWhenExists() {
        UUID orderId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Order order = Order.builder()
                .id(orderId)
                .orderCode("Pedido-7E9X2P")
                .customerCpf("12345678901")
                .status(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("50.00"))
                .createdAt(now)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        GetOrderByIdOutput result = getOrderByIdUseCase.execute(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.id());
        assertEquals("Pedido-7E9X2P", result.orderCode());
        assertEquals(OrderStatus.PENDING, result.status());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> getOrderByIdUseCase.execute(orderId));

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository).findById(orderId);
    }
}
