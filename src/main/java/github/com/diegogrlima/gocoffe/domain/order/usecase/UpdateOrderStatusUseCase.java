package github.com.diegogrlima.gocoffe.domain.order.usecase;

import github.com.diegogrlima.gocoffe.application.dto.order.UpdateOrderStatusInput;
import github.com.diegogrlima.gocoffe.config.exception.ResourceNotFoundException;
import github.com.diegogrlima.gocoffe.domain.order.entity.Order;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateOrderStatusUseCase {

    private final OrderRepository orderRepository;

    public void execute(UpdateOrderStatusInput input) {
        Order order = orderRepository.findById(input.id())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(input.status());

        orderRepository.save(order);
    }
}
