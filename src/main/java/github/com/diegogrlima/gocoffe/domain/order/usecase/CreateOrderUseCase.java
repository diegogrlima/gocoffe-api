package github.com.diegogrlima.gocoffe.domain.order.usecase;

import github.com.diegogrlima.gocoffe.application.dto.order.CreateOrderInput;
import github.com.diegogrlima.gocoffe.application.dto.order.CreateOrderOutput;
import github.com.diegogrlima.gocoffe.domain.order.entity.Order;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderItem;
import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderItemRepository;
import github.com.diegogrlima.gocoffe.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public CreateOrderOutput execute(CreateOrderInput input) {
        String orderCode = generateOrderCode();

        List<OrderItem> orderItems = input.items().stream()
                .map(item -> OrderItem.builder()
                        .productId(item.productId())
                        .quantity(item.quantity())
                        .priceUnit(item.priceUnit())
                        .subtotal(item.priceUnit().multiply(BigDecimal.valueOf(item.quantity())))
                        .build())
                .collect(Collectors.toList());

        Order order = Order.builder()
                .orderCode(orderCode)
                .customerCpf(input.customerCpf())
                .status(OrderStatus.PENDING)
                .items(new ArrayList<>())
                .build();

        Order savedOrder = orderRepository.save(order);

        orderItems.forEach(item -> item.setOrderId(savedOrder.getId()));
        List<OrderItem> savedItems = orderItemRepository.saveAll(orderItems);

        BigDecimal totalPrice = savedItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        savedOrder.setItems(savedItems);
        savedOrder.setTotalPrice(totalPrice);
        orderRepository.save(savedOrder);

        List<CreateOrderOutput.OrderItemOutput> itemOutputs = savedItems.stream()
                .map(item -> new CreateOrderOutput.OrderItemOutput(
                        item.getId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPriceUnit(),
                        item.getSubtotal()))
                .collect(Collectors.toList());

        return new CreateOrderOutput(
                savedOrder.getId(),
                savedOrder.getOrderCode(),
                savedOrder.getCustomerCpf(),
                savedOrder.getStatus(),
                savedOrder.getTotalPrice(),
                itemOutputs,
                savedOrder.getCreatedAt()
        );
    }

    private String generateOrderCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder("Pedido-");
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(uuid.charAt(i) % characters.length()));
        }
        return code.toString();
    }
}
