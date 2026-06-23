package github.com.diegogrlima.gocoffe.domain.order.usecase;

import github.com.diegogrlima.gocoffe.application.dto.order.GetOrderMetricsOutput;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderMetricsUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetOrderMetricsUseCase getOrderMetricsUseCase;

    @Test
    void shouldReturnOrderMetrics() {
        when(orderRepository.count()).thenReturn(10L);
        when(orderRepository.countByStatus(github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus.PREPARING)).thenReturn(3L);
        when(orderRepository.countByStatus(github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus.READY)).thenReturn(5L);

        GetOrderMetricsOutput result = getOrderMetricsUseCase.execute();

        assertNotNull(result);
        assertEquals(10L, result.totalOrders());
        assertEquals(3L, result.preparingOrders());
        assertEquals(5L, result.readyOrders());

        verify(orderRepository).count();
        verify(orderRepository).countByStatus(github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus.PREPARING);
        verify(orderRepository).countByStatus(github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus.READY);
    }

    @Test
    void shouldReturnZeroMetricsWhenNoOrders() {
        when(orderRepository.count()).thenReturn(0L);
        when(orderRepository.countByStatus(github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus.PREPARING)).thenReturn(0L);
        when(orderRepository.countByStatus(github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus.READY)).thenReturn(0L);

        GetOrderMetricsOutput result = getOrderMetricsUseCase.execute();

        assertNotNull(result);
        assertEquals(0L, result.totalOrders());
        assertEquals(0L, result.preparingOrders());
        assertEquals(0L, result.readyOrders());
    }
}
