package com.rogerio.saga.orchestrator.OrchestratorService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rogerio.saga.orchestrator.OrchestratorService.enums.OrderStatusEnum;
import com.rogerio.saga.orchestrator.OrchestratorService.models.OrderDTO;
import com.rogerio.saga.orchestrator.OrchestratorService.models.requests.order.ApproveOrderRequest;
import com.rogerio.saga.orchestrator.OrchestratorService.models.requests.order.CreateOrderRequest;
import com.rogerio.saga.orchestrator.OrchestratorService.models.requests.order.RejectOrderRequest;
import com.rogerio.saga.orchestrator.OrchestratorService.models.response.order.ApproveOrderResponse;
import com.rogerio.saga.orchestrator.OrchestratorService.models.response.order.CreateOrderResponse;

@Service
public class OrderService {
	
	@Autowired
	RestTemplate rest;
	
	public OrderDTO createOrder(String user, double total) {
		CreateOrderRequest createOrderRequest = new CreateOrderRequest(user, total);
		CreateOrderResponse createOrderResponse = rest.postForObject("http://ORDER-SERVICE/api/v1/order/create", createOrderRequest, CreateOrderResponse.class);
		return createOrderResponse.getOrderDTO();
	}
	
	public OrderStatusEnum approveOrder(Long orderId) {
		ApproveOrderRequest approveOrderRequest = new ApproveOrderRequest(orderId);
		ApproveOrderResponse response = rest.postForObject("http://ORDER-SERVICE/api/v1/order/approve", approveOrderRequest , ApproveOrderResponse.class);
		return response.getStatus();
	}
	
	public OrderStatusEnum rejectOrder(Long orderId) {
		ResponseEntity<?> response = rest.postForEntity("http://ORDER-SERVICE/api/v1/order/reject", new RejectOrderRequest(orderId) , HttpEntity.class);
		return OrderStatusEnum.REJECTED;
	}
	
	public OrderStatusEnum deleteOrder(Long orderId) {
		rest.delete("http://ORDER-SERVICE/api/v1/order/delete/" + orderId);
		return OrderStatusEnum.DELETED;
	}

}
