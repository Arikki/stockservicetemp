package com.stockService.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Document(collection = "stock")
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class StockReadModel {
//	
//	@Id
//	private int _id;
	private String company_code;
	private String date;
	private String time;
	private Double stock_price;
//	private String __deleted;

}
