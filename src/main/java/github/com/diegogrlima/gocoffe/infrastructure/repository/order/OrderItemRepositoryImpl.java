package github.com.diegogrlima.gocoffe.infrastructure.repository.order;

import github.com.diegogrlima.gocoffe.domain.order.entity.OrderItem;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderItemRepository;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.order.OrderItemJpaEntity;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.order.OrderItemJpaRepository;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.order.OrderJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final OrderItemJpaRepository orderItemJpaRepository;

    @Override
    public List<OrderItem> saveAll(List<OrderItem> orderItems) {
        List<OrderItemJpaEntity> jpaEntities = orderItems.stream()
                .map(this::toJpaEntity)
                .collect(Collectors.toList());
        List<OrderItemJpaEntity> savedEntities = orderItemJpaRepository.saveAll(jpaEntities);
        return savedEntities.stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItem> findAllByOrderId(UUID orderId) {
        return orderItemJpaRepository.findAllByOrderId(orderId).stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByOrderId(UUID orderId) {
        orderItemJpaRepository.deleteAllByOrderId(orderId);
    }

    private OrderItemJpaEntity toJpaEntity(OrderItem orderItem) {
        OrderJpaEntity orderJpa = OrderJpaEntity.builder()
                .id(orderItem.getOrderId())
                .build();

        return OrderItemJpaEntity.builder()
                .id(orderItem.getId())
                .order(orderJpa)
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .priceUnit(orderItem.getPriceUnit())
                .subtotal(orderItem.getSubtotal())
                .build();
    }

    private OrderItem toDomainEntity(OrderItemJpaEntity jpaEntity) {
        return OrderItem.builder()
                .id(jpaEntity.getId())
                .orderId(jpaEntity.getOrder().getId())
                .productId(jpaEntity.getProductId())
                .quantity(jpaEntity.getQuantity())
                .priceUnit(jpaEntity.getPriceUnit())
                .subtotal(jpaEntity.getSubtotal())
                .build();
    }
}
