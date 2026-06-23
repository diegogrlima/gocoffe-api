package github.com.diegogrlima.gocoffe.domain.order.usecase;

import github.com.diegogrlima.gocoffe.application.dto.order.GetOrderMetricsOutput;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetOrderMetricsUseCase {

    private final OrderRepository orderRepository;

    public GetOrderMetricsOutput execute() {
        long totalOrders = orderRepository.count();
        long preparingOrders = orderRepository.countByStatus(OrderStatus.PREPARING);
        long readyOrders = orderRepository.countByStatus(OrderStatus.READY);

        return new GetOrderMetricsOutput(
                totalOrders,
                preparingOrders,
                readyOrders
        );
    }
}
