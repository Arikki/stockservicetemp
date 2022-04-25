package com.stockService.query.api.projection;

import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stockService.data.Stock;
import com.stockService.data.StockRepository;
import com.stockService.query.api.controller.StockQueryController;
import com.stockService.query.api.queries.GetStockQuery;
import com.stockService.query.api.queries.GetStocksQuery;

@Component
public class StockProjection {

	private static final Logger logger = LoggerFactory.getLogger(StockProjection.class);
	@Autowired
	private StockRepository stockRepository;

	@QueryHandler
	public List<Stock> getStock(GetStocksQuery getStocksQuery) {
		List<Stock> stocksList = null;
		if (getStocksQuery.getCompanyCode() != null && getStocksQuery.getStartDate() != null) {

			stocksList = stockRepository.findAllStockWithDateRange(getStocksQuery.getEndDate(),
					getStocksQuery.getStartDate(), getStocksQuery.getCompanyCode());
		} else {
			stocksList = stockRepository.findLatestStockPriceForAll();
		}
		
		logger.info("Number of fetched stocks:" +stocksList.size());
		
		return stocksList;
	}

	@QueryHandler
	public Stock getStock(GetStockQuery getStockQuery) {

		List<Stock> stocksList = stockRepository
				.findAllByCompanyCodeOrderByStartDateTimeDesc(getStockQuery.getCompanyCode());
		logger.info("Number of fetched stocks:" +stocksList.size());
		return stocksList.get(0);

	}

}
