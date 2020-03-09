package com.rogerio.saga.orchestrator.OrchestratorService.models.requests.order;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateOrderRequest {

	private String user;
	private double total;
	private UUID uuid;	
	
	public CreateOrderRequest(String user, double total) {
		this.user = user;
		this.total = total;
		this.uuid = UUID.randomUUID();
	}
	
	
}
