package github.com.diegogrlima.gocoffe.domain.order.repository;

import github.com.diegogrlima.gocoffe.domain.order.entity.Order;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findAll();

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByCustomerCpf(String customerCpf);

    void deleteById(UUID id);
}
