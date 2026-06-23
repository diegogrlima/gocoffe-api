package github.com.diegogrlima.gocoffe.domain.order.usecase;

import github.com.diegogrlima.gocoffe.application.dto.PageOutput;
import github.com.diegogrlima.gocoffe.application.dto.order.GetAllOrderOutput;
import github.com.diegogrlima.gocoffe.domain.order.entity.Order;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetAllOrderUseCase getAllOrderUseCase;

    @Test
    void shouldReturnPaginatedOrders() {
        UUID orderId1 = UUID.randomUUID();
        UUID orderId2 = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Order order1 = Order.builder()
                .id(orderId1)
                .orderCode("Pedido-7E9X2P")
                .customerCpf("12345678901")
                .status(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("50.00"))
                .createdAt(now)
                .build();

        Order order2 = Order.builder()
                .id(orderId2)
                .orderCode("Pedido-ABC123")
                .customerCpf("98765432100")
                .status(OrderStatus.PREPARING)
                .totalPrice(new BigDecimal("30.00"))
                .createdAt(now)
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Order> orderPage = new PageImpl<>(List.of(order1, order2), pageable, 2);

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);

        PageOutput<GetAllOrderOutput> result = getAllOrderUseCase.execute(0, 10);

        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(2, result.totalElements());
        assertEquals(1, result.totalPages());

        assertEquals(orderId1, result.content().get(0).id());
        assertEquals("Pedido-7E9X2P", result.content().get(0).orderCode());
        assertEquals(OrderStatus.PENDING, result.content().get(0).status());

        assertEquals(orderId2, result.content().get(1).id());
        assertEquals("Pedido-ABC123", result.content().get(1).orderCode());
        assertEquals(OrderStatus.PREPARING, result.content().get(1).status());

        verify(orderRepository).findAll(pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoOrders() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Order> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(orderRepository.findAll(pageable)).thenReturn(emptyPage);

        PageOutput<GetAllOrderOutput> result = getAllOrderUseCase.execute(0, 10);

        assertNotNull(result);
        assertEquals(0, result.content().size());
        assertEquals(0, result.totalElements());
        assertEquals(0, result.totalPages());
    }
}
