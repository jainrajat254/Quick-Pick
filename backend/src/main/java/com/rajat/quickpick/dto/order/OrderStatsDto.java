package com.rajat.quickpick.dto.order;

import lombok.Data;

@Data
public class OrderStatsDto {
    private long totalOrders;
    private long pendingOrders;
    private long completedOrders;
    private long cancelledOrders;
}