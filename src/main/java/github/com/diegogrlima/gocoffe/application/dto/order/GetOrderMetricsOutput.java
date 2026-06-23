package github.com.diegogrlima.gocoffe.application.dto.order;

public record GetOrderMetricsOutput(
        long totalOrders,
        long preparingOrders,
        long readyOrders
) {
}
