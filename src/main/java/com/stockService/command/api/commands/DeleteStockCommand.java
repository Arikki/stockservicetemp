package com.stockService.command.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class DeleteStockCommand {
	@TargetAggregateIdentifier
	private String uuid;
	private String companyCode;
}
