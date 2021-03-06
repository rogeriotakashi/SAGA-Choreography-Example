package com.rogerio.saga.choreography.OrderService.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rogerio.saga.choreography.OrderService.enums.OrderStatusEnum;
import com.rogerio.saga.choreography.OrderService.models.Order;
import com.rogerio.saga.choreography.OrderService.repositories.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	@Autowired
	private OrderRepository orderRepo;

	public Order createOrder(String user, double total) {
		log.info("Entering createorder user. Creating the Order register with status PENDING");
		Order order = new Order();
		order.setUser(user);
		order.setTotal(total);
		order.setStatus(OrderStatusEnum.PENDING.getStatus());
		return orderRepo.save(order);
	}

	public OrderStatusEnum approveOrder(Long orderId) {
		log.info("Entering approveOrder method. Approving Order ID: {}", orderId);
		Optional<Order> orderOptional = orderRepo.findById(orderId);
		Optional<OrderStatusEnum> statusOptional = orderOptional.map(order -> {
			order.setStatus(OrderStatusEnum.APPROVED.getStatus());
			orderRepo.saveAndFlush(order);
			return OrderStatusEnum.APPROVED;
		});

		log.warn("Order ID: {} not found: {} . Returning status NOT_FOUND", orderId);
		return statusOptional.orElse(OrderStatusEnum.NOT_FOUND);

	}

	public OrderStatusEnum rejectOrder(Long orderId) {
		Optional<Order> orderOptional = orderRepo.findById(orderId);
		Optional<OrderStatusEnum> statusOptional = orderOptional.map((order) -> {
			order.setStatus(OrderStatusEnum.REJECTED.getStatus());
			orderRepo.saveAndFlush(order);
			return OrderStatusEnum.REJECTED;
		});

		return statusOptional.orElse(OrderStatusEnum.NOT_FOUND);
	}

	public void deleteOrder(Long id) {
		orderRepo.deleteById(id);
	}

	public void updateOrderStatus(Long id, OrderStatusEnum status) {
		Optional<Order> orderOptional = orderRepo.findById(id);
		orderOptional.ifPresent((order) -> {
			order.setStatus(status.getStatus());
			orderRepo.saveAndFlush(order);
		});
	}
}
