package github.com.diegogrlima.gocoffe.domain.order.usecase;

import github.com.diegogrlima.gocoffe.application.dto.order.UpdateOrderStatusInput;
import github.com.diegogrlima.gocoffe.domain.order.entity.Order;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateOrderStatusUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Test
    void shouldUpdateOrderStatusWhenExists() {
        UUID orderId = UUID.randomUUID();

        Order order = Order.builder()
                .id(orderId)
                .orderCode("Pedido-7E9X2P")
                .customerCpf("12345678901")
                .status(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("50.00"))
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        UpdateOrderStatusInput input = new UpdateOrderStatusInput(orderId, OrderStatus.PREPARING);

        updateOrderStatusUseCase.execute(input);

        assertEquals(OrderStatus.PREPARING, order.getStatus());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        UpdateOrderStatusInput input = new UpdateOrderStatusInput(orderId, OrderStatus.PREPARING);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> updateOrderStatusUseCase.execute(input));

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldUpdateStatusFromPendingToReady() {
        UUID orderId = UUID.randomUUID();

        Order order = Order.builder()
                .id(orderId)
                .orderCode("Pedido-ABC123")
                .customerCpf("12345678901")
                .status(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("30.00"))
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        UpdateOrderStatusInput input = new UpdateOrderStatusInput(orderId, OrderStatus.READY);

        updateOrderStatusUseCase.execute(input);

        assertEquals(OrderStatus.READY, order.getStatus());
        verify(orderRepository).save(order);
    }
}
