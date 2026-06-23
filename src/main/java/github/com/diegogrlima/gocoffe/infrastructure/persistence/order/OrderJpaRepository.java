package github.com.diegogrlima.gocoffe.infrastructure.persistence.order;

import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {

    List<OrderJpaEntity> findByStatus(OrderStatus status);

    List<OrderJpaEntity> findByCustomerCpf(String customerCpf);
}
