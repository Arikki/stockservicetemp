package com.stockService.query.api.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockService.command.api.controller.StockCommandController;
import com.stockService.data.Stock;
import com.stockService.query.api.queries.GetStockQuery;
import com.stockService.query.api.queries.GetStocksQuery;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1.0/market/stock/")
@Tag(name = "Fetch Stock", description = "API to fetch stock")
public class StockQueryController {

	private QueryGateway queryGateway;
	
	private static final Logger logger = LoggerFactory.getLogger(StockQueryController.class);

	public StockQueryController(QueryGateway queryGateway) {
		this.queryGateway = queryGateway;
	}

	@GetMapping("get/{companyCode}/{startDate}/{endDate}")
	@Operation(summary = "1. Gets stock on date range for a company, 2. If the query params are na, then fetches latest stocks for all companies")
	public List<Stock> getStockBasedOnDates(@PathVariable String companyCode, @PathVariable String startDate,
			@PathVariable String endDate) throws ParseException {
		GetStocksQuery getStocksQuery = new GetStocksQuery();

		getStocksQuery.setCompanyCode(companyCode);

		getStocksQuery.setStartDate(
				startDate.equalsIgnoreCase("na") ? null : new SimpleDateFormat("yyyy-MM-dd").parse(startDate));

		getStocksQuery
				.setEndDate(endDate.equalsIgnoreCase("na") ? null : new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
		
		logger.info("Created query to get stock with company code:" + companyCode + " and start date:"+startDate + " and end date:"+endDate);

		return queryGateway.query(getStocksQuery, ResponseTypes.multipleInstancesOf(Stock.class)).join();
	}

	@GetMapping("/getLatestStock/{companyCode}")
	@Hidden
	@Operation(summary="Get latest stock for a company")
	public Stock getLatestStock(@PathVariable String companyCode) throws InterruptedException, ExecutionException {

		GetStockQuery getStockQuery = new GetStockQuery();
		getStockQuery.setCompanyCode(companyCode);
		logger.info("Created query to get latest stock with company code:" + companyCode );
		CompletableFuture<Stock> future = queryGateway.query(getStockQuery, ResponseTypes.instanceOf(Stock.class));
		return future.get();
	}
}
