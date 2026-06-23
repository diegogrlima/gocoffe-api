package github.com.diegogrlima.gocoffe.domain.order.repository;

import github.com.diegogrlima.gocoffe.domain.order.entity.Order;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findAll();

    Page<Order> findAll(Pageable pageable);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByCustomerCpf(String customerCpf);

    void deleteById(UUID id);

    long count();

    long countByStatus(OrderStatus status);
}
