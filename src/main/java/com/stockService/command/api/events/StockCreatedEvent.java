package com.stockService.command.api.events;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class StockCreatedEvent {

	private String uuid;
	private String companyCode;
	private BigDecimal stockPrice;
	private Long numberOfHours;

}
