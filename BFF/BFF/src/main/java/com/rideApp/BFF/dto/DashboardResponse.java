package com.rideApp.BFF.dto;

import com.rideApp.BFF.domain.Notification;
import com.rideApp.BFF.domain.Order;
import com.rideApp.BFF.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashboardResponse {
    private User user;
    private List<Order> recentOrders;
    private List<Notification> notifications;
}
