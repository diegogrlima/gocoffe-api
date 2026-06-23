package github.com.diegogrlima.gocoffe.domain.order.repository;

import github.com.diegogrlima.gocoffe.domain.order.entity.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository {

    List<OrderItem> saveAll(List<OrderItem> orderItems);

    List<OrderItem> findAllByOrderId(UUID orderId);

    void deleteAllByOrderId(UUID orderId);
}
