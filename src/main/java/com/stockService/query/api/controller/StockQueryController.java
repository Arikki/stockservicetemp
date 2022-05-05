package com.stockService.query.api.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stockService.command.api.controller.StockCommandController;

import com.stockService.model.StockReadModel;
import com.stockService.query.api.queries.GetAllStocksQuery;
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
	@Operation(summary = "Gets stock on date range for a company")
	@CrossOrigin("http://localhost:4200")
	public List<StockReadModel> getStockBasedOnDates(@PathVariable String companyCode, @PathVariable String startDate,
			@PathVariable String endDate) throws ParseException {
		GetStocksQuery getStocksQuery = new GetStocksQuery();

		getStocksQuery.setCompanyCode(companyCode);

		getStocksQuery.setStartDate(startDate);

		getStocksQuery.setEndDate(endDate);
		
//		getStocksQuery.setPageNo(pageNo);
//		
//		getStocksQuery.setSize(size);
	
		logger.info("Created query to get stock with company code:" + companyCode + " and start date:"+startDate + " and end date:"+endDate);
		

		List<StockReadModel> stocksList = queryGateway.query(getStocksQuery, ResponseTypes.multipleInstancesOf(StockReadModel.class)).join();
		
		
		
		
		 return stocksList;
	}

	@GetMapping("/getLatestStock/{companyCode}")
	@Hidden
	@Operation(summary="Get latest stock for a company")
	public StockReadModel getLatestStock(@PathVariable String companyCode) throws InterruptedException, ExecutionException {

		GetStockQuery getStockQuery = new GetStockQuery();
		getStockQuery.setCompanyCode(companyCode);
		logger.info("Created query to get latest stock with company code:" + companyCode );
		CompletableFuture<StockReadModel> future = queryGateway.query(getStockQuery, ResponseTypes.instanceOf(StockReadModel.class));
		return future.get();
	}
	
	@GetMapping("/getall")
	@Hidden
	@Operation(summary="Get latest stock for a company")	
public List<StockReadModel> getAllStocks(){
	
		GetAllStocksQuery getAllStocksQuery = new GetAllStocksQuery();
		
		
		List<StockReadModel> stocksList = queryGateway.query(getAllStocksQuery, ResponseTypes.multipleInstancesOf(StockReadModel.class)).join();
	return stocksList;
}
	
	
}
