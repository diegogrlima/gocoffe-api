package github.com.diegogrlima.gocoffe.infrastructure.repository.order;

import github.com.diegogrlima.gocoffe.domain.order.entity.Order;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderRepository;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.order.OrderJpaEntity;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.order.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        OrderJpaEntity jpaEntity = toJpaEntity(order);
        OrderJpaEntity savedEntity = orderJpaRepository.save(jpaEntity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderJpaRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderJpaRepository.findAll(pageable).map(this::toDomainEntity);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return orderJpaRepository.findByStatus(status).stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByCustomerCpf(String customerCpf) {
        return orderJpaRepository.findByCustomerCpf(customerCpf).stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        orderJpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        return orderJpaRepository.count();
    }

    @Override
    public long countByStatus(OrderStatus status) {
        return orderJpaRepository.findByStatus(status).size();
    }

    private OrderJpaEntity toJpaEntity(Order order) {
        return OrderJpaEntity.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .customerCpf(order.getCustomerCpf())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .build();
    }

    private Order toDomainEntity(OrderJpaEntity jpaEntity) {
        return Order.builder()
                .id(jpaEntity.getId())
                .orderCode(jpaEntity.getOrderCode())
                .customerCpf(jpaEntity.getCustomerCpf())
                .status(jpaEntity.getStatus())
                .totalPrice(jpaEntity.getTotalPrice())
                .items(new ArrayList<>())
                .createdAt(jpaEntity.getCreatedAt())
                .build();
    }
}
