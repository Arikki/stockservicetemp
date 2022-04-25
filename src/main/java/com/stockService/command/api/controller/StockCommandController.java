package com.stockService.command.api.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.stockService.command.api.commands.CreateStockCommand;
import com.stockService.command.api.commands.DeleteStockCommand;
import com.stockService.model.StockRestModel;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1.0/market/stock/")
@Tag(name = "Add Stock", description = "API to add stock")
public class StockCommandController {
	
	 private static final Logger logger = LoggerFactory.getLogger(StockCommandController.class);

	private CommandGateway commandGateway;

	public StockCommandController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@PostMapping("add")
	@Operation(summary = "Add a new stock", security = @SecurityRequirement(name = "bearerAuth"))
	public String addStock(@RequestBody StockRestModel stockRestModel) {

		CreateStockCommand createStockCommand = CreateStockCommand.builder().uuid(UUID.randomUUID().toString())
				.companyCode(stockRestModel.getCompanyCode()).stockPrice(stockRestModel.getStockPrice())
				.numberOfHours(stockRestModel.getNumberOfHours()).build();

		logger.info("Created command to add a stock with company code:" + stockRestModel.getCompanyCode());
		String result = commandGateway.sendAndWait(createStockCommand);
		return result;
	}

	@DeleteMapping("delete/{companyCode}")
	@Hidden
	@Operation(summary = "Delete all stocks associated with a company code")
	public String deleteStocks(@PathVariable String companyCode) {
		DeleteStockCommand deleteStockCommand = DeleteStockCommand.builder().uuid(UUID.randomUUID().toString())
				.companyCode(companyCode).build();
		logger.info("Created command to delete a stock with company code:"+companyCode);
		String result = commandGateway.sendAndWait(deleteStockCommand);
		return result;
	}
}
