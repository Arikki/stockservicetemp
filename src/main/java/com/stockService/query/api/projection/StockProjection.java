package com.stockService.query.api.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.stockService.data.StockReadRepo;
import com.stockService.model.StockReadModel;
import com.stockService.query.api.queries.GetAllStocksQuery;
import com.stockService.query.api.queries.GetStockQuery;
import com.stockService.query.api.queries.GetStocksQuery;

@Component
public class StockProjection {

	private static final Logger logger = LoggerFactory.getLogger(StockProjection.class);
	@Autowired
	private StockReadRepo stockReadRepo;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@QueryHandler
	public List<StockReadModel> getStock(GetStocksQuery getStocksQuery) {
		
		Query query = new Query();
		
//		Pageable pageable = PageRequest.of(getStocksQuery.getPageNo(), getStocksQuery.getSize());
		
//		String endDate = LocalDate.parse(getStocksQuery.getEndDate()).plusDays(1L).toString(); 
		
		query.addCriteria(Criteria.where("company_code").is(getStocksQuery.getCompanyCode())
				.andOperator(Criteria.where("date").lte(getStocksQuery.getEndDate()),
						Criteria.where("__deleted").is("false"),
						Criteria.where("date").gte(getStocksQuery.getStartDate())));
//		query.with(pageable);

		List<StockReadModel>	stocksList = mongoTemplate.find(query, StockReadModel.class);
//		 Page<StockReadModel> stockPage = new PageImpl<StockReadModel>(stocksList,pageable, mongoTemplate.count((query), StockReadModel.class));
		 
//		 logger.info("StockPage:" + stockPage.toString());
	
		
		logger.info("Number of fetched stocks:" +stocksList.size());
	
		
		
		return stocksList;
	}

	@QueryHandler
	public StockReadModel getStock(GetStockQuery getStockQuery) {
	
		Query query = new Query();
		 query.addCriteria(Criteria.where("company_code").is(getStockQuery.getCompanyCode())
				 .andOperator(Criteria.where("__deleted").is("false")));
		 query.with(Sort.by(Direction.DESC,"date","time"));
		 
		 List<StockReadModel> stockList= mongoTemplate.find(query, StockReadModel.class);

		return stockList.get(0);
		

	}


	@QueryHandler
	public List<StockReadModel> getAllStocks(GetAllStocksQuery getAllStocksQuery){
		Map<String,StockReadModel> stockMap= new HashMap<String, StockReadModel>();
		
		Query query = new Query();
		query.addCriteria(Criteria.where("__deleted").is("false"));
			
		List<StockReadModel>	stocksList = mongoTemplate.find(query, StockReadModel.class);
			
			stocksList.stream().forEach(stock->{
				
				if(!stockMap.containsKey(stock.getCompany_code())) {
				stockMap.put(stock.getCompany_code(), stock);
				}
				else {
					
					
					
					if(stockMap.get(stock.getCompany_code()).getDate().compareTo(stock.getDate())<0) {
						stockMap.put(stock.getCompany_code(), stock);
					}
					else if (stockMap.get(stock.getCompany_code()).getDate().compareTo(stock.getDate()) == 0) {
						if(stockMap.get(stock.getCompany_code()).getTime().compareTo(stock.getTime())<0) {
							stockMap.put(stock.getCompany_code(), stock);
						}
						
					}
					
				}
				
			});
			
		List<StockReadModel> endList = new ArrayList<StockReadModel>(stockMap.values());
		logger.info(endList.toString());
			return endList;
		
		
	}
}
