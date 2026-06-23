package github.com.diegogrlima.gocoffe.infrastructure.persistence.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemJpaRepository extends JpaRepository<OrderItemJpaEntity, UUID> {

    List<OrderItemJpaEntity> findAllByOrderId(UUID orderId);

    void deleteAllByOrderId(UUID orderId);
}
