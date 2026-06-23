package github.com.diegogrlima.gocoffe.domain.order.usecase;

import github.com.diegogrlima.gocoffe.application.dto.PageOutput;
import github.com.diegogrlima.gocoffe.application.dto.order.GetAllOrderOutput;
import github.com.diegogrlima.gocoffe.domain.order.entity.Order;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllOrderUseCase {

    private final OrderRepository orderRepository;

    public PageOutput<GetAllOrderOutput> execute(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> orderPage = orderRepository.findAll(pageable);

        java.util.List<GetAllOrderOutput> content = orderPage.getContent().stream()
                .map(order -> new GetAllOrderOutput(
                        order.getId(),
                        order.getOrderCode(),
                        order.getCustomerCpf(),
                        order.getStatus(),
                        order.getTotalPrice(),
                        order.getCreatedAt()
                ))
                .toList();

        return new PageOutput<>(
                content,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages()
        );
    }
}
