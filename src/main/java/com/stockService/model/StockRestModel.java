package com.stockService.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockRestModel {

	private String companyCode;
	private BigDecimal stockPrice;
	private Long numberOfHours;

}
