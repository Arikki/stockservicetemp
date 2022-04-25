package com.stockService.command.api.commands;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateStockCommand {

	@TargetAggregateIdentifier
	private String uuid;
	private String companyCode;
	private BigDecimal stockPrice;
	private Long numberOfHours;

}
